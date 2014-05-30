package com.mustangexchange.polymeal.Sorting;

import com.mustangexchange.polymeal.Venue;

import java.util.Comparator;

/**
 * Created by Blake on 5/10/2014.
 */
public class VenueNameComparator implements Comparator<String> {
    @Override
    public int compare(String a, String b)
    {
        return a.compareTo(b);
    }
}
