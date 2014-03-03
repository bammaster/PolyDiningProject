package com.mustangexchange.polymeal;

import android.util.Log;

/**
 * Created by Blake on 2/28/14.
 */
public class Time
{
    private int hour;
    private int minutes;
    public Time(int hour, int minutes)
    {
        this.hour = hour;
        this.minutes = minutes;
    }
    public int getHour()
    {
        return hour;
    }
    public int getMinutes()
    {
        return minutes;
    }
    public int getTimeInMinutes()
    {
        return hour*Constants.HOURS_TO_MINUTES + minutes;
    }
}
