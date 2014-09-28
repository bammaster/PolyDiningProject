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
public class TransactionFragment extends Fragment {

    protected static Activity mActivity;
    protected static ActionBar mActionBar;
    protected ListView lv;
    protected View v;
    protected TransactionAdapter ta;

    private MyAccountPresenter presenter;
    private PolyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = inflater.inflate(R.layout.fragment_trans, container, false);

        mActivity = getActivity();
        mActionBar = getActivity().getActionBar();

        presenter = new MyAccountPresenter(this);
        app = (PolyApplication) getActivity().getApplication();
        init(v);

        isTransactionsEmpty();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                item.setEnabled(false);
                presenter.refresh(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init(View v) {
        this.setHasOptionsMenu(true);

        lv = (ListView) v.findViewById(R.id.listView);
        lv.setAdapter(ta = new TransactionAdapter(getActivity(), app.user.getAccountTransactions()));
    }

    protected void refresh() {
        ta.updateData(app.user.getAccountTransactions());
    }

    private void isTransactionsEmpty() {
        if (app.user.getAccountTransactions().size() == 0) {
            v.findViewById(R.id.transactions).setVisibility(View.GONE);
            v.findViewById(R.id.emptyTransactions).setVisibility(View.VISIBLE);
        }
    }
}