package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.TextView;

import com.themotlcode.polydining.models.MoneyTime;

public class SettingsActivity extends PreferenceActivity {

    private static PolyApplication app;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(PolyApplication.APP_COLOR)));

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ErrorSender.sendErrorToDeveloper(ex, SettingsActivity.this);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        app = (PolyApplication) getApplication();
    }


    public static class MyPreferenceFragment extends PreferenceFragment {
        private String[] mealTimes = {"Breakfast", "Lunch", "Dinner", "Late Night", "Automatic"};

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            Preference timePref = findPreference("currentTime");
            timePref.setTitle(mealTimes[MoneyTime.calcRealTime()]);
            timePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder
                            .setTitle(R.string.mealtimepreftitle)
                            .setMessage(R.string.mealtimemessage)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    Dialog d = builder.show();
                    int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                    int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
                    return true;
                }
            });

            Preference sortPref = findPreference("sortMode");
            sortPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final String[] sortingOptions = getResources().getStringArray(R.array.sortMode);
                    builder.setTitle(R.string.pref_sort_mode);
                    int sortMode = Integer.valueOf(app.defaultSP.getString("sortMode", "0"));
                    builder.setSingleChoiceItems(sortingOptions, sortMode,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    app.defaultSP.edit().putString("sortMode", String.valueOf(item)).commit();
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    Dialog d = builder.show();
                    int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                    int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
                    return true;
                }
            });

            Preference moneyPref = findPreference("moneyMode");
            moneyPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final String[] moneyOptions = getResources().getStringArray(R.array.moneyMode);
                    builder.setTitle(R.string.pref_money_mode);
                    int moneyMode = Integer.valueOf(app.defaultSP.getString("moneyMode", "4"));
                    builder.setSingleChoiceItems(moneyOptions, moneyMode,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    app.defaultSP.edit().putString("moneyMode", String.valueOf(item)).commit();
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    Dialog d = builder.show();
                    int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                    int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
                    return true;
                }
            });
        }
    }

}
