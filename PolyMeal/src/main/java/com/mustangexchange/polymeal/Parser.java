package com.mustangexchange.polymeal;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Blake on 8/6/13.
 */
public class Parser
{

    private ItemSet set;
    private ArrayList<ItemSet> sets;
    private int counter;
    public Parser(ArrayList<ItemSet> sets)
    {
        this.sets = sets;
    }
    public void parse(Document doc)
    {
            Elements h2Eles = doc.getElementsByTag("h2");
            Elements tables = doc.select("table");
            String tempH2;
            for(Element table : tables)
            {
                set = new ItemSet();
                String h2 = h2Eles.get(counter).text();
                tempH2 = h2;
                set.setTitle("h2!#$"+h2);
                for(Element tr : table.select("tr"))
                {
                    for(Element td : tr.select("td"))
                    {
                        String strongName = td.select("strong").text();
                        if(strongName.contains("$"))
                        {
                            strongName = strongName.replace("$","");
                            set.getPrices().add(strongName);
                        }
                        else if(!tempH2.equals("Breakfast")&&!strongName.equals("")
                                &&!strongName.contains("Sandwich Factory"))
                        {
                            if(strongName.contains("Combos - "))
                            {
                                strongName = strongName.replace("Combos - ","");
                                set.getNames().add(strongName);
                            }
                            else if(strongName.contains("Just Burgers - "))
                            {
                                strongName = strongName.replace("Just Burgers - ","");
                                set.getNames().add(strongName);
                            }
                            else if(strongName.contains("Just Sandwiches - "))
                            {
                                strongName = strongName.replace("Just Sandwiches - ","");
                                set.getNames().add(strongName);
                            }
                            else if(strongName.contains("Just Sandwiches - "))
                            {
                                strongName = strongName.replace("Just Sandwiches- ","");
                                set.getNames().add(strongName);
                            }
                            else
                            {
                                set.getNames().add(strongName);
                            }
                        }
                    }
                }
                sets.add(set);
                counter++;
            }

    }
}
