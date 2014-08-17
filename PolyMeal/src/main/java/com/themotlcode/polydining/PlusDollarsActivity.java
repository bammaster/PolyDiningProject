package com.themotlcode.polydining;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

/**
 * Created by Blake on 2/20/14.
 */
public class PlusDollarsActivity extends BaseActivity implements android.support.v4.app.FragmentManager.OnBackStackChangedListener
{
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_dollars);

        super.init(this, getActionBar(), true);

        PlusDollarsFragment fragment = new PlusDollarsFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_layout, fragment).commit();

        init();
    }

    @Override
    public void onBackStackChanged()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            finish();
        }
    }

    private void init()
    {
        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);
        setColor();
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

    public void setColor()
    {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.APP_COLOR)));

        Fragment f = fm.findFragmentById(R.id.fragment_layout);
        if (f instanceof PolyDiningFragment)
        {
            PolyDiningFragment pdf = (PolyDiningFragment) f;
            pdf.greeting.setTitleColor(Constants.APP_COLOR);
            pdf.greeting.setDividerColor(Constants.APP_COLOR);
            pdf.greeting.show();
        }

    }

    public void setGreeting(String s)
    {
        Fragment f = fm.findFragmentById(R.id.fragment_layout);
        if (f instanceof PolyDiningFragment)
        {
            PolyDiningFragment pdf = (PolyDiningFragment) f;
            pdf.greeting.setMessage(s);
        }
    }
}
