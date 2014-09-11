package com.themotlcode.polydining;

import android.app.Application;
import com.google.gson.reflect.TypeToken;
import com.themotlcode.polydining.models.Account;
import com.themotlcode.polydining.models.Venue;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;

public class PolyApplication extends Application
{
    public static final NumberFormat currency = NumberFormat.getCurrencyInstance();
    public static final Type gsonType = new TypeToken<TreeMap<String, Venue>>() {}.getType();
    public static final String DEFAULT_PRICE = "0.00";
    public static final String URL = "http://107.170.238.171/java/venues.json";
    public static final String DATE_URL = "http://107.170.238.171/dates.txt";
    public static final String MESSAGE_URL = "http://107.170.238.171/message.txt";
    public static final String COLOR_URL = "http://107.170.238.171/color.txt";
    public static final String GREETING_KEY = "Greeting";
    public static final String spKey = "Poly Dining";
    public static final String accSpKey = "Account";
    public static final String speKey = "Cache";
    public static final String firstLaunch = "firstLaunch";
    //Conversion for hours to minutes
    public static final int HOURS_TO_MINUTES = 60;
    public static final int ELEVEN_O_CLOCK_MINUTES = 1379;
    public static final int TWELVE_O_CLOCK_MINUTES = 1439;
    public static final String SKEYCHECK_URL = "https://services.jsatech.com/login-check.php?skey=";
    public static final String JSA_HOSTNAME = "services.jsatech.com";
    public static final String CP_HOSTNAME = "my.calpoly.edu";
    public static final String JSA_LOGIN_URL = "https://services.jsatech.com/login.php?skey=";
    public static final String JSA_INDEX_URL = "https://services.jsatech.com/index.php?skey=";
    public static String APP_COLOR = "#036228";
    public static String ACCENT_COLOR = "#036228";
    public static final int DATE_ARRAY_SIZE = 3;
    public static boolean plus = false;

    public int[] endOfQuarter;
    public int[] startOfQuarter;
    public Account user;
    public String lastVenue = "";
    //Main data structure. Contains all venue data.
    public TreeMap<String, Venue> venues;
    //Stores the venue names for the PolyMEalActivity list view.
    public ArrayList<String> names = new ArrayList<String>();
    //not constant as it needs to be changed but should only be one of them.
    public String activityTitle = "";

}
