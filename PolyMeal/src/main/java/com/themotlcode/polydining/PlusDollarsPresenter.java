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

    public PlusDollarsPresenter(Fragment fragment) {
        this.fragment = (PlusDollarsFragment) fragment;
        this.activity = (PlusDollarsActivity) fragment.getActivity();
        sharedPrefs = activity.getSharedPreferences(Constants.accSpKey, activity.MODE_PRIVATE);
    }

    public boolean init() {
        //If the loaded Constants.user does not exist or the user said not to remember.
        if(Statics.user == null || !Statics.user.isRemembered())
        {
            fragment.handleLogin();
            return false;
        }
        else if(Statics.user.isRemembered()) {
            loadBudget();
            return true;
        }
        return false;
    }

    void storeDates()
    {
        sharedPrefs.edit().putInt("StartYear", Statics.startOfQuarter[0])
                .putInt("EndYear",Statics.endOfQuarter[0])
                .putInt("StartMonth",Statics.startOfQuarter[1])
                .putInt("EndMonth",Statics.endOfQuarter[1])
                .putInt("StartDay",Statics.startOfQuarter[2])
                .putInt("EndDay",Statics.endOfQuarter[2]).commit();
    }

    void clearAccount()
    {
        sharedPrefs.edit().putString(Constants.accSpKey,"");
    }

    public void loadBudget()
    {
        Statics.startOfQuarter = new int[3];
        Statics.endOfQuarter = new int[3];
        Statics.startOfQuarter[0] = sharedPrefs.getInt("StartYear",0);
        Statics.endOfQuarter[0] = sharedPrefs.getInt("EndYear",0);
        Statics.startOfQuarter[1] = sharedPrefs.getInt("StartMonth",0);
        Statics.endOfQuarter[1] = sharedPrefs.getInt("EndMonth",0);
        Statics.startOfQuarter[2] = sharedPrefs.getInt("StartDay",0);
        Statics.endOfQuarter[2] = sharedPrefs.getInt("EndDay",0);
    }

    public void loadData() {
        new ThreadTask().execute();
    }

    class ThreadTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... args) {
            GetDiningAccount getPlusData = new GetDiningAccount(Statics.user);

            try {
                Statics.user = getPlusData.getAccountInfo();
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
