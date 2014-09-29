package com.mustangexchange.polymeal.models;

/**
 * Stores the hour and minutes for a time.
 */
public class Time {
    /**
     * Conversion constant for hours to minutes.
     */
    private static final int HOURS_TO_MINUTES = 60;

    /**
     * Default hour in case of an invalid time.
     */
    private static final int DEFAULT_HOUR = 23;

    /**
     * Default minutes in case of an invalid time.
     */
    private static final int DEFAULT_MINUTES = 59;

    /**
     * Default time in minutes in case of an invalid time.
     */
    private static final int DEFAULT_TIME_IN_MINUTES = 1439;

    /**
     * Used to determine if the time time is AM or PM.
     */
    private static final int AM_TO_PM_HOUR = 12;

    /**
     * This times hour value.
     */
    private int hour;

    /**
     * The number of minutes for this time
     */
    private int minutes;

    /**
     * Builds a new time.
     *
     * @param hour    The hour for the new time.
     * @param minutes The number of minutes for the new time.
     */
    public Time(int hour, int minutes) {
        if (hour > DEFAULT_HOUR) {
            this.hour = DEFAULT_HOUR;
        } else {
            this.hour = hour;
        }
        if (minutes > DEFAULT_MINUTES) {
            this.minutes = DEFAULT_MINUTES;
        } else {
            this.minutes = minutes;
        }
    }

    /**
     * Builds a Time by copying another Time.
     *
     * @param time The Time to copy.
     */
    public Time(Time time) {
        this.hour = time.hour;
        this.minutes = time.minutes;
    }

    /**
     * Builds a time from the time in minutes. Useful for building times from the database.
     *
     * @param timeInMinutes The time in minutes to make this time for.
     */
    public Time(int timeInMinutes) {
        if (timeInMinutes <= DEFAULT_TIME_IN_MINUTES) {
            hour = timeInMinutes / HOURS_TO_MINUTES;
            minutes = timeInMinutes % HOURS_TO_MINUTES;
        } else {
            hour = DEFAULT_HOUR;
            minutes = DEFAULT_MINUTES;
        }
    }

    /**
     * Gets the value for this time in minutes.
     *
     * @return The value of this time in minutes.
     */
    public int getTimeInMinutes() {
        return hour * HOURS_TO_MINUTES + minutes;
    }

    /**
     * Converts this time to the String representation of a time such as 8:00 PM
     *
     * @return The String representation of a time.
     */
    @Override
    public String toString() {
        String amOrPm = "PM";
        if (hour < AM_TO_PM_HOUR) {
            amOrPm = "AM";
        }
        return hour + ":" + minutes + " " + amOrPm;
    }

}

