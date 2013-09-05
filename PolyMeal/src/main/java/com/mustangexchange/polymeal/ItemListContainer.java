package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blake on 9/1/13.
 */
public class ItemListContainer
{
    public static ArrayList<ItemSet> vgItems = new ArrayList<ItemSet>();
    public static ArrayList<ItemSet> sandItems = new ArrayList<ItemSet>();
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();
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
