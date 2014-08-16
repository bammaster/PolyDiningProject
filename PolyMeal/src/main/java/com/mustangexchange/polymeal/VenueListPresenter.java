package com.mustangexchange.polymeal;

import android.support.v4.app.Fragment;
import com.mustangexchange.polymeal.models.ItemSet;

//VenueListPresenter holds the ItemSet for the current page in the ViewPager

public class VenueListPresenter extends Presenter
{
    private VenueListFragment fragment;
    private VenuePresenter presenter;
    ItemSet items;

    public VenueListPresenter(ItemSet items, VenuePresenter presenter)
    {
        this.items = items;
        this.presenter = presenter;
    }

    public void setFragment(Fragment fragment)
    {
        this.fragment = (VenueListFragment) fragment;
    }
    public void updateBalance()
    {
        presenter.fragment.updateBalance();
    }
}
