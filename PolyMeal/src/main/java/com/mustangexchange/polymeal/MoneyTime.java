package com.mustangexchange.polymeal;

import android.text.format.Time;

import java.math.BigDecimal;

/**
 * Created by Blake on 8/7/13.
 */
public class MoneyTime
{
    public static double moneySpent;
    public static Time today = new Time(Time.getCurrentTimezone());
    public static double money;
    public static BigDecimal bd;
    //gets time, calculates monetary value of a meal minus whatever has been spent then returns that value
    public static BigDecimal calcTotalMoney()
    {
        bd = new BigDecimal(money+"");
        bd.setScale(2,BigDecimal.ROUND_HALF_EVEN);
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
        //return bd.subtract(new BigDecimal(moneySpent));
        System.out.println(money);
        return new BigDecimal(money);
    }
}
