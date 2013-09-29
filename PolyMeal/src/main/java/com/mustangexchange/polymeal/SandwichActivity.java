package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.gson.reflect.TypeToken;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class SandwichActivity extends BaseActivity {

    private static ArrayList<FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();

    public static boolean clear;
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venueIndex = 1;
        mContext = this;
        mActivity = this;

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
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
            for(int i = 0;i<ItemSetContainer.sandItems.size();i++)
            {
                if(ItemSetContainer.sandItems.get(i).size() != 0) //check in case it's not the right time of day
                {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                    ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.sandItems.get(i).getItems());
                    Collections.sort(sortedList, new ItemNameComparator());
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.sandItems.get(i).getTitle(), sortedList)));
                }
            }
        }
        else if(sortMode == 1)
        {
            foodAdapterList.clear();
            for(int i = 0;i<ItemSetContainer.sandItems.size();i++)
            {
                if(ItemSetContainer.sandItems.get(i).size() != 0) //check in case it's not the right time of day
                {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                    ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.sandItems.get(i).getItems());
                    Collections.sort(sortedList, new ItemNameComparator());
                    Collections.reverse(sortedList);
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.sandItems.get(i).getTitle(), sortedList)));
                }

            }
        }
        else if(sortMode == 2)
        {
            foodAdapterList.clear();
            for(int i = 0;i<ItemSetContainer.sandItems.size();i++)
            {
                if(ItemSetContainer.sandItems.get(i).size() != 0) //check in case it's not the right time of day
                {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                    ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.sandItems.get(i).getItems());
                    Collections.sort(sortedList, new ItemPriceComparator());
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.sandItems.get(i).getTitle(), sortedList)));
                }

            }
        }
        else
        {
            foodAdapterList.clear();
            for(int i = 0;i<ItemSetContainer.sandItems.size();i++)
            {
                if(ItemSetContainer.sandItems.get(i).size() != 0) //check in case it's not the right time of day
                {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
                    ArrayList<Item> sortedList = new ArrayList<Item>(ItemSetContainer.sandItems.get(i).getItems());
                    Collections.sort(sortedList, new ItemPriceComparator());
                    Collections.reverse(sortedList);
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(ItemSetContainer.sandItems.get(i).getTitle(), sortedList)));
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