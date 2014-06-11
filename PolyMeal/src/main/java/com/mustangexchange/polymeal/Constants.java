package com.mustangexchange.polymeal;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;


/**
 * Used to store most static types in the app. If multiple activities need access to it,
 * and you only need one reference to it put it here. All constants should also go here.
 */
public class Constants {
    public static final NumberFormat currency = NumberFormat.getCurrencyInstance();
    public static final Type gsonType = new TypeToken<HashMap<String, Venue>>() {
    }.getType();
    public static final String DEFAULT_PRICE = "0.00";
    public static final String URL = "http://www.calpolydining.com/_mobiledata/locations.xml";
    public static final String spKey = "Venue Cache";
    public static final String accSpKey = "Account";
    public static final String speKey = "Cache";
    public static String firstLaunch = "firstLaunch";
    public static int[] endOfSpring = {2014, 06, 13};
    public static Account user;
    public static String lastVenue = "";
    //Main data structure. Contains all venue data.
    public static HashMap<String, Venue> venues;
    //Stores the venue names for the PolyMEalActivity list view.
    public static ArrayList<String> names = new ArrayList<String>();
    //not constant as it needs to be changed but should only be one of them.
    public static String activityTitle = "";
    //Conversion for hours to minutes
    public static final int HOURS_TO_MINUTES = 60;
    public static final String SKEYCHECK_URL = "https://services.jsatech.com/login-check.php?skey=";
    public static final String JSA_HOSTNAME = "services.jsatech.com";
    public static final String CP_HOSTNAME = "my.calpoly.edu";
    public static final String CP_DINING_HOSTNAME = "www.calpolydining.com";
    public static final String JSA_LOGIN_URL = "https://services.jsatech.com/login.php?skey=";
    public static final String JSA_INDEX_URL = "https://services.jsatech.com/index.php?skey=";
    public static final String CAL_POLY_GREEN = "#1E4D2B";

    public static String getHTML(String urlStr) throws IOException
    {
        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=br.readLine())!=null)
                sb.append(line);
        return sb.toString();
    }
    private Constants(){}
}
