package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mustangexchange.polymeal.models.Cart;
import com.mustangexchange.polymeal.models.MoneyTime;

import java.math.BigDecimal;

public class CartPresenter extends Presenter
{
    private Activity activity;

    public CartPresenter(Activity activity)
    {
        this.activity = activity;

    }

    public BigDecimal getTotalAmount()
    {
        return new BigDecimal(MoneyTime.calcTotalMoney().toString());
    }

    public void updateSettings() {
        SharedPreferences defaultSp = PreferenceManager.getDefaultSharedPreferences(activity);
        int sortMode;

        sortMode = Integer.valueOf(defaultSp.getString("sortMode", "0"));

        Cart.sort(sortMode);
    }
}
