package com.mustangexchange.polymeal;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PolyDiningActivity extends android.support.v4.app.FragmentActivity implements android.support.v4.app.FragmentManager.OnBackStackChangedListener
{
    private FragmentManager fm;
    private PolyDiningActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        activity = this;

        getConfigFromWeb();
    }

    private void init()
    {
        fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged()
    {
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
            finish();
        }
    }

    public void setColor()
    {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.APP_COLOR)));

        Fragment f = fm.findFragmentById(R.id.fragment);
        if (f instanceof PolyDiningFragment)
        {
            PolyDiningFragment pdf = (PolyDiningFragment) f;
            pdf.greeting.setTitleColor(Constants.APP_COLOR);
            pdf.greeting.setDividerColor(Constants.APP_COLOR);
            pdf.greeting.show();
        }

    }

    public void setGreeting(String s)
    {
        Fragment f = fm.findFragmentById(R.id.fragment);
        if (f instanceof PolyDiningFragment)
        {
            PolyDiningFragment pdf = (PolyDiningFragment) f;
            pdf.greeting.setMessage(s);
        }
    }

    protected void getConfigFromWeb()
    {
        new HomeUpdate().execute();
    }

    class HomeUpdate extends AsyncTask<Void, Void, Boolean>
    {

        protected Boolean doInBackground(Void... args) {
            try
            {
                getDates();
                getMessage();
                getColor();
            }
            catch(IOException e){
                Log.e("Blake", "IOError: ", e);
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean b) {
            activity.setColor();
        }

        private void getDates() throws IOException
        {
            URL dateUrl = new URL(Constants.DATE_URL);
            URLConnection dateCon = dateUrl.openConnection();
            InputStream is = dateCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String start = br.readLine();
            String end = br.readLine();
            String[] temp = end.split("/");
            if(checkDate(temp)) {
                Statics.endOfQuarter = new int[3];
                Statics.endOfQuarter[0] = new Integer(temp[2]);
                Statics.endOfQuarter[1] = new Integer(temp[0]);
                Statics.endOfQuarter[2] = new Integer(temp[1]);
            }
            temp = start.split("/");
            if(checkDate(temp)) {
                Statics.startOfQuarter = new int[3];
                Statics.startOfQuarter[0] = new Integer(temp[2]);
                Statics.startOfQuarter[1] = new Integer(temp[0]);
                Statics.startOfQuarter[2] = new Integer(temp[1]);
            }
        }
        private void getMessage() throws IOException
        {
            URL dateUrl = new URL(Constants.MESSAGE_URL);
            URLConnection dateCon = dateUrl.openConnection();
            InputStream is = dateCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
                sb.append(line);
            activity.setGreeting(sb.toString());
        }
        private void getColor() throws IOException
        {
            URL dateUrl = new URL(Constants.COLOR_URL);
            URLConnection dateCon = dateUrl.openConnection();
            InputStream is = dateCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Constants.APP_COLOR = "#" + br.readLine();
        }
        private boolean checkDate(String[] dates) {
            if (dates.length != Constants.DATE_ARRAY_SIZE) {
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
