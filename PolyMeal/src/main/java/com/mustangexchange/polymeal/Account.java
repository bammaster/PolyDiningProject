package com.mustangexchange.polymeal;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    public String username, password, name;
    public boolean remember = false;
    public int meals;
    public BigDecimal campusExpress, plusDollars;
    public DateTime updated;
    public ArrayList<Transaction> transactions;
    private final String key = "Account";
    public String plusAsMoney()
    {
        return "$" + plusDollars.toString();
    }
    public String expressAsMoney()
    {
        return "$" + campusExpress.toString();
    }
    public Account loadAccount(SharedPreferences sp)
    {
        Account acc = new Gson().fromJson(sp.getString(key, ""), Constants.gsonType);
        return acc;
    }
    public void saveAccount(SharedPreferences sp)
    {
        sp.edit().putString("Account",new Gson().toJson(this));
    }
    public Account(String username, String password, boolean remember)
    {
        this.username = username;
        this.password = password;
        this.remember = remember;
        transactions = new ArrayList<Transaction>();
    }
    public Account(){}
}