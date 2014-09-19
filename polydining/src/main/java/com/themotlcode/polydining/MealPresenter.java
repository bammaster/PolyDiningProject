package com.themotlcode.polydining;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.themotlcode.polydining.models.ItemList;
import com.themotlcode.polydining.models.MoneyTime;

import java.math.BigDecimal;

public class MealPresenter
{
    protected static Fragment fragment;
    private static BigDecimal totalAmount;
    private PolyApplication app;
    protected ItemList items;

    protected void setFragment(Fragment fragment)
    {
        this.fragment = fragment;
        app = (PolyApplication) fragment.getActivity().getApplication();
    }

    private static void updateTotalAmount()
    {
        totalAmount = MoneyTime.calcTotalMoney();
    }

    private void setSubtitle()
    {
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView) fragment.getActivity().findViewById(titleId);
        updateTotalAmount();
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            yourTextView.setTextColor(Color.RED);
        }
        else
        {
            yourTextView.setTextColor(Color.WHITE);
        }
        yourTextView.setAlpha(0);
        yourTextView.animate().alpha(1.0f).setStartDelay(150).setDuration(500).start();
        //for some reason, this must be here for the subtitle to display
        if(app.user.getMeals() > 0 || PolyApplication.plus)
        {
            fragment.getActivity().getActionBar().setSubtitle("$" + totalAmount + " Remaining");
        }
        else if(app.user.getMeals() == 0 && app.cart.size() == 0)
        {
            fragment.getActivity().getActionBar().setSubtitle("No Meals" + " Remaining");
        }
        else
        {
            fragment.getActivity().getActionBar().setSubtitle("$" + totalAmount + " Remaining");
        }
    }

    protected void updateBalance() {
        updateTotalAmount();
        setSubtitle();
    }
}
