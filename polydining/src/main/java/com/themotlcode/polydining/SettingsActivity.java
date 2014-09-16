package com.themotlcode.polydining;


/*import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;

import net.saik0.android.unifiedpreference.UnifiedPreferenceActivity;
import net.saik0.android.unifiedpreference.UnifiedPreferenceFragment;

public class SettingsActivity extends UnifiedPreferenceActivity
{

    private String[] mealTimes = {"Breakfast","Lunch","Dinner","Late Night","Automatic"};
    public Preference timePref;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(PolyApplication.APP_COLOR)));
        mActivity = this;
        timePref = findPreference("currentTime");
        timePref.setTitle(mealTimes[MoneyTime.calcRealTime()]);
        Preference myPref = findPreference("currentTime");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                QustomDialogBuilder builder = new QustomDialogBuilder(mActivity);
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

    public static class GeneralPreferenceFragment extends UnifiedPreferenceFragment
    {}

    public static class NotificationPreferenceFragment extends UnifiedPreferenceFragment {}

    public static class DataSyncPreferenceFragment extends UnifiedPreferenceFragment {}

}*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.themotlcode.polydining.models.MoneyTime;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    protected SharedPreferences defaultSP;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(PolyApplication.APP_COLOR)));

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        defaultSP = PreferenceManager.getDefaultSharedPreferences(this);

    }


    public static class MyPreferenceFragment extends PreferenceFragment
    {
        private String[] mealTimes = {"Breakfast","Lunch","Dinner","Late Night","Automatic"};

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
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
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
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
                    int sortMode = Integer.valueOf(((SettingsActivity) getActivity()).defaultSP.getString("sortMode", "0"));
                    builder.setSingleChoiceItems(sortingOptions, sortMode,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int item)
                                {
                                    ((SettingsActivity) getActivity()).defaultSP.edit().putString("sortMode", String.valueOf(item)).commit();
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
                    int moneyMode = Integer.valueOf(((SettingsActivity) getActivity()).defaultSP.getString("moneyMode", "4"));
                    builder.setSingleChoiceItems(moneyOptions, moneyMode,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int item)
                                {
                                    ((SettingsActivity) getActivity()).defaultSP.edit().putString("moneyMode", String.valueOf(item)).commit();
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
