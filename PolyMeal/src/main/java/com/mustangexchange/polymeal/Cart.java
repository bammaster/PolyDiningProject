package com.mustangexchange.polymeal;

import java.math.BigDecimal;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Blake on 8/9/13.
 */
public class Cart
{
    private static ArrayList<String> cart = new ArrayList<String>();
    private static ArrayList<String> cartMoney = new ArrayList<String>();
    private static Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();
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
    public static void saveCart(SharedPreferences sp)
    {
        int slot = sp.getInt("slot",0);
        SharedPreferences.Editor prefsEditor = sp.edit();
        Gson gson = new Gson();
        String cart = gson.toJson(Cart.cart,gsonType);
        String cartPrice = gson.toJson(Cart.cartMoney,gsonType);
        prefsEditor.putString("Cart-Name-Slot "+slot, cart);
        prefsEditor.putString("Cart-Price-Slot "+slot, cartPrice);
        slot++;
        prefsEditor.putInt("slot",slot);
        prefsEditor.commit();
    }
    public static void loadCart(SharedPreferences sp, int slot)
    {
        cart.clear();
        cartMoney.clear();
        Gson gson = new Gson();
        cart = gson.fromJson(sp.getString("Cart-Name-Slot "+slot, "Error Loading!"),gsonType);
        cartMoney = gson.fromJson(sp.getString("Cart-Price-Slot "+slot, "Error Loading!"),gsonType);
    }
    public static void removeCart(SharedPreferences sp,int slot)
    {
        int totalSlot = sp.getInt("slot",0);
        SharedPreferences.Editor prefsEditor = sp.edit();
        Gson gson = new Gson();
        String cart = gson.toJson(Cart.cart,gsonType);
        prefsEditor.putString("Cart-Name-Slot "+slot, cart);
        prefsEditor.putString("Cart-Price-Slot "+slot, cart);
        for(int i = 0;i<slot;i++)
        {
            if(i>=slot)
            {
                prefsEditor.putString("Cart-Name-Slot "+i,sp.getString("Cart-Name-Slot "+i+1,"Error Loading!"));
                prefsEditor.putString("Cart-Name-Slot "+i,sp.getString("Cart-Rrice-Slot "+i+1,"Error Loading!"));
            }
        }
        slot--;
        prefsEditor.putInt("slot",slot);
        prefsEditor.commit();
    }

}
