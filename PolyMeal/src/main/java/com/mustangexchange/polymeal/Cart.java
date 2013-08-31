package com.mustangexchange.polymeal;

import java.math.BigDecimal;
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
        System.out.println("item: " + item + "price: " + price);
        MoneyTime.moneySpent = MoneyTime.moneySpent.add(new BigDecimal(price));
    }
    public static void remove(int index)
    {
        cart.remove(index);
        MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(new BigDecimal(cartMoney.get(index)));
        cartMoney.remove(index);
    }
    //returns cart
    public static ArrayList<String> getCart()
    {
        return cart;
    }
    public static ArrayList<String> getCartMoney()
    {
        return cartMoney;
    }
    public static void clear()
    {
        int i = cart.size()-1;
        while(cart.size()>0)
        {
            MoneyTime.moneySpent = MoneyTime.moneySpent.subtract(new BigDecimal(cartMoney.get(i)));
            cart.remove(i);
            cartMoney.remove(i);
            i--;
        }
    }

}
