package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarApp;

import com.mustangexchange.polymeal.models.Account;
import com.mustangexchange.polymeal.models.Cart;
import com.mustangexchange.polymeal.models.Venue;
import com.mustangexchange.polymeal.models.VenuesString;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeMap;

public class PolyApplication extends SugarApp {
    public static final NumberFormat currency = NumberFormat.getCurrencyInstance();
    public static final Type gsonType = new TypeToken<TreeMap<String, Venue>>() {
    }.getType();
    public static final Type gsonTypeDate = new TypeToken<DateTime>() {
    }.getType();
    public static final String DEFAULT_PRICE = "0.00";
    public static final String URL = "http://107.170.238.171/java/venues.json";
    public static final String DATE_URL = "http://107.170.238.171/dates.txt";
    public static final String MESSAGE_URL = "http://107.170.238.171/message.txt";
    public static final String COLOR_URL = "http://107.170.238.171/color.txt";
    public static final String GREETING_KEY = "Greeting";
    public static final String START_OF_QUARTER_KEY = "startOfQuarter_";
    public static final String END_QUARTER_KEY = "endOfQuarter_";
    public static final String APP_COLOR_KEY = "APP_COLOR";
    public static final String ACCENT_COLOR_KEY = "ACCENT_COLOR";
    public static final String REFRESH_DATE_KEY = "REFRESH_DATE";

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

    public VenuesString venuesString;
    public Cart cart;
    public SharedPreferences defaultSP;

    Thread venueCache = new Thread() {
        public void run() {
            venues = new Gson().fromJson(venuesString.gson, PolyApplication.gsonType);
            names = new ArrayList<String>();
            names.addAll(venues.keySet());
        }
    };

    public static void throwError(int message, int title, final Throwable exception, final Activity activity) {
        final AlertDialog.Builder error = new AlertDialog.Builder(activity);
        error.setTitle(title);
        error.setMessage(message);
        error.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {

            }
        });
        error.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                ErrorSender.sendErrorToDeveloper(exception, activity);
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                error.create();
                Dialog d = error.show();
                int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
            }
        });
    }
}
