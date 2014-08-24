package com.themotlcode.polydining;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.MoneyTime;

import java.math.BigDecimal;

public class PolyMealFragment extends Fragment
{
    private ListView lv;
    private ListAdapter listAdapter;
    private static Activity mActivity;
    private PolyApplication app;

    private PolyMealPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_poly_meal, container, false);

        init(v);

        presenter = new PolyMealPresenter(this, listAdapter);
        presenter.getData();

        setupList();

        return v;
    }

    private void init(View v)
    {
        mActivity = getActivity();

        app = (PolyApplication) mActivity.getApplication();

        lv = (ListView) v.findViewById(R.id.listView);
        listAdapter = new ListAdapter(getActivity(), R.id.polymealListItem, app.names);

        this.setHasOptionsMenu(true);


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
                presenter.refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupList()
    {
        presenter.setupList(lv);
    }
}
