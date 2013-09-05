package com.mustangexchange.polymeal;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.math.BigDecimal;

public class ItemSet {
    private ArrayList<Item> items = new ArrayList<Item>();
    public String title;
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> prices = new ArrayList<String>();
    private ArrayList<String> desc = new ArrayList<String>();

    public ItemSet(String title, ArrayList<Item> items) {
        this.title = title;
        this.items = items;
        for(int i = 0;i<items.size();i++) {
            names.add(items.get(i).getName());
            prices.add(items.get(i).getPrice());
            desc.add(items.get(i).getDesc());
        }
    }

    public ItemSet() {}

    public int size() {
        return items.size();
    }

    public void setLastItemName(String name) {
        Item lastItem;
        if(items.size() > 0) {
           lastItem = items.get(items.size() - 1);
            if(lastItem.name != null) {
                items.add(new Item());
                lastItem = items.get(items.size() - 1);
            }
        } else {
            items.add(new Item());
            lastItem = items.get(items.size() - 1);
        }
        lastItem.name = name;
    }

    public void setLastItemPrice(String price) {
        Item lastItem;
        System.out.println("price: '" + price + "'");
        if(price != null && !price.equals(""))
        {
            price = price.substring(1);
        }
        else
        {
            price = "0.00";
        }
        BigDecimal bdPrice = new BigDecimal(price);
        if(items.size() > 0) {
            lastItem = items.get(items.size() - 1);
            if(lastItem.price != null) {
                items.add(new Item());
                lastItem = items.get(items.size() - 1);
            }
        } else {
            items.add(new Item());
            lastItem = items.get(items.size() - 1);
        }
        lastItem.price = bdPrice;
    }

    public void setItemPrice(String price, int index) {
        Item lastItem = items.get(index);
        if(price != null)
        {
            price = price.substring(1);
        }
        else
        {
            price = "0.00";
        }
        BigDecimal bdPrice = new BigDecimal(price);
        lastItem.price = bdPrice;
    }
    public void setLastItemDesc(String desc) {
        Item lastItem;
        if(items.size() > 0) {
            lastItem = items.get(items.size() - 1);
            if(lastItem.desc != null) {
                items.add(new Item());
                lastItem = items.get(items.size() - 1);
            }
        } else {
            items.add(new Item());
            lastItem = items.get(items.size() - 1);
        }
        lastItem.desc = desc;
    }

    public void setItemDesc(String desc, int index) {
        Item lastItem = items.get(index);
        lastItem.desc = desc;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<String> getNames() {
        if(names.size() == items.size()) {
            return names;
        } else 
        {
            names.clear();
            for(int i = 0;i<items.size();i++) {
                names.add(items.get(i).getName());
            }
            return names;
        }
    }

    public ArrayList<String> getPrices() {
        if(prices.size() == items.size()) {
            return prices;
        } else
        {
            prices.clear();
            for(int i = 0;i<items.size();i++) {
                try {
                    prices.add(items.get(i).getPrice());
                }
                catch(NullPointerException e) {
                    items.get(i).price = new BigDecimal("0.00");
                    prices.add(items.get(i).getPrice());
                }
            }
            return prices;
        }
    }

    public ArrayList<String> getDesc() {
        if(desc.size() == items.size()) {
            return desc;
        } else
        {
            desc.clear();
            for(int i = 0;i<items.size();i++) {
                desc.add(items.get(i).getDesc());
            }
            return desc;
        }
    }

    public String getTitle() {
        return title;
    }

    public void clear() {
        items.clear();
        names.clear();
        prices.clear();
        desc.clear();
    }

    public void setTitle(String title) {
        this.title = title;
    }






    public static class Item {
        private String name;
        private BigDecimal price;
        private String desc;

        public Item(String name, BigDecimal price, String desc) {
            this.name = name;
            this.price = price;
            this.desc = desc;
        }

        public Item(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public Item() {
            //price = new BigDecimal("0.00");
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            String tempPrice = price.toString();
            tempPrice = currency.format(Double.valueOf(tempPrice));
            tempPrice = tempPrice.substring(1);
            return tempPrice;
        }

        public BigDecimal getPriceBD() {
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            String tempPrice = price.toString();
            tempPrice = currency.format(Double.valueOf(tempPrice));
            tempPrice = tempPrice.substring(1);
            price = new BigDecimal(tempPrice);
            return price;
        }

        public String getDesc() {
            return desc;
        }
    }
}