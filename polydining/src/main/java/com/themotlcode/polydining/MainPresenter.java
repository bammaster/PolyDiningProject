package com.themotlcode.polydining;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainPresenter extends Presenter {
    private MainActivity activity;
    private PolyApplication app;

    public MainPresenter(Activity a) {
        this.activity = (MainActivity) a;
        app = (PolyApplication) activity.getApplication();
    }
}
