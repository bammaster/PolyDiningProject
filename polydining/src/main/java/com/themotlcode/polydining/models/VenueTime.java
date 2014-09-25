package com.themotlcode.polydining.models;

import com.themotlcode.polydining.PolyApplication;

import java.util.ArrayList;

/**
 * Stores the open and close times for a venue. Can also determine if venue is open or closing soon.
 */
public class VenueTime {
    /**
     * Used to store if a venue is closed all day.
     */
    private boolean closedAllDay = false;
    /**
     * All of the open times. Will work with breaks in service. For example 12-3 then 5-8.
     * This is why open and closed are array lists. Open is for the opening times and closed is for
     * the closing times.
     */
    private ArrayList<Time> open;
    private ArrayList<Time> closed;

    /**
     * The default constructor.
     */
    public VenueTime() {
        open = new ArrayList<Time>();
        closed = new ArrayList<Time>();
    }

    /**
     * Builds a VenueTime by copying another VenueTime.
     *
     * @param venueTime The VenueTime to copy.
     */
    public VenueTime(VenueTime venueTime) {
        open = new ArrayList<Time>();
        closed = new ArrayList<Time>();
        for (Time time : venueTime.open) {
            open.add(new Time(time));
        }
        for (Time time : venueTime.closed) {
            closed.add(new Time(time));
        }
        this.closedAllDay = venueTime.closedAllDay;
    }

    /**
     * Gets a deep copy of the open times for its venue.
     *
     * @return The times the venue is open.
     */
    public ArrayList<Time> getOpenTimes() {
        ArrayList<Time> temp = new ArrayList<Time>();
        for (Time t : open) {
            temp.add(new Time(t));
        }
        return temp;
    }

    /**
     * Add an opening time for this venue time.
     *
     * @param open When the venue opens.
     */
    public void addOpen(Time open) {
        this.open.add(open);
    }

    /**
     * Gets a deep copy of the closed times for its venue.
     *
     * @return The times the venue closes.
     */
    public ArrayList<Time> getClosedTimes() {
        ArrayList<Time> temp = new ArrayList<Time>();
        for (Time t : closed) {
            temp.add(new Time(t));
        }
        return temp;
    }

    /**
     * Add a closing time for the venue.
     *
     * @param closed When the venue closes.
     */
    public void addClosed(Time closed) {
        this.closed.add(closed);
    }

    /**
     * Returns true if the venue is currently open or false if it is closed.
     *
     * @param now The current time.
     * @return Whether or not the venue is open.
     */
    public boolean isOpen(Time now) {
        if (closedAllDay) {
            return false;
        } else if (this.open.size() == closed.size()) {
            for (int i = 0; i < open.size(); i++) {
                if (now.getTimeInMinutes() >= open.get(i).getTimeInMinutes() &&
                        now.getTimeInMinutes() <= closed.get(i).getTimeInMinutes()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the venue is closing within an hour or false otherwise.
     *
     * @param now The current time.
     * @return Whether or not the venue will close within the hour.
     */
    public boolean closedSoon(Time now) {
        for (int i = 0; i < open.size(); i++) {
            if (now.getTimeInMinutes() >= closed.get(i).getTimeInMinutes() - PolyApplication.HOURS_TO_MINUTES
                    && now.getTimeInMinutes() <= closed.get(i).getTimeInMinutes()) {
                if (now.getTimeInMinutes() >= PolyApplication.ELEVEN_O_CLOCK_MINUTES &&
                        closed.get(i).getTimeInMinutes() <= PolyApplication.TWELVE_O_CLOCK_MINUTES) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Setter method to store if the venue is closed all day.
     */
    public void setClosedAllDay() {
        closedAllDay = true;
    }
}

