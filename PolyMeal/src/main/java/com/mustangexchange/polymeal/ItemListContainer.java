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
    private Parser parseHtml;
    private Handler uiUpdate= new Handler();
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    public void refresh(Context context,Activity activity,ProgressDialog pd,ViewPager vp)
    {
        final Context mContext = context;
        final Activity mActivity = activity;
        final ProgressDialog mDialog = pd;
        final ViewPager mPager = vp;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    vgItems.clear();
                    sandItems.clear();
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.show();
                        }
                    });
                    Connection one = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1014&name=VG%20Cafe").timeout(10000);
                    Connection two = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1012&name=Sandwich%20Factory").timeout(10000);
                    Document docVg = one.get();
                    try
                    {
                        parseHtml = new Parser(ItemListContainer.vgItems);
                        parseHtml.parse(docVg,false);
                    }
                    catch(Exception e)
                    {
                        uiUpdate.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder onErrorConn= new AlertDialog.Builder(mContext);
                                onErrorConn.setTitle("Error Parsing!");
                                onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                                onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {
                                        mActivity.finish();
                                    }
                                });
                                onErrorConn.show();
                            }
                        });
                    }
                    Document docSand = two.get();
                    try
                    {
                        parseHtml = new Parser(ItemListContainer.sandItems);
                        parseHtml.parse(docSand,true);
                    }
                    catch(Exception e)
                    {
                        uiUpdate.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder onErrorConn= new AlertDialog.Builder(mContext);
                                onErrorConn.setTitle("Error Parsing!");
                                onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                                onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {
                                        mActivity.finish();
                                    }
                                });
                                onErrorConn.show();
                            }
                        });
                    }
                }
                catch (Exception e){
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder onErrorConn= new AlertDialog.Builder(mContext);
                            onErrorConn.setTitle("Error Connecting!");
                            onErrorConn.setMessage("There was an error connecting to the website to download the menu. Please check your data connection and try again.");
                            onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    mActivity.finish();
                                }
                            });
                            onErrorConn.show();
                        }
                    });
                }
                uiUpdate.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                });
            }
        }).start();
    }
    public void loadFromCache(SharedPreferences sp,ViewPager vp)
    {
        Gson gson = new Gson();
        String vg = sp.getString("Vista Grande Items", "");
        vgItems = gson.fromJson(vg,gsonType);
        String sand = sp.getString("Sandwich Factory Items", "");
        sandItems = gson.fromJson(sand, gsonType);
        vp.getAdapter().notifyDataSetChanged();
    }
}
