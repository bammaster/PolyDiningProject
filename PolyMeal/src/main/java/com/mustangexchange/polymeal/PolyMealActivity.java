package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolyMealActivity extends Activity
{
    private Context mContext;
    private List<Map<String,String>> data;
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    //private SimpleAdapter adapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(Constants.spKey, MODE_PRIVATE);
        lv = (ListView)findViewById(R.id.listView);
        //data = new ArrayList<Map<String,String>>();
        //adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                //new String[] {"venue", "status"},new int[] {android.R.id.text1, android.R.id.text2});
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constants.names);
        listAdapter.setNotifyOnChange(true);
        mContext = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                Constants.activityTitle = Constants.names.get(index);
                final Intent intentVenue = new Intent(mContext, VenueActivity.class);
                intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intentVenue);
            }
        });
        lv.setAdapter(listAdapter);
        String gson = sp.getString(Constants.speKey,"");
        Constants.venues = new Gson().fromJson(gson,Constants.gsonType);
        if(sp.getBoolean(Constants.firstLaunch,true) || Constants.venues == null)
        {
            setProgressBarIndeterminateVisibility(true);
            Constants.venues = new HashMap<String, Venue>();
            new GetData(listAdapter, this, sp).execute();
        }
        else if(Constants.names.isEmpty())
        {
            Log.e("Blake","Hello2");
            for(String venue : Constants.venues.keySet())
            {
                listAdapter.add(venue);
            }
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
}