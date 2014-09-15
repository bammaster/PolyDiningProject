package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.themotlcode.polydining.Sorting.VenueNameComparator;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.DataCollector;
import com.themotlcode.polydining.models.Venue;

import java.util.TreeMap;

public class PolyMealPresenter extends Presenter
{

    private SharedPreferences sp;
    private PolyMealFragment fragment;
    private MainActivity activity;
    private PolyMealAdapter polyMealAdapter;
    private PolyApplication app;
    private GetDataThread thread;

    public PolyMealPresenter(Fragment fragment) {
        this.fragment = (PolyMealFragment) fragment;
        this.activity = (MainActivity) fragment.getActivity();

        app = (PolyApplication) activity.getApplication();

        sp = activity.getSharedPreferences(PolyApplication.spKey, activity.MODE_PRIVATE);
    }

    public void getData()
    {
        thread = new GetDataThread();
        thread.execute();
    }

    public void setDataCancelled()
    {
        thread.cancel(true);
    }


    public void setListAdapter(PolyMealAdapter polyMealAdapter)
    {
        this.polyMealAdapter = polyMealAdapter;
    }

    protected class GetDataThread extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected void onPreExecute()
        {
            fragment.getActivity().setProgressBarIndeterminateVisibility(true);
        }

        protected Boolean doInBackground(Void... args)
        {
            String gson = sp.getString(PolyApplication.speKey, "");
            app.venues = new Gson().fromJson(gson, PolyApplication.gsonType);
            if (app.venues == null)
            {
                app.venues = new TreeMap<String, Venue>(new VenueNameComparator());
                try {
                    if(isCancelled())
                    {
                        return false;
                    }
                    new DataCollector(fragment.getActivity(), sp, app).getData();
                    if(isCancelled())
                    {
                        return false;
                    }

                }
                catch(Exception e)
                {
                    PolyApplication.throwError(R.string.login_error_msg, R.string.login_error_title, e, fragment.getActivity());
                }
            }
            return true;
        }

        protected void onPostExecute(Boolean b)
        {
            fragment.setupList();
            fragment.getActivity().setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            activity.setProgressBarIndeterminateVisibility(false);
        }
    }

    protected void refresh()
    {
        polyMealAdapter.clear();
        app.venues = null;
        getData();
        polyMealAdapter.notifyData();
    }


    protected void setupList(ListView lv)
    {
        polyMealAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                final int fIndex = index;

                app.activityTitle = app.names.get(index);
                if(!app.lastVenue.equals(app.names.get(index)) && Cart.size() > 0) {
                    final AlertDialog.Builder onListClick = new AlertDialog.Builder(fragment.getActivity());
                    onListClick.setTitle("Clear Cart?");
                    onListClick.setMessage("Your cart has items that are not from this venue. " +
                            "Would you like to clear it now?");
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.clear();
                            /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                            app.lastVenue= app.names.get(fIndex);
                            intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(intentVenue);*/
                            app.lastVenue = app.names.get(fIndex);
                            VenueFragment venueFragment = new VenueFragment();
                            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_layout, venueFragment)
                                    .addToBackStack(null);
                            transaction.commit();
                        }
                    });
                    onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    onListClick.create();
                    Dialog d = onListClick.show();
                    int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                    int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
                } else {
                    /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                    app.lastVenue = app.names.get(index);
                    intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mActivity.startActivity(intentVenue);*/
                    app.lastVenue = app.names.get(fIndex);
                    VenueFragment venueFragment = new VenueFragment();
                    FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_layout, venueFragment)
                            .addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        lv.setAdapter(polyMealAdapter);
    }

}
