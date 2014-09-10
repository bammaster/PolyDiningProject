package com.themotlcode.polydining;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import com.themotlcode.polydining.models.Cart;

public class CartPresenter extends MealPresenter
{
    private CartFragment fragment;

    public CartPresenter(Fragment fragment)
    {
        this.fragment = (CartFragment) fragment;
        setFragment(fragment);

    }

    protected void updateSettings() {
        SharedPreferences defaultSp = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        int sortMode;

        sortMode = Integer.valueOf(defaultSp.getString("sortMode", "0"));

        Cart.sort(sortMode);
    }
}
