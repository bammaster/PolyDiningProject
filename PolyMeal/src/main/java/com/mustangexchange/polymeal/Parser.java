package com.mustangexchange.polymeal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Blake on 8/6/13.
 */
public class Parser
{

    private ItemSet set;
    private ArrayList<ItemSet> sets;
    private int counter;
    private boolean money;
    private boolean soupAndSalad = false;
    private NumberFormat currency = NumberFormat.getCurrencyInstance();
    public Parser(ArrayList<ItemSet> sets)
    {
        this.sets = sets;
    }
    //receives doc to parse and a boolean that determines whether breakfast is valid or not.
    public void parse(Document doc,boolean breakfast)
    {
        Elements h2Eles = doc.getElementsByTag("h2");
        Elements tables = doc.select("table");
        Elements brEles;
        String tempH2;
        String tempName = "";
        String tempPrice = "";
        DecimalFormat df = new DecimalFormat("#.##");
            //parses html with tag hierarchy starting with each table the moving to each table row, then table data and then each strong tag
            for(Element table : tables)
            {
                set = new ItemSet();
                String h2 = h2Eles.get(counter).text();
                tempH2 = h2;
                set.setTitle(h2);
                for(Element tr : table.select("tr"))
                {
                    for(Element td : tr.select("td"))
                    {
                        String strongName = td.select("strong").text();
                        String description = tr.select("td").text();
                        //handle special cases and remove unnecessary part of string for looks.
                        if(!strongName.equals("")&&!tempH2.equals("Breakfast"))
                        {
                            if(!strongName.contains("$"))
                            {
                                tempName = strongName;
                                money = false;
                                if(strongName.contains("Combos - "))
                                {
                                    tempName = strongName;
                                    strongName = strongName.replace("Combos - ","");
                                    //set.getNames().add(strongName);
                                    set.setLastItemName(strongName);
                                }
                                else if(strongName.contains("Just Burgers - "))
                                {
                                    strongName = strongName.replace("Just Burgers - ","");
                                    //set.getNames().add(strongName);
                                    set.setLastItemName(strongName);
                                }
                                else if(strongName.contains("Just Sandwiches - "))
                                {
                                    strongName = strongName.replace("Just Sandwiches - ","");
                                    //set.getNames().add(strongName);
                                    set.setLastItemName(strongName);
                                }
                                else if(strongName.contains("Just Sandwiches - "))
                                {
                                    strongName = strongName.replace("Just Sandwiches- ","");
                                    //set.getNames().add(strongName);
                                    set.setLastItemName(strongName);
                                }
                                else if(strongName.contains("Soup and Salad"))
                                {
                                    soupAndSalad = true;
                                    String one = strongName.substring(0,5);
                                    String two = strongName.substring(9,14);
                                    //set.getNames().add("@#$"+one);
                                    set.setLastItemName("@#$"+one);
                                    //set.getNames().add("@#$"+two);
                                    set.setLastItemName("@#$"+two);
                                }
                                else
                                {
                                    //set.getNames().add(strongName);
                                    set.setLastItemName(strongName);
                                }

                            }
                            else if(strongName.contains("$"))
                            {
                                tempPrice = strongName;
                                money = true;
                                if(strongName.contains(","))
                                {
                                    strongName = strongName.replace(",",".");
                                }
                                //automatically calculates tax if any.
                                if(strongName.contains("plus tax"))
                                {
                                    strongName = strongName.replace(" plus tax","");
                                    strongName = strongName.replace("$","");
                                    //set.getPrices().add(df.format(new Double(strongName)+new Double(strongName)*.08)+"");
                                    set.setLastItemPrice(currency.format(new Double(strongName)+new Double(strongName)*.08));
                                }
                                //gets proper values for anything per oz items by substringing them out.
                                else if(strongName.contains("per oz"))
                                {
                                    String priceOne = strongName.substring(7,11);
                                    String priceTwo = strongName.substring(26,30);
                                    //set.getPrices().add(priceOne);
                                    set.setItemPrice(currency.format(Double.valueOf(priceOne)), 0);
                                    //set.getPrices().add(priceTwo);
                                    set.setItemPrice(currency.format(Double.valueOf(priceTwo)), 1);

                                }
                                else
                                {
                                    //NumberFormat nm = NumberFormat.getCurrencyInstance();
                                    strongName = strongName.replace("$","");
                                    //String formattedPrice = nm.format(Double.valueOf(strongName));
                                    //formattedPrice = formattedPrice.replace("$","");
                                    //set.getPrices().add(formattedPrice);
                                    set.setLastItemPrice(currency.format(Double.valueOf(strongName)));
                                }
                            }
                            descParse(money,tempName,tempPrice,description);
                        }
                        else if(tempH2.equals("Breakfast")&&breakfast)
                        {
                            if(strongName.contains("$"))
                            {
                                money = true;
                                tempPrice = strongName;
                                strongName = strongName.replace("$","");
                                //String formattedPrice = nm.format(Double.valueOf(strongName));
                                //formattedPrice = formattedPrice.replace("$","");
                                //set.getPrices().add(formattedPrice);
                                set.setLastItemPrice(currency.format(Double.valueOf(strongName)));
                            }
                            else
                            {
                                tempName = strongName;
                                money = false;
                                //set.getNames().add(strongName);
                                set.setLastItemName(strongName);
                            }
                            descParse(money,tempName,tempPrice,description);
                        }
                    }
                }
                //adds each table to itemset arraylist then adds one to counter to allow h2 tag selection(workaround for Cal Poly table formatting)
                sets.add(set);
                System.out.println(sets.get(counter).getTitle());
                counter++;

            }

    }
    private void descParse(boolean money,String tempName,String tempPrice,String description)
    {
        if(money)
        {
            //if it is a price value remove both the name and price from the description string.
            description = description.replace(tempName,"");
            description = description.replace(tempPrice,"");
            description = description.replace("$","");
            if(description.equals(" "))
            {
                //set.addDesc("No Description Available!");
                set.setLastItemDesc("No Description Available!");
            }
            else
            {
                if(!soupAndSalad)
                {
                    description = description.replaceFirst(" ", "");
                    //set.addDesc(description);
                    set.setLastItemDesc(description);
                }
                else
                {
                    description = description.replaceFirst(" ", "");
                    //set.addDesc(description);
                    set.setLastItemDesc(description);
                    //set.addDesc(description);
                    set.setItemDesc(description, 1);
                    soupAndSalad = false;
                }
            }
        }
    }
}
