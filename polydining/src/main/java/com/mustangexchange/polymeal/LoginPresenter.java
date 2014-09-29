package com.mustangexchange.polymeal;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mustangexchange.polymeal.Exceptions.PasswordException;

import com.mustangexchange.polymeal.models.Account;
import com.mustangexchange.polymeal.models.AccountTransaction;
import com.mustangexchange.polymeal.models.DataCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class LoginPresenter extends Presenter {
    private Fragment fragment;
    private PolyApplication app;
    private Button loginButton;
    private boolean loadingFromDb = false;
    private String greeting;

    public LoginPresenter(Fragment fragment) {
        this.fragment = fragment;

        app = (PolyApplication) fragment.getActivity().getApplication();
    }

    /*
     * Checks DB for account; sets loadingFromDb to true if so. If not, loads the config from web
     */
    protected void checkLogin(Button loginButton) {
        this.loginButton = loginButton;
        List<Account> accounts = Account.listAll(Account.class);
        if (!accounts.isEmpty() && accounts.get(0).isRemembered()) {
            loadingFromDb = true;
            loadData(null);
        } else {
            getConfigFromWeb();
        }
    }

    /*
     * Login performed after user hits loginbutton
     */
    protected void login(String username, String password, Boolean remember, Button loginButton) {

        this.loginButton = loginButton;
        app.user = new Account(username, password, remember);
        loadData(null);
    }

    /*
     * Loads Account data; will perform getConfigFromWeb if loading from database
     */
    protected void loadData(MenuItem item) {
        new ThreadTask(item).execute();
    }

    protected class ThreadTask extends AsyncTask<Void, Void, Void> {

        MenuItem item;

        public ThreadTask() {
        }

        public ThreadTask(MenuItem item) {
            this.item = item;
        }

        protected void onPreExecute() {
            if (fragment instanceof LoginFragment) {
                loginButton.setEnabled(false);
            }
            fragment.getActivity().setProgressBarIndeterminateVisibility(true);
        }

        protected Void doInBackground(Void... args) {
            DataCollector getPlusData = new DataCollector(app.user);

            try {
                List<Account> accounts = Account.listAll(Account.class);
                if (accounts.isEmpty()) {
                    getPlusData.getAccountInfo();
                } else {
                    checkDB();
                }
                if (loadingFromDb) {
                    try {
                        getDates();
                        getColor();
                        greeting = getMessage();
                    } catch (IOException e) {
                        Log.e("Blake", "IOError: ", e);
                    }
                }
            } catch (PasswordException e) {
                app.user = new Account();
                PolyApplication.throwError(R.string.password_error_msg, R.string.password_error_title, e, fragment.getActivity());
            } catch (LoginException e) {
                app.user = new Account();
                PolyApplication.throwError(R.string.login_error_msg, R.string.login_error_title, e, fragment.getActivity());
            } catch (Exception e) {
                app.user = new Account();
                PolyApplication.throwError(R.string.error_msg, R.string.error_title, e, fragment.getActivity());
            }
            return null;
        }

        public void checkDB() {
            List<Account> accounts = Account.listAll(Account.class);
            if (!accounts.isEmpty() && accounts.get(0).isRemembered()) {
                app.user = accounts.get(0);
                app.user.setAccountTransactions(new ArrayList<AccountTransaction>(AccountTransaction.listAll(AccountTransaction.class)));
                app.startOfQuarter = new int[3];

                for (int i = 0; i < 3; i++) {

                    app.startOfQuarter[i] = app.defaultSP.getInt(PolyApplication.START_OF_QUARTER_KEY + i, 0);
                }

                app.endOfQuarter = new int[3];

                for (int i = 0; i < 3; i++) {

                    app.endOfQuarter[i] = app.defaultSP.getInt(PolyApplication.END_QUARTER_KEY + i, 0);
                }
                PolyApplication.APP_COLOR = app.defaultSP.getString(PolyApplication.APP_COLOR_KEY, "#000000");
                PolyApplication.ACCENT_COLOR = app.defaultSP.getString(PolyApplication.APP_COLOR_KEY, "#000000");
            }
        }

        @Override
        protected void onPostExecute(Void a) {
            if (fragment instanceof LoginFragment) {
                if (loadingFromDb) {
                    setMessage(greeting);
                }
                ((LoginFragment) fragment).returnThread();
            } else if (fragment instanceof PlusDollarsFragment) {
                ((PlusDollarsFragment) fragment).loadData();
            } else if (fragment instanceof TransactionFragment) {
                ((TransactionFragment) fragment).refresh();
            }
            if (app.user.isRemembered()) {
                app.user.save();
            }
            app.defaultSP.edit().putString(PolyApplication.APP_COLOR_KEY, PolyApplication.APP_COLOR).apply();
            app.defaultSP.edit().putString(PolyApplication.ACCENT_COLOR_KEY, PolyApplication.ACCENT_COLOR).apply();
            fragment.getActivity().setProgressBarIndeterminateVisibility(false);
            if (item != null) {
                item.setEnabled(true);
            }
        }
    }

    protected void getConfigFromWeb() {
        new GetConfigFromWeb().execute();
    }

    protected class GetConfigFromWeb extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            if (!loadingFromDb) {
                loginButton.setEnabled(false);
                fragment.getActivity().setProgressBarIndeterminateVisibility(true);
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                getDates();
                getColor();
                return getMessage();
            } catch (IOException e) {
                Log.e("Blake", "IOError: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            MainActivity activity = ((MainActivity) fragment.getActivity());
            activity.setColor();
            setMessage(s);
            if (!loadingFromDb) {
                loginButton.setEnabled(true);
                fragment.getActivity().setProgressBarIndeterminateVisibility(false);
            }
        }
    }

    private void getDates() throws IOException {
        URL dateUrl = new URL(PolyApplication.DATE_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String start = br.readLine();
        String end = br.readLine();
        String[] temp = end.split("/");
        if (checkDate(temp)) {
            app.endOfQuarter = new int[3];
            app.endOfQuarter[0] = new Integer(temp[2]);
            app.endOfQuarter[1] = new Integer(temp[0]);
            app.endOfQuarter[2] = new Integer(temp[1]);

            for (int i = 0; i < 3; i++) {

                app.defaultSP.edit().putInt(PolyApplication.END_QUARTER_KEY + i, app.endOfQuarter[i]).apply();
            }
        }
        temp = start.split("/");
        if (checkDate(temp)) {
            app.startOfQuarter = new int[3];
            app.startOfQuarter[0] = new Integer(temp[2]);
            app.startOfQuarter[1] = new Integer(temp[0]);
            app.startOfQuarter[2] = new Integer(temp[1]);

            for (int i = 0; i < 3; i++) {

                app.defaultSP.edit().putInt(PolyApplication.START_OF_QUARTER_KEY + i, app.startOfQuarter[i]).apply();
            }
        }
    }

    private String getMessage() throws IOException {
        URL dateUrl = new URL(PolyApplication.MESSAGE_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        return sb.toString();
    }

    private void getColor() throws IOException {
        URL dateUrl = new URL(PolyApplication.COLOR_URL);
        URLConnection dateCon = dateUrl.openConnection();
        InputStream is = dateCon.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        PolyApplication.APP_COLOR = "#" + br.readLine();
        PolyApplication.ACCENT_COLOR = "#" + br.readLine();
    }

    private boolean checkDate(String[] dates) {
        if (dates.length != PolyApplication.DATE_ARRAY_SIZE) {
            return false;
        } else {
            for (String date : dates) {
                try {
                    date.replace(" ", "");
                    Integer.parseInt(date);
                } catch (NumberFormatException ne) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setMessage(String message) {
        MainActivity activity = ((MainActivity) fragment.getActivity());
        activity.setColor();
        if (message != null) {
            LoginFragment lf = (LoginFragment) fragment;
            if (!app.defaultSP.getString(PolyApplication.GREETING_KEY, "").equals(message)) {
                lf.greeting.setMessage(message);
                lf.greeting.create();
                Dialog d = lf.greeting.show();
                int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));

                app.defaultSP.edit().putString(PolyApplication.GREETING_KEY, message).apply();
            }
        }
    }
}
