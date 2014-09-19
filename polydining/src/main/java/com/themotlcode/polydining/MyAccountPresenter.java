package com.themotlcode.polydining;

import android.support.v4.app.Fragment;

import com.themotlcode.polydining.models.Account;
import com.themotlcode.polydining.models.AccountTransaction;

public class MyAccountPresenter
{

    private Fragment fragment;
    private PolyApplication app;

    public MyAccountPresenter(Fragment fragment)
    {
        this.fragment = fragment;
        this.app = (PolyApplication) fragment.getActivity().getApplication();
    }

    public void refresh()
    {

        app.user = new Account(app.user.getUsername(), app.user.getPassword(), app.user.isRemembered());
        Account.deleteAll(Account.class);
        AccountTransaction.deleteAll(AccountTransaction.class);

        LoginPresenter lPresenter = new LoginPresenter(fragment);
        lPresenter.loadData();
    }

}
