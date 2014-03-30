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
                        .setTitle(R.string.mealtimepreftitle)
                        .setMessage(R.string.mealtimemessage)
                        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
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
