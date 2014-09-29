package com.mustangexchange.polymeal;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;



import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to send an error to the server.
 */
public class ErrorSender {
    private static Activity activity;

    private ErrorSender() {
    }

    public static void sendErrorToDeveloper(final Throwable error, final Activity activity) {
        ErrorSender.activity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("Blake", "Started!");
                    DefaultHttpClient hc = new DefaultHttpClient();
                    ResponseHandler<String> res = new BasicResponseHandler();
                    HttpPost postMethod = new HttpPost("http://themotlcode.com/email.php");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("stackTrace", stackTraceToString(error)));
                    postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    String response = hc.execute(postMethod, res);
                    Log.e("Blake", "Posted!");
                    if (response.equals("1")) {
                        exists();
                    } else {
                        success();
                    }

                } catch (IOException io) {
                    fail();
                    Log.e("Blake", "Uncaught Error!");

                } finally {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

            }
        }).start();
    }

    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Log.e("Blake", sw.toString());
        return sw.toString();
    }

    private static void success() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.send_success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void fail() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.send_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void exists() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.send_exists, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

