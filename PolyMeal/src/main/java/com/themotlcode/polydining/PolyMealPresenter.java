package com.themotlcode.polydining;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.themotlcode.polydining.Sorting.VenueNameComparator;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.GetAndStoreVenueData;
import com.themotlcode.polydining.models.Venue;

import java.util.TreeMap;

public class PolyMealPresenter extends Presenter
{

    private SharedPreferences sp;
    private PolyMealFragment fragment;
    private MainActivity activity;
    private ListAdapter listAdapter;
    private PolyApplication app;

    public PolyMealPresenter(Fragment fragment, ListAdapter listAdapter) {
        this.fragment = (PolyMealFragment) fragment;
        this.activity = (MainActivity) fragment.getActivity();
        this.listAdapter = listAdapter;
        
        app = (PolyApplication) activity.getApplication();
    }

    public void getData()
    {
        sp = activity.getSharedPreferences(PolyApplication.spKey, activity.MODE_PRIVATE);
        if (sp.getBoolean(PolyApplication.firstLaunch, true))
        {
            getDataHelper();
        } else if (app.venues == null)
        {
            new GetDataThread().execute();
        }
    }

    /*
    Gets called on doInBackground so no UI manipulation can occur in this method.
     */
    private void getDataHelper()
    {
        app.venues = new TreeMap<String, Venue>(new VenueNameComparator());
        new GetAndStoreVenueData(listAdapter, fragment.getActivity(), sp, app).execute();
    }

    protected class GetDataThread extends AsyncTask<Void, Void, Boolean>
    {

        protected Boolean doInBackground(Void... args)
        {
            String gson = sp.getString(PolyApplication.speKey, "");
            app.venues = new Gson().fromJson(gson, PolyApplication.gsonType);
            if (app.venues == null)
            {
                getDataHelper();
            }
            return true;
        }

        protected void onPostExecute(Boolean b)
        {
            listAdapter.addAll(app.venues.keySet());
        }
    }

    protected void refresh()
    {
        listAdapter.clear();
        app.venues = null;
        getData();
        listAdapter.notifyData();
    }


    protected void setupList(ListView lv)
    {
        listAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                final int fIndex = index;

                app.activityTitle = app.names.get(index);
                if(!app.lastVenue.equals(app.names.get(index)) && Cart.size() > 0) {
                    final QustomDialogBuilder onListClick = new QustomDialogBuilder(fragment.getActivity());
                    onListClick.setDividerColor(PolyApplication.APP_COLOR);
                    onListClick.setTitleColor(PolyApplication.APP_COLOR);
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
                    onListClick.show();
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
        lv.setAdapter(listAdapter);
    }

}
