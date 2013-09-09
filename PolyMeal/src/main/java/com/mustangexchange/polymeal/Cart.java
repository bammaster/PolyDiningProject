package com.mustangexchange.polymeal;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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
    /*public static void saveCart(SharedPreferences sp)
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
    }*/

}
