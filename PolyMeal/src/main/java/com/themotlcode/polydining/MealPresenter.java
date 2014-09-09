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
        fragment.getActivity().getActionBar().setSubtitle("$" + totalAmount + " Remaining");
    }

    protected void updateBalance() {
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
    }
}
