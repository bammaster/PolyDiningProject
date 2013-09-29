package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TacoActivity extends BaseActivity {

    private static ArrayList<FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();

    public static boolean clear;
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venueIndex = 3;
        mContext = this;
        mActivity = this;

        mDrawerList.setOnItemClickListener(new BaseActivity.DrawerItemClickListener());
        mActionBar = getActionBar();
        updateBalance();
        if(clear)
        {
            Cart.clear();
        }
    }
    public void onResume()
    {
        super.onResume();
        updateBalance();
        updateSettings();
    }

    public void updateSettings()
    {
        try
        {
            SharedPreferences defaultSP;
            int sortMode;
            defaultSP = PreferenceManager.getDefaultSharedPreferences(mActivity);
            sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));


            if(sortMode == 0)
            {
                foodAdapterList.clear();
                for(int i = 0;i<ItemSetContainer.tacoItems.size();i++)
                {
                    if(ItemSetContainer.tacoItems.get(i).size() != 0) //check in case it's not the right time of day
                    {
            /* Each of the components of tacoItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                        ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.tacoItems.get(i).getItems());
                        Collections.sort(sortedList, new ItemNameComparator());
                        foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.tacoItems.get(i).getTitle(), sortedList)));
                    }
                }
            }
            else if(sortMode == 1)
            {
                foodAdapterList.clear();
                for(int i = 0;i<ItemSetContainer.tacoItems.size();i++)
                {
                    if(ItemSetContainer.tacoItems.get(i).size() != 0) //check in case it's not the right time of day
                    {
            /* Each of the components of tacoItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                        ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.tacoItems.get(i).getItems());
                        Collections.sort(sortedList, new ItemNameComparator());
                        Collections.reverse(sortedList);
                        foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.tacoItems.get(i).getTitle(), sortedList)));
                    }

                }
            }
            else if(sortMode == 2)
            {
                foodAdapterList.clear();
                for(int i = 0;i<ItemSetContainer.tacoItems.size();i++)
                {
                    if(ItemSetContainer.tacoItems.get(i).size() != 0) //check in case it's not the right time of day
                    {
            /* Each of the components of tacoItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                        ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.tacoItems.get(i).getItems());
                        Collections.sort(sortedList, new ItemPriceComparator());
                        foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.tacoItems.get(i).getTitle(), sortedList)));
                    }

                }
            }
            else
            {
                foodAdapterList.clear();
                for(int i = 0;i<ItemSetContainer.tacoItems.size();i++)
                {
                    if(ItemSetContainer.tacoItems.get(i).size() != 0) //check in case it's not the right time of day
                    {
            /* Each of the components of tacoItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                        ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.tacoItems.get(i).getItems());
                        Collections.sort(sortedList, new ItemPriceComparator());
                        Collections.reverse(sortedList);
                        foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.tacoItems.get(i).getTitle(), sortedList)));
                    }

                }
            }

            vp.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), foodAdapterList));
            vp.getAdapter().notifyDataSetChanged();
        }
        catch(NullPointerException e)
        {
            Intent intentHome = new Intent(mContext, MainActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intentHome);
        }

    }
}
