package com.mustangexchange.polymeal;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Stores and handles all information associated with the users
 * my cal poly account used to get dining information.
 */
public class Account
{
    /**The default value for Campus Express and Plus Dollars if no value is found.*/
    private static final String DEFAULT_MONEY = "$0.00";

    /**The username of the user for logging in to My Cal Poly.*/
    private String username;

    /**The password of the user for logging in to My Cal Poly.*/
    private String password;

    /**The name of the user retrieved from My Cal Poly*/
    private String name;

    /**Whether or not to remember the users information.*/
    private boolean remember;

    /**The number of meals for the user.*/
    private int meals;

    /**The Campus Express balance for the user.*/
    private BigDecimal campusExpress;

    /**The Plus Dollars balance for the user*/
    private BigDecimal plusDollars;

    /**A recent list of all the things the user has bought throughout campus dining.*/
    private ArrayList<Transaction> transactions;

    /**
     * Creates a new Account for the user as specified when they login.
     * @param username The entered username.
     * @param password The entered password.
     * @param remember Whether or not to save the account for future use.
     */
    public Account(String username, String password, boolean remember)
    {
        this.username = username;
        this.password = password;
        this.remember = remember;
        transactions = new ArrayList<Transaction>();
    }

    /**
     * The default constructor.
     */
    public Account()
    {

    }

    /**
     * Gets the username for this My Cal Poly Account.
     * @return The username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Gets the password for this My Cal Poly Account.
     * @return
     */
    public String getPassword()
    {
        return username;
    }

    /**
     * Gets the name of the user from their My Cal Poly.
     * @return The name of the user.
     */
    public String getName()
    {
        return username;
    }

    /**
     * Sets the name of the user.
     * @param name The name of the user.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets whether or not the account should be remembered for storage.
     * @return Whether ot not the account should be remembered.
     */
    public boolean isRemembered()
    {
        return remember;
    }

    /**
     * Gets the number of meals for this user.
     * @return The number of meals for this user.
     */
    public int getMeals()
    {
        return meals;
    }

    /**
     * Sets the number of meals for this user.
     * @param meals The number of meals for the user.
     */
    public void setMeals(int meals)
    {
        this.meals = meals;
    }

    /**
     * Sets the Campus Express balance for the user with a Big Decimal.
     * @param campusExpress The Campus Express balance.
     */
    public void setCampusExpress(BigDecimal campusExpress)
    {
        this.campusExpress = new BigDecimal(campusExpress.toString());
    }

    /**
     * Sets the Campus Express balance for the user with a String.
     * @param campusExpress The Campus Express balance.
     */
    public void setCampusExpress(String campusExpress)
    {
        this.campusExpress = new BigDecimal(campusExpress);
    }

    /**
     * Sets the Plus Dollars balance for the user with a Big Decimal.
     * @param plusDollars The Plus Dollars balance.
     */
    public void setPlusDollars(BigDecimal plusDollars)
    {
        this.campusExpress = new BigDecimal(campusExpress.toString());
    }

    /**
     * Sets the Plus Dollars balance for the user with a String.
     * @param plusDollars The Plus Dollars balance.
     */
    public void setPlusDollars(String plusDollars)
    {
        this.campusExpress = new BigDecimal(plusDollars);
    }

    /**
     * Gets a deep copy of this users transaction history.
     * @return The transaction history.
     */
    public ArrayList<Transaction> getTransactions()
    {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        for(Transaction t : this.transactions)
        {
            transactions.add(new Transaction(t));
        }
        return transactions;
    }

    /**
     * Sets this users transaction history.
     * @param transactions The transaction history.
     */
    public void setTransactions(ArrayList<Transaction> transactions)
    {
        for(Transaction t : transactions)
        {
            this.transactions.add(new Transaction(t));
        }
    }

    /**
     * Returns a string value for the Plus Dollars with a dollar sign.
     * @return String value of the accounts plus dollars.
     */
    public String plusAsMoney()
    {
        if(plusDollars != null)
        {
            return "$" + plusDollars.toString();
        }
        else
        {
            return DEFAULT_MONEY;
        }
    }

    /**
     * Returns a string value for Campus Express dollars with a dollar sign.
     * @return String value of the accounts campus express.
     */
    public String expressAsMoney()
    {
        if(campusExpress != null)
        {
            return "$" + campusExpress.toString();
        }
        else
        {
            return DEFAULT_MONEY;
        }
    }
}