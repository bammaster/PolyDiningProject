package com.mustangexchange.polymeal;

public class Transaction
{
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
    public String toString()
    {
        return "Place: " + place + "Date: " + date + " Amount: " + amount;
    }
}