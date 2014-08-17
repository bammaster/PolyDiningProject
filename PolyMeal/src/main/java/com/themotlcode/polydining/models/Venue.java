package com.themotlcode.polydining.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class is used to represent a restaurant location on campus. For example VG's Cafe.
 * This class stores all of the data associated with this venue including when it is open,
 * when it is closed and its entire menu.
 */
public class Venue
{
    /**Time representing the current day.*/
    private android.text.format.Time today;

    /**Holds the item sets for a venue.*/
    private ArrayList<ItemList> venueItemLists;

    /**All of the opening and closing times for a week.*/
    private ArrayList<VenueTime> venueTimes;

    /**String key for retrieving data from shared preferences.*/
    private String name;

    /**ID used for situations where a numerical identifier is needed.*/
    private int id;

    /**Specifies if the venue accepts meal credits or not.*/
    private MealType mealType;

    /**
     * Construct a venue with a name and numerical id.
     * @param name The name of the new Venue.
     * @param id The numerical ID of the new Venue.
     * @param mealType Specifies if the venue accepts meal credits or not.
     */
    public Venue(String name, int id, MealType mealType)
    {
        venueItemLists = new ArrayList<ItemList>();
        venueTimes = new ArrayList<VenueTime>();
        this.name = name;
        this.id = id;
        this.mealType = mealType;
    }

    /**
     * Gets a deep copy of all of the ItemSets for this Venue.
     * @return A deep copy of the ItemSets for this Venue.
     */
    public ArrayList<ItemList> getVenueItemLists()
    {
        ArrayList<ItemList> deepCopy = new ArrayList<ItemList>();
        for(ItemList set : venueItemLists)
        {
            deepCopy.add(new ItemList(set));
        }
        return deepCopy;
    }

    /**
     * Adds a deep copy of an ItemSet to this Venue's list of ItemSets.
     * @param set The ItemSet to add to this Venue.
     */
    public void addVenueItemSet(ItemList set)
    {
        venueItemLists.add(new ItemList(set));
    }

    /**
     * Gets a deep copy of all of the VenueTimes for this Venue.
     * @return A deep copy of all of the ItemSets for this Venue.
     */
    public ArrayList<VenueTime> getVenueTimes()
    {
        ArrayList<VenueTime> deepCopy = new ArrayList<VenueTime>();
        for(VenueTime time : venueTimes)
        {
            deepCopy.add(new VenueTime(time));
        }
        return deepCopy;
    }

    /**
     * Adds a deep copy of a VenueTime to this Venue's list of VenueTimes.
     * @param time The VenueTime to add to this Venue.
     */
    public void addVenueTime(VenueTime time)
    {
        venueTimes.add(new VenueTime(time));
    }

    /**
     * Gets the name of this venue.
     * @return The name of this venue.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the numerical ID for this Venue.
     * @return The numerical ID for this Venue.
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the number of ItemSets for this Venue.
     * @return The number of ItemSets for this Venue.
     */
    public int numberOfItemSets()
    {
        return venueItemLists.size();
    }

    /**
     * Determines whether or not this Venue is currently open.
     * @return Whether or not this Venue is currently open.
     */
    public boolean isOpen()
    {
        today = new android.text.format.Time(android.text.format.Time.getCurrentTimezone());
        int day = getIntFromDay(getDay());
        if(day == -1)
        {
            Log.e("PolyMeal","The method isOpen returned -1 for Venue: " + name);
            return false;
        }
        return venueTimes.get(day).isOpen(new Time(today.hour, today.minute));
    }

    /**
     * Determines whether or not this Venue will close within the next hour.
     * @return Whether or not the Venue will close within the next hour.
     */
    public boolean closeSoon()
    {
        today = new android.text.format.Time(android.text.format.Time.getCurrentTimezone());
        int day = getIntFromDay(getDay());
        if(day == -1)
        {
            Log.e("PolyMeal","The method closeSoon returned -1 for Venue: " + name);
            return false;
        }
        return venueTimes.get(day).closedSoon(new Time(today.hour, today.minute));
    }

    /**
     * Gets an integer value for the day of the week. Used in this class only.
     * @param day The String representation of the day of the week,
     * @return The integer value for the day of the week or -1 if there is an error.
     */
    private int getIntFromDay(String day)
    {
        if(day.equals("Sunday"))
        {
            return 0;
        }
        else if(day.equals("Monday"))
        {
            return 1;
        }
        else if(day.equals("Tuesday"))
        {
            return 2;
        }
        else if(day.equals("Wednesday"))
        {
            return 3;
        }
        else if(day.equals("Thursday"))
        {
            return 4;
        }
        else if(day.equals("Friday"))
        {
            return 5;
        }
        else if(day.equals("Saturday"))
        {
            return 6;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Gets a String representation of the current day of the week.
     * @return The day of the week.
     */
    private String getDay()
    {
        today.setToNow();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String weekDay = dayFormat.format(calendar.getTime());
        return weekDay;
    }

    /**
     * Adds a deep copy of a VenueTime to this Venue.
     * @param venueTime The VenueTime to add to this Venues list of opening and closing times.
     */
    public void addTime(VenueTime venueTime)
    {
        this.venueTimes.add(new VenueTime(venueTime));
    }

    /**
     * Gets if the venue accepts meal credits or not.
     * @return Does the venue accept meal credits?
     */
    private MealType getType()
    {
        if(mealType == MealType.meal)
        {
            return MealType.meal;
        }
        else
        {
            return MealType.plus;
        }
    }

}

