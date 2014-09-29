package com.mustangexchange.polymeal.Sorting;


import com.mustangexchange.polymeal.models.Item;

import java.util.Comparator;

public class ItemNameComparator implements Comparator<Item> {
    Boolean reverse;

    public ItemNameComparator(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public int compare(Item a, Item b) {
        return reverse ? b.getName().compareTo(a.getName()) : a.getName().compareTo(b.getName());
    }

}