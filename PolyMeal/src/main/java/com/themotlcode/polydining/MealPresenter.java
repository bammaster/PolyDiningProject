package com.themotlcode.polydining;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import com.themotlcode.polydining.models.MoneyTime;

import java.math.BigDecimal;

public class MealPresenter
{
    private static Fragment fragment;
    private static BigDecimal totalAmount;
    private PolyApplication app;

    public void setFragment(Fragment fragment)
    {
        this.fragment = fragment;
        app = (PolyApplication) fragment.getActivity().getApplication();
    }

    public static void updateTotalAmount()
    {
        totalAmount = MoneyTime.calcTotalMoney();
    }

    public void setSubtitle()
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
        fragment.getActivity().getActionBar().setSubtitle("$" + totalAmount + " Remaining");
    }
    /*
        TODO: Figure out someway to check the PlusDollars balance from here.
     */
    /*public boolean updateBalance()
    {
        updateTotalAmount();
        //Alerts the user if they will exceed their plus dollars with whats in the cart.
        if (totalAmount.compareTo(new BigDecimal("0.00")) < 0)
        {
            if (Statics.user != null && totalAmount.multiply(new BigDecimal("-1")).compareTo(Statics.user.getPlusDollars()) > 0)
            {
                return true;
            }
        }
        return false;
    }*/
    public void updateBalance() {
        updateTotalAmount();
        //Alerts the user if they will exceed their plus dollars with whats in the cart.
        if(totalAmount.compareTo(new BigDecimal("0.00")) < 0)
        {
            if(app.user != null && totalAmount.multiply(new BigDecimal("-1")).compareTo(app.user.getPlusDollars()) > 0)
            {
                QustomDialogBuilder plusDollarsExceeded = new QustomDialogBuilder(fragment.getActivity());
                plusDollarsExceeded.setDividerColor(PolyApplication.APP_COLOR);
                plusDollarsExceeded.setTitleColor(PolyApplication.APP_COLOR);
                plusDollarsExceeded.setTitle(R.string.plusdollarsalert);
                plusDollarsExceeded.setMessage(R.string.plusdollarsalertmessage);
                plusDollarsExceeded.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                plusDollarsExceeded.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                plusDollarsExceeded.show();
            }
        }
        setSubtitle();
        fragment.getActivity().getActionBar().setSubtitle("$" + totalAmount + " Remaining");
    }
}
