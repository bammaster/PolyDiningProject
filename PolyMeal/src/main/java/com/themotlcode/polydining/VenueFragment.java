package com.themotlcode.polydining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.MoneyTime;

//VenueFragment holds the ViewPager and the ViewPagerTabString

public class VenueFragment extends Fragment
{
    protected ViewPager vp;
    protected PagerTabStrip myPagerTabStrip;

    private VenuePresenter presenter;
    private PolyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_venue, container, false);

        presenter = new VenuePresenter(this);

        if(presenter.loadFromCache(savedInstanceState))
        {
            startActivity(new Intent(getActivity(), PolyMealActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION));
            getActivity().overridePendingTransition(0, 0); //no animation
        } else
        {
            init(v);
        }

        return v;
    }

    private void init(View v)
    {
        this.setHasOptionsMenu(true);
        app = (PolyApplication) getActivity().getApplication();

        vp = (ViewPager) v.findViewById(R.id.pager);

        myPagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_title_strip);
        myPagerTabStrip.setTabIndicatorColor(0xC6930A);
        getActivity().getActionBar().setTitle(app.activityTitle);



        presenter.updateBalance();
        updateSettings();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        // This gets called by the system when it's about to kill your app
        // Put all your data in the outState bundle
        outState.putParcelableArrayList("cart", Cart.getCart());
        outState.putString("moneySpent", MoneyTime.getMoneySpent().toString());
        outState.putString("lastVenue", app.lastVenue);
    }
    //Handles chaning the view based upon user input

    public void updateSettings()
    {
        presenter.updateSettings();

        vp.setAdapter(presenter.adapterInit(this.getChildFragmentManager()));
        vp.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.updateBalance();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().getActionBar().setSubtitle(null);
        getActivity().getActionBar().setTitle("PolyMeal");
        vp.getAdapter().notifyDataSetChanged();
        vp.invalidate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.venue, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cart:
                CartFragment cartFragment = new CartFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout, cartFragment)
                        .addToBackStack(null);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
