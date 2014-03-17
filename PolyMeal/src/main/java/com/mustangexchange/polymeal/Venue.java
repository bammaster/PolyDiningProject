package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Blake on 9/28/13.
 */
public class Venue
{
    //holds the item sets for a venue.
    protected ArrayList<ItemSet> venueItems;
    private ArrayList<DayTimes> times;
    //String key for retrieving data from shared preferences.
    private String name;
    private String url;
    private int id;
    //construct a venu with a name and url.
    public Venue(String name, String url, int id)
    {
        venueItems = new ArrayList<ItemSet>();
        times = new ArrayList<DayTimes>();
        this.name = name;
        this.url = url;
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public int getId()
    {
        return id;
    }
    public int size() { return venueItems.size();}
    public boolean isOpen()
    {
        android.text.format.Time today =
                new android.text.format.Time(android.text.format.Time.getCurrentTimezone());
        today.setToNow();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String weekDay = dayFormat.format(calendar.getTime());
        if(weekDay.equals("Sunday"))
        {
            return times.get(0).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Monday"))
        {
            return times.get(1).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Tuesday"))
        {
            return times.get(2).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Wednesday"))
        {
            return times.get(3).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Thursday"))
        {
            return times.get(4).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Friday"))
        {
            return times.get(5).isOpen(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Saturday"))
        {
            return times.get(6).isOpen(new Time(today.hour, today.minute));
        }
        return false;
    }
    public boolean closeSoon()
    {
        android.text.format.Time today =
                new android.text.format.Time(android.text.format.Time.getCurrentTimezone());
        today.setToNow();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String weekDay = dayFormat.format(calendar.getTime());
        if(weekDay.equals("Sunday"))
        {
            return times.get(0).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Monday"))
        {
            return times.get(1).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Tuesday"))
        {
            return times.get(2).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Wednesday"))
        {
            return times.get(3).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Thursday"))
        {
            return times.get(4).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Friday"))
        {
            return times.get(5).closedSoon(new Time(today.hour, today.minute));
        }
        else if(weekDay.equals("Saturday"))
        {
            return times.get(6).closedSoon(new Time(today.hour, today.minute));
        }
        return false;
    }
    public void addTime(DayTimes openTimes)
    {
        times.add(openTimes);
    }
}
