package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jon on 3/23/14.
 */
public class TransactionActivity extends BaseActivity {

    //protected static Account Constants.user = Constants.user;
    protected Thread update;
    protected static Activity mActivity;
    protected static Context mContext;
    protected static ActionBar mActionBar;
    protected ListView lv;
    protected TransactionAdapter ta;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_trans);
        mActivity = this;
        mContext = this;
        mActionBar = getActionBar();
        init(mContext, mActionBar);

        update = buildThread();
        if(Constants.user == null) {
            Constants.user = new Account().loadAccount(getSharedPreferences(Constants.accSpKey,MODE_PRIVATE));
        }
        if(Constants.user == null)
        {
            Toast.makeText(mContext, "Please login.", Toast.LENGTH_LONG).show();
            try
            {
                Thread.sleep(200);
                Intent PDIntent = new Intent(mContext, PlusDollarsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("login", 1);
                PDIntent.putExtras(extras);
                startActivity(PDIntent);
            }
            catch(InterruptedException e)
            {
                Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ta = new TransactionAdapter(mContext, Constants.user.transactions));
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(Constants.user != null) {
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ta = new TransactionAdapter(mContext, Constants.user.transactions));
        }
    }

    private Thread buildThread()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                GetAllTheThings getPlusData = new GetAllTheThings(Constants.user);
                Constants.user = getPlusData.getTheThings();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setProgressBarIndeterminateVisibility(false);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trans, menu);
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
                ta.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}