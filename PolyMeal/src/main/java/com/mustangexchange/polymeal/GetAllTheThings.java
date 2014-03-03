package com.mustangexchange.polymeal;

import android.util.Log;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

    public Account getTheThings()
    {
        Log.e("Blake","Started.");
        try {
            // accept all SSL certificates
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            {
                        public boolean verify(String hostname,SSLSession session)
                        {
                            return true;
                        }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {}
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {}
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

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
            Log.e("Blake","Connected!");
            // try to get the CASTGC cookie to see if the login succeeded
            if (loginResponse.cookie("CASTGC") == null)
            {
                return null;
            }
            Document login = loginResponse.parse();

            // follow redirects to meal plan loading page and get skey from
            // the jcript in the page
            String loginPageHtml = login.html();
            int offset = loginPageHtml.indexOf("skey=") + 5;
            String skey = loginPageHtml.substring(offset, offset + 32);
            Jsoup.connect(
                    "https://services.jsatech.com/login.php?skey=" + skey
                            + "&cid=79&fullscreen=1&wason=").execute();
            Log.e("Blake","Starting login check.");
            if(loginCheck(skey) == null)
            {
                return null;
            }
            Log.e("Blake","Finished login check.");
            // connect to this page with skey or the login doesn't work
            Jsoup.connect(
                    "https://services.jsatech.com/login.php?skey=" + skey
                            + "&cid=79&fullscreen=1&wason=").execute();

            Log.e("Blake","Getting name.");
            // get the page with the meal plan info on it and parse it
            Document mealInfoPage = Jsoup
                    .connect(
                            "https://services.jsatech.com/index.php?skey="
                                    + skey + "&cid=79").timeout(10000)
                    .execute().parse();
            Element name = null;
            if(mealInfoPage.getElementsByClass("sidebar1body").select("b").size() > 0)
            {
                 name = mealInfoPage.getElementsByClass("sidebar1body").select("b").get(0);
                 a.name = name.text();
            }
            a.updated = DateTime.now();
            Log.e("Blake","Starting balances.");
            Elements balances = mealInfoPage
                    .getElementsContainingOwnText("Current Balance:");
            ArrayList<String> balanceList = getPlusMealInfo(balances);
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
                Log.e("Blake", "Failed to get data. Size: " + balanceList.size());
            }
            Log.e("Blake", "Finished balances. Started tables.");
            // get the tables with transaction info

            Elements transactionsTables = mealInfoPage
                    .getElementsByAttributeValue("class", "boxoutside");
            // if person doesn't have a meal plan only campus express
            // balance will be present
            Elements campusExpressTransactions = null;
            Elements plusTransactions = null;
            Elements mealTransactions = null;
            handleTransactions(transactionsTables, campusExpressTransactions,
                               plusTransactions, mealTransactions);
            Log.e("Blake","Finished tables.");
            return a;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Blake","GetTheThingsError!");
            return null;
        }
    }
    private ArrayList<String> getPlusMealInfo(Elements balances)
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
    private void handleTransactions(Elements transactionsTables, Elements campusExpressTransactions,
                                   Elements plusTransactions, Elements mealTransactions)
    {
        if (transactionsTables.size() == 1) {
            campusExpressTransactions = transactionsTables.first()
                    .children().first().children();
        }
        else if(transactionsTables.size() == 2)
        {
            plusTransactions = transactionsTables.get(1).children()
                    .first().children();
        }
        else if(transactionsTables.size() == 3)
        {
            mealTransactions = transactionsTables.get(2).children()
                    .first().children();
        }

        // now.toString("h:mm aa MM/dd/YYYY");

        // transaction data
        if (mealTransactions != null) {
            a.transactions.add(new Transaction(Transaction.MEAL,
                    mealTransactions.last().child(1).text(), // place
                    mealTransactions.last().child(0).text())); // date
        }
        if (plusTransactions != null) {
            a.transactions.add(new Transaction(Transaction.PLUS_DOLLARS,
                    plusTransactions.last().child(1).text(), // place
                    plusTransactions.last().child(0).text(), // date
                    plusTransactions.last().child(2).text())); // amount

        }
        if (campusExpressTransactions != null) {
            a.transactions.add(new Transaction(Transaction.CAMPUS_EXPRESS,
                    campusExpressTransactions.last().child(1).text(), // place
                    campusExpressTransactions.last().child(0).text(), // date
                    campusExpressTransactions.last().child(2).text())); // amount
        }
    }
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
            loginCheck = Jsoup
                    .connect(
                            "https://services.jsatech.com/login-check.php?skey="
                                    + skey).execute().parse();

            if (attempts == 30)
                return null;
            attempts++;
            Log.e("Blake",skey);
            Log.e("Blake", loginCheck.text());
        }
        while (!loginCheck.getElementsByTag("message").text().equals("1"));
        return a;
    }
}