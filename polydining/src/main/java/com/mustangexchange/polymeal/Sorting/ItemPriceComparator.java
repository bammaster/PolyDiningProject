package com.mustangexchange.polymeal.Sorting;


import com.mustangexchange.polymeal.models.Item;

import java.util.Comparator;

public class ItemPriceComparator implements Comparator<Item> {
    Boolean reverse;

    public ItemPriceComparator(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public int compare(Item a, Item b) {
        return reverse ? b.getPrice().compareTo(a.getPrice()) : a.getPrice().compareTo(b.getPrice());
    }

}