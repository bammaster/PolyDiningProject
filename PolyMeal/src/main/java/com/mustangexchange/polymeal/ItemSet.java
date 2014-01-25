package com.mustangexchange.polymeal;

import java.util.ArrayList;

public class ItemSet {
    protected String title;
    protected ArrayList<Item> items = new ArrayList<Item>();

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