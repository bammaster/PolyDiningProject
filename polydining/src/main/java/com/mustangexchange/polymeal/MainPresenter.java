package com.mustangexchange.polymeal;

import android.app.Activity;

public class MainPresenter extends Presenter {
    public MainPresenter(Activity a) {
        MainActivity activity;
        PolyApplication app;
        activity = (MainActivity) a;
        app = (PolyApplication) activity.getApplication();
    }
}
