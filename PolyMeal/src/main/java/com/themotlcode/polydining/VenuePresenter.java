package com.themotlcode.polydining;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.themotlcode.polydining.Sorting.ItemNameComparator;
import com.themotlcode.polydining.Sorting.ItemPriceComparator;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.ItemList;
import com.themotlcode.polydining.models.MoneyTime;

import java.math.BigDecimal;
import java.util.ArrayList;

//VenuePresenter communicates with models to update the ActionBar subtitle and hold the ArrayList<ItemSet> for the current venue

public class VenuePresenter extends MealPresenter
{

    private VenueFragment fragment;
    private ArrayList<ItemList> itemLists;
    private PolyApplication app;

    public VenuePresenter(Fragment fragment)
    {
        this.app = (PolyApplication) fragment.getActivity().getApplication();
        this.fragment = (VenueFragment) fragment;
        this.itemLists = app.venues.get(app.activityTitle).getVenueItemLists();
        setFragment(fragment);
        updateSettings();

    }

    protected boolean loadFromCache(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            MoneyTime.setMoneySpent(new BigDecimal(savedInstanceState.getString("moneySpent")));
            Cart.setCart(savedInstanceState.getParcelableArrayList("cart"));
            app.lastVenue = savedInstanceState.getString("lastVenue");
            return true;
        }
        return false;
    }

    protected void updateSettings()
    {
        SharedPreferences defaultSP;
        int sortMode;
        defaultSP = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));


        for(ItemList itemList : itemLists)
        {
            switch (sortMode)
            {
                case 0:
                    itemList.sortItems(new ItemNameComparator(false));
                    break;
                case 1:
                    itemList.sortItems(new ItemNameComparator(true));
                    break;
                case 2:
                    itemList.sortItems(new ItemPriceComparator(false));
                    break;
                case 3:
                    itemList.sortItems(new ItemPriceComparator(true));
                    break;
                default:
                    break;
            }
        }

    }

    protected VenueAdapter adapterInit(FragmentManager supportFragmentManager)
    {
        return new VenueAdapter(supportFragmentManager);
    }

    protected String getListTitle(int pos)
    {
        return app.venues.get(app.lastVenue).getVenueItemLists().get(pos).getTitle();
    }

    protected int getListCount()
    {
        return app.venues.get(app.lastVenue).numberOfItemSets();
    }

    protected class VenueAdapter extends FragmentStatePagerAdapter
    {

        public VenueAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            return new VenueListFragment(i, new VenueListPresenter(itemLists.get(i)));
        }

        @Override
        public int getCount()
        {
            return getListCount();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return getListTitle(position);
        }
        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

    }
}
