package com.mustangexchange.polymeal.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemSet {
    protected String title;
    protected ArrayList<Item> items;

    public ItemSet(String title, ArrayList<Item> items)
    {
        this.title = title;
        this.items = items;
    }

    public ItemSet(ItemSet is)
    {
        this.title = is.title;
        this.items = new ArrayList<Item>();
        for(Item i : is.items)
        {
            this.items.add(i);
        }
    }

    public ItemSet(String title)
    {
        this.title = title;
        items = new ArrayList<Item>();
    }

    public void sortItems(Comparator<Item> comparator)
    {
        Collections.sort(items, comparator);
    }

    public ArrayList<Item> getItems()
    {
        ArrayList<Item> deepCopy = new ArrayList<Item>();
        for(Item i : items)
        {
            deepCopy.add(i);
        }
        return deepCopy;
    }

    public String getTitle()
    {
        return title;
    }

    public ItemSet()
    {
        items = new ArrayList<Item>();
    }
    public int size()
    {
        return items.size();
    }

    public void add(Item item)
    {
        items.add(new Item(item));
    }

    public void addAt(Item item,int index)
    {
        items.add(index,item);
    }

    public Item getItem(int index)
    {
        return items.get(index);
    }

    public void clear()
    {
        title = null;
        items.clear();
    }

}