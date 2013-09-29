package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Blake on 9/28/13.
 */
public class Venue
{
    //holds the item sets for a venue.
    private ArrayList<ItemSet> venueItems;

    //String key for retrieving data from shared preferences.
    private String gsonKey;
    private String name;
    private String url;
    private int id;
    //construct a venu with a name and url.
    public Venue(String name, String url, int id)
    {
        venueItems = new ArrayList<ItemSet>();
        gsonKey = name + " Items";
        this.name = name;
        this.url = url;
        this.id = id;
    }
    //loads items from shared preferences using GSON.
    public void loadFromCache(SharedPreferences sp)
    {
        venueItems = Constants.gson.fromJson(sp.getString(gsonKey, ""),Constants.gsonType);
    }
    //serializes and saves items to shared preferences using GSON.
    public void saveToCache(SharedPreferences sp)
    {
        sp.edit().putString(Constants.gson.toJson(venueItems),"").commit();
    }
    public String getName()
    {
        return name;
    }
    public int getId()
    {
        return id;
    }
    public void checkVenueCart(Activity activity)
    {
        final Activity mActivity = activity;
        if(Constants.venueNumber != id)
        {
            AlertDialog.Builder venueCartError = new AlertDialog.Builder(activity);
            venueCartError.setTitle("Warning!");
            venueCartError.setMessage("You have " + Constants.venues.get(Constants.venueNumber) + " item's in your cart. If you continue these items will be removed.");
            venueCartError.setPositiveButton("Continue",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Cart.clear();
                    Toast.makeText(mActivity,"Cart cleared!",Toast.LENGTH_SHORT).show();
                    Constants.venueNumber = id;
                }
            });
            venueCartError.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
        }
    }
}
