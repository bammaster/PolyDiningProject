package com.themotlcode.polydining;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import com.themotlcode.polydining.Exceptions.BudgetException;
import com.themotlcode.polydining.models.Account;
import com.themotlcode.polydining.models.GetDiningAccount;

public class LoginPresenter extends Presenter
{
    private Fragment fragment;
    private PolyApplication app;
    SharedPreferences sharedPrefs;


    public LoginPresenter(Fragment fragment)
    {
        this.fragment = fragment;

        app = (PolyApplication) fragment.getActivity().getApplication();
        sharedPrefs = fragment.getActivity().getSharedPreferences(PolyApplication.accSpKey, fragment.getActivity().MODE_PRIVATE);
    }

    protected void login(String username, String password, Boolean remember)
    {
        app.user = new Account(username, password, remember);
        loadData();
    }

    protected void loadBudget()
    {
        app.startOfQuarter = new int[3];
        app.endOfQuarter = new int[3];
        app.startOfQuarter[0] = sharedPrefs.getInt("StartYear",0);
        app.endOfQuarter[0] = sharedPrefs.getInt("EndYear",0);
        app.startOfQuarter[1] = sharedPrefs.getInt("StartMonth",0);
        app.endOfQuarter[1] = sharedPrefs.getInt("EndMonth",0);
        app.startOfQuarter[2] = sharedPrefs.getInt("StartDay",0);
        app.endOfQuarter[2] = sharedPrefs.getInt("EndDay",0);
    }

    protected void loadData() {
        new ThreadTask().execute();
    }

    protected class ThreadTask extends AsyncTask<Void, Void, Boolean>
    {

        protected void onPreExecute()
        {
            fragment.getActivity().setProgressBarIndeterminateVisibility(true);
        }

        protected Boolean doInBackground(Void... args) {
            GetDiningAccount getPlusData = new GetDiningAccount(app.user);

            try {
                app.user = getPlusData.getAccountInfo();
            } catch (BudgetException e) {
                loadBudget();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean b) {
            if(fragment instanceof LoginFragment)
            {
                ((LoginFragment) fragment).returnThread(b);
            }
            else if(fragment instanceof PlusDollarsFragment)
            {
                ((PlusDollarsFragment) fragment).loadData();
            }
            else if(fragment instanceof TransactionFragment)
            {
                ((TransactionFragment) fragment).refresh();
            }
            fragment.getActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

}
