package com.themotlcode.polydining.models;

import com.orm.SugarRecord;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Stores and handles all information associated with the users
 * my cal poly account used to get dining information.
 */
public class Account extends SugarRecord<Account> {
    /**
     * The default value for Campus Express and Plus Dollars if no value is found.
     */
    private static final String DEFAULT_MONEY = "$0.00";

    /**
     * The username of the user for logging in to My Cal Poly.
     */
    private String username;

    /**
     * The password of the user for logging in to My Cal Poly.
     */
    private String password;

    /**
     * The name of the user retrieved from My Cal Poly
     */
    private String name;

    /**
     * Whether or not to remember the users information.
     */
    private boolean remember;

    /**
     * The number of meals for the user.
     */
    private int meals;

    /**
     * The Campus Express balance for the user.
     */
    private String campusExpress;

    /**
     * The Plus Dollars balance for the user
     */
    private String plusDollars;

    /**
     * A recent list of all the things the user has bought throughout campus dining.
     */
    private ArrayList<AccountTransaction> accountTransactions;

    /**
     * Creates a new Account for the user as specified when they login.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @param remember Whether or not to save the account for future use.
     */
    public Account(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
        accountTransactions = new ArrayList<AccountTransaction>();
    }

    /**
     * The default constructor.
     */
    public Account() {

    }

    /**
     * Gets the username for this My Cal Poly Account.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password for this My Cal Poly Account.
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the name of the user from their My Cal Poly.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets whether or not the account should be remembered for storage.
     *
     * @return Whether ot not the account should be remembered.
     */
    public boolean isRemembered() {
        return remember;
    }

    /**
     * Gets whether or not the account should be remembered for storage.
     *
     * @return Whether ot not the account should be remembered.
     */
    public void setRemembered() {
        this.remember = true;
    }

    /**
     * Gets the number of meals for this user.
     *
     * @return The number of meals for this user.
     */
    public int getMeals() {
        return meals;
    }

    /**
     * Sets the number of meals for this user.
     *
     * @param meals The number of meals for the user.
     */
    public void setMeals(int meals) {
        this.meals = meals;
    }

    /**
     * Sets the Campus Express balance for the user with a Big Decimal.
     *
     * @param campusExpress The Campus Express balance.
     */
    public void setCampusExpress(BigDecimal campusExpress) {
        this.campusExpress = campusExpress.toString();
    }

    /**
     * Sets the Campus Express balance for the user with a String.
     *
     * @param campusExpress The Campus Express balance.
     */
    public void setCampusExpress(String campusExpress) {
        this.campusExpress = campusExpress;
    }

    /**
     * Returns a deep copy of the Campus Express value for this account.
     *
     * @return The accounts Campus Express dollars.
     */
    public BigDecimal getCampusExpress() {
        return new BigDecimal(campusExpress.toString());
    }

    /**
     * Sets the Plus Dollars balance for the user with a Big Decimal.
     *
     * @param plusDollars The Plus Dollars balance.
     */
    public void setPlusDollars(BigDecimal plusDollars) {
        this.plusDollars = plusDollars.toString();
    }

    /**
     * Sets the Plus Dollars balance for the user with a String.
     *
     * @param plusDollars The Plus Dollars balance.
     */
    public void setPlusDollars(String plusDollars) {
        this.plusDollars = plusDollars;
    }

    /**
     * Returns a deep copy of the Plus Dollars value for this account.
     *
     * @return The accounts Plus Dollars.
     */
    public BigDecimal getPlusDollars() {
        return new BigDecimal(plusDollars.toString());
    }

    /**
     * Gets a deep copy of this users transaction history.
     *
     * @return The transaction history.
     */
    public ArrayList<AccountTransaction> getAccountTransactions() {
        ArrayList<AccountTransaction> accountTransactions = new ArrayList<AccountTransaction>();
        for (AccountTransaction t : this.accountTransactions) {
            accountTransactions.add(new AccountTransaction(t));
        }
        return accountTransactions;
    }

    /**
     * Sets this users transaction history.
     *
     * @param newAccountTransactions The transaction history.
     */
    public void setAccountTransactions(ArrayList<AccountTransaction> newAccountTransactions) {
        if (accountTransactions == null) {
            accountTransactions = new ArrayList<AccountTransaction>();
        }
        for (AccountTransaction t : newAccountTransactions) {
            accountTransactions.add(new AccountTransaction(t));
        }
    }

    /**
     * Returns a string value for the Plus Dollars with a dollar sign.
     *
     * @return String value of the accounts plus dollars.
     */
    public String plusAsMoney() {
        if (plusDollars != null) {
            return "$" + plusDollars.toString();
        } else {
            return DEFAULT_MONEY;
        }
    }

    /**
     * Returns a string value for Campus Express dollars with a dollar sign.
     *
     * @return String value of the accounts campus express.
     */
    public String expressAsMoney() {
        if (campusExpress != null) {
            return "$" + campusExpress.toString();
        } else {
            return DEFAULT_MONEY;
        }
    }

    /**
     * Clears the transactions to update the list.
     */
    public void clearTransactions() {
        accountTransactions.clear();
    }
}