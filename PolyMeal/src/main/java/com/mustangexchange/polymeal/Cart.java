package com.mustangexchange.polymeal;

import java.util.ArrayList;

public class Cart
{
    private static ArrayList<Item> cart = new ArrayList<Item>();

    public static void add(Item item)
    {
        cart.add(item);
        MoneyTime.moneySpent = MoneyTime.moneySpent.add(cart.get(cart.size() - 1).getPrice());
    }
    public static void remove(int index)
    {
        MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(cart.get(index).getPrice());
        cart.remove(index);
    }
    public static ArrayList<Item> getCart()
    {
        return cart;
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
}