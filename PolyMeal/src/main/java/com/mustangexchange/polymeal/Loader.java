package com.mustangexchange.polymeal;
/*
import android.util.Log;

import java.util.ArrayList;

public class Loader
{
    private ArrayList<ItemSet> sets;
    private ArrayList<String> boundNames = new ArrayList<String>();
    private ArrayList<String> boundPrices = new ArrayList<String>();
    public Loader(ArrayList<ItemSet> sets)
    {
        this.sets = sets;
    }
    //call both these methods to get access to array lists before calling load
    public ArrayList<String> getBoundNames()
    {
        return boundNames;
    }
    public ArrayList<String> getBoundPrices()
    {
        return boundPrices;
    }
    public void load(int lower,int upper)
    {
        int i = lower+1;
        while(i<upper)
        {
            if(!(names.get(i).equals("Grab & Go")))
            {
                boundNames.add(sets.names.get(i));
                boundPrices.add(sets,get(i).prices.get(i));
            }
            i++;
        }
    }
    public void loadAll()
    {
        int i = 0;
        while(i<names.size())
        {
            String temp = names.get(i);
            if(!(temp.contains("h2!#$")))
            {
                boundNames.add(temp);
                boundPrices.add(prices.get(i));
            }
            i++;
        }
    }
}
*/