package com.mustangexchange.polymeal.models;

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
    public ArrayList<ItemSet> venueItems;
    protected ArrayList<VenueTime> times;
    //String key for retrieving data from shared preferences.
    private String name;
    private int id;
    //construct a venue with a name and url.
    public Venue(String name, int id)
    {
        venueItems = new ArrayList<ItemSet>();
        times = new ArrayList<VenueTime>();
        this.name = name;
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
    /**
     * Gets a deep copy of all of the ItemSets for this Venue.
     * @return A deep copy of the ItemSets for this Venue.
     */
    public ArrayList<ItemSet> getVenueItems()
    {
        ArrayList<ItemSet> deepCopy = new ArrayList<ItemSet>();
        for(ItemSet set : venueItems)
        {
            deepCopy.add(new ItemSet(set));
        }
        return deepCopy;
    }
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
    public void addTime(VenueTime openTimes)
    {
        times.add(openTimes);
    }

    public String getVenueTitle(int pos)
    {
        return venueItems.get(pos).getTitle();
    }
}
