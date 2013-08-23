package com.mustangexchange.polymeal;

import android.text.format.Time;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Blake on 8/7/13.
 */
public class MoneyTime
{
    public static double moneySpent;
    public static Time today = today = new Time(Time.getCurrentTimezone());
    public static DecimalFormat df = new DecimalFormat("#.##");
    public static double money;
    //gets time, calculates monetary value of a meal then returns that value
    public static double calcTotalMoney()
    {
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        today.setToNow();
        int minutes = (today.hour*60)+today.minute;
        if(minutes>=420&&minutes<=599)
        {
             money = 7.90;
        }
        else if(minutes>=600&&minutes<=1019)
        {
             money = 9.00;
        }
        else if(minutes>=1020&&minutes<=1214)
        {
             money = 10.75;
        }
        else
        {
            money = 8.75;
        }
        return new Double(MoneyTime.df.format(money-moneySpent));
    }
}
