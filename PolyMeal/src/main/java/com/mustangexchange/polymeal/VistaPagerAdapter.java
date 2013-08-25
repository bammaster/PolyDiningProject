package com.mustangexchange.polymeal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class VistaPagerAdapter extends FragmentPagerAdapter {

    //ArrayList<ItemSet> foodList;
    static ArrayList<VistaActivity.FoodItemAdapter> foodAdapterList;

    public VistaPagerAdapter(FragmentManager fm, ArrayList<VistaActivity.FoodItemAdapter> foodAdapterList) {
        super(fm);
        //this.foodList = foodList;
        this.foodAdapterList = foodAdapterList;
    }

    @Override
    public Fragment getItem(int i) {
        return new MyFragment(i);
    }

    @Override
    public int getCount() {
        return foodAdapterList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return foodAdapterList.get(position).getTitle();
    }
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    private static class MyFragment extends Fragment {

        private int position;

        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.main_fragment, container, false);
            //Actual Fragment inflating happens in the next line.
            ((ListView) rootView.findViewById(R.id.list)).setAdapter(foodAdapterList.get(position));
            return rootView;
        }
    }
}