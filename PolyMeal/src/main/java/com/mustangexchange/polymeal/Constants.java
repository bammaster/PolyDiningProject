package com.mustangexchange.polymeal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Blake on 9/28/13.
 */
public class Constants
{
    public static final Type gsonType = new TypeToken<HashMap<String, Venue>>() {}.getType();
    public static final String DEFAULT_PRICE = "0.00";
    public static final String URL = "http://www.calpolydining.com/_mobiledata/locations.xml";
    public static final String spKey = "Venue Cache";
    public static final String accSpKey = "Account";
    public static final String speKey = "Cache";
    public static String firstLaunch = "firstLaunch";
    public static Account user;
    public static HashMap<String, Venue> venues;
    //Stores the venue names for the PolyMEalActivity list view.
    public static ArrayList<String> names = new ArrayList<String>();
    //not constant as it needs to be changed but should only be one of them.
    public static String activityTitle = "";
    //From Plus Dollars app.
    public static final String FILENAME = "account";
    public static final int HOURS_TO_MINUTES = 60;
    public static final String SKEYCHECK_URL = "https://services.jsatech.com/login-check.php?skey=";
    public static final String JSA_HOSTNAME = "services.jsatech.com";
    public static final String CP_HOSTNAME = "my.calpoly.edu";
    public static final String JSA_LOGIN_URL = "https://services.jsatech.com/login.php?skey=";
    public static final String JSA_INDEX_URL = "https://services.jsatech.com/index.php?skey=";
    public static final String CAL_POLY_GREEN = "#1E4D2B";
    private Constants(){}
}
