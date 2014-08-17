package com.themotlcode.polydining.models;

import android.content.Context;
import android.widget.Toast;

import com.themotlcode.polydining.R;

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
 * Created by Blake on 8/16/2014.
 */
public class SendError
{
    public SendError()
    {}
    public void sendErrorToDeveloper(final String error, final Context mContext)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    DefaultHttpClient hc = new DefaultHttpClient();
                    ResponseHandler<String> res = new BasicResponseHandler();
                    HttpPost postMethod = new HttpPost("http://themotlcode.com/email.php");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("stackTrace", error));
                    postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    hc.execute(postMethod, res);
                    Toast.makeText(mContext, R.string.send_success, Toast.LENGTH_SHORT).show();
                }
                catch (IOException io)
                {
                    Toast.makeText(mContext, R.string.send_error, Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    public String stackTraceToString(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
