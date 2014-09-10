package com.themotlcode.polydining;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.astuetz.PagerSlidingTabStrip;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.MoneyTime;

//VenueFragment holds the ViewPager and the ViewPagerTabString

public class VenueFragment extends Fragment
{
    protected ViewPager vp;
    protected PagerSlidingTabStrip tabs;

    private VenuePresenter presenter;
    private PolyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        System.out.println("onCreateView)");
        View v = inflater.inflate(R.layout.fragment_venue, container, false);

        ((MainActivity) getActivity()).viewDrawer(true);

        presenter = new VenuePresenter(this);

        presenter.loadFromCache(savedInstanceState);
        init(v);

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.updateBalance();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        getActivity().getActionBar().setSubtitle(null);
        getActivity().getActionBar().setTitle("PolyMeal");
        vp.getAdapter().notifyDataSetChanged();
        vp.invalidate();
        vp = null;
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
            case R.id.sort:
                //popupMenu.show();
                return true;
            case R.id.mc_choice:
                System.out.println("MC CHOICE");
                item.setChecked(true);
                return true;
            case R.id.pd_choice:
                System.out.println("PD CHOICE");
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void init(View v)
    {
        this.setHasOptionsMenu(true);
        app = (PolyApplication) getActivity().getApplication();

        vp = (ViewPager) v.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        vp.setSaveEnabled(false);

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        vp = (ViewPager) v.findViewById(R.id.pager);

        getActivity().getActionBar().setTitle(app.activityTitle);

        presenter.updateBalance();
        updateSettings();
    }

    public void updateSettings()
    {
        presenter.updateSettings();

        vp.setAdapter(presenter.adapterInit(this.getChildFragmentManager()));
        vp.getAdapter().notifyDataSetChanged();

        tabs.setIndicatorColor(Color.parseColor(PolyApplication.ACCENT_COLOR));
        tabs.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));
        tabs.setDividerColor(Color.WHITE);
        tabs.setTextColor(Color.WHITE);
        tabs.setViewPager(vp);
    }
}
