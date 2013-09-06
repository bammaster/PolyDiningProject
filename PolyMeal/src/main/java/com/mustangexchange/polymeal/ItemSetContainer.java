package com.mustangexchange.polymeal;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Blake on 9/1/13.
 */
public class ItemSetContainer
{
    public static ArrayList<ItemSet> vgItems;
    public static ArrayList<ItemSet> sandItems;
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    public ItemSetContainer(){}

    public ItemSetContainer(ArrayList<ItemSet> sandItems,ArrayList<ItemSet> vgItems)
    {
        this.vgItems = vgItems;
        this.sandItems = sandItems;
    }

    public void loadFromCache(SharedPreferences sp)
    {
        Gson gson = new Gson();
        String vg = sp.getString("Vista Grande Items", "");
        vgItems = gson.fromJson(vg,gsonType);
        String sand = sp.getString("Sandwich Factory Items", "");
        sandItems = gson.fromJson(sand, gsonType);
    }
    /*public ItemSet sortByName(ItemSet is) {
        String title = is.getTitle();
        ArrayList<String> names = new ArrayList<String>(is.getNames());
        ArrayList<String> prices = new ArrayList<String>(is.getPrices());
        ArrayList<String> desc = new ArrayList<String>(is.getDesc());

        /* guava, throws an IllegalStateException if your array aren't of the same length
        Preconditions.checkState(names.size() == prices.size(), "data and names must be of equal length");

        /* put your values in a MultiMap
        Multimap<String, String> mmap = LinkedListMultimap.create();
        for (int i=0; i<names.size(); i++) {
            mmap.put(names.get(i), prices.get(i));
        }

        /* our output, 'newArrayList()' is just a guava convenience function
        List<String> sortedNames = Lists.newArrayList();
        List<String> sortedPrices = Lists.newArrayList();

        /* cycle through a sorted copy of the MultiMap's keys...
        for (String name : Ordering.natural().sortedCopy(mmap.keys())) {

            /* ...and add all of the associated values to the lists
            for (String value : mmap.get(name)) {
                sortedNames.add(name);
                sortedPrices.add(value);
            }
        }
        return new ItemSet(title, (ArrayList) sortedNames, (ArrayList) sortedPrices, desc);
    }
    public static ItemSet sortByPrice(ItemSet is) {
        String title = is.getTitle();
        ArrayList<String> names = new ArrayList<String>(is.getNames());
        ArrayList<String> prices = new ArrayList<String>(is.getPrices());
        ArrayList<String> desc = new ArrayList<String>(is.getDesc());

        /* guava, throws an IllegalStateException if your array aren't of the same length
        Preconditions.checkState(names.size() == prices.size(), "data and names must be of equal length");

        /* put your values in a MultiMap
        Multimap<String, String> mmap = LinkedListMultimap.create();
        for (int i=0; i<names.size(); i++) {
            mmap.put(prices.get(i), names.get(i));
        }

        /* our output, 'newArrayList()' is just a guava convenience function
        List<String> sortedNames = Lists.newArrayList();
        List<String> sortedPrices = Lists.newArrayList();

        /* cycle through a sorted copy of the MultiMap's keys...
        for (String value : Ordering.natural().reverse().sortedCopy(mmap.keys())) {

            /* ...and add all of the associated values to the lists
            for (String name : mmap.get(value)) {
                sortedNames.add(name);
                sortedPrices.add(value);
            }
        }
        return new ItemSet(title, (ArrayList) sortedNames, (ArrayList) sortedPrices);
    }*/
}
