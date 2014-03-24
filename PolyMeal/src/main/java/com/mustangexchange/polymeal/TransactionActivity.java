package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jon on 3/23/14.
 */
public class TransactionActivity extends Activity {

    //protected static Account Constants.user = Constants.user;
    protected Thread update;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected String[]  mDrawerItems;
    protected static Activity mActivity;
    protected static Context mContext;
    protected static ActionBar mActionBar;
    protected ListView lv;
    protected TransactionAdapter ta;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_trans);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(R.array.drawerItemsPlus);
        mActivity = this;
        mContext = this;
        mActionBar = getActionBar();

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

        update = buildThread();
        if(Constants.user == null) {
            Constants.user = new Account().loadAccount(getSharedPreferences(Constants.accSpKey,MODE_PRIVATE));
        }
        if(Constants.user == null)
        {
            Toast.makeText(mContext, "Please login.", Toast.LENGTH_LONG).show();
            try
            {
                Thread.sleep(200);
                Intent PDIntent = new Intent(mContext, PlusDollarsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("login", 1);
                PDIntent.putExtras(extras);
                startActivity(PDIntent);
            }
            catch(InterruptedException e)
            {
                Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ta = new TransactionAdapter(mContext, Constants.user.transactions));
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(Constants.user != null) {
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ta = new TransactionAdapter(mContext, Constants.user.transactions));
        }
    }

    private Thread buildThread()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                GetAllTheThings getPlusData = new GetAllTheThings(Constants.user);
                Constants.user = getPlusData.getTheThings();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setProgressBarIndeterminateVisibility(false);
                    }
                });
            }
        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trans, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                new Thread(update).start();
                setProgressBarIndeterminateVisibility(true);
                ta.notifyDataSetChanged();
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
                                startActivity(new Intent(mContext, PolyDiningActivity.class));
                                break;
                            case 1:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyMealActivity.class));
                                break;
                            case 2:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PlusDollarsActivity.class));
                                break;
                            case 3:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, CompleteorActivity.class));
                                break;
                            case 4:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, SettingsActivity.class));
                                break;
                            case 5:
                                mDrawerLayout.closeDrawers();
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
            }).start();
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}