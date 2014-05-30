package com.mustangexchange.polymeal;

import android.util.Log;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class GetAllTheThings
{
    private Account a;

    public GetAllTheThings(Account a)
    {
        this.a = a;
    }

    public Account getTheThings() throws BudgetException
    {
        Log.v("Blake","Started.");
        try
        {
            getDates();
        }
        catch(IOException e)
        {
            throw new BudgetException();
        }
        try
        {

            handleInitConnection();
            Connection.Response loginResponse = handleCookies();
            Log.v("Blake","Connected!");
            // try to get the CASTGC cookie to see if the login succeeded
            if (loginResponse.cookie("CASTGC") == null)
            {
                return null;
            }
            Document login = loginResponse.parse();

            // follow redirects to meal plan loading page and get skey from
            // the jcript in the page
            String skey = getSkey(login.html());
            Jsoup.connect(Constants.JSA_LOGIN_URL + skey + "&cid=79&fullscreen=1&wason=").execute();
            Log.v("Blake","Starting login check.");
            if(loginCheck(skey) == null)
            {
                return null;
            }
            Log.v("Blake","Finished login check.");

            // connect to this page with skey or the login doesn't work
            Jsoup.connect(Constants.JSA_LOGIN_URL + skey + "&cid=79&fullscreen=1&wason=").execute();
            Log.v("Blake","Getting name.");

            // get the page with the meal plan info on it and parse it
            Document mealInfoPage = Jsoup.connect(Constants.JSA_INDEX_URL + skey + "&cid=79")
                                    .timeout(10000).execute().parse();

            Element name;
            Elements temp = mealInfoPage.getElementsByClass("sidebar1body").select("b");
            if(temp.size() > 0)
            {
                 name = temp.get(0);
                 a.name = name.text();
            }
            else
            {
                a.name = "Error";
            }
            Log.v("Blake","Starting balances.");
            Elements balances = mealInfoPage
                    .getElementsContainingOwnText("Current Balance:");
            handleAllValueInfo(getAllValueInfo(balances));
            Log.v("Blake", "Finished balances. Started tables.");

            // get the tables with transaction info
            Elements transactionsTables = mealInfoPage
                    .getElementsByClass("boxoutside");
            // if person doesn't have a meal plan only campus express
            // balance will be present
            handleTransactions(transactionsTables);
            Log.v("Blake","Finished tables.");
            Collections.reverse(a.transactions);
            return a;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.v("Blake","GetTheThingsError!");
            return null;
        }
    }
    private ArrayList<String> getAllValueInfo(Elements balances)
    {
        ArrayList<String> balanceList = new ArrayList<String>();
        for(Element value : balances)
        {
            Scanner extractor = new Scanner(value.text());
            while(extractor.hasNext())
            {
                String tempValue = extractor.next();
                String valueBuilder= "";
                for(int i = 0; i < tempValue.length(); i++)
                {
                    if(Character.isDigit(tempValue.charAt(i))||tempValue.charAt(i) == '.')
                    {
                        valueBuilder += tempValue.charAt(i);
                    }
                }
                if(valueBuilder.length() > 0)
                {
                    balanceList.add(valueBuilder);
                }
            }
        }
        return balanceList;
    }
    private void handleAllValueInfo(ArrayList<String> balanceList)
    {
        // first occurrence of "current balance" is campus express
        // dollars, second is plus dollars,third is meals
        if(balanceList.size() == 1)
        {
            a.campusExpress = new BigDecimal(balanceList.get(0));
            a.plusDollars = new BigDecimal("0.00");
            a.meals = 0;
        }
        else if(balanceList.size() == 2)
        {
            a.campusExpress = new BigDecimal(balanceList.get(0));
            a.plusDollars = new BigDecimal(balanceList.get(1));
            a.meals = 0;
        }
        else if(balanceList.size() == 3)
        {
            a.campusExpress = new BigDecimal(balanceList.get(0));
            a.plusDollars = new BigDecimal(balanceList.get(1));
            a.meals = Integer.parseInt(balanceList.get(2));
        }
        else
        {
            a.campusExpress = new BigDecimal("0.00");
            a.plusDollars = new BigDecimal("0.00");
            a.meals = 0;
            Log.v("Blake", "Failed to get data. Size: " + balanceList.size());
        }
    }
    private void handleTransactions(Elements transactionsTables)
    {
        a.transactions.clear();
        for(int i = 0; i < transactionsTables.size(); i++)
        {
            for(Element tbody : transactionsTables.get(i).select("tbody")) {
                for (Element element : tbody.select("tr")) {
                    String time = "";
                    String location = "";
                    String ammount = "";
                    if (element.getElementsByClass("tablefirstcol").size() > 0) {
                        time = element.getElementsByClass("tablefirstcol").get(0).text();
                    }
                    if (element.getElementsByClass("tablecol").size() > 0) {
                        location = element.getElementsByClass("tablecol").get(0).text();
                    }
                    if (element.getElementsByClass("tablecolnum").size() > 0) {
                        ammount = element.getElementsByClass("tablecolnum").get(0).text();
                    }
                    if (!time.isEmpty() && !location.isEmpty() && !ammount.isEmpty()) {
                        Log.v("Blake",time+"|"+location+"|"+ammount);
                        a.transactions.add(new Transaction(i, time, location, ammount));
                    }
                }
            }
        }
        Log.e("Blake",a.transactions.toString());
    }

    /**
     * Handles the login check to make sure the user is logged in.= which happens when a 1 is returned at Constants.SKEYCHECK_URL
     * @param skey The skey that pertains to the user.
     * @return Returns a because if login check fails account is null.
     * @throws InterruptedException If something goes wrong with the connection.
     * @throws IOException If something goes wrong with the connection.
     */
    private Account loginCheck(String skey) throws InterruptedException, IOException
    {
        // to check if the login has completed.
        // Document loginCheck =
        // Jsoup.connect("https://services.jsatech.com/login-check.php?skey="+skey).execute().parse();
        Document loginCheck;
        int attempts = 0;
        do
        {
            Thread.sleep(500);
            loginCheck = Jsoup.connect(Constants.SKEYCHECK_URL + skey).execute().parse();

            if (attempts == 30)
            {
                return null;
            }
            attempts++;
            Log.e("Blake",skey);
            Log.e("Blake", loginCheck.text());
        }
        while (!loginCheck.getElementsByTag("message").text().equals("1"));
        return a;
    }

    /**
     * Opens the initial connection to the server with the ssl credentials
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private void handleInitConnection() throws NoSuchAlgorithmException, KeyManagementException
    {
        // accept all SSL certificates
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
        {
            public boolean verify(String hostname,SSLSession session)
            {
                if(hostname.equals(Constants.JSA_HOSTNAME)||hostname.equals(Constants.CP_HOSTNAME))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {

            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
    private Connection.Response handleCookies() throws IOException
    {
        // get the login page so we can get the required "lt" string
        // from the form and our jsessionid
        Connection.Response initial = Jsoup.connect(
                "https://my.calpoly.edu/cas/login?service=https://services.jsatech.com/login.php?cid=79")
                .timeout(10000).method(Method.GET).execute();
        String lt = initial.parse().getElementsByAttributeValue("name", "lt").attr("value");
        String jSessionId = initial.cookie("JSESSIONID");
        // do the login request to get CASTGC cookie
        Connection.Response loginResponse = Jsoup
                .connect(
                        "https://my.calpoly.edu/cas/login;jsessionid="
                                + jSessionId
                                + "?service=https://services.jsatech.com/login.php?cid=79")
                .method(Method.POST).timeout(10000)
                .data("username", a.username).data("password", a.password)
                .data("_eventId", "submit").data("submit", "Login")
                .data("lt", lt).cookie("JSESSIONID", jSessionId).execute();

        return loginResponse;
    }

    /**
     * This method is for getting the skey from an html page that is returned.
     * @param docText The html to get the skey for authenticating.
     * @return  The skey needed to use and authenticate with the server.
     */
    private String getSkey(String docText)
    {
        StringBuilder skeyBuilder = new StringBuilder();
        for(int i = docText.indexOf("skey=")+5; i < docText.indexOf("&cid"); i++)
        {
            skeyBuilder.append(docText.charAt(i));
        }
        return skeyBuilder.toString();
    }
    private void getDates() throws IOException
    {
        URL dateUrl = new URL(Constants.DATE_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String start = br.readLine();
        String end = br.readLine();
        String[] temp = end.split("/");
        Constants.endOfQuarter = new int[3];
        Constants.endOfQuarter[0] = new Integer(temp[2]);
        Constants.endOfQuarter[1] = new Integer(temp[0]);
        Constants.endOfQuarter[2] = new Integer(temp[1]);
        temp = start.split("/");
        Constants.startOfQuarter = new int[3];
        Constants.startOfQuarter[0] = new Integer(temp[2]);
        Constants.startOfQuarter[1] = new Integer(temp[0]);
        Constants.startOfQuarter[2] = new Integer(temp[1]);
    }
}