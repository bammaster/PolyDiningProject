package com.mustangexchange.polymeal;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mustangexchange.polymeal.Sorting.VenueNameComparator;
import com.mustangexchange.polymeal.models.GetData;
import com.mustangexchange.polymeal.models.Venue;

import java.util.TreeMap;

public class PolyMealPresenter extends Presenter {

    private SharedPreferences sp;
    private PolyMealFragment fragment;
    private PolyMealActivity activity;
    private ListAdapter listAdapter;

    public PolyMealPresenter(Fragment fragment, ListAdapter listAdapter) {
        this.fragment = (PolyMealFragment) fragment;
        this.activity = (PolyMealActivity) fragment.getActivity();
        this.listAdapter = listAdapter;
    }

    public void getData()
    {
        sp = activity.getSharedPreferences(Constants.spKey, activity.MODE_PRIVATE);
        if (sp.getBoolean(Constants.firstLaunch, true))
        {
            getDataHelper();
        } else if (Statics.venues == null)
        {
            new GetDataThead().execute();
        }
    }

    private void getDataHelper()
    {
         activity.setProgressBarIndeterminateVisibility(true);
        Statics.venues = new TreeMap<String, Venue>(new VenueNameComparator());
        new GetData(listAdapter, fragment.getActivity(), sp).execute();
    }

    class GetDataThead extends AsyncTask<Void, Void, Boolean>
    {

        protected Boolean doInBackground(Void... args)
        {
            String gson = sp.getString(Constants.speKey, "");
            Statics.venues = new Gson().fromJson(gson, Constants.gsonType);
            if (Statics.venues == null)
            {
                getDataHelper();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean b)
        {
           if (b)
           {
               listAdapter.addAll(Statics.venues.keySet());
           }
           else
           {
               Toast.makeText(activity, "Error! Reloading data!", Toast.LENGTH_LONG).show();
           }
        }
    }



}
