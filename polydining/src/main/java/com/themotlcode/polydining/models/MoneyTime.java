package com.themotlcode.polydining.models;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;
import com.themotlcode.polydining.MainActivity;

import java.math.BigDecimal;

/**
 * Created by Blake on 8/7/13.
 */
public class MoneyTime
{
    protected static BigDecimal[] mealWorth = {new BigDecimal("7.90"),new BigDecimal("9.00"),new BigDecimal("10.75"),new BigDecimal("8.75")};
    protected static BigDecimal moneySpent = new BigDecimal("0.00");
    protected static Time today = new Time(Time.getCurrentTimezone());
    protected static BigDecimal money;
    //public static boolean manual;
    public static int manualTime;
    private static int realTime;

    //gets time, calculates monetary value of a meal minus whatever has been spent then returns that value
    public static BigDecimal calcTotalMoney()
    {
        SharedPreferences defaultSP;
        defaultSP = PreferenceManager.getDefaultSharedPreferences(MainActivity.mActivity);
        manualTime = Integer.valueOf(defaultSP.getString("moneyMode", "4"));


        if(manualTime==4)
        {
            today.setToNow();
            int minutes = (today.hour*60)+today.minute;
            if(minutes>=420&&minutes<=599)
            {
                money = mealWorth[0];
            }
            else if(minutes>=600&&minutes<=1019)
            {
                money = mealWorth[1];
            }
            else if(minutes>=1020&&minutes<=1214)
            {
                money = mealWorth[2];
            }
            else
            {
                money = mealWorth[3];
            }
            return money.subtract(moneySpent).setScale(2);
        }
        else
        {
            return mealWorth[manualTime].subtract(moneySpent).setScale(2);
        }
    }
    public static int calcRealTime()
    {
        today.setToNow();
        int minutes = (today.hour*60)+today.minute;
        if(minutes>=420&&minutes<=599)
        {
            realTime=0;
        }
        else if(minutes>=600&&minutes<=1019)
        {
            realTime=1;
        }
        else if(minutes>=1020&&minutes<=1214)
        {
            realTime=2;
        }
        else
        {
            realTime=3;
        }
        return realTime;
    }

    public static void setMoneySpent(BigDecimal newMoneySpent)
    {
        moneySpent = newMoneySpent;
    }

    public static BigDecimal getMoneySpent()
    {
        return new BigDecimal(moneySpent.toString());
    }
}