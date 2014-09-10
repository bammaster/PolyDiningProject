package com.themotlcode.polydining.models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import com.google.gson.Gson;
import com.themotlcode.polydining.PolyApplication;
import com.themotlcode.polydining.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Handles getting and parsing all of the data for the app from the internet.
 * Can be used by constructing GetData and calling execute on it.
 */
public class GetAndStoreVenueData
{

    private Activity mActivity;
    private SharedPreferences sp;
    private Database db;
    private AlertDialog.Builder error;
    private PolyApplication app;

    /**
     * Builds a GetData object.
     * @param activity The activity to push progress to.
     * @param sp Shared preferences to the parsed store data to.
     */
    public GetAndStoreVenueData(Activity activity, SharedPreferences sp, PolyApplication app)
    {
        super();
        this.sp = sp;
        mActivity= activity;
        this.app = app;
    }

    public void getData()
    {
        app.venues.clear();

        String venues = "";
        try
        {
            URL url = new URL(PolyApplication.URL);
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
            app.venues = new Gson().fromJson(venues, PolyApplication.gsonType);
            app.names = new ArrayList<String>();
            app.names.addAll(app.venues.keySet());
            Collections.sort(app.names);
        }
        catch(Exception e)
        {
            throwError(R.string.error_msg, R.string.error_title, new SendError().stackTraceToString(e));
        }

        sp.edit().putBoolean(PolyApplication.firstLaunch,false).apply();
        int version = sp.getInt("DBVersion", 1);
        db = new Database(mActivity, version++);
        sp.edit().putInt("DBVersion", version).apply();
    }

    private void storeData(Database db)
    {
        for(Map.Entry<String, Venue> entry : app.venues.entrySet())
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