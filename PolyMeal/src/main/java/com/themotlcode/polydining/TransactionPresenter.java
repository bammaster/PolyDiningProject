package com.themotlcode.polydining;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.themotlcode.polydining.Exceptions.BudgetException;
import com.themotlcode.polydining.models.GetDiningAccount;
import com.themotlcode.polydining.models.Transaction;

public class TransactionPresenter
{
    private TransactionFragment fragment;
    private PolyApplication app;

    public TransactionPresenter(Fragment fragment)
    {
        this.fragment = (TransactionFragment) fragment;
        this.app = (PolyApplication) fragment.getActivity().getApplication();
    }

    public void refresh()
    {
        new ThreadTask().execute();
    }

    class ThreadTask extends AsyncTask<Void, Void, Boolean>
    {

        protected void onPreExecute(Void... args)
        {
            fragment.showIndeterminate(true);
        }


        protected Boolean doInBackground(Void... args) {

            try {
                GetDiningAccount getPlusData = new GetDiningAccount(app.user);
                app.user = getPlusData.getAccountInfo();
            } catch (BudgetException e) {
                Log.e("Blake", "Ruh Roh!");
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean b) {
            fragment.showIndeterminate(false);
        }
    }
}
