package com.mustangexchange.polymeal.Sorting;


import com.mustangexchange.polymeal.Item;

import java.util.Comparator;


public class ItemPriceComparator implements Comparator<Item> {

    @Override
    public int compare(Item a, Item b) {
        return a.getPrice().compareTo(b.getPrice());
    }

}