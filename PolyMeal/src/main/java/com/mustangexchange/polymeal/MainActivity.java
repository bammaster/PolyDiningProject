package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity
{

    private TextView download;
    private ProgressBar downloadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = (TextView)findViewById(R.id.textDownload);
        downloadProgress = (ProgressBar)findViewById(R.id.progressBar);
        final TextView welcome = (TextView)findViewById(R.id.welcomeText);
        final TextView select = (TextView)findViewById(R.id.selectText);
        final Button sandwich = (Button)findViewById(R.id.buttonSand);
        final Button vista = (Button)findViewById(R.id.buttonVista);
        final Button taco = (Button)findViewById(R.id.buttonTaco);

        //animates in objects to screen.
        select.setVisibility(View.INVISIBLE);
        sandwich.setVisibility(View.INVISIBLE);
        vista.setVisibility(View.INVISIBLE);
        taco.setVisibility(View.INVISIBLE);
        Constants.inAnimation.setDuration(1000);
        Constants.inAnimation2.setDuration(1000);
        welcome.startAnimation(Constants.inAnimation);
        Constants.inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation)
            {
                select.setVisibility(View.VISIBLE);
                sandwich.setVisibility(View.VISIBLE);
                vista.setVisibility(View.VISIBLE);
                taco.setVisibility(View.VISIBLE);
                select.startAnimation(Constants.inAnimation2);
                sandwich.startAnimation(Constants.inAnimation2);
                vista.startAnimation(Constants.inAnimation2);
                taco.setAnimation(Constants.inAnimation2);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    //start onClick methods for each venue
    public void sandwich(View v)
    {
        Constants.venues.get(0).checkVenueCart(this);
    }

    public void vg(View v)
    {
        Constants.venues.get(1).checkVenueCart(this);
    }

    public void taco(View v)
    {
        Constants.venues.get(2).checkVenueCart(this);
    }

    public void bagel(View v)
    {
        Constants.venues.get(3).checkVenueCart(this);
    }
    //end onClick methods for each venue
}