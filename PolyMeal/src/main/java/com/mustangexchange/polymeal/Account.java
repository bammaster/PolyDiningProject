package com.mustangexchange.polymeal;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Stores and handles all information associated with the users
 * my cal poly account used to get dining information.
 */
public class Account
{
    /**The username, password and name of the user.*/
    protected String username, password, name;
    /**Whether or not to remember the users information.*/
    protected boolean remember = false;
    /**The number of meals for the user.*/
    protected int meals;
    /**The Campus Express and Plus Dollars user values.*/
    protected BigDecimal campusExpress, plusDollars;
    /**A recent list of all the things the user has bought through campus dining.*/
    protected ArrayList<Transaction> transactions;


    /**
     * Returns a string value for the Plus Dollars with a dollar sign.
     * @return String value of the accounts plus dollars.
     */
    public String plusAsMoney()
    {
        if(plusDollars != null) {
            return "$" + plusDollars.toString();
        }
        else{
            return "$0.00";
        }
    }
    /**
     * Returns a string value for Campus Express dollars with a dollar sign.
     * @return String value of the accounts campus express.
     */
    public String expressAsMoney()
    {
        if(campusExpress != null) {
            return "$" + campusExpress.toString();
        }
        else{
            return "$0.00";
        }
    }
    /**
     * Loads the account from Shared Preferences.
     * @param sp Access to shared preferences from the calling activity.
     * @return The account that was loaded in.
     */
    public Account loadAccount(SharedPreferences sp)
    {
        String account = sp.getString(Constants.accSpKey, "");
        if(!account.equals("")) {
            return new Gson().fromJson(account, new TypeToken<Account>() {
            }.getType());
        }
        else
        {
            return null;
        }
    }
    /**
     * Saves the account to Shared Preferences by serializing the account with GSON.
     * @param sp Access to shared preferences from the calling activity.
     */
    public void saveAccount(SharedPreferences sp)
    {
        sp.edit().putString(Constants.accSpKey, new Gson().toJson(this)).commit();
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