package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.Weeks;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Blake on 2/20/14.
 */
public class PlusDollarsActivity extends BaseActivity
{
    private Thread update;
    private TextView name;
    private TextView expressHeader;
    private TextView express;
    private TextView plusHeader;
    private TextView plus;
    private TextView mealHeader;
    private TextView meal;
    private TextView budgetHeader;
    private TextView budget1;
    private TextView budget2;
    private View loginView;
    private EditText username;
    private EditText password;
    private CheckBox remember;
    private LayoutInflater factory;
    private ActionBar mActionBar;
    private Context mContext;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_plus_dollars);
        mActivity = this;
        mContext = this;
        mActionBar = getActionBar();
        init(mContext, mActionBar);
        getViews();

        setAlphaToZero();
        factory = LayoutInflater.from(this);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        name.setTypeface(font);
        Constants.user = new Account().loadAccount(getSharedPreferences(Constants.accSpKey,MODE_PRIVATE));
        //If the loaded Constants.user does not exist or the user said not to remember.
        if(Constants.user == null || !Constants.user.remember)
        {
            handleLogin();
        }
        else if(Constants.user.remember)
        {
            DateTime start = new DateTime();
            DateTime end = new DateTime(Constants.endOfSpring[0], Constants.endOfSpring[1], Constants.endOfSpring[2], 0, 0, 0, 0);
            Days d = Days.daysBetween(start, end);
            Weeks w = Weeks.weeksBetween(start, end);
            String temp = Constants.user.plusAsMoney();
            temp = temp.substring(1);

            name.setText(Constants.user.name);
            plus.setText(Constants.user.plusAsMoney());
            express.setText(Constants.user.expressAsMoney());
            meal.setText(Constants.user.meals + "");
            budget1.setText("$" + new BigDecimal(temp).divide(new BigDecimal(d.getDays()), 2, BigDecimal.ROUND_HALF_DOWN) + "/day");
            budget2.setText("$" + new BigDecimal(temp).divide(new BigDecimal(w.getWeeks()), 2, BigDecimal.ROUND_HALF_DOWN) + "/week");
        }
        fadeIn();
        update = buildThread(name,remember);
    }

    /**
     * Gets access to the views on screen for manipulation and animation.
     */
    private void getViews()
    {
        name = (TextView)findViewById(R.id.nameText);
        expressHeader = (TextView)findViewById(R.id.expHeader);
        express = (TextView)findViewById(R.id.expValue);
        plusHeader = (TextView)findViewById(R.id.plusHeader);
        plus = (TextView)findViewById(R.id.plusValue);
        mealHeader = (TextView)findViewById(R.id.mealHeader);
        meal = (TextView)findViewById(R.id.mealText);
        budgetHeader = (TextView) findViewById(R.id.budgetHeader);
        budget1 = (TextView) findViewById(R.id.budgetText1);
        budget2 = (TextView) findViewById(R.id.budgetText2);
    }

    /**
     * Prepares the views on the screen for fading in.
     */
    private void setAlphaToZero()
    {
        name.setAlpha(0);
        expressHeader.setAlpha(0);
        express.setAlpha(0);
        plusHeader.setAlpha(0);
        plus.setAlpha(0);
        mealHeader.setAlpha(0);
        meal.setAlpha(0);
        budgetHeader.setAlpha(0);
        budget1.setAlpha(0);
        budget2.setAlpha(0);
    }

    /**
     * Fades in UI elements. To add an elements, add 1 to the multiple(#) of duration and follow
     * the format "# * duration/2 + delay" for the cascading effect.
     */
    private void fadeIn()
    {
        final int duration = 300;
        final int delay = 150;
        name.animate().alpha(1.0f).setStartDelay(delay).setDuration(duration).start();
        expressHeader.animate().alpha(1.0f).setStartDelay(duration/2+delay).setDuration(duration).start();
        express.animate().alpha(1.0f).setStartDelay(2*duration/2+delay).setDuration(duration).start();
        plusHeader.animate().alpha(1.0f).setStartDelay(3 * duration/2+delay).setDuration(duration).start();
        plus.animate().alpha(1.0f).setStartDelay(4 * duration/2+delay).setDuration(duration).start();
        mealHeader.animate().alpha(1.0f).setStartDelay(5 * duration/2+delay).setDuration(duration).start();
        meal.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budgetHeader.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budget1.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budget2.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
    }
    protected void onResume()
    {
        super.onResume();
        if(Constants.user!= null && name != null)
        {
            setTextSizeName(Constants.user.name, name);
        }
    }
    protected void onStop()
    {
        super.onStop();
        if(Constants.user != null) {
            if (Constants.user.remember) {
                Constants.user.saveAccount(getSharedPreferences(Constants.accSpKey, MODE_PRIVATE));
            }
        }
    }

    /**
     * Helps auto size text based on the length of the users name.
     * @param name The name of the person from the meal plan website.
     * @param nameText The view to set the parameter name to.
     */
    private void setTextSizeName(String name, TextView nameText)
    {
        if(name.length() > 10 && name.length() < 15)
        {
            nameText.setTextSize(60f);
        }
        else if(name.length() >= 15 && name.length() < 20 )
        {
            nameText.setTextSize(50f);
        }
        else if(name.length() >= 20)
        {
            nameText.setTextSize(40f);
        }
    }

    /**
     * Handles a user login by giving the user a dialog to enter their information into.
     * Also starts the thread to get the user data and updates the UI.
     */
    private void handleLogin()
    {
        loginView = factory.inflate(R.layout.login_dialog, null);
        username = (EditText)loginView.findViewById(R.id.username);
        password = (EditText)loginView.findViewById(R.id.password);
        remember = (CheckBox)loginView.findViewById(R.id.remember);
        QustomDialogBuilder login = new QustomDialogBuilder(this);
        login.setTitleColor(Constants.CAL_POLY_GREEN);
        login.setDividerColor(Constants.CAL_POLY_GREEN);
        login.setTitle("Please login.");
        login.setCustomView(loginView, this);
        login.setPositiveButton("Login", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id)
            {
                Constants.user = new Account(username.getText().toString(), password.getText().toString(), remember.isChecked());
                setProgressBarIndeterminateVisibility(true);
                buildThread(name, remember).start();
                dialog.dismiss();
            }
        });
        login.show();
    }

    /**
     * Builds the thread used to get all of the plus dollars data.
     * @param name Name view to update with the name that is found.
     * @param remember Whether or not to remember the account.
     * @return A copy of the Thread.
     */
    private Thread buildThread(final TextView name, final CheckBox remember)
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                GetAllTheThings getPlusData = new GetAllTheThings(Constants.user);
                Constants.user = getPlusData.getTheThings();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Constants.user == null)
                        {
                            Toast.makeText(PlusDollarsActivity.this, "Unable to login. Please try again.", Toast.LENGTH_LONG).show();
                            handleLogin();
                        }
                        else
                        {
                            setTextSizeName(Constants.user.name, name);
                            name.setText(Constants.user.name);
                            if(remember != null)
                            {
                                if(remember.isChecked() && Constants.user != null)
                                {
                                    Constants.user.remember = true;
                                    Constants.user.saveAccount(getSharedPreferences(Constants.accSpKey,MODE_PRIVATE));
                                }
                            }
                            plus.setText(Constants.user.plusAsMoney());
                            express.setText(Constants.user.expressAsMoney());
                            meal.setText(Constants.user.meals+"");

                            DateTime start = new DateTime();
                            DateTime end = new DateTime(Constants.endOfSpring[0], Constants.endOfSpring[1], Constants.endOfSpring[2], 0, 0, 0, 0);
                            Days d = Days.daysBetween(start, end);
                            Weeks w = Weeks.weeksBetween(start, end);
                            String temp = Constants.user.plusAsMoney();
                            temp = temp.substring(1);

                            budget1.setText("$" + new BigDecimal(temp).divide(new BigDecimal(d.getDays()), 2, BigDecimal.ROUND_HALF_DOWN) + "/day");
                            budget2.setText("$" + new BigDecimal(temp).divide(new BigDecimal(w.getWeeks()), 2, BigDecimal.ROUND_HALF_DOWN) + "/week");
                        }
                        setProgressBarIndeterminateVisibility(false);
                        //used when TransactionActivity calls this activity, closes immediately for a more seamless transition
                        if(mActivity.getIntent().getExtras() != null && mActivity.getIntent().getExtras().getInt("login") == 1)
                            mActivity.finish();
                        setAlphaToZero();
                        fadeIn();
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plusdollars, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                new Thread(update).start();
                setProgressBarIndeterminateVisibility(true);
                return true;
            case R.id.login:
                handleLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
