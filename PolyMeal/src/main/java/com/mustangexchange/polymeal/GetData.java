package com.mustangexchange.polymeal;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;


import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Handles getting and parsing all of the data for the app from the internet.
 * Can be used by constructing GetData and calling execute on it.
 */
public class GetData extends AsyncTask<String, String, Integer> {

    private ArrayAdapter<String> list;
    private Activity mActivity;
    private SharedPreferences sp;

    /**
     * Builds a GetData object.
     * @param list The list view to update with progress.
     * @param activity The activity to push progress to.
     * @param sp Shared preferences to the parsed store data to.
     */
    public GetData(ArrayAdapter<String> list, Activity activity, SharedPreferences sp)
    {
        super();
        this.list = list;
        this.sp = sp;
        mActivity= activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * Stores the data to a cache on finish.
     * @param result
     */
    protected void onPostExecute(Integer result)
    {
        list.clear();
        list.addAll(Statics.venues.keySet());
        mActivity.setProgressBarIndeterminateVisibility(false);
        sp.edit().putBoolean(Constants.firstLaunch,false).commit();
    }

    /**
     * Handles all HTTP tasks in the background.
     * @param params N/A
     * @return N/A
     */
    @Override
    protected Integer doInBackground(String... params)
    {
        try
        {
            URL url = new URL(Constants.URL);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
                sb.append(line);
            //Parses and stores all of the apps data.
            new VenueParser().parse(Jsoup.parse(sb.toString(), "", Parser.xmlParser()), this);
            int version = sp.getInt("DBVersion", 1);
            Database db = new Database(mActivity, version++);
            sp.edit().putInt("DBVersion", version).commit();
            store(db);
        }
        catch(IOException e)
        {
            //Displays an error message in the event of a connection error.
            final AlertDialog.Builder connectionError = new AlertDialog.Builder(mActivity);
            connectionError.setTitle(R.string.error_conn_title);
            connectionError.setMessage(R.string.error_conn_msg);
            connectionError.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {}
            });
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String json = sp.getString(Constants.speKey, "");
                    if (!json.equals("")) {
                        Statics.venues = new Gson().fromJson(json, Constants.gsonType);
                        list.addAll(Statics.venues.keySet());
                    }
                    else{
                        connectionError.show();
                    }
                }
            });
        }
        catch(Exception e)
        {
            Log.e("Blake","Error somewhere.",e);
            //Displays an error message in the event of a connection error.
            final AlertDialog.Builder error = new AlertDialog.Builder(mActivity);
            error.setTitle(R.string.error_title);
            error.setMessage(R.string.error_msg);
            error.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    mActivity.finish();
                }
            });
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error.show();
                }
            });
        }
        return 0;
    }

    /**
     * Confirms that the String to update the list with is not already there and updates the list.
     * @param values Array of strings with data to update.
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if(!Statics.names.contains(values[0]))
        {
            list.add(values[0]);
        }
    }

    /**
     * Used for external class calling to publish progress of the thread.
     * @param name The value to update with.
     */
    protected void update(String name)
    {
        publishProgress(name);
    }
    private void store(Database db)
    {
         for(String s : Statics.names)
         {
                db.updateVenues(Statics.venues.get(s));
         }
    }
}