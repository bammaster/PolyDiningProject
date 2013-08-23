package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static ArrayList<ItemSet> vgItems;
    public static ArrayList<ItemSet> sandItems;
    private Parser parseHtml;
    private Thread internet;
    public static int vgOrSand;
    private Handler uiUpdate= new Handler();
    private TextView download;
    private ProgressBar downloadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = (TextView)findViewById(R.id.textDownload);
        downloadProgress = (ProgressBar)findViewById(R.id.progressBar);
        final TextView welcome = (TextView)findViewById(R.id.welcomeText);
        final TextView select = (TextView)findViewById(R.id.selectText);
        final Button sandwich = (Button)findViewById(R.id.buttonSand);
        final Button vista = (Button)findViewById(R.id.buttonVista);
        Cart.clear();
        vgItems = new ArrayList<ItemSet>();
        sandItems = new ArrayList<ItemSet>();
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        final Animation in2 = new AlphaAnimation(0.0f, 1.0f);
        select.setVisibility(View.INVISIBLE);
        sandwich.setVisibility(View.INVISIBLE);
        vista.setVisibility(View.INVISIBLE);
        in.setDuration(1000);
        in2.setDuration(1000);
        welcome.startAnimation(in);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                select.setVisibility(View.VISIBLE);
                sandwich.setVisibility(View.VISIBLE);
                vista.setVisibility(View.VISIBLE);
                select.startAnimation(in2);
                sandwich.startAnimation(in2);
                vista.startAnimation(in2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        internet = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection one = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1014&name=VG%20Cafe").timeout(2000);
                    Connection two = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1012&name=Sandwich%20Factory").timeout(2000);
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            vista.setEnabled(false);
                            sandwich.setEnabled(false);
                            download.setVisibility(View.VISIBLE);
                            downloadProgress.setVisibility(View.VISIBLE);
                        }
                    });
                    Document docVg = one.get();
                    parseHtml = new Parser(vgItems);
                    parseHtml.parse(docVg);
                    Document docSand = two.get();
                    parseHtml = new Parser(sandItems);
                    parseHtml.parse(docSand);
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            vista.setEnabled(true);
                            sandwich.setEnabled(true);
                            download.setVisibility(View.INVISIBLE);
                            downloadProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (Exception e) {
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder onErrorConn= new AlertDialog.Builder(MainActivity.this);
                            onErrorConn.setTitle("Error Connecting!");
                            onErrorConn.setMessage("There was an error connecting to the website to download the menu. Please check your data connection and try again.");
                            onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    finish();
                                }
                            });
                            onErrorConn.show();
                        }
                    });
                }
            }
        });
        internet.start();

    }

    public void onResume()
    {
        super.onResume();
        vgItems = new ArrayList<ItemSet>();
        sandItems= new ArrayList<ItemSet>();
        Cart.clear();
    }

    public void vg(View v)
    {
        startActivity(new Intent(this,VistaActivity.class));
        vgOrSand = 1;
    }

    public void sandwich(View v)
    {
        LoadListActivity.whichToLoad = "sandFac";
        startActivity(new Intent(this,SandwichActivity.class));
        vgOrSand = 2;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}