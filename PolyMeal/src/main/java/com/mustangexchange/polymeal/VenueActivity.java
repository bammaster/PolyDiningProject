package com.mustangexchange.polymeal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jon Amireh on 12/22/13.
 */
public class VenueActivity extends BaseActivity {
    private static ArrayList<FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();
    private Venue venue;
    public static boolean clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
        mContext = this;
        mActionBar = getActionBar();
        mActionBar.setTitle(Constants.activityTitle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        venue = Constants.venues.get(Constants.activityTitle);

        //updateBalance();
        //if(clear)
        //{
            //Cart.clear();
       // }
    }

    public void onResume()
    {
        super.onResume();
        //updateBalance();
        //updateSettings();
    }

    public void updateSettings()
    {
        try
        {
            SharedPreferences defaultSP;
            int sortMode;
            defaultSP = PreferenceManager.getDefaultSharedPreferences(mContext);
            sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));

            //Sort by name, a-z
            if(sortMode == 0)
            {
                foodAdapterList.clear();
                for(int i = 0;i<venue.size();i++)
                {
                    ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                    Collections.sort(sortedList, new ItemNameComparator());
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(venue.venueItems.get(i).title, sortedList)));

                }
            }
            //sort by name, z-a
            else if(sortMode == 1)
            {
                foodAdapterList.clear();
                for(int i = 0;i<venue.size();i++)
                {

                    ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                    Collections.sort(sortedList, new ItemNameComparator());
                    Collections.reverse(sortedList);
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(venue.venueItems.get(i).title, sortedList)));

                }
            }
            //sort by price, low to high
            else if(sortMode == 2)
            {
                foodAdapterList.clear();
                for(int i = 0;i<venue.size();i++)
                {

                    ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                    Collections.sort(sortedList, new ItemPriceComparator());
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(venue.venueItems.get(i).title, sortedList)));

                }
            }
            //sort by price, high to low
            else
            {
                foodAdapterList.clear();
                for(int i = 0;i<venue.size();i++)
                {
                    ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                    Collections.sort(sortedList, new ItemPriceComparator());
                    Collections.reverse(sortedList);
                    foodAdapterList.add(new FoodItemAdapter(this, new ItemSet(venue.venueItems.get(i).title, sortedList)));
                }
            }

            vp.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), foodAdapterList));
            vp.getAdapter().notifyDataSetChanged();
        }
        catch(NullPointerException e)
        {
            Intent intentHome = new Intent(mContext, PolyMealActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intentHome);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int positionTapped = parent.getPositionForView(view);
            //if home
            if(positionTapped == 0)
            {
                mDrawerLayout.closeDrawer(mDrawerList);
                Thread threadHome = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intentHome = new Intent(mContext, PolyMealActivity.class);
                        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intentHome);
                    }
                });
                threadHome.start();
            }
            //if venue
            else if(positionTapped <= Constants.numVenues)
            {
                mDrawerLayout.closeDrawer(mDrawerList);
                Thread threadVenue = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Constants.activityTitle = mDrawerItems[positionTapped - 1];
                                venue = Constants.venues.get(Constants.activityTitle);
                                mActionBar.setTitle(Constants.activityTitle);
                                updateSettings();
                            }
                        });
                    }
                });
                threadVenue.run();
            }
            else if(positionTapped == Constants.numVenues + 1)
            {
                mDrawerLayout.closeDrawer(mDrawerList);
                final Thread threadCP = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //final Intent intentCP = new Intent(mContext, CompleteorActivity.class);
                        //intentCP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //mContext.startActivity(intentCP);
                    }
                });
                threadCP.start();
            }
            else if(positionTapped == Constants.numVenues + 2)
            {
                final Thread threadST = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //final Intent intentST = new Intent(mContext, SettingsActivity.class);
                        //intentST.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        //mContext.startActivity(intentST);
                    }
                });
                mDrawerLayout.closeDrawer(mDrawerList);
                threadST.start();
            }
        }
    }

}