package com.mustangexchange.polymeal;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;


import com.google.gson.Gson;


import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;


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
            //Next few lines handle making an XML String to parse.

            String sb = Constants.getHTML(Constants.URL);
            //Parses and stores all of the apps data.
            new VenueParser().parseAndStore(Jsoup.parse(sb.toString(), "", Parser.xmlParser()), this, sp.edit());
        }
        catch(IOException e)
        {
            Log.e("Blake","Connection error.",e);
            //Displays an error message in the event of a connection error.
            final AlertDialog.Builder connectionError = new AlertDialog.Builder(mActivity);
            connectionError.setTitle(R.string.error_conn_title);
            connectionError.setMessage(R.string.error_conn_msg);
            connectionError.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    //If data is in the cache, load from it.
                    if (!sp.getString(Constants.speKey, "").equals("")) {
                        Constants.venues = new Gson().fromJson(sp.getString(Constants.speKey, ""), Constants.gsonType);
                        for (String venue : Constants.venues.keySet()) {
                            list.add(venue);
                        }
                    }
                }
            });
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectionError.show();
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
        if(!Constants.names.contains(values[0]))
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

}