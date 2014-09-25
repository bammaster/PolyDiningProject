package com.themotlcode.polydining.models;

import com.orm.SugarRecord;

/**
 * Stores data regarding a purchase or deposit using Plus Dollars, Campus Express, or Meals.
 */
public class AccountTransaction extends SugarRecord<AccountTransaction> {
    /**
     * The type, meal or dollar, of the purchase made.
     */
    private MealType type;

    /**
     * The location of the purchase.
     */
    private String place;

    /**
     * The date of the transaction.
     */
    private String date;

    /**
     * The amount of money in the transaction.
     */
    private String amount;

    /**
     * Default Constructor required by SugarRecord
     */
    public AccountTransaction() {
    }

    /**
     * Builds a new transaction with all of its necessary parameters.
     *
     * @param type   The type, meal or dollar, of the transaction made.
     * @param place  The place the transaction was made.
     * @param date   The date the transaction was made.
     * @param amount The amount of the transaction.
     */
    public AccountTransaction(int type, String place, String date, String amount) {
        if (type == 0) {
            this.type = MealType.meal;
        } else if (type == 1) {
            this.type = MealType.plus;
        } else if (type == 2) {
            this.type = MealType.express;
        } else {
            this.type = MealType.none;
        }
        this.place = place;
        this.amount = amount;
        this.date = date;
    }

    /**
     * Creates a new transaction for another transaction.
     *
     * @param t The transaction to copy.
     */
    public AccountTransaction(AccountTransaction t) {
        this.type = t.type;
        this.place = t.place;
        this.date = t.date;
        this.amount = t.amount;
    }

    /**
     * Gets the type of transaction.
     *
     * @return The type of the transaction.
     */
    public MealType getType() {
        return type;
    }

    /**
     * Gets the location that the transaction took place.
     *
     * @return The location of the transaction.
     */
    public String getPlace() {
        return place;
    }

    /**
     * Gets the date of the transaction.
     *
     * @return The date of the transaction.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the amount of the transaction. Either 1 for a meal or a money value.
     *
     * @return
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Returns the String type agnostic representation of this transaction.
     *
     * @return
     */
    public String toString() {
        return "Place: " + place + "Date: " + date + " Amount: " + amount;
    }
}
