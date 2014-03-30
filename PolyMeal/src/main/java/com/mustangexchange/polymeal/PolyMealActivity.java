package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PolyMealActivity extends BaseActivity
{
    private Context mContext;
    private ListView lv;
    private ListAdapter listAdapter;
    private SharedPreferences sp;
    protected static Activity mActivity;
    protected static ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_polymeal);
        mActivity = this;
        mContext = this;
        mActionBar = getActionBar();
        init(mContext, mActionBar);


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
                final int fIndex = index;
                Constants.activityTitle = Constants.names.get(index);
                Log.e("Blake",Constants.venues.get(Constants.names.get(index)).isOpen()+"");
                if(!Constants.lastVenue.equals(Constants.names.get(index))
                    && MoneyTime.moneySpent.compareTo(new BigDecimal("0.00")) != 0) {
                    final QustomDialogBuilder onListClick = new QustomDialogBuilder(PolyMealActivity.mActivity);
                    onListClick.setDividerColor(Constants.CAL_POLY_GREEN);
                    onListClick.setTitleColor(Constants.CAL_POLY_GREEN);
                    onListClick.setTitle("Clear Cart?");
                    onListClick.setMessage("Your cart has items that are not from this venue. " +
                            "Would you like to clear it now?");
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.clear();
                            final Intent intentVenue = new Intent(mContext, VenueActivity.class);
                            Constants.lastVenue= Constants.names.get(fIndex);
                            intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mContext.startActivity(intentVenue);
                        }
                    });
                    onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    onListClick.show();
                } else {
                    final Intent intentVenue = new Intent(mContext, VenueActivity.class);
                    Constants.lastVenue = Constants.names.get(index);
                    intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intentVenue);
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.polymeal, menu);
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
}