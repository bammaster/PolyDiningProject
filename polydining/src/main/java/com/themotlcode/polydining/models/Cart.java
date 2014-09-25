package com.themotlcode.polydining.models;

import com.themotlcode.polydining.PolyApplication;
import com.themotlcode.polydining.Sorting.ItemNameComparator;
import com.themotlcode.polydining.Sorting.ItemPriceComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Data Entity modelling the cart.
 */

public class Cart {
    protected ArrayList<Item> cart = new ArrayList<Item>();

    public Cart() {
    }

    /**
     * Add Item to Cart
     *
     * @param item item to add
     */
    public void add(Item item) {
        cart.add(item);
        item.numInCart += 1;
        MoneyTime.moneySpent = MoneyTime.moneySpent.add(cart.get(cart.size() - 1).getPrice());
        item.save();
    }

    /**
     * Remove Item at index from Cart
     *
     * @param index index of Item to remove from Cart
     */
    public void remove(int index) {
        MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(cart.get(index).getPrice());
        cart.get(index).numInCart -= 1;
        if (cart.get(index).numInCart == 0) {
            Item.findById(Item.class, cart.get(index).getId()).delete();
        } else {
            cart.get(index).save();
        }
        cart.remove(index);
    }

    /**
     * Get a deep copy of the ArrayList of Items stored in the Cart
     *
     * @return ArrayList of Items
     */
    public ArrayList<Item> getCart() {
        ArrayList<Item> deepCopy = new ArrayList<Item>();
        for (Item i : cart) {
            deepCopy.add(i);
        }
        return deepCopy;
    }

    /**
     * Get the number of Items in the Cart
     *
     * @return size of the Item ArrayList
     */
    public int size() {
        return cart.size();
    }

    /**
     * Get an Item at index pos
     *
     * @param pos index of the Item in the ArrayList
     * @return
     */
    public Item get(int pos) {
        return new Item(cart.get(pos));
    }

    /**
     * For database use, set the Cart; linearly uses add() method
     *
     * @param items ArrayList to set
     */
    public void setCart(ArrayList<Item> items) {
        for (Item item : items) {
            cart.add(item);
            MoneyTime.moneySpent = MoneyTime.moneySpent.add(cart.get(cart.size() - 1).getPrice());
        }
    }

    /**
     * Clears the cart of all Items; linearly uses remove()
     */
    public void clear() {
        int i = cart.size() - 1;
        while (cart.size() > 0) {
            MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(cart.get(i).getPrice());
            cart.remove(i);
            i--;
        }

        for (Item item : cart) {
            item.numInCart = 0;
        }
        Item.deleteAll(Item.class);
    }

    /**
     * Sorts the cart based on a sortMode
     *
     * @param sortMode 0 for A-Z, 1 for Z-A, 2 for Low-High, & 3 for High-Low
     */
    public void sort(int sortMode) {
        switch (sortMode) {
            case 0:
                Collections.sort(cart, new ItemNameComparator(false));
                break;
            case 1:
                Collections.sort(cart, new ItemNameComparator(true));
                break;
            case 2:
                Collections.sort(cart, new ItemPriceComparator(false));
                break;
            case 3:
                Collections.sort(cart, new ItemPriceComparator(true));
                break;
            default:
                break;
        }
    }
}