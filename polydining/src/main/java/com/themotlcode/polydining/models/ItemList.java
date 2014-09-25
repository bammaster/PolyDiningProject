package com.themotlcode.polydining.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This class is used to categorize a set of items within a Venue's menu.
 * For example Sandwich Stop at VG's has a set of items associated with it.
 */
public class ItemList {
    /**
     * The title of this set of items.
     */
    private String title;

    /**
     * The actual set of items.
     */
    private ArrayList<Item> items;

    /**
     * The default constructor.
     */
    public ItemList() {
        items = new ArrayList<Item>();
    }

    /**
     * Builds a new ItemSet with a title.
     *
     * @param title The ItemSet title.
     */
    public ItemList(String title) {
        this.title = title;
        items = new ArrayList<Item>();
    }

    /**
     * Builds a new ItemSet with another ItemSet.
     *
     * @param itemList The ItemSet to deep copy.
     */
    public ItemList(ItemList itemList) {
        this.title = itemList.title;
        items = new ArrayList<Item>();
        for (Item item : itemList.items) {
            items.add(new Item(item));
        }
    }

    /**
     * Gets the title of this ItemSet.
     *
     * @return The title of this ItemSet.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets a deep copy of the ItemSet.
     *
     * @return A deep copy of the ItemSet.
     */
    public ArrayList<Item> getItems() {
        ArrayList<Item> deepCopy = new ArrayList<Item>();
        for (Item item : items) {
            deepCopy.add(new Item(item));
        }
        return deepCopy;
    }

    /**
     * Gets the number of items on the menu for this ItemSet.
     *
     * @return The number of items.
     */
    public int numberOfItems() {
        return items.size();
    }

    /**
     * Add a deep copy of an Item to this ItemSet.
     *
     * @param item The item to add.
     */
    public void add(Item item) {
        items.add(new Item(item));
    }

    /**
     * Gets the Item at the specified index.
     *
     * @param index The index of the Item to get.
     * @return The Item at the index.
     */
    public Item getItem(int index) {
        Iterator<Item> itemIter = items.iterator();
        for (int iter = 0; iter <= index; iter++) {
            if (itemIter.hasNext()) {
                Item item = itemIter.next();
                if (iter == index) {
                    return item;
                }
            }
        }
        return null;
    }

    public void sortItems(Comparator<Item> sorter) {
        Collections.sort(items, sorter);
    }
}
