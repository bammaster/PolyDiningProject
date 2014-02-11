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

public class PolyMealActivity extends Activity
{
    final private Context mContext = this;
    private ArrayList<String> names = new ArrayList<String>();
    private ListView lv;
    private ArrayAdapter<String> listAdapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(Constants.spKey, MODE_PRIVATE);
        lv = (ListView)findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                Constants.activityTitle = names.get(index);
                Intent intentVenue = new Intent(mContext, VenueActivity.class);
                intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentVenue);
            }
        });
        lv.setAdapter(listAdapter);
        if(sp.getBoolean(Constants.firstLaunch,true))
        {
            setProgressBarIndeterminateVisibility(true);
            new GetData(listAdapter, this, sp).execute();
        }
        else
        {
            Constants.venues = new Gson().fromJson(sp.getString(Constants.speKey,""),Constants.gsonType);
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
                new GetData(listAdapter, this, sp).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}