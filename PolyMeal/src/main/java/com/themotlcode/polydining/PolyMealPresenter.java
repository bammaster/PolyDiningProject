package com.themotlcode.polydining;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.themotlcode.polydining.Sorting.VenueNameComparator;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.GetAndStoreVenueData;
import com.themotlcode.polydining.models.MoneyTime;
import com.themotlcode.polydining.models.Venue;

import java.math.BigDecimal;
import java.util.List;
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
            new GetDataThread().execute();
        }
    }

    /*
    Gets called on doInBackground so no UI manipulation can occur in this method.
     */
    private void getDataHelper()
    {
        Statics.venues = new TreeMap<String, Venue>(new VenueNameComparator());
        new GetAndStoreVenueData(listAdapter, fragment.getActivity(), sp).execute();
    }

    class GetDataThread extends AsyncTask<Void, Void, Boolean>
    {

        protected Boolean doInBackground(Void... args)
        {
            String gson = sp.getString(Constants.speKey, "");
            Statics.venues = new Gson().fromJson(gson, Constants.gsonType);
            if (Statics.venues == null)
            {
                getDataHelper();
            }
            return true;
        }

        protected void onPostExecute(Boolean b)
        {
            listAdapter.addAll(Statics.venues.keySet());
        }
    }

    void refresh()
    {
        listAdapter.clear();
        Statics.venues = null;
        getData();
        listAdapter.notifyData();
    }


    void setupList(ListView lv)
    {
        listAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                final int fIndex = index;
                Statics.activityTitle = Statics.names.get(index);
                if(!Statics.lastVenue.equals(Statics.names.get(index))
                        && MoneyTime.getMoneySpent().compareTo(new BigDecimal("0.00")) != 0) {
                    final QustomDialogBuilder onListClick = new QustomDialogBuilder(fragment.getActivity());
                    onListClick.setDividerColor(Constants.APP_COLOR);
                    onListClick.setTitleColor(Constants.APP_COLOR);
                    onListClick.setTitle("Clear Cart?");
                    onListClick.setMessage("Your cart has items that are not from this venue. " +
                            "Would you like to clear it now?");
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.clear();
                            /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                            Statics.lastVenue= Statics.names.get(fIndex);
                            intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(intentVenue);*/
                            Statics.lastVenue= Statics.names.get(fIndex);
                            VenueFragment venueFragment = new VenueFragment();
                            FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
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
                    Statics.lastVenue = Statics.names.get(index);
                    intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mActivity.startActivity(intentVenue);*/
                    Statics.lastVenue= Statics.names.get(fIndex);
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
