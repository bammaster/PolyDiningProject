package com.themotlcode.polydining.models;

import android.util.Log;

import com.themotlcode.polydining.Exceptions.BudgetException;
import com.themotlcode.polydining.PolyApplication;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Scanner;

public class GetDiningAccount
{
    private Account a;
    private static final String URL_CID = "&cid=79";
    public GetDiningAccount(Account a)
    {
        this.a = a;
    }

    public Account getAccountInfo() throws BudgetException
    {
        try
        {
            handleInitConnection();
            Connection.Response loginResponse = handleCookies();
            // try to get the CASTGC cookie to see if the login succeeded
            if (loginResponse.cookie("CASTGC") == null)
            {
                return null;
            }
            Document login = loginResponse.parse();
            // follow redirects to meal plan loading page and get skey from
            // the jscript in the page
            String skey = getSkey(login.html());
            Jsoup.connect(PolyApplication.JSA_LOGIN_URL + skey + URL_CID).execute();
            if(loginCheck(skey) == null)
            {
                return null;
            }
            // connect to this page with skey or the login doesn't work
            Jsoup.connect(PolyApplication.JSA_LOGIN_URL + skey + URL_CID).execute();

            // get the page with the meal plan info on it and parse it
            Document mealInfoPage = Jsoup.connect(PolyApplication.JSA_INDEX_URL + skey + URL_CID)
                                    .timeout(10000).execute().parse();

            Elements temp = mealInfoPage.getElementsByClass("sidebar1body").select("b");
            if(temp.size() > 0)
            {
                 Element name = temp.get(0);
                 a.setName(name.text());
            }
            else
            {
                a.setName("Error");
            }
            Elements balances = mealInfoPage
                    .getElementsContainingOwnText("Current Balance:");
            handleAllValueInfo(getAllValueInfo(balances));

            // get the tables with transaction info
            Elements transactionsTables = mealInfoPage.getElementsByClass("boxoutside");
            // if person doesn't have a meal plan only campus express
            // balance will be present
            handleTransactions(transactionsTables);
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
            a.setCampusExpress(balanceList.get(0));
            a.setPlusDollars(PolyApplication.DEFAULT_PRICE);
            a.setMeals(0);
        }
        else if(balanceList.size() == 2)
        {
            a.setCampusExpress(balanceList.get(0));
            a.setPlusDollars(balanceList.get(1));
            a.setMeals(0);
        }
        else if(balanceList.size() == 3)
        {
            a.setCampusExpress(balanceList.get(0));
            a.setPlusDollars(balanceList.get(1));
            a.setMeals(Integer.parseInt(balanceList.get(2)));
        }
        else
        {
            a.setCampusExpress(PolyApplication.DEFAULT_PRICE);
            a.setPlusDollars(PolyApplication.DEFAULT_PRICE);
            a.setMeals(0);
            Log.v("Blake", "Failed to get dining data. Size: " + balanceList.size());
        }
    }
    private void handleTransactions(Elements transactionsTables)
    {
        a.clearTransactions();
        ArrayList<Transaction> tempTrans = new ArrayList<Transaction>();
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
                        tempTrans.add(new Transaction(i, time, location, ammount));
                    }
                }
            }
        }
        a.setTransactions(tempTrans);
    }

    /**
     * Handles the login check to make sure the user is logged in.= which happens when a 1 is returned at Constants.SKEYCHECK_URL
     * @param skey The skey that pertains to the user.
     * @return Returns a because if login check fails account is null.
     * @throws InterruptedException If something goes wrong with the connection.
     * @throws java.io.IOException If something goes wrong with the connection.
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
            loginCheck = Jsoup.connect(PolyApplication.SKEYCHECK_URL + skey).execute().parse();

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
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     */
    private void handleInitConnection() throws NoSuchAlgorithmException, KeyManagementException
    {
        // accept all SSL certificates
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
        {
            public boolean verify(String hostname,SSLSession session)
            {
                if(hostname.equals(PolyApplication.JSA_HOSTNAME)||hostname.equals(PolyApplication.CP_HOSTNAME))
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
                .data("username", a.getUsername()).data("password", a.getPassword())
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
}