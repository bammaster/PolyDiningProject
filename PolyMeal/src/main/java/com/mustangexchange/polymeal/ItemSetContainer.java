package com.mustangexchange.polymeal;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Blake on 9/1/13.
 */
public class ItemSetContainer
{
    public static ArrayList<ItemSet> vgItems;
    public static ArrayList<ItemSet> sandItems;
    public static ArrayList<ItemSet> tacoItems;
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    public ItemSetContainer(){}

    public ItemSetContainer(ArrayList<ItemSet> sandItems,ArrayList<ItemSet> vgItems,ArrayList<ItemSet> tacoItems)
    {
        this.vgItems = vgItems;
        this.sandItems = sandItems;
        this.tacoItems = tacoItems;
    }

    public void loadFromCache(SharedPreferences sp)
    {
        Gson gson = new Gson();
        String vg = sp.getString("Vista Grande Items", "");
        vgItems = gson.fromJson(vg,gsonType);
        String sand = sp.getString("Sandwich Factory Items", "");
        sandItems = gson.fromJson(sand, gsonType);
        String taco = sp.getString("Tacos to Go Items","");
        tacoItems = gson.fromJson(taco,gsonType);
    }
}
//testing git push
