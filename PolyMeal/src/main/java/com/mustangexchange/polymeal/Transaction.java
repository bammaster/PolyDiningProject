package com.mustangexchange.polymeal;

import java.io.Serializable;

public class Transaction implements Serializable{

    private static final long serialVersionUID = 1L;
    public static final int PLUS_DOLLARS = 0;
    public static final int CAMPUS_EXPRESS = 1;
    public static final int MEAL = 2;

    public int type;
    public String place, date, amount;

    public Transaction(int type, String place, String date, String amount) {
        this.type = type;
        this.place = place;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(int type, String place, String date) {
        this.type = type;
        this.place = place;
        this.date = date;
    }
}