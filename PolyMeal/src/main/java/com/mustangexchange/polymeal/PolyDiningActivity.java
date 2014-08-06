package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Blake on 2/16/14.
 */
public class PolyDiningActivity extends Activity
{
    private TextView welcome;
    private TextView header;
    private Button plus;
    private Button meal;
    private QustomDialogBuilder greeting;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly_dining);
        welcome = (TextView)findViewById(R.id.welcomeText);
        header = (TextView)findViewById(R.id.mainHeader);
        plus = (Button)findViewById(R.id.plusDollarsButton);
        meal = (Button)findViewById(R.id.polyMealButton);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        welcome.setTypeface(font);
        welcome.setAlpha(0);
        header.setAlpha(0);
        plus.setAlpha(0);
        meal.setAlpha(0);
        fadeIn();
        greeting = new QustomDialogBuilder(this);
        greeting.setTitle("Greeting");
        greeting.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        getConfigFromWeb();
    }
    private void fadeIn()
    {
        final int duration = 300;
        final int delay = 100;
        welcome.animate().alpha(1.0f).setStartDelay(delay).setDuration(duration).start();
        header.animate().alpha(1.0f).setStartDelay(duration/2+delay).setDuration(duration).start();
        plus.animate().alpha(1.0f).setStartDelay(duration + duration/2+delay).setDuration(duration).start();
        meal.animate().alpha(1.0f).setStartDelay(2 * duration + duration/2+delay).setDuration(duration).start();
    }
    public void polymeal(View v)
    {
        startActivity(new Intent(this,PolyMealActivity.class));
    }
    public void plusdollars(View v)
    {
        startActivity(new Intent(this,PlusDollarsActivity.class));
    }
    public void getConfigFromWeb()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getDates();
                    getMessage();
                    getColor();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.CAL_POLY_GREEN)));
                            greeting.setTitleColor(Constants.CAL_POLY_GREEN);
                            greeting.setDividerColor(Constants.CAL_POLY_GREEN);
                            greeting.show();
                        }
                    });
                }
                catch(IOException e){
                    Log.e("Blake","IOError: ",e);
                }
            }
        }).start();
    }
    private void getDates() throws IOException
    {
        URL dateUrl = new URL(Constants.DATE_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String start = br.readLine();
        String end = br.readLine();
        String[] temp = end.split("/");
        if(checkDate(temp)) {
            Statics.endOfQuarter = new int[3];
            Statics.endOfQuarter[0] = new Integer(temp[2]);
            Statics.endOfQuarter[1] = new Integer(temp[0]);
            Statics.endOfQuarter[2] = new Integer(temp[1]);
        }
        temp = start.split("/");
        if(checkDate(temp)) {
            Statics.startOfQuarter = new int[3];
            Statics.startOfQuarter[0] = new Integer(temp[2]);
            Statics.startOfQuarter[1] = new Integer(temp[0]);
            Statics.startOfQuarter[2] = new Integer(temp[1]);
        }
    }
    private void getMessage() throws IOException
    {
        URL dateUrl = new URL(Constants.MESSAGE_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
            sb.append(line);
        greeting.setMessage(sb.toString());
    }
    private void getColor() throws IOException
    {
        URL dateUrl = new URL(Constants.COLOR_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Constants.CAL_POLY_GREEN = "#" + br.readLine();
    }
    private boolean checkDate(String[] dates) {
        if (dates.length != Constants.DATE_ARRAY_SIZE) {
            return false;
        } else {
            for (int i = 0; i < dates.length; i++) {
                try {
                    dates[i].replace(" ", "");
                    Integer.parseInt(dates[i]);
                } catch (NumberFormatException ne) {
                    return false;
                }
            }
        }
        return true;
    }
}
