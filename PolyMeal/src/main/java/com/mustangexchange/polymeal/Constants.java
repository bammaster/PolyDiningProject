package com.mustangexchange.polymeal;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Blake on 9/28/13.
 */
public class Constants
{
    public static final Type gsonType = new TypeToken<HashMap<String, Venue>>() {}.getType();
    public static final String URL = "http://www.calpolydining.com/_mobiledata/locations.xml";
    public static final String spKey = "Venue Cache";
    public static final String speKey = "Cache";
    public static String firstLaunch = "firstLaunch";
    public static final Gson gson = new Gson();
    public static HashMap<String, Venue> venues;
    public static final int numVenues = 4;
    //not constant as it needs to be changed but should only be one of them.
    public static String activityTitle = "";
    public static int venueNumber;
    private Constants(){}
}
