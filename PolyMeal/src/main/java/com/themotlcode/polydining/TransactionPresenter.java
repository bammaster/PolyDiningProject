package com.themotlcode.polydining;

import android.support.v4.app.Fragment;

public class TransactionPresenter
{
    private TransactionFragment fragment;

    public TransactionPresenter(Fragment fragment)
    {
        this.fragment = (TransactionFragment) fragment;
    }

    protected void refresh()
    {
        LoginPresenter lPresenter = new LoginPresenter(fragment);
        lPresenter.loadData();
    }
}
