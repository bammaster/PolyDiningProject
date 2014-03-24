package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class Item implements Parcelable
{
    private String name = null;
    private BigDecimal price = null;
    private String description;
    private boolean isValid = true;
    private boolean ounces = false;
    private DecimalFormat currency = new DecimalFormat("#.##");

    public Item(String name, BigDecimal price, String description, boolean isValid)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isValid = isValid;
    }

    public Item(Parcel in) {
        String[] strData = new String[3];
        boolean[] boolData = new boolean[2];

        in.readStringArray(strData);
        this.name = strData[0];
        this.price = new BigDecimal(strData[1]);
        this.description = strData[2];
        in.readBooleanArray(boolData);
        this.isValid = boolData[0];
        this.ounces = boolData[1];
    }

    public Item(Item item)
    {
        name = item.getName();
        price = item.getPrice();
        description = item.getDescription();
        isValid = item.getValid();
        ounces = item.getOunces();
    }

    public Item(Item item, BigDecimal price) {
        this.name = item.getName();
        this.price = price;
        this.description = item.getDescription();
        this.isValid = item.getValid();
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
        if(price != null)
        {
            return format(price);
        }
        else
        {
            return new BigDecimal("0.00");
        }
    }

    public String getPriceString()
    {
        if(price!=null)
        {
            return "$"+ format(price);
        }
        else
        {
            return "";
        }
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

    private BigDecimal format(BigDecimal price)
    {
        String priceString = price.toString();
        priceString = currency.format(Double.valueOf(priceString));
        return new BigDecimal(priceString);
    }

    public void displayAddDialog(Activity activity)
    {
        final Activity mActivity = activity;
        final AlertDialog.Builder onListClick= new AlertDialog.Builder(activity);
        onListClick.setCancelable(false);
        onListClick.setTitle("Add to Cart?");
        onListClick.setMessage("Would you like to add " + name + " to your cart? Price: " + getPriceString());
        onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
            //Cart.add(Item.this);
            //BaseActivity.updateBalance();
            }
        });
        onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
            }
        });
        onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                AlertDialog.Builder onDialogClick = new AlertDialog.Builder(mActivity);
                onDialogClick.setTitle("Description");
                onDialogClick.setMessage(description);
                onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {

                    }
                });
                onDialogClick.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        onListClick.show();
                    }
                });
                onDialogClick.show();
            }
        });
        onListClick.show();
    }


    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.name,
                this.price.toString(),
                this.description});
        dest.writeBooleanArray(new boolean[] {this.isValid,
                this.ounces});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}