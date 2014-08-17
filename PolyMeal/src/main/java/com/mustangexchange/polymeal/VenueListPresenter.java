package com.mustangexchange.polymeal;

//VenueListPresenter holds the ItemSet for the current page in the ViewPager

import com.mustangexchange.polymeal.models.ItemSet;

public class VenueListPresenter extends MealPresenter
{
    ItemSet items;

    public VenueListPresenter(ItemSet items)
    {
        this.items = items;
    }
}
