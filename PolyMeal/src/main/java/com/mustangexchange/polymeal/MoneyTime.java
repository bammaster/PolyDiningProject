package com.mustangexchange.polymeal;

import android.text.format.Time;

import java.math.BigDecimal;

/**
 * Created by Blake on 8/7/13.
 */
public class MoneyTime
{
    public static BigDecimal[] mealWorth = {new BigDecimal("7.90"),new BigDecimal("9.00"),new BigDecimal("10.75"),new BigDecimal("8.75")};
    public static BigDecimal moneySpent = new BigDecimal("0.00");
    public static Time today = new Time(Time.getCurrentTimezone());
    public static BigDecimal money;
    public static boolean manual;
    public static int whichTime;

    //gets time, calculates monetary value of a meal minus whatever has been spent then returns that value
    public static BigDecimal calcTotalMoney()
    {
        if(!manual||whichTime==4)
        {
            today.setToNow();
            int minutes = (today.hour*60)+today.minute;
            if(minutes>=420&&minutes<=599)
            {
                money = mealWorth[0];
                whichTime=0;
            }
            else if(minutes>=600&&minutes<=1019)
            {
                money = mealWorth[1];
                whichTime=1;
            }
            else if(minutes>=1020&&minutes<=1214)
            {
                money = mealWorth[2];
                whichTime=2;
            }
            else
            {
                money = mealWorth[3];
                whichTime=3;
            }
            return money.subtract(moneySpent).setScale(2);
        }
        else
        {
            return mealWorth[whichTime].subtract(moneySpent).setScale(2);
        }
    }
}
