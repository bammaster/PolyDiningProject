package com.mustangexchange.polymeal;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.math.BigDecimal;

public class ItemSet {
    private String title;
    private ArrayList<Item> items;

    public ItemSet(String title, ArrayList<Item> items)
    {
        this.title = title;
        this.items = items;
    }

    public ItemSet() {}

    public int size()
    {
        return items.size();
    }

    public void add(Item item)
    {
        items.add(item);
    }

    public void addAt(Item item,int index)
    {
        items.add(index,item);
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Item getItem(int index)
    {
        return items.get(index);
    }

}