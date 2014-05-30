package com.mustangexchange.polymeal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Blake on 5/9/2014.
 */
public class Database extends SQLiteOpenHelper
{
    private static final String dbName = "MenuItems";

    private static final String venueTable = "Venues";
    private static final String colName="VenueName";
    private static final String colIdVenue="VenueId";

    private static final String timesTable = "Times";
    private static final String colIdTime="TimeId";
    private static final String colOpen="TimeOpen";
    private static final String colClosed="TimeClosed";
    private static final String colTimeModifier="TimeModifier";

    private static final String itemsetTable="ItemSets";
    private static final String colItemsetName="ItemSetName";

    private static final String itemTable="Items";
    private static final String colItemName="ItemName";
    private static final String colItemPrice="ItemPrice";
    private static final String colItemDesc="ItemDesc";
    private static final String colItemOunces="ItemOunces";
    private static final String colItemModifier="ItemModifier";

    public Database(Context context, int version){
        super(context, dbName, null, version);
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + venueTable + " (" + colName + " TEXT , " + colIdVenue
                + " INTEGER)");
        db.execSQL("CREATE TABLE " + timesTable + " (" + colIdTime + " INTEGER , "
                + colOpen +" INTEGER , " + colClosed + " INTEGER , "
                + colTimeModifier + " INTEGER)");
        db.execSQL("CREATE TABLE " + itemsetTable + " (" + colItemsetName + " TEXT)");
        db.execSQL("CREATE TABLE " + itemTable + " (" + colItemName + " TEXT , "
                + colItemPrice + " TEXT , " + colItemDesc + " TEXT , "
                + colItemOunces + " INTEGER , " + colItemModifier + " INTEGER)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + venueTable);
        db.execSQL("DROP TABLE IF EXISTS " + timesTable);
        db.execSQL("DROP TABLE IF EXISTS " + itemsetTable);
        db.execSQL("DROP TABLE IF EXISTS " + itemTable);
        onCreate(db);
    }
    public void updateVenues(Venue venue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colName, venue.getName());
        cv.put(colIdVenue, venue.getId());
        db.insert(venueTable, colIdVenue, cv);
        cv = new ContentValues();
        cv.put(colIdTime, venue.getName());
        for(VenueTime vt : venue.times)
        {
            for(Time t : vt.open)
            {
                cv.put(colOpen, t.getTimeInMinutes());
            }
            for(Time t : vt.closed)
            {
                cv.put(colClosed, t.getTimeInMinutes());
            }
        }
        cv.put(colTimeModifier, venue.getId());
        db.insert(timesTable, colIdVenue, cv);
        cv = new ContentValues();
        int counter = 0;
        ContentValues cv2 = new ContentValues();
        for(ItemSet is : venue.venueItems)
        {
            cv.put(colItemsetName, is.title);
            cv2 = new ContentValues();
            for(Item i : is.items)
            {
                cv2.put(colItemName, i.getName());
                cv2.put(colItemPrice, i.getPrice().toString());
                cv2.put(colItemDesc, i.getDescription());
                cv2.put(colItemOunces, i.getOunces());
                cv2.put(colItemModifier, counter);
            }
            counter++;
        }
        db.insert(itemsetTable, colIdVenue, cv);
        db.insert(itemTable, colIdVenue, cv2);
    }
}
