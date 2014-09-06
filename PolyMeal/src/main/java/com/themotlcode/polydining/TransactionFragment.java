package com.themotlcode.polydining;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.themotlcode.polydining.models.Account;

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
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        mActivity = getActivity();
        mActionBar = getActivity().getActionBar();

        presenter = new TransactionPresenter(this);
        app = (PolyApplication) getActivity().getApplication();
        init(v);

        return v;
    }

    private void init(View v)
    {
        if(app.user == null)
        {
            Toast.makeText(getActivity(), "Please login.", Toast.LENGTH_LONG).show();
            try
            {
                Thread.sleep(200);
                Intent PDIntent = new Intent(getActivity(), PlusDollarsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("login", 1);
                PDIntent.putExtras(extras);
                startActivity(PDIntent);
                presenter.refresh();
            }
            catch(InterruptedException e)
            {
                Toast.makeText(getActivity(), "An unknown error occurred!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            lv = (ListView) v.findViewById(R.id.listView);
            lv.setAdapter(ta = new TransactionAdapter(getActivity(), app.user.getTransactions()));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("onResume");
        //init(getView());
    }

    public void showIndeterminate(boolean status)
    {
        this.getActivity().setProgressBarIndeterminateVisibility(status);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.trans, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.refresh();
                getActivity().setProgressBarIndeterminateVisibility(true);
                ta.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}