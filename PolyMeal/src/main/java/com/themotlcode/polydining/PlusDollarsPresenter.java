package com.themotlcode.polydining;

import android.support.v4.app.Fragment;

public class PlusDollarsPresenter
{

    private PlusDollarsFragment fragment;

    public PlusDollarsPresenter(Fragment fragment)
    {
        this.fragment = (PlusDollarsFragment) fragment;
    }

    public void refresh()
    {
        LoginPresenter lPresenter = new LoginPresenter(fragment);
        lPresenter.loadData();
    }

}
