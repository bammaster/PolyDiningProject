package com.mustangexchange.polymeal;

import com.jamireh.PolyDiningDemo.models.ItemSet;

//VenueListPresenter holds the ItemSet for the current page in the ViewPager

public class VenueListPresenter extends MealPresenter
{
    ItemSet items;

    public VenueListPresenter(ItemSet items)
    {
        this.items = items;
    }
}
