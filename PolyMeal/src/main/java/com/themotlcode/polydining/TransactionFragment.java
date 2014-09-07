package com.themotlcode.polydining;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;

/**
 * Created by jon on 3/23/14.
 */
public class TransactionFragment extends Fragment
{

    protected static Activity mActivity;
    protected static ActionBar mActionBar;
    protected ListView lv;
    protected TransactionAdapter ta;

    private TransactionPresenter presenter;
    private PolyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.activity_trans, container, false);

        mActivity = getActivity();
        mActionBar = getActivity().getActionBar();

        presenter = new TransactionPresenter(this);
        app = (PolyApplication) getActivity().getApplication();
        init(v);

        return v;
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
        this.setHasOptionsMenu(true);

        lv = (ListView) v.findViewById(R.id.listView);
        lv.setAdapter(ta = new TransactionAdapter(getActivity(), app.user.getTransactions()));
    }

    protected void refresh()
    {
        ta.updateData(app.user.getTransactions());
    }
}