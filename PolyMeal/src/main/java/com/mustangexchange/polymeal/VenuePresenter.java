package com.mustangexchange.polymeal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;
import com.mustangexchange.polymeal.models.Cart;
import com.mustangexchange.polymeal.models.ItemSet;
import com.mustangexchange.polymeal.models.MoneyTime;

import java.math.BigDecimal;
import java.util.ArrayList;

//VenuePresenter communicates with models to update the ActionBar subtitle and hold the ArrayList<ItemSet> for the current venue

public class VenuePresenter extends MealPresenter
{

    VenueFragment fragment;
    private ArrayList<ItemSet> itemSets;
    private VenuePresenter presenter;

    private static BigDecimal totalAmount;

    public VenuePresenter(Fragment fragment)
    {
        this.fragment = (VenueFragment) fragment;
        this.itemSets = Statics.venues.get(Statics.activityTitle).getVenueItems();
        this.presenter = this;
        presenter.setFragment(fragment);
        updateSettings();

    }

    boolean loadFromCache(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            System.out.println("got here");
            MoneyTime.setMoneySpent(new BigDecimal(savedInstanceState.getString("moneySpent")));
            Cart.setCart(savedInstanceState.getParcelableArrayList("cart"));
            Statics.lastVenue = savedInstanceState.getString("lastVenue");
            return true;
        }
        return false;
    }

    void updateSettings()
    {
        SharedPreferences defaultSP;
        int sortMode;
        defaultSP = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));


        for(ItemSet itemSet : itemSets)
        {
            switch (sortMode)
            {
                case 0:
                    itemSet.sortItems(new ItemNameComparator(false));
                    break;
                case 1:
                    itemSet.sortItems(new ItemNameComparator(true));
                    break;
                case 2:
                    itemSet.sortItems(new ItemPriceComparator(false));
                    break;
                case 3:
                    itemSet.sortItems(new ItemPriceComparator(true));
                    break;
                default:
                    break;
            }
        }

    }

    public PagerAdapter adapterInit(FragmentManager supportFragmentManager)
    {
        return new VenueAdapter(supportFragmentManager);
    }

    public class VenueAdapter extends FragmentStatePagerAdapter
    {

        public VenueAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            return new VenueListFragment(i, new VenueListPresenter(itemSets.get(i)));
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

    public String getListTitle(int pos)
    {
        return Statics.venues.get(Statics.lastVenue).getVenueTitle(pos);
    }

    public int getListCount()
    {
        return Statics.venues.get(Statics.lastVenue).getVenueItems().size();
    }
}
