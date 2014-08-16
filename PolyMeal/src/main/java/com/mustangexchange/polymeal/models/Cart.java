package com.mustangexchange.polymeal.models;

import android.os.Parcelable;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Cart
{
    protected static ArrayList<Item> cart = new ArrayList<Item>();

    public static void add(Item item)
    {
        cart.add(item);
        System.out.println(cart.size());
        MoneyTime.moneySpent = MoneyTime.moneySpent.add(cart.get(cart.size() - 1).getPrice());
    }
    public static void remove(int index)
    {
        MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(cart.get(index).getPrice());
        cart.remove(index);
    }
    public static ArrayList<Item> getCart()
    {
        ArrayList<Item> deepCopy = new ArrayList<Item>();
        for(Item i : cart)
        {
            deepCopy.add(i);
        }
        return deepCopy;
    }

    public static Item get(int pos)
    {
        return new Item(cart.get(pos));
    }

    public static <T extends Parcelable> void setCart(ArrayList<T> newCart)
    {
        cart = (ArrayList<Item>) newCart;
    }
    public static void clear()
    {
        int i = cart.size()-1;
        while(cart.size()>0)
        {
            MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(cart.get(i).getPrice());
            cart.remove(i);
            i--;
        }
    }

    public static void sort(int sortMode)
    {
        switch (sortMode)
        {
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