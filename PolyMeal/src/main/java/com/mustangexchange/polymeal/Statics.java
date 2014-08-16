package com.mustangexchange.polymeal;


import com.mustangexchange.polymeal.models.Account;
import com.mustangexchange.polymeal.models.Venue;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Stores all static fields for the app such as the main data structures.
 * Put any fields that are not constants and are needed across multiple activities.
 * For constants, see Constants.java
 */
public class Statics {
    public static int[] endOfQuarter;
    public static int[] startOfQuarter;
    public static Account user;
    public static String lastVenue = "";
    //Main data structure. Contains all venue data.
    public static TreeMap<String, Venue> venues;
    //Stores the venue names for the PolyMEalActivity list view.
    public static ArrayList<String> names = new ArrayList<String>();
    //not constant as it needs to be changed but should only be one of them.
    public static String activityTitle = "";
    private Statics(){}
}
