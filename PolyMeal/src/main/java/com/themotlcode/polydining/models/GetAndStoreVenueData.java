package com.themotlcode.polydining.models;

import com.themotlcode.polydining.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import com.google.gson.Gson;
import com.themotlcode.polydining.Constants;
import com.themotlcode.polydining.Statics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Handles getting and parsing all of the data for the app from the internet.
 * Can be used by constructing GetData and calling execute on it.
 */
public class GetAndStoreVenueData extends AsyncTask<String, String, Integer> {

    private ArrayAdapter<String> list;
    private Activity mActivity;
    private SharedPreferences sp;
    private Database db;
    private AlertDialog.Builder error;

    /**
     * Builds a GetData object.
     * @param list The list view to update with progress.
     * @param activity The activity to push progress to.
     * @param sp Shared preferences to the parsed store data to.
     */
    public GetAndStoreVenueData(ArrayAdapter<String> list, Activity activity, SharedPreferences sp)
    {
        super();
        this.list = list;
        this.sp = sp;
        mActivity= activity;
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        list.clear();
        list.addAll(Statics.venues.keySet());
        mActivity.setProgressBarIndeterminateVisibility(false);
        sp.edit().putBoolean(Constants.firstLaunch,false).apply();
        int version = sp.getInt("DBVersion", 1);
        db = new Database(mActivity, version++);
        sp.edit().putInt("DBVersion", version).apply();
        //storeData(db);
    }

    @Override
    protected Integer doInBackground(String... params)
    {
        String venues = "";
        try
        {
            URL url = new URL(Constants.URL);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            venues = sb.toString();
        }
        catch(IOException io)
        {
            throwError(R.string.error_conn_msg, R.string.error_conn_title, new SendError().stackTraceToString(io));
        }
        try
        {
            Statics.venues = new Gson().fromJson(venues.toString(), Constants.gsonType);
        }
        catch(Exception e)
        {
            throwError(R.string.error_msg, R.string.error_title, new SendError().stackTraceToString(e));
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(listAdapterContains(values[0]))
        {
            list.add(values[0]);
        }
    }

    private boolean listAdapterContains(String toCheck)
    {
        for(int i = 0; i < list.getCount(); i++)
        {
            if(list.getItem(i).equals(toCheck))
            {
                return true;
            }
        }
        return false;
    }

    private void storeData(Database db)
    {
        for(Map.Entry<String, Venue> entry : Statics.venues.entrySet())
        {
            db.updateVenues(entry.getValue());
        }
    }

    private void throwError(int message, int title, final String errorMessage)
    {
        error = new AlertDialog.Builder(mActivity);
        error.setTitle(title);
        error.setMessage(message);
        error.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {

            }
        });
        error.setNegativeButton(R.string.send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button)
            {
                new SendError().sendErrorToDeveloper(errorMessage, mActivity);
            }
        });
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                error.show();
            }
        });
    }
}