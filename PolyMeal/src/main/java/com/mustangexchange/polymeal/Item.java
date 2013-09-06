package com.mustangexchange.polymeal;

import java.math.BigDecimal;

/**
 * Created by Blake on 9/4/13.
 */
public class Item
{
    private String name = null;
    private BigDecimal price = null;
    private String description;
    private boolean isValid;
    private boolean ounces = false;
    public Item(String name, BigDecimal price, String description, boolean isValid)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isValid = isValid;
    }

    public Item(){}

    public void setName(String title)
    {
        this.name = title;
    }

    public String getName()
    {
        return this.name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public String getPriceString()
    {
        return "$"+price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setValid(boolean valid)
    {
        isValid = valid;
    }

    public boolean getValid()
    {
        return isValid;
    }

    public void setOunces(boolean ounces)
    {
        this.ounces = ounces;
    }

    public boolean getOunces()
    {
        return ounces;
    }
}