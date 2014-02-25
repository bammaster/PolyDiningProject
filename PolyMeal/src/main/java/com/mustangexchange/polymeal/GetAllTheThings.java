package com.mustangexchange.polymeal;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class GetAllTheThings {
    Account a;
    ProgressListener listener = null;

    public GetAllTheThings(Account a) {
        this.a = a;
    }

    public void setProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    public interface ProgressListener {
        void publishProgress(int value);
    }

    public Account getTheThings() {
        if (listener != null)
            listener.publishProgress(0);

        try {
            // accept all SSL certificates
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname,
                                              SSLSession session) {
                            return true;
                        }
                    });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context
                    .getSocketFactory());

            // get the login page so we can get the required "lt" string
            // from the form and our jsessionid
            Connection.Response initial = Jsoup
                    .connect(
                            "https://my.calpoly.edu/cas/login?service=https://services.jsatech.com/login.php?cid=79")
                    .timeout(10000).method(Method.GET).execute();
            String lt = initial.parse()
                    .getElementsByAttributeValue("name", "lt").attr("value");
            String jSessionId = initial.cookie("JSESSIONID");

            // update progress
            if (listener != null)
                listener.publishProgress(1);

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

            // try to get the CASTGC cookie to see if the login succeeded
            if (loginResponse.cookie("CASTGC") == null) {
                return null;
            }
            Document login = loginResponse.parse();

            // follow redirects to meal plan loading page and get skey from
            // the jcript in the page
            String loginPageHtml = login.html();
            int offset = loginPageHtml.indexOf("skey=") + 5;
            String skey = loginPageHtml.substring(offset, offset + 32);

            // update progress
            if (listener != null)
                listener.publishProgress(2);

            // connect to this page with skey or the login doesn't work
            Jsoup.connect(
                    "https://services.jsatech.com/login.php?skey=" + skey
                            + "&cid=79&fullscreen=1&wason=").execute();

            // update progress
            if (listener != null)
                listener.publishProgress(3);

            // keep checking if login worked
            Document loginCheck;
            int attempts = 0;
            do {
                Thread.sleep(500);
                loginCheck = Jsoup
                        .connect(
                                "https://services.jsatech.com/login-check.php?skey="
                                        + skey).execute().parse();

                if (attempts == 30)
                    return null;
                attempts++;
            } while (!loginCheck.text().contains("1"));

            // update progress
            if (listener != null)
                listener.publishProgress(4);

            // to check if the login has completed.
            // Document loginCheck =
            // Jsoup.connect("https://services.jsatech.com/login-check.php?skey="+skey).execute().parse();

            // get the page with the meal plan info on it and parse it
            Document mealInfoPage = Jsoup
                    .connect(
                            "https://services.jsatech.com/index.php?skey="
                                    + skey + "&cid=79").timeout(10000)
                    .execute().parse();
            Elements balances = mealInfoPage
                    .getElementsContainingOwnText("Current Balance:");
            // update progress
            if (listener != null)
                listener.publishProgress(5);

            // first occurrence of "current balance" is campus express
            // dollars, second is plus dollars,third is meals
            a.campusExpress = new BigDecimal(balances.get(0).text()
                    .substring(17));

            // get the tables with transaction info
            Elements transactionsTables = mealInfoPage
                    .getElementsByAttributeValue("class", "boxoutside");

            // if person doesn't have a meal plan only campus express
            // balance will be present
            Elements campusExpressTransactions = null;
            Elements plusTransactions = null;
            Elements mealTransactions = null;
            int index = 0;
            if (transactionsTables.size() == 3) {
                campusExpressTransactions = transactionsTables.first()
                        .children().first().children();
                index = 1;
            }
            if (balances.size() == 3) {
                a.plusDollars = new BigDecimal(balances.get(1).text()
                        .substring(17));
                a.meals = Integer
                        .parseInt(balances.get(2).text().substring(17));
                plusTransactions = transactionsTables.get(index).children()
                        .first().children();
                mealTransactions = transactionsTables.get(index + 1).children()
                        .first().children();
            } else {
                a.plusDollars = null;
                a.meals = 0;
            }
            Elements b = mealInfoPage.getElementsByTag("b");
            a.name = b.get(b.size() - 2).text();
            a.updated = DateTime.now();
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
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}