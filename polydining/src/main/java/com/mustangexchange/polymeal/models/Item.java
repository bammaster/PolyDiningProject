package com.mustangexchange.polymeal.models;

import android.os.Parcel;

import com.orm.SugarRecord;
import com.mustangexchange.polymeal.PolyApplication;

import java.math.BigDecimal;

/**
 * This class represents a Item on a menu at a restaurant on campus.
 */
public class Item extends SugarRecord<Item> {
    /**
     * The name of the item.
     */
    private String name;

    /**
     * The price of the item.
     */
    private String price;

    /**
     * The description of the item if it exists.
     */
    private String description;

    /**
     * Whether or not this item is priced per ounce.
     */
    private boolean isPricePerOunce;

    /**
     * Number of times this item appears in the current Cart
     */
    protected int numInCart = 0;

    /**
     * Builds a new Item with all of its properties.
     *
     * @param name        The name of the new Item.
     * @param price       The price of the new Item.
     * @param description The description of the new Item.
     */
    public Item(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price.toString();
        this.description = description;
    }

    /**
     * Builds a new Item from a Parcel.
     *
     * @param in The Parcel to build the Item from.
     */
    public Item(Parcel in) {
        String[] strData = new String[3];
        boolean[] boolData = new boolean[2];

        in.readStringArray(strData);
        this.name = strData[0];
        this.price = strData[1];
        this.description = strData[2];
        in.readBooleanArray(boolData);
        this.isPricePerOunce = boolData[0];
    }

    /**
     * Builds a new Item by copying another Item.
     *
     * @param item The Item to copy.
     */
    public Item(Item item) {
        this.name = item.name;
        this.price = item.price;
        this.description = item.description;
        this.isPricePerOunce = item.isPricePerOunce;
    }

    /**
     * Builds an item by partially copying another item and a new price.
     *
     * @param item  The Item to partially copy.
     * @param price The price of the new Item.
     */
    public Item(Item item, BigDecimal price) {
        this.name = item.name;
        this.price = price.toString();
        this.description = item.description;
    }

    /**
     * The default constructor.
     */
    public Item() {
    }

    /**
     * Get the number of times this Item appears in the Cart
     *
     * @return number of times this Item appears in the Cart
     */
    public int getNumInCart() {
        return numInCart;
    }

    /**
     * Sets the name of the Item.
     *
     * @param name The name of the Item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this Item.
     *
     * @return The name of the Item.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets this Item's price as a BigDecimal
     *
     * @return This Item's price.
     */
    public BigDecimal getPrice() {
        if (price != null) {
            return format();
        } else {
            return new BigDecimal(PolyApplication.DEFAULT_PRICE);
        }
    }

    /**
     * Gets this Item's price as a String.
     *
     * @return The Item's price as a String.
     */
    public String getPriceString() {
        if (price != null) {
            return "$" + format();
        } else {
            return PolyApplication.DEFAULT_PRICE;
        }
    }

    /**
     * Sets this Item's price.
     *
     * @param price The BigDecimal to set this Item's price to.
     */
    public void setPrice(BigDecimal price) {
        this.price = price.toString();
    }

    /**
     * Gets this Item's description.
     *
     * @return The Item's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets this Item's description.
     *
     * @param description The Item's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets this Item as price per ounce.
     *
     * @param isPricePerOunce Whether or not the Item is price per ounce.
     */
    public void setIsPricePerOunce(boolean isPricePerOunce) {
        this.isPricePerOunce = isPricePerOunce;
    }

    /**
     * Gets whether or not this Item is price per ounce.
     *
     * @return Whether or not this Item is price per ounce.
     */
    public boolean getIsPricePerOunce() {
        return isPricePerOunce;
    }

    /**
     * Formats the price as currency.
     *
     * @return The formatted price.
     */
    private BigDecimal format() {
        String priceString = price;
        priceString = PolyApplication.currency.format(Double.valueOf(priceString));
        return new BigDecimal(priceString.replace("$", ""));
    }
}
