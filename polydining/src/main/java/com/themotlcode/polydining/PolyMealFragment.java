package com.themotlcode.polydining;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;

public class PolyMealFragment extends Fragment
{
    private ListView lv;
    private PolyMealAdapter polyMealAdapter;
    private static Activity mActivity;
    private PolyApplication app;

    private PolyMealPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_poly_meal, container, false);

        init(v);
        presenter = new PolyMealPresenter(this);
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
        inflater.inflate(R.menu.refresh, menu);
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

    private void init(View v)
    {
        mActivity = getActivity();

        app = (PolyApplication) mActivity.getApplication();

        lv = (ListView) v.findViewById(R.id.listView);


        this.setHasOptionsMenu(true);


    }

    public void setupList()
    {
        polyMealAdapter = new PolyMealAdapter(getActivity(), R.id.polymealListItem, app.names);
        System.out.println(app.names);
        presenter.setListAdapter(polyMealAdapter);
        presenter.setupList(lv);
    }
}
