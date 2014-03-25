package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
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

import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class VenueActivity extends FragmentActivity {

    protected ViewPager vp;
    protected PagerTabStrip myPagerTabStrip;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected String[]  mDrawerItems;
    protected static Activity mActivity;
    public static BigDecimal totalAmount;
    public static Context mContext;
    public static ActionBar mActionBar;
    private static String restoreVenue;

    private static ArrayList<FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();
    private Venue venue;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(R.array.drawerItemsMeal);
        mActivity = this;
        mContext = this;
        mActionBar = getActionBar();
        if(savedInstanceState != null) {
            System.out.println("got here");
            MoneyTime.moneySpent = new BigDecimal(savedInstanceState.getString("moneySpent"));
            Cart.cart = savedInstanceState.getParcelableArrayList("cart");
            Constants.lastVenue = savedInstanceState.getString("lastVenue");
            startActivity(new Intent(mContext, PolyMealActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION));
            //finish();
            overridePendingTransition(0, 0); //no animation
        } else {
            finishOnCreate();
        }
    }

    private void finishOnCreate() {
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));*/
        mDrawerList.setAdapter(new ListViewArrayAdapter(this, new ArrayList<String>(Arrays.asList(mDrawerItems))));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerList.setItemChecked(-1, true);
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        /* The next couple lines of code dynamically sets up an ArrayList of FoodItemAdapters.
           One for each tab in the ViewPager. FoodItemAdapters are Adapters for the Card ListViews
           of each ViewPager Fragment. It gets passed in with the ViewPager adapter because the ViewPager Adapter
           will draw each fragment. MyPagerAdapter is the single adapter for the ViewPager which also uses a custom
           Fragment inner class called MyFragment.
         */

        vp = (ViewPager) findViewById(R.id.pager);
        //updateSettings();
        /*vp.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), foodAdapterList));
        vp.getAdapter().notifyDataSetChanged();*/

        myPagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        myPagerTabStrip.setTabIndicatorColor(0xC6930A);
        venue = Constants.venues.get(Constants.activityTitle);
        mActionBar.setTitle(Constants.activityTitle);

        updateBalance();
        updateSettings();


    }
    protected void onSaveInstanceState(Bundle outState){
        // This gets called by the system when it's about to kill your app
        // Put all your data in the outState bundle
        outState.putParcelableArrayList("cart", Cart.getCart());
        outState.putString("moneySpent", MoneyTime.moneySpent.toString());
        outState.putString("lastVenue", Constants.lastVenue);
    }
    public void updateSettings()
    {
        SharedPreferences defaultSP;
        int sortMode;
        defaultSP = PreferenceManager.getDefaultSharedPreferences(mContext);
        sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));

        //Sort by name, a-z
        if(sortMode == 0)
        {
            foodAdapterList.clear();
            for(int i = 0;i < venue.size();i++)
            {
                ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                Collections.sort(sortedList, new ItemNameComparator());
                foodAdapterList.add(new FoodItemAdapter(this, venue.venueItems.get(i).title, sortedList));

            }
        }
        //sort by name, z-a
        else if(sortMode == 1)
        {
            foodAdapterList.clear();
            for(int i = 0;i < venue.size();i++)
            {

                ArrayList<Item> sortedList = new ArrayList<Item>(venue.venueItems.get(i).items);
                Collections.sort(sortedList, new ItemNameComparator());
                Collections.reverse(sortedList);
                foodAdapterList.add(new FoodItemAdapter(this, venue.venueItems.get(i).title, sortedList));

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
                foodAdapterList.add(new FoodItemAdapter(this, venue.venueItems.get(i).title, sortedList));

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
                foodAdapterList.add(new FoodItemAdapter(this, venue.venueItems.get(i).title, sortedList));
            }
        }

        vp.setAdapter(new PagerAdapter(this, getSupportFragmentManager(), foodAdapterList));
        vp.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static void setSubtitleColor()
    {
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView) mActivity.findViewById(titleId);
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            yourTextView.setTextColor(Color.RED);
        }
        else
        {
            yourTextView.setTextColor(Color.WHITE);
        }
    }

    public static void updateBalance() {
        try
        {
            totalAmount = MoneyTime.calcTotalMoney();
            /*
            if(totalAmount.compareTo(new BigDecimal("0.00")) < 0)
            {
                if(totalAmount.multiply(new BigDecimal("-1")).compareTo(PlusDollarsActivity.account.plusDollars) > 0)
                {
                    QustomDialogBuilder plusDollarsExceeded = new QustomDialogBuilder(mContext);
                    plusDollarsExceeded.setDividerColor(Constants.CAL_POLY_GREEN);
                    plusDollarsExceeded.setTitleColor(Constants.CAL_POLY_GREEN);
                    plusDollarsExceeded.setTitle(R.string.plusdollarsalert);
                    plusDollarsExceeded.setMessage(R.string.plusdollarsalertmessage);
                    plusDollarsExceeded.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    plusDollarsExceeded.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                }
            }
            */
            setSubtitleColor();
            mActionBar.setSubtitle("$" + totalAmount + " Remaining");
        }
        catch (NullPointerException e)
        {
            Intent intentHome = new Intent(mContext, PolyMealActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intentHome);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateBalance();
        updateSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.venue, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /* The click listner for ListView in the navigation drawer */
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final int delay = 200;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        switch(position)
                        {
                            case 0:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyDiningActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                break;
                            case 1:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyMealActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case 2:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PlusDollarsActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case 3:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, CompleteorActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case 4:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, TransactionActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case 5:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, SettingsActivity.class));
                                break;
                            default:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyDiningActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                break;
                        }
                    }
                    catch(InterruptedException e)
                    {
                        Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}