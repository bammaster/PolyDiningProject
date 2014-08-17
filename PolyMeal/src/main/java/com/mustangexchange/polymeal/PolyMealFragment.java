package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.mustangexchange.polymeal.models.Cart;
import com.mustangexchange.polymeal.models.MoneyTime;

import java.math.BigDecimal;

public class PolyMealFragment extends Fragment
{
    private ListView lv;
    private ListAdapter listAdapter;
    private static Activity mActivity;

    private PolyMealPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_poly_meal, container, false);

        init(v);

        presenter = new PolyMealPresenter(this, listAdapter);
        presenter.getData();

        return v;
    }

    private void init(View v)
    {
        lv = (ListView) v.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getActivity(), R.id.polymealListItem, Statics.names);
        setupList();

        this.setHasOptionsMenu(true);

        mActivity = getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(listAdapter != null)
        {
            listAdapter.notifyData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.polymeal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                listAdapter.clear();
                presenter.getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupList()
    {
        listAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                final int fIndex = index;
                Statics.activityTitle = Statics.names.get(index);
                if(!Statics.lastVenue.equals(Statics.names.get(index))
                        && MoneyTime.getMoneySpent().compareTo(new BigDecimal("0.00")) != 0) {
                    final QustomDialogBuilder onListClick = new QustomDialogBuilder(getActivity());
                    onListClick.setDividerColor(Constants.APP_COLOR);
                    onListClick.setTitleColor(Constants.APP_COLOR);
                    onListClick.setTitle("Clear Cart?");
                    onListClick.setMessage("Your cart has items that are not from this venue. " +
                            "Would you like to clear it now?");
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.clear();
                            /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                            Statics.lastVenue= Statics.names.get(fIndex);
                            intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(intentVenue);*/
                            Statics.lastVenue= Statics.names.get(fIndex);
                            VenueFragment venueFragment = new VenueFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_layout, venueFragment)
                                    .addToBackStack(null);
                            transaction.commit();
                        }
                    });
                    onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    onListClick.show();
                } else {
                    /*final Intent intentVenue = new Intent(mActivity, VenueActivity.class);
                    Statics.lastVenue = Statics.names.get(index);
                    intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mActivity.startActivity(intentVenue);*/
                    Statics.lastVenue= Statics.names.get(fIndex);
                    VenueFragment venueFragment = new VenueFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_layout, venueFragment)
                            .addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        lv.setAdapter(listAdapter);
    }
}
