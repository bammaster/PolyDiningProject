package com.mustangexchange.polymeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

    private String[] mealTimes = {"Breakfast","Lunch","Dinner","Late Night","Automatic"};
    public Preference timePref;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        mActivity = this;
        timePref = findPreference("currentTime");
        timePref.setTitle(mealTimes[MoneyTime.calcRealTime()]);
        Preference myPref = findPreference("currentTime");
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
    }
    
}
