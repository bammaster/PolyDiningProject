package com.mustangexchange.polymeal;

import java.util.ArrayList;

/**
 * Created by Blake on 8/19/13.
 */
public class ItemSet
{
    private String title;
    private String desc;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> prices = new ArrayList<String>();
    public ItemSet(String title,ArrayList<String> names,ArrayList<String> prices)
    {
        this.title = title;
        this.names = names;
        this.prices = prices;
    }
    public ItemSet(){}
    public ArrayList<String> getNames()
    {
        return names;
    }
    public ArrayList<String> getPrices()
    {
        return prices;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void clear() {
        names.clear();
        prices.clear();
    }

}
