package com.themotlcode.polydining.Sorting;


import com.themotlcode.polydining.models.Item;

import java.util.Comparator;

public class ItemNameComparator implements Comparator<Item> {
    Boolean reverse;

    public ItemNameComparator(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public int compare(Item a, Item b) {
        return reverse ? a.getName().compareTo(b.getName()) : a.getName().compareTo(b.getName());
    }

}