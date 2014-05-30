package com.mustangexchange.polymeal;

/**
 * Stores the hour and minutes for a time.
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

    /**
     * Gets the value for this time in minutes.
     * @return The value of this time in minutes.
     */
    public int getTimeInMinutes()
    {
        return hour*Constants.HOURS_TO_MINUTES + minutes;
    }

    @Override
    public String toString()
    {
        String amOrPm = "PM";
        if(hour < 12)
        {
            amOrPm = "AM";
        }
        return hour + ":" + minutes + " " + amOrPm;
    }

}
