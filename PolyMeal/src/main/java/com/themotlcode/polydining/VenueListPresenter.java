package com.themotlcode.polydining;

//VenueListPresenter holds the ItemSet for the current page in the ViewPager

import com.themotlcode.polydining.models.ItemList;

public class VenueListPresenter extends MealPresenter
{
    ItemList items;

    public VenueListPresenter(ItemList items)
    {
        this.items = items;
    }
}
