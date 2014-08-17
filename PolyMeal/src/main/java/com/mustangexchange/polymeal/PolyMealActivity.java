package com.mustangexchange.polymeal;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

public class PolyMealActivity extends BaseActivity implements android.support.v4.app.FragmentManager.OnBackStackChangedListener
{
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_poly_meal);

        super.init(this, getActionBar(), true);

        PolyMealFragment fragment = new PolyMealFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_layout, fragment).commit();

        mActivity = this;
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

    @Override
    public void onBackStackChanged()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            finish();
        }
    }
}