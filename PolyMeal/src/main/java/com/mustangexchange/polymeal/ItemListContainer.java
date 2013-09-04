package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Blake on 9/1/13.
 */
public class ItemListContainer
{
    public static ArrayList<ItemSet> vgItems = new ArrayList<ItemSet>();
    public static ArrayList<ItemSet> sandItems = new ArrayList<ItemSet>();
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();
    public void loadFromCache(SharedPreferences sp)
    {
        Gson gson = new Gson();
        String vg = sp.getString("Vista Grande Items", "");
        vgItems = gson.fromJson(vg,gsonType);
        String sand = sp.getString("Sandwich Factory Items", "");
        sandItems = gson.fromJson(sand, gsonType);
    }
}
