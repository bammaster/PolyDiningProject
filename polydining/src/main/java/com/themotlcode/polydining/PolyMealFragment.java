package com.themotlcode.polydining;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PolyMealFragment extends Fragment
{
    private ListView lv;
    private PolyMealAdapter polyMealAdapter;
    private static Activity mActivity;
    private PolyApplication app;

    private PolyMealPresenter presenter;
    private Menu mMenu;
    protected boolean loading;
    protected int spinnerChoice = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_poly_meal, container, false);

        init(v);
        presenter = new PolyMealPresenter(this);

        presenter.setFilter(spinnerChoice);
        presenter.getData();
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(polyMealAdapter != null)
        {
            polyMealAdapter.notifyData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.polymeal, menu);

        mMenu = menu;

        if(loading)
        {
            MenuItem refresh = mMenu.findItem(R.id.refresh);
            refresh.setVisible(!refresh.isVisible());

            MenuItem dice = mMenu.findItem(R.id.dice);
            dice.setVisible(!dice.isVisible());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                loading = true;
                getActivity().invalidateOptionsMenu();
                ActionBar actionBar = getActivity().getActionBar();

                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                actionBar.setTitle(R.string.app_name);
                presenter.refresh();
                return true;
            case R.id.dice:
                final String venueName = presenter.pickVenue();

                if(venueName == null)
                {
                    Toast.makeText(getActivity(), "No venues open!", Toast.LENGTH_LONG).show();
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = (LayoutInflater) builder.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dice_progress, null);
                builder.setView(view);
                builder.create();
                final Dialog d = builder.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        d.cancel();
                        try
                        {
                            Thread.sleep(500);

                        }
                        catch(InterruptedException e)
                        {

                        }
                        app.lastVenue = venueName;
                        app.activityTitle = venueName;
                        VenueFragment venueFragment = new VenueFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_layout, venueFragment)
                                .addToBackStack(null);
                        transaction.commit();
                    }
                }, 1000);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ActionBar actionBar = getActivity().getActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        presenter.setDataCancelled();
    }

    private void init(View v)
    {
        mActivity = getActivity();

        app = (PolyApplication) mActivity.getApplication();

        lv = (ListView) v.findViewById(R.id.listView);


        this.setHasOptionsMenu(true);


    }

    private void setupList()
    {

        polyMealAdapter = new PolyMealAdapter(getActivity(), R.id.polymealListItem, presenter.getFilteredList());
        presenter.setListAdapter(polyMealAdapter);
        presenter.setupList(lv);
    }

    public void updateUI()
    {
        setupList();

        getActivity().supportInvalidateOptionsMenu();

        setActionBarSpinner(true);
    }

    public void setActionBarSpinner(boolean visible)
    {
        ActionBar actionBar = getActivity().getActionBar();

        if(visible)
        {

            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            String[] list = getResources().getStringArray(R.array.venueFilter);

            actionBar.setListNavigationCallbacks(new NavigationSpinnerAdapter(getActivity(), list), new ActionBar.OnNavigationListener()
            {

                @Override
                public boolean onNavigationItemSelected(int itemPosition, long itemId)
                {
                    spinnerChoice = itemPosition;
                    presenter.setFilter(spinnerChoice);
                    setupList();
                    return false;
                }
            });
            actionBar.setSelectedNavigationItem(spinnerChoice);
        }
        else
        {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setTitle(R.string.app_name);
        }
    }

    public class NavigationSpinnerAdapter extends BaseAdapter
    {

        private TextView txtTitle;
        private Context context;
        private String[] items;

        public NavigationSpinnerAdapter(Context context, String[] items) {
            this.context=context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i)
        {
            return items[i];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.actionbar_spinner,null);
            }
            txtTitle=(TextView) convertView.findViewById(R.id.txtActionBarSpinnerTitle);
            txtTitle.setText(items[position]);
            return convertView;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.spinner_list,null);
            }
            txtTitle=(TextView) convertView.findViewById(R.id.txtSpinnerListTitle);
            txtTitle.setText(items[position]);

            return convertView;
        }
    }
}