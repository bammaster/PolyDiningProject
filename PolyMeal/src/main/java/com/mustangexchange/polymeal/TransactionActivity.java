package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.mustangexchange.polymeal.Exceptions.BudgetException;

/**
 * Created by jon on 3/23/14.
 */
public class TransactionActivity extends BaseActivity {

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
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Statics.user = new Account().loadAccount(getSharedPreferences(Constants.accSpKey,MODE_PRIVATE));
        if(Statics.user == null)
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
            lv.setAdapter(ta = new TransactionAdapter(mContext, Statics.user.transactions));
        }
    }

    private Thread buildThread()
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetAllTheThings getPlusData = new GetAllTheThings(Statics.user);
                    Statics.user = getPlusData.getTheThings();
                }
                catch(BudgetException e)
                {
                    Log.e("Blake", "Ruh Roh!");
                }
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
                buildThread().start();
                setProgressBarIndeterminateVisibility(true);
                ta.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}