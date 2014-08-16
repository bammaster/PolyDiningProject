package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;


public class BaseActivity extends FragmentActivity
{
    protected DrawerLayout mDrawerLayout;
    protected RelativeLayout mRelLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected String[]  mDrawerItems;

    @Override
    public void setContentView(final int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mRelLayout = (RelativeLayout) mDrawerLayout.findViewById(R.id.rel_layout);
        getLayoutInflater().inflate(layoutResID, mRelLayout, true); // Setting the content of layout your provided to the act_content frame
        super.setContentView(mDrawerLayout);
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
    public void init(Context mContext, ActionBar mActionBar, boolean plus) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(plus ? R.array.drawerItemsPlus : R.array.drawerItemsMeal);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));*/
        mDrawerList.setAdapter(new DrawerAdapter(this, new ArrayList<String>(Arrays.asList(mDrawerItems))));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(mContext));

        // enable ActionBar app icon to behave as action to toggle nav drawer
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

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
    }

    /* The click listner for ListView in the navigation drawer */
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        Context mContext;
        public DrawerItemClickListener(Context mContext) {
            this.mContext = mContext;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            /*final int delay = 200;
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
                                Thread.sleep(delay);
                                break;
                            case 3:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, TransactionActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                break;
                            case 4:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, SettingsActivity.class));
                                break;
                            default:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyDiningActivity.class));
                                break;
                        }
                    }
                    catch(InterruptedException e)
                    {
                        Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }).start();*/
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}