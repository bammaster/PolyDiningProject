package com.themotlcode.polydining.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.themotlcode.polydining.Exceptions.PasswordException;
import com.themotlcode.polydining.PolyApplication;
import com.themotlcode.polydining.R;

import org.apache.http.auth.AuthenticationException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.login.LoginException;

public class DataCollector {

    public DataCollector(Account account)
    {
        this.account = account;
    }
    public DataCollector(Activity activity, SharedPreferences sp, PolyApplication app){
        this.activity = activity;
        this.sp = sp;
        this.app = app;
    }

    //Dining account fields.
    private Account account;
    private static final String URL_CID = "&cid=79";
    private static final String COOKIE = "&fullscreen=1&wason=";

    /**
     * Gets the users dining account data such as account balance and transaction history.
     * @throws IOException Thrown if reading from internet fails.
     * @throws PasswordException Thrown if wrong username or password is entered.
     * @throws LoginException Thrown if logging in and getting the session cookie fails.
     * @throws AuthenticationException Thrown if the login check fails.
     * @throws InterruptedException Thrown id the connection is interrupted.
     * @throws KeyManagementException Thrown if SSL fails.
     * @throws NoSuchAlgorithmException Thrown if SSL fails.
     */
    public void getAccountInfo() throws IOException, PasswordException, LoginException,
                                        AuthenticationException, InterruptedException,
                                        KeyManagementException, NoSuchAlgorithmException
    {
            // setup SSL and host checking
            handleInitConnection();
            Connection.Response loginResponse = handleCookies();
            // try to get the CASTGC cookie to see if the login succeeded
            Document loginDoc = loginResponse.parse();
            /* follow redirects to meal plan loading page and get skey from
               the jscript in the page. */
            String skey = getSkey(loginDoc.html());
            Jsoup.connect(PolyApplication.JSA_LOGIN_URL + skey + URL_CID + COOKIE).execute();
            loginCheck(skey);
            // connect to this page with skey or the login doesn't work
            Jsoup.connect(PolyApplication.JSA_LOGIN_URL + skey + URL_CID + COOKIE).execute();
            // get the page with the meal plan info on it and parse it
            Document mealInfoPage = Jsoup.connect(PolyApplication.JSA_INDEX_URL + skey + URL_CID)
                    .timeout(10000).execute().parse();
            Elements nameElements = mealInfoPage.getElementsByClass("sidebar1body").select("b");
            // confirms that elements exist.
            if(nameElements.size() > 0)
            {
                Element name = nameElements.get(0);
                account.setName(name.text());
            }
            else
            {
                account.setName("Error");
            }
            // gets the elements for various balances, such as plus dollars and meals.
            Elements balances = mealInfoPage
                    .getElementsContainingOwnText("Current Balance:");
            handleAllValueInfo(getAllValueInfo(balances));
            // get the tables with transaction info
            Elements transactionsTables = mealInfoPage.getElementsByClass("boxoutside");
            /* if person doesn't have a meal plan only campus express
               balance will be present*/
            handleTransactions(transactionsTables);
    }

