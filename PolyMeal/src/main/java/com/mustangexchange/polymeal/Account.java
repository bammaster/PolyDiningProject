package com.mustangexchange.polymeal;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Account
{
    protected String username, password, name;
    protected boolean remember = false;
    protected int meals;
    protected BigDecimal campusExpress, plusDollars;
    protected ArrayList<Transaction> transactions;
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
        Account acc = new Gson().fromJson(sp.getString(key, ""), new TypeToken<Account>() {}.getType());
        return acc;
    }
    public void saveAccount(SharedPreferences sp)
    {
        sp.edit().putString(key,new Gson().toJson(this)).commit();
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