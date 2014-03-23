package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PolyMealActivity extends Activity
{
    private Context mContext;
    private ListView lv;
    private ListAdapter listAdapter;
    private SharedPreferences sp;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected String[]  mDrawerItems;
    protected static Activity mActivity;
    protected static ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_polymeal);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(R.array.drawerItemsMeal);
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
        sp = getSharedPreferences(Constants.spKey, MODE_PRIVATE);
        lv = (ListView)findViewById(R.id.listView);
        //data = new ArrayList<Map<String,String>>();
        //adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                //new String[] {"venue", "status"},new int[] {android.R.id.text1, android.R.id.text2});
        listAdapter = new ListAdapter(this, R.id.polymealListItem, Constants.names);
        listAdapter.setNotifyOnChange(true);
        mContext = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                Constants.activityTitle = Constants.names.get(index);
                Log.e("Blake",Constants.venues.get(Constants.names.get(index)).isOpen()+"");
                final Intent intentVenue = new Intent(mContext, VenueActivity.class);
                intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intentVenue);
            }
        });
        lv.setAdapter(listAdapter);
        if(sp.getBoolean(Constants.firstLaunch,true))
        {
            setProgressBarIndeterminateVisibility(true);
            Constants.venues = new HashMap<String, Venue>();
            new GetData(listAdapter, this, sp).execute();
        }
        else if(Constants.venues == null)
        {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    String gson = sp.getString(Constants.speKey,"");
                    Constants.venues = new Gson().fromJson(gson,Constants.gsonType);
                    for(final String venue : Constants.venues.keySet())
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listAdapter.add(venue);
                            }
                        });
                    }
                }
            }).start();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(lv!=null)
        {
            lv.invalidate();
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.polymeal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                setProgressBarIndeterminateVisibility(true);
                listAdapter.clear();
                new GetData(listAdapter, this, sp).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public class ListAdapter extends ArrayAdapter<String>
    {
        public ListAdapter(Context context, int resource, List<String> items)
        {
            super(context, resource, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.polymeal_list_item, parent, false);
            }
            TextView tt = (TextView) convertView.findViewById(R.id.polymealListItem);
            tt.setText("  " + Constants.names.get(position));
            if(Constants.venues.get(Constants.names.get(position)).closeSoon())
            {
                tt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.soon_dot),null,null,null);
            }
            else if(Constants.venues.get(Constants.names.get(position)).isOpen())
            {
                tt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.open_dot),null,null,null);
            }
            else
            {
                tt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.close_dot),null,null,null);
            }
            return convertView;

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