package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Blake on 2/16/14.
 */
public class PolyDiningActivity extends Activity
{
    private TextView welcome;
    private TextView header;
    private Button plus;
    private Button meal;
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
        QustomDialogBuilder login = new QustomDialogBuilder(this);
        if(getSharedPreferences(Constants.spKey, MODE_PRIVATE).getInt("MessageClick",0)==0) {
            login.setTitleColor(Constants.CAL_POLY_GREEN);
            login.setDividerColor(Constants.CAL_POLY_GREEN);
            login.setTitle("Hello!");
            login.setMessage("The Plus Dollars issue has been fixed. The app was dividing by 0 since there are \"0 weeks\" " +
                    "left in the quarter and ripped a hole in the space time continuum causing the app to crash. Anyway, good luck on finals and have an awesome summer!");
            login.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getSharedPreferences(Constants.spKey, MODE_PRIVATE).edit().putInt("MessageClick", 1).commit();

                }
            });
            login.show();
        }
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
}
