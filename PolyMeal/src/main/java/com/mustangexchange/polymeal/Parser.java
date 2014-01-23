package com.mustangexchange.polymeal;

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
public class  Parser
{
    private Item item;
    private ItemSet items;
    private ArrayList<ItemSet> listItems;

    //counter for getting each h2 element since they are not a part of the the html table
    private int counter;

    private boolean parseDesc;
    private boolean price;
    private boolean name;
    private boolean soupAndSalad = false;

    private Document doc;

    public Parser(ArrayList<ItemSet> listItems,Document doc)
    {
        this.listItems = listItems;
        this.doc = doc;
    }

    //receives doc to parse
    public void parse() throws IOException
    {
        for(Element venue : doc.select("item"))
        {
            String name = venue.select("name").text();
            String link = venue.select("link").text();
            String loc = venue.select("location_name").text();
            String phoneNumber = venue.select("number").text();
            Venue tempVenue = new Venue(name,link,loc,phoneNumber);
            Constants.venues.add(tempVenue);
            Document venDoc = Jsoup.connect(link).get();
            for(Element category : venDoc.select("category"))
            {
                ItemSet itemSet = new ItemSet(category.select("name").text(),new ArrayList<Item>());
                for(Element item : category.select("menu_item"))
                {
                    Item tempItem = new Item();
                    tempItem.setName(item.select("product_name").text());
                    String tempPrice = item.select("price").text();
                    if(handlePrice(tempPrice))
                    {
                    
                    }
                    else
                    {
                        tempItem.setValid(false);
                    }
                    tempItem.setPrice();
                    tempItem.setDescription(item.select("description").toString());
                    itemSet.add(tempItem);
                }
            }
        }
    }
    private boolean handlePrice(String temp)
    {

    }
}
