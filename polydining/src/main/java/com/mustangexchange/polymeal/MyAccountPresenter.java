package com.mustangexchange.polymeal;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.mustangexchange.polymeal.models.Account;
import com.mustangexchange.polymeal.models.AccountTransaction;

public class MyAccountPresenter {

    private Fragment fragment;
    private PolyApplication app;

    public MyAccountPresenter(Fragment fragment) {
        this.fragment = fragment;
        this.app = (PolyApplication) fragment.getActivity().getApplication();
    }

    public void refresh(MenuItem item) {

        app.user = new Account(app.user.getUsername(), app.user.getPassword(), app.user.isRemembered());
        Account.deleteAll(Account.class);
        AccountTransaction.deleteAll(AccountTransaction.class);

        LoginPresenter lPresenter = new LoginPresenter(fragment);
        lPresenter.loadData(item);
    }

}
