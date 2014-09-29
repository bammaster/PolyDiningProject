package com.mustangexchange.polymeal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;


//VenueFragment holds the ViewPager and the ViewPagerTabString

public class VenueFragment extends Fragment {
    protected ViewPager vp;
    protected PagerSlidingTabStrip tabs;

    private VenuePresenter presenter;
    private PolyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_venue, container, false);

        ((MainActivity) getActivity()).viewDrawer(true);
        presenter = new VenuePresenter(this);

        //presenter.loadFromCache(savedInstanceState);
        init(v);
        System.out.println("VenueFragment: onCreate");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSettings();
        presenter.updateBalance();
        getActivity().invalidateOptionsMenu();
        System.out.println("VenueFragment: onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getActionBar().setSubtitle(null);
        getActivity().getActionBar().setTitle("PolyMeal");
        vp.getAdapter().notifyDataSetChanged();
        vp.invalidate();
        vp = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.venue, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                CartFragment cartFragment = new CartFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout, cartFragment)
                        .addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.sort:
                if (PolyApplication.plus) {
                    item.getSubMenu().findItem(R.id.pd_choice).setChecked(true);
                }
                return true;
            case R.id.mc_choice:
                item.setChecked(true);
                PolyApplication.plus = false;
                presenter.updateBalance();
                return true;
            case R.id.pd_choice:
                item.setChecked(true);
                PolyApplication.plus = true;
                presenter.updateBalance();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(View v) {
        this.setHasOptionsMenu(true);
        app = (PolyApplication) getActivity().getApplication();

        vp = (ViewPager) v.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        vp.setSaveEnabled(false);

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setSaveEnabled(false);

        getActivity().getActionBar().setTitle(app.activityTitle);
    }

    public void updateSettings() {
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
