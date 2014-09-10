package com.themotlcode.polydining;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainPresenter extends Presenter
{
    private MainActivity activity;
    private PolyApplication app;

    public MainPresenter(Activity a)
    {
        this.activity = (MainActivity) a;
        app = (PolyApplication) activity.getApplication();
    }

    protected void getConfigFromWeb()
    {
        new HomeUpdate().execute();
    }

    class HomeUpdate extends AsyncTask<Void, Void, String>
    {

        protected String doInBackground(Void... args) {
            try
            {
                getDates();
                getColor();
                return getMessage();
            }
            catch(IOException e){
                Log.e("Blake", "IOError: ", e);
            }
            return null;
        }

        protected void onPostExecute(String s) {
            activity.setColor();
            if(s != null)
            {
                Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_layout);

                //should always be true
                if (f instanceof LoginFragment)
                {
                    LoginFragment lf = (LoginFragment) f;
                    lf.greeting.setMessage(s);
                    String greeting = activity.getSharedPreferences(PolyApplication.spKey, activity.MODE_PRIVATE).getString(PolyApplication.GREETING_KEY, "");
                    if(!s.equals(greeting)) {
                        activity.getSharedPreferences(PolyApplication.spKey, activity.MODE_PRIVATE).edit().putString(PolyApplication.GREETING_KEY, s).apply();
                    }
                    lf.greeting.show();
                }
            }
        }

        private void getDates() throws IOException
        {
            URL dateUrl = new URL(PolyApplication.DATE_URL);
            URLConnection dateCon = dateUrl.openConnection();
            InputStream is = dateCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String start = br.readLine();
            String end = br.readLine();
            String[] temp = end.split("/");
            if(checkDate(temp)) {
                app.endOfQuarter = new int[3];
                app.endOfQuarter[0] = new Integer(temp[2]);
                app.endOfQuarter[1] = new Integer(temp[0]);
                app.endOfQuarter[2] = new Integer(temp[1]);
            }
            temp = start.split("/");
            if(checkDate(temp)) {
                app.startOfQuarter = new int[3];
                app.startOfQuarter[0] = new Integer(temp[2]);
                app.startOfQuarter[1] = new Integer(temp[0]);
                app.startOfQuarter[2] = new Integer(temp[1]);
            }
        }
        private String getMessage() throws IOException
        {
            URL dateUrl = new URL(PolyApplication.MESSAGE_URL);
            URLConnection dateCon = dateUrl.openConnection();
            InputStream is = dateCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
                sb.append(line);
            return sb.toString();
        }
        private void getColor() throws IOException
        {
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
                for (int i = 0; i < dates.length; i++) {
                    try {
                        dates[i].replace(" ", "");
                        Integer.parseInt(dates[i]);
                    } catch (NumberFormatException ne) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
