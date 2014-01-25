package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity
{

    final ArrayList<String> names = new ArrayList<String>();
    final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView)findViewById(R.id.listView);

        /**
         * BLAKE CALL PARSE
         */
        for(Map.Entry<String, Venue> entry : Constants.venues.entrySet()) {
            names.add(entry.getKey());
        }

        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, (String[])names.toArray()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final int positionTapped = parent.getPositionForView(view);
                Constants.activityTitle = names.get(positionTapped);
                Intent intentVenue = new Intent(mContext, VenueActivity.class);
                intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }

        });
    }
}