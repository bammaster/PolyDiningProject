package com.mustangexchange.polymeal;

import android.text.format.Time;

import java.math.BigDecimal;

/**
 * Created by Blake on 8/7/13.
 */
public class MoneyTime
{
    public static BigDecimal moneySpent = new BigDecimal("0.00");
    public static Time today = new Time(Time.getCurrentTimezone());
    public static BigDecimal money;

    //gets time, calculates monetary value of a meal minus whatever has been spent then returns that value
    public static BigDecimal calcTotalMoney()
    {
        today.setToNow();
        int minutes = (today.hour*60)+today.minute;
        if(minutes>=420&&minutes<=599)
        {
             money = new BigDecimal("7.90");
        }
        else if(minutes>=600&&minutes<=1019)
        {
             money = new BigDecimal("9.00");
        }
        else if(minutes>=1020&&minutes<=1214)
        {
             money = new BigDecimal("10.75");
        }
        else
        {
            money = new BigDecimal("8.75");
        }
        return money.subtract(moneySpent).setScale(2);
    }
}
