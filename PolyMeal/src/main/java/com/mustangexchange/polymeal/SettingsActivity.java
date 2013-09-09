package com.mustangexchange.polymeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private AlertDialog.Builder displayTimes;
    private int checked;
    private String[] mealTimes = {"Breakfast","Lunch","Dinner","Late Night","Automatic"};
    private TextView current;
    public Preference timePref;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mActivity = this;
        timePref = findPreference("currentTime");
        timePref.setTitle(mealTimes[MoneyTime.calcRealTime()]);
        //timePref.setSelectable(false);
        Preference myPref = (Preference) findPreference("currentTime");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder
                        .setTitle("What's a meal worth?")
                        .setMessage(
                                "Breakfast: $7.90 (7:00-10:00am)\n"
                                        + "Lunch: $9.00 (10:00am-5pm)\n"
                                        + "Dinner: $10.75 (5-8:14pm)\n"
                                        + "Late Night: $8.75 (8:15pm-2am)\n\n"
                                        + "Meals reset weekly on Saturday morning at 2:00am")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
                return true;
            }
        });
        /*setContentView(R.layout.activity_settings);
        current = (TextView)findViewById(R.id.textCurrent);
        current.setText(mealTimes[MoneyTime.whichTime]);*/
    }

    /*public void select(View v)
    {
        sp = getSharedPreferences("PolyMeal",MODE_PRIVATE);
        spe = sp.edit();
        checked = sp.getInt("checked",4);
        displayTimes = new AlertDialog.Builder(this);
        displayTimes.setTitle("Select Meal Time:");
        displayTimes.setSingleChoiceItems(mealTimes,checked,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                checked = button;
            }
        });
        displayTimes.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                MoneyTime.manual=true;
                MoneyTime.whichTime = checked;
                spe.putInt("checked",checked);
                spe.commit();
            }
        });
        displayTimes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {

            }
        });
        displayTimes.show();
    }
    public void onResume()
    {
        super.onResume();
        current.setText(mealTimes[MoneyTime.whichTime]);
    }
    public void onStart()
    {
        super.onStart();
        current.setText(mealTimes[MoneyTime.whichTime]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }*/
    
}
