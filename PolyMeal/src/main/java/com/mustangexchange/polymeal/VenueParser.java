package com.mustangexchange.polymeal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Blake on 8/6/13.
 */
public class  VenueParser
{
    /**
     * Parses the data from the internet and stores it into shared preferences.
     * @param doc The xml document to parse.
     * @param data Reference to the GetData Object that called this method.
     * @param spe Shared preferences editor to store data with.
     * @throws IOException If a connection error occurs.
     */
    public void parseAndStore(Document doc, GetData data, SharedPreferences.Editor spe) throws IOException
    {
        Gson cache = new Gson();
        Elements links = doc.select("link");
        ItemSet subVenue = new ItemSet();
        int counter = 0;
        //Gets each venues name.
        for(Element name : doc.select("name"))
        {
            String venue = name.text();
            String link = links.get(counter).text();
            Constants.venues.put(venue, new Venue(venue,link,counter));
            Document venDoc = Jsoup.connect(link).get();
            //Gets each category of each venue which represents an ItemSet.
            for(Element category : venDoc.select("category"))
            {
                String subName = category.getElementsByTag("name").text();
                subVenue = new ItemSet(subName);
                //Gets each menu item from the category which represents an Item.
                for(Element menuItem : category.select("menu_item"))
                {
                    Item item = new Item();
                    item.setName(menuItem.getElementsByTag("product_name").text());
                    try
                    {
                        item.setPrice(new BigDecimal(menuItem.getElementsByTag("price").text().replace("$","")));
                    }
                    catch(Exception e)
                    {
                        item.setPrice(new BigDecimal("69.69"));
                    }
                    item.setDescription(menuItem.getElementsByTag("description").text());
                    subVenue.add(item);
                }
                Constants.venues.get(venue).venueItems.add(subVenue);
            }
            data.update(venue);
            counter++;
        }
        spe.putString(Constants.speKey,cache.toJson(Constants.venues));
        spe.commit();
    }
}
