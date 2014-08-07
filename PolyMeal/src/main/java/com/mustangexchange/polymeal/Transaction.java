package com.mustangexchange.polymeal;

/**
 * Stores data regarding a purchase or deposit using Plus Dollars, Campus Express, or Meals.
 */
public class Transaction
{
    /**The type, meal of dollar, of the purchase made.*/
    private int type;

    /**The location of the purchase.*/
    private String place;

    /**The date of the transaction.*/
    private String date;

    /**The amount of money in the transaction.*/
    private String amount;

    /**
     * Builds a new transaction with all of its necessary parameters.
     * @param type The type, meal or dollar, of the transaction made.
     * @param place The place the transaction was made.
     * @param date The date the transaction was made.
     * @param amount The amount of the transaction.
     */
    public Transaction(int type, String place, String date, String amount) {
        this.type = type;
        this.place = place;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Creates a new transaction for another transaction.
     * @param t The transaction to copy.
     */
    public Transaction(Transaction t) {
        this.type = t.type;
        this.place = t.place;
        this.date = t.date;
        this.amount = t.amount;
    }

    /**
     * Gets the type of transaction.
     * @return The type of the transaction.
     */
    public int getType()
    {
        return type;
    }

    /**
     * Gets the location that the transaction took place.
     * @return The location of the transaction.
     */
    public String getPlace()
    {
        return place;
    }

    /**
     * Gets the date of the transaction.
     * @return The date of the transaction.
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Gets the amount of the transaction. Either 1 for a meal or a money value.
     * @return
     */
    public String getAmount()
    {
        return amount;
    }

    /**
     * Returns the String type agnostic representation of this transaction.
     * @return
     */
    public String toString()
    {
        return "Place: " + place + "Date: " + date + " Amount: " + amount;
    }
}