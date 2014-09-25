package com.themotlcode.polydining;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class MyAccountFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    protected ViewPager pager;
    private MyAccountPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);
        setupActivity();

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new MyAccountPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        Boolean plus = true;
        if (getArguments() != null) {
            plus = getArguments().getBoolean("plus");
        }

        if (!plus) {
            pager.setCurrentItem(1, true);
        }

        tabs.setIndicatorColor(Color.parseColor(PolyApplication.ACCENT_COLOR));
        tabs.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));
        tabs.setDividerColor(Color.WHITE);
        tabs.setTextColor(Color.WHITE);
        tabs.setViewPager(pager);

        ((MainActivity) getActivity()).viewDrawer(true);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getActionBar().setSubtitle(null);
    }

    private void setupActivity() {
        ((MainActivity) getActivity()).viewDrawer(true);
    }

    public class MyAccountPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"My Account", "Transactions"};

        public MyAccountPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PlusDollarsFragment();
                case 1:
                    return new TransactionFragment();
            }
            return null;
        }
    }

}
