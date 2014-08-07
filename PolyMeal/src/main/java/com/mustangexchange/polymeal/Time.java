package com.mustangexchange.polymeal;

/**
 * Stores the hour and minutes for a time.
 */
public class Time
{
    /**Conversion constant for hours to minutes.*/
    private static final int HOURS_TO_MINUTES = 60;

    /**This times hour value.*/
    private int hour;

    /**The number of minutes for this time*/
    private int minutes;

    /**
     * Builds a new time.
     * @param hour The hour for the new time.
     * @param minutes The number of minutes for the new time.
     */
    public Time(int hour, int minutes)
    {
        if(hour > 23)
        {
            this.hour = 23;
        }
        else
        {
            this.hour = hour;
        }
        if(minutes > 60)
        {
            this.minutes = 60;
        }
        else
        {
            this.minutes = minutes;
        }
    }

    /**
     * Builds a Time by copying another Time.
     * @param time The Time to copy.
     */
    public Time(Time time)
    {
        this.hour = time.hour;
        this.minutes = time.minutes;
    }

    /**
     * Gets the value for this time in minutes.
     * @return The value of this time in minutes.
     */
    public int getTimeInMinutes()
    {
        return hour*HOURS_TO_MINUTES + minutes;
    }

    /**
     * Converts this time to the String representation of a time such as 8:00 PM
     * @return The String representation of a time.
     */
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
