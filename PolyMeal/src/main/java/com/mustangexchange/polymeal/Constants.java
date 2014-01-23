package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Blake on 9/28/13.
 */
public class Constants
{
    public static final Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();
    public static final Gson gson = new Gson();
    public static final Animation inAnimation = new AlphaAnimation(0.0f, 1.0f);
    public static final Animation inAnimation2 = new AlphaAnimation(0.0f, 1.0f);
    public static final ArrayList<Venue> venues = new ArrayList<Venue>();
    public static final int numVenues = 4;
    //not constant as it needs to be changed but should only be one of them.
    public static int venueNumber;
    private Constants(){}
}
