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
import java.util.Scanner;

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
        ItemSet subVenue;
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
                subName = subName.replace("amp;","");
                subVenue = new ItemSet(subName);
                //Gets each menu item from the category which represents an Item.
                for(Element menuItem : category.select("menu_item"))
                {
                    Item item = new Item();
                    item.setName(menuItem.getElementsByTag("product_name").text());
                    String price = menuItem.getElementsByTag("price").text();
                    if(price.contains("per oz."))
                    {
                        item.setOunces(true);
                    }
                    try
                    {
                        item.setPrice(new BigDecimal(price.replace("$","")));
                    }
                    catch(Exception e)
                    {
                        String tempPrice = handlePriceParseFail(price);
                        if(tempPrice.length() != 0)
                        {
                            item.setPrice(new BigDecimal(tempPrice));
                        }
                        else
                        {
                            item.setPrice(new BigDecimal(Constants.DEFAULT_PRICE));
                        }
                    }
                    item.setDescription(menuItem.getElementsByTag("description").text());
                    subVenue.add(item);
                }
                Constants.venues.get(venue).venueItems.add(subVenue);
            }
            data.update(venue);
            counter++;
        }
        //Write venue object to storage.
        spe.putString(Constants.speKey,cache.toJson(Constants.venues));
        spe.commit();
    }

    /**
     * Handles the situation where parsing the price is not as simple as making it a Big Decimal.
     * In that case the price must be extracted from the price string.
     * @param price The string to extract the price from.
     * @return The extracted price.
     */
    private String handlePriceParseFail(String price)
    {
        Scanner priceScanner = new Scanner(price);
        String value;
        //Iterates  over each part of the price String.
        while(priceScanner.hasNext())
        {
            value = priceScanner.next();
            StringBuilder parsedVal = new StringBuilder();
            //Ensures one of the parts of the string has a dollar sign.
            if(value.contains("$"))
            {
                value.replace("$","");
                //Iterates over the part of the price with a dollar sign and gets the value.
                for(int i = 0; i < value.length(); i++)
                {
                    if(Character.isDigit(value.charAt(i)) || value.charAt(i) == '.')
                    {
                        parsedVal.append(value.charAt(i));
                    }
                }
                return parsedVal.toString();
            }
        }
        return "0.00";
    }
}
