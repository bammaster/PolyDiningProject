package com.themotlcode.polydining;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener
{
    // Drawer UI
    protected DrawerLayout mDrawerLayout;
    protected RelativeLayout mRelLayout;
    protected ListView mDrawerList;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected String[]  mDrawerItems;

    private FragmentManager fm;
    public static MainActivity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        initActivity();

        fm = getSupportFragmentManager();
        MainPresenter presenter = new MainPresenter(this);
        presenter.getConfigFromWeb();

        fm.beginTransaction()
                .add(R.id.fragment_layout, new LoginFragment()).commit();
    }

    @Override
    public void onBackStackChanged()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            finish();
        }
    }

    @Override
    public void setContentView(final int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        mRelLayout = (RelativeLayout) mDrawerLayout.findViewById(R.id.rel_layout);
        getLayoutInflater().inflate(layoutResID, mRelLayout, true); // Setting the content of layout your provided to the act_content frame
        super.setContentView(mDrawerLayout);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return false;
    }

    protected void logout(View v)
    {
        ((PolyApplication) getApplication()).user = null;
        LoginFragment loginFragment = new LoginFragment();FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, loginFragment);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();

    }

    private void initActivity() {
        mActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerItems = getResources().getStringArray(R.array.drawerItems);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new DrawerAdapter(this, new ArrayList<String>(Arrays.asList(mDrawerItems))));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerList.setItemChecked(-1, true);
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    protected void viewDrawer(boolean b)
    {
        if(b)
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
        }
        else
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(b);
        getActionBar().setHomeButtonEnabled(b);
    }

    protected void setColor()
    {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(PolyApplication.APP_COLOR)));

    }

    /* The click listner for ListView in the navigation drawer */
    protected class DrawerItemClickListener implements ListView.OnItemClickListener {
        Context mContext;
        protected DrawerItemClickListener(Context mContext) {
            this.mContext = mContext;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final int delay = 200;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(delay);
                        switch(position)
                        {
                            case 0:
                                Fragment inView = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
                                if(inView instanceof MyAccountFragment)
                                {
                                    ((MyAccountFragment) inView).pager.setCurrentItem(0, true);
                                }
                                else
                                {
                                    Bundle pData = new Bundle();
                                    pData.putBoolean("plus", true);

                                    MyAccountFragment plusFragment = new MyAccountFragment();
                                    plusFragment.setArguments(pData);

                                    FragmentTransaction pTransaction = getSupportFragmentManager().beginTransaction();
                                    pTransaction.replace(R.id.fragment_layout, plusFragment)
                                            .addToBackStack(null);
                                    pTransaction.commit();
                                }
                                break;
                            case 1:
                                Fragment inView2 = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
                                if(inView2 instanceof MyAccountFragment)
                                {
                                    ((MyAccountFragment) inView2).pager.setCurrentItem(1, true);
                                }
                                else
                                {
                                    MyAccountFragment transFragment = new MyAccountFragment();
                                    Bundle tData = new Bundle();
                                    tData.putBoolean("plus", false);
                                    transFragment.setArguments(tData);

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_layout, transFragment)
                                            .addToBackStack(null);
                                    transaction.commit();
                                }
                                break;
                            case 2:
                                PolyMealFragment venuesFragment = new PolyMealFragment();
                                FragmentTransaction vTransaction = getSupportFragmentManager().beginTransaction();
                                vTransaction.replace(R.id.fragment_layout, venuesFragment)
                                        .addToBackStack(null);
                                vTransaction.commit();
                                break;
                            case 3:
                                break;
                        }
                    }
                    catch(InterruptedException e)
                    {
                        Toast.makeText(mContext, "An unknown error occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}