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
import com.themotlcode.polydining.models.Item;
import com.themotlcode.polydining.models.MealType;
import com.themotlcode.polydining.models.MoneyTime;
import com.themotlcode.polydining.models.Venue;
import com.themotlcode.polydining.models.VenuesString;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PolyMealPresenter extends Presenter {

    private PolyMealFragment fragment;
    private MainActivity activity;
    private PolyMealAdapter polyMealAdapter;
    private PolyApplication app;
    private GetDataThread thread;
    private int filter;

    public PolyMealPresenter(Fragment fragment) {
        this.fragment = (PolyMealFragment) fragment;
        this.activity = (MainActivity) fragment.getActivity();
        app = (PolyApplication) activity.getApplication();
    }

    public void getData() {
        if (app.venues != null && app.names != null) {
            fragment.updateUI();
            return;
        }
        thread = new GetDataThread();
        thread.execute();
    }

    public void setDataCancelled() {
        if (thread != null) {
            thread.cancel(true);
        }
    }


    public void setListAdapter(PolyMealAdapter polyMealAdapter) {
        this.polyMealAdapter = polyMealAdapter;
    }

    protected class GetDataThread extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            fragment.loading = true;
            fragment.getActivity().setProgressBarIndeterminateVisibility(true);
        }

        protected Boolean doInBackground(Void... args) {
            checkDB();
            app.defaultSP.edit().putString(app.REFRESH_DATE_KEY, new Gson().toJson(DateTime.now())).apply();
            if (app.venuesString == null) {
                app.venuesString = new VenuesString();
                app.venues = new TreeMap<String, Venue>(new VenueNameComparator());
                try {
                    if (isCancelled()) {
                        return false;
                    }
                    new DataCollector(app).getData();
                    if (isCancelled()) {
                        return false;
                    }

                } catch (Exception e) {
                    PolyApplication.throwError(R.string.login_error_msg, R.string.login_error_title, e, fragment.getActivity());
                }
            }
            return true;
        }

        protected void checkDB() {
            List<VenuesString> venuesStrings = VenuesString.listAll(VenuesString.class);
            if (!venuesStrings.isEmpty() && venuesStrings.get(0).gson != null) {
                app.venuesString = venuesStrings.get(0);
                app.lastVenue = app.venuesString.lastVenue;
                app.venueCache.start();
                try {
                    app.venueCache.join();
                } catch (Exception e) {
                    app.venuesString = null;
                    app.lastVenue = null;
                }

            }
            app.cart = new Cart();
            List<Item> items = Item.listAll(Item.class);
            ArrayList<Item> cart = new ArrayList<Item>();
            for (Item item : items) {
                for (int i = 0; i < item.getNumInCart(); i++) {
                    cart.add(item);
                }
            }
            app.cart.setCart(cart);
        }

        protected void onPostExecute(Boolean b) {
            fragment.loading = false;
            fragment.updateUI();
            fragment.getActivity().setProgressBarIndeterminateVisibility(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            activity.setProgressBarIndeterminateVisibility(false);
        }
    }

    protected void refresh() {
        polyMealAdapter.clear();
        app.venues = null;
        app.venuesString = null;
        getData();
    }

    protected ArrayList<String> getFilteredList() {
        ArrayList<String> filteredList = new ArrayList<String>();
        if (filter == 0) {
            return app.names;
        }
        for (Venue v : app.venues.values()) {
            if (filter == 1) {
                if (MealType.meal == v.getType()) {
                    filteredList.add(v.getName());
                }
            } else if (filter == 2) {
                if (MealType.plus == v.getType()) {
                    filteredList.add(v.getName());
                }
            }
        }
        return filteredList;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    protected void setupList(ListView lv) {
        polyMealAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int index, long id) {
                final int fIndex = index;

                app.activityTitle = app.names.get(index);
                if (!app.lastVenue.equals(app.names.get(index)) && app.cart.size() > 0) {
                    final AlertDialog.Builder onListClick = new AlertDialog.Builder(fragment.getActivity());
                    onListClick.setTitle("Clear Cart?");
                    onListClick.setMessage("Your cart has items that are not from this venue. " +
                            "Would you like to clear it now?");
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            app.cart.clear();
                            /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                            app.lastVenue= app.names.get(fIndex);
                            intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(intentVenue);*/
                            app.lastVenue = app.names.get(fIndex);
                            app.venuesString.lastVenue = app.lastVenue;
                            app.venuesString.save();

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
                    app.venuesString.lastVenue = app.lastVenue;
                    app.venuesString.save();

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

    public String pickVenue() {
        ArrayList<Venue> openVenues = new ArrayList<Venue>();
        for (String v : getFilteredList()) {
            if (app.venues.get(v).isOpen() || app.venues.get(v).closeSoon()) {
                openVenues.add(app.venues.get(v));
            }
        }
        if (!openVenues.isEmpty()) {
            return openVenues.get(((Double) (Math.random() * openVenues.size())).intValue()).getName();
        }
        return null;
    }

}
