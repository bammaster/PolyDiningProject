package com.themotlcode.polydining;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import com.themotlcode.polydining.Exceptions.BudgetException;
import com.themotlcode.polydining.models.GetDiningAccount;


public class PlusDollarsPresenter extends Presenter {
    SharedPreferences sharedPrefs;
    private PlusDollarsFragment fragment;
    private PlusDollarsActivity activity;
    private PolyApplication app;

    public PlusDollarsPresenter(Fragment fragment) {
        this.fragment = (PlusDollarsFragment) fragment;
        this.activity = (PlusDollarsActivity) fragment.getActivity();
        sharedPrefs = activity.getSharedPreferences(PolyApplication.accSpKey, activity.MODE_PRIVATE);
        app = (PolyApplication) activity.getApplication();
        
    }

    public boolean init() {
        //If the loaded Constants.user does not exist or the user said not to remember.
        if(app.user == null || !app.user.isRemembered())
        {
            fragment.handleLogin();
            return false;
        }
        else if(app.user.isRemembered()) {
            loadBudget();
            return true;
        }
        return false;
    }

    void storeDates()
    {
        sharedPrefs.edit().putInt("StartYear", app.startOfQuarter[0])
                .putInt("EndYear",app.endOfQuarter[0])
                .putInt("StartMonth",app.startOfQuarter[1])
                .putInt("EndMonth",app.endOfQuarter[1])
                .putInt("StartDay",app.startOfQuarter[2])
                .putInt("EndDay",app.endOfQuarter[2]).commit();
    }

    void clearAccount()
    {
        sharedPrefs.edit().putString(PolyApplication.accSpKey,"");
    }

    public void loadBudget()
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

    public void loadData() {
        new ThreadTask().execute();
    }

    class ThreadTask extends AsyncTask<Void, Void, Boolean> {

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
            fragment.returnThread(b);
        }
    }

}
