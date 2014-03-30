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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Blake on 2/20/14.
 */
public class PlusDollarsActivity extends BaseActivity
{
    //protected static Account account;
    private Thread update;
    private TextView name;
    private TextView expressHeader;
    private TextView express;
    private TextView plusHeader;
    private TextView plus;
    private TextView mealHeader;
    private TextView meal;
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
            name.setText(Constants.user.name);
            plus.setText(Constants.user.plusAsMoney());
            express.setText(Constants.user.expressAsMoney());
            meal.setText(Constants.user.meals + "");
        }
        fadeIn();
        update = buildThread(name,remember);
    }

    /**
     * Gets access to the views on screen for manipulation and animation.
     */
    private void getViews()
    {
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //mDrawerItems = getResources().getStringArray(R.array.drawerItemsPlus);
        name = (TextView)findViewById(R.id.nameText);
        expressHeader = (TextView)findViewById(R.id.expHeader);
        express = (TextView)findViewById(R.id.expValue);
        plusHeader = (TextView)findViewById(R.id.plusHeader);
        plus = (TextView)findViewById(R.id.plusValue);
        mealHeader = (TextView)findViewById(R.id.mealHeader);
        meal = (TextView)findViewById(R.id.mealText);
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
    /* The click listner for ListView in the navigation drawer
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final int delay = 200;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        switch(position)
                        {
                            case 0:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyDiningActivity.class));
                                break;
                            case 1:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyMealActivity.class));
                                break;
                            case 2:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PlusDollarsActivity.class));
                                Thread.sleep(delay);
                                break;
                            case 3:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, SettingsActivity.class));
                                break;
                            case 4:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, TransactionActivity.class));
                                break;
                            default:
                                Thread.sleep(delay);
                                startActivity(new Intent(mContext, PolyDiningActivity.class));
                                break;
                        }
                    }
                    catch(InterruptedException e)
                    {
                        Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG);
                    }
                }
            }).start();
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }*/
}
