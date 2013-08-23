package com.mustangexchange.polymeal;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Blake on 8/9/13.
 */
public class Cart
{
    private static ArrayList<String> cart = new ArrayList<String>();
    private static ArrayList<String> cartMoney = new ArrayList<String>();
    public static void add(String item,String price)
    {
        cart.add(item);
        cartMoney.add(price);
    }
    public static void remove(int index)
    {
        cart.remove(index);
        MoneyTime.moneySpent-= new Double(cartMoney.get(index));
        cartMoney.remove(index);
    }
    //returns cart
    public static ArrayList<String> get()
    {
        return cart;
    }
    public static void clear()
    {
        int i = cart.size()-1;
        while(cart.size()>0)
        {
            MoneyTime.moneySpent-=new Double(cartMoney.get(i));
            cart.remove(i);
            cartMoney.remove(i);
            i--;
        }
        //cart = new ArrayList<String>();
        //cartMoney = new ArrayList<String>();

    }

}