    /**
     * Gets all the users balances for meals, plus dollars, and campus express.
     * @param balances The elements that contain the balances.
     * @return The list of balances grabbed from the html elements.
     */
    private ArrayList<String> getAllValueInfo(Elements balances)
    {
        ArrayList<String> balanceList = new ArrayList<String>();
        //Gets each balance element fro mthe html.
        for(Element value : balances)
        {
            Scanner extractor = new Scanner(value.text());
            //Iterates to extract the balance values from the html to sanitize the input.
            while(extractor.hasNext())
            {
                String tempValue = extractor.next();
                String valueBuilder= "";
                //Iterates over each character in the balance html to sanitize input by checking each character.
                for(int i = 0; i < tempValue.length(); i++)
                {
                    //Ensures that the character is a number or that a decimal has numbers to the right and left.
                    if(Character.isDigit(tempValue.charAt(i))||(tempValue.charAt(i) == '.' &&
                            Character.isDigit(tempValue.charAt(i - 1)) &&
                            Character.isDigit(tempValue.charAt(i + 1))))
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

    /**
     * Figures out which values to add to the account based on the number of balance elements found.
     * @param balanceList the list of balances grabbed from the dining website html.
     */
    private void handleAllValueInfo(ArrayList<String> balanceList)
    {
        // first occurrence of "current balance" is campus express
        // dollars, second is plus dollars,third is meals
        if(balanceList.size() == 1)
        {
            account.setCampusExpress(balanceList.get(0));
            account.setPlusDollars(PolyApplication.DEFAULT_PRICE);
            account.setMeals(0);
        }
        else if(balanceList.size() == 2)
        {
            account.setCampusExpress(balanceList.get(0));
            account.setPlusDollars(balanceList.get(1));
            account.setMeals(0);
        }
        else if(balanceList.size() == 3)
        {
            account.setCampusExpress(balanceList.get(0));
            account.setPlusDollars(balanceList.get(1));
            account.setMeals(Integer.parseInt(balanceList.get(2)));
        }
        else
        {
            account.setCampusExpress(PolyApplication.DEFAULT_PRICE);
            account.setPlusDollars(PolyApplication.DEFAULT_PRICE);
            account.setMeals(0);
            Log.v("Blake", "Failed to get dining data. Size: " + balanceList.size());
        }
    }

    /**
     * Clears old account transactions and then adds new ones.
     * @param transactionsTables The html elements that contain transaction data.
     */
    private void handleTransactions(Elements transactionsTables)
    {
        account.clearTransactions();
        ArrayList<Transaction> tempTrans = new ArrayList<Transaction>();
        //Iterates over all the transaction data from the html.
        for(int i = 0; i < transactionsTables.size(); i++)
        {
            //Drills down through the html tables to get at the data.
            for(Element tbody : transactionsTables.get(i).select("tbody")) {
                for (Element element : tbody.select("tr")) {
                    String time = "";
                    String location = "";
                    String ammount = "";
                    //Gets the data from each column of a row for thr transaction tables.
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
                        tempTrans.add(new Transaction(i, time, location, ammount));
                    }
                }
            }
        }
        account.setTransactions(tempTrans);
    }

    /**
     * Handles the login check to make sure the user is logged in.= which happens when a 1 is returned at Constants.SKEYCHECK_URL
     * @param skey The skey that pertains to the user.
     * @return Returns a because if login check fails account is null.
     * @throws InterruptedException If something goes wrong with the connection.
     * @throws java.io.IOException If something goes wrong with the connection.
     */
    private void loginCheck(String skey) throws InterruptedException, IOException, AuthenticationException
    {
        // to check if the login has completed. URL returns 1 when authenticated.
        // Document loginCheck =
        // Jsoup.connect("https://services.jsatech.com/login-check.php?skey="+skey).execute().parse();
        Document loginCheck;
        int attempts = 0;
        do
        {
            loginCheck = Jsoup.connect(PolyApplication.SKEYCHECK_URL + skey).execute().parse();

            if (attempts == 30)
            {
                throw new AuthenticationException();
            }
            attempts++;
            Thread.sleep(500);
        }
        while (!loginCheck.getElementsByTag("message").text().equals("1"));
    }

    /**
     * Opens the initial connection to the server with the ssl credentials
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     */
    private void handleInitConnection() throws NoSuchAlgorithmException, KeyManagementException
    {
        //Sets up hostname checking to make sure the user connects to Cal Poly.
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname,SSLSession session) {
                if (hostname.equals(PolyApplication.JSA_HOSTNAME) || hostname.equals(PolyApplication.CP_HOSTNAME)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        //Setups certificate checking.
        SSLContext context = SSLContext.getInstance("SSL");
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
    private Connection.Response handleCookies() throws IOException, PasswordException, LoginException
    {
        // get the login page so we can get the required "lt" string
        // from the form and our jsessionid
        Connection.Response initial = Jsoup.connect(
                "https://my.calpoly.edu/cas/login?service=https://services.jsatech.com/login.php?cid=79")
                .timeout(10000).method(Connection.Method.GET).execute();
        String lt = initial.parse().getElementsByAttributeValue("name", "lt").attr("value");
        String jSessionId = initial.cookie("JSESSIONID");
        // do the login request to get CASTGC cookie
        Connection.Response loginResponse = Jsoup
                .connect(
                        "https://my.calpoly.edu/cas/login;jsessionid="
                                + jSessionId
                                + "?service=https://services.jsatech.com/login.php?cid=79")
                .method(Connection.Method.POST).timeout(10000)
                .data("username", account.getUsername()).data("password", account.getPassword())
                .data("_eventId", "submit").data("submit", "Login")
                .data("lt", lt).cookie("JSESSIONID", jSessionId).execute();
        if(loginResponse.parse().text().contains("invalid username or password"))
        {
            throw new PasswordException();
        }
        else if(loginResponse.cookie("CASTGC") == null)
        {
            throw new LoginException();
        }
        return loginResponse;
    }

    /**
     * This method is for getting the skey from an html page that is returned.
     * @param docText The html to get the skey for authenticating.
     * @return  The skey needed to use and authenticate with the server.
     */
    private String getSkey(String docText)
    {
        //Gets the skey which is basically the session id for a user.
        StringBuilder skeyBuilder = new StringBuilder();
        for(int i = docText.indexOf("skey=")+5; i < docText.indexOf("&cid"); i++)
        {
            skeyBuilder.append(docText.charAt(i));
        }
        return skeyBuilder.toString();
    }

    //Venue fields.
    private Activity activity;
    private SharedPreferences sp;
    private Database db;
    private AlertDialog.Builder error;
    private PolyApplication app;

    /**
     * Gets the venue data from json.
     * @throws IOException If the connection fails.
     */
    public void getData() throws IOException
    {
        //Gets all the json from the internet to build the venues.
        app.venues.clear();
        URL url = new URL(PolyApplication.URL);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        String venues = sb.toString();
        app.venues = new Gson().fromJson(venues, PolyApplication.gsonType);
        app.names = new ArrayList<String>();
        app.names.addAll(app.venues.keySet());
    }

    /**
     * Stores all the venue and account data to a sqlite database
     * @param db The database to store to.
     */
    public void storeData(Database db)
    {
        sp.edit().putBoolean(PolyApplication.firstLaunch,false).apply();
        int version = sp.getInt("DBVersion", 1);
        db = new Database(activity, version++);
        sp.edit().putInt("DBVersion", version).apply();
        for(Map.Entry<String, Venue> entry : app.venues.entrySet())
        {
            db.updateVenues(entry.getValue());
        }
    }

}
