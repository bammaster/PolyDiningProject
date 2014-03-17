package com.mustangexchange.polymeal;

import java.util.ArrayList;

/**
 * Created by Blake on 2/28/14.
 */
public class DayTimes
{
    private boolean closedAllDay = false;
    private ArrayList<Time> open;
    private ArrayList<Time> closed;
    public DayTimes()
    {
        open = new ArrayList<Time>();
        closed = new ArrayList<Time>();
    }
    public void addOpen(Time open)
    {
        this.open.add(open);
    }
    public void addClosed(Time closed)
    {
        this.closed.add(closed);
    }
    public boolean isOpen(Time now)
    {
        if(closedAllDay)
        {
            return false;
        }
        else if(this.open.size() == closed.size())
        {
            for(int i = 0; i < open.size(); i++)
            {
                if(now.getTimeInMinutes() >= open.get(i).getTimeInMinutes() &&
                    now.getTimeInMinutes() <= closed.get(i).getTimeInMinutes())
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean closedSoon(Time now)
    {
        for(int i = 0; i < open.size(); i++)
        {
            if(now.getTimeInMinutes() >= closed.get(i).getTimeInMinutes() - Constants.HOURS_TO_MINUTES
               &&now.getTimeInMinutes() <= closed.get(i).getTimeInMinutes())
            {
                return true;
            }
        }
        return false;
    }
    public void setClosedAllDay()
    {
        closedAllDay = true;
    }
}
