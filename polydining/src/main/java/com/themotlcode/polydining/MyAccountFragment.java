package com.themotlcode.polydining;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyAccountFragment extends Fragment
{
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setProgressBarIndeterminateVisibility(false);
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);
        ((MainActivity) getActivity()).viewDrawer(true);

        mTabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("MY ACCOUNT"),
                PlusDollarsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("TRANSACTIONS"),
                TransactionFragment.class, null);
        ((MainActivity) getActivity()).viewDrawer(true);

        Boolean plus = true;
        if(getArguments() != null)
        {
            plus = getArguments().getBoolean("plus");
        }

        if(!plus)
        {
            mTabHost.getTabWidget().getChildAt(1).performClick();
        }
        return v;
    }
}
