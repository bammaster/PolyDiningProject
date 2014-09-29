package com.mustangexchange.polymeal;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.mustangexchange.polymeal.models.Item;
import com.mustangexchange.polymeal.models.ItemList;
import com.mustangexchange.polymeal.models.MoneyTime;
import com.mustangexchange.polymeal.models.Venue;

import java.math.BigDecimal;

/**
 * Created by jon on 9/12/14.
 */
public class MealCompleterPresenter extends MealPresenter {
    private MealCompleterFragment fragment;
    private PolyApplication app;

    private ItemList possibleItems;

    public MealCompleterPresenter(Fragment fragment) {
        this.fragment = (MealCompleterFragment) fragment;
        this.app = (PolyApplication) fragment.getActivity().getApplication();
        setFragment(fragment);
    }

    public void calcPossibleItems() {
        possibleItems = new ItemList();
        for (Venue venue : app.venues.values()) {
            if (venue.getName().equals(app.activityTitle))
                for (ItemList itemList : venue.getVenueItemLists()) {
                    for (Item item : itemList.getItems()) {
                        if (item.getPrice().compareTo(MoneyTime.calcTotalMoney()) <= 0 && !(item.getPrice().compareTo(new BigDecimal("0.00")) == 0)) {
                            possibleItems.add(item);
                        }
                    }
                }
        }
        items = possibleItems;
    }

    private class CalcCompleter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            possibleItems = new ItemList();
            for (Venue venue : app.venues.values()) {
                if (venue.getName().equals(app.activityTitle))
                    for (ItemList itemList : venue.getVenueItemLists()) {
                        for (Item item : itemList.getItems()) {
                            if (item.getPrice().compareTo(MoneyTime.calcTotalMoney()) <= 0) {
                                possibleItems.add(item);
                            }
                        }
                    }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
