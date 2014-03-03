package com.mustangexchange.polymeal;

import android.content.Context;

import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    public String username, password, name;
    public boolean remember;
    public int meals;
    public BigDecimal campusExpress, plusDollars;
    public DateTime updated;
    public ArrayList<Transaction> transactions;
    public String plusAsMoney()
    {
        return "$" + plusDollars.toString();
    }
    public String expressAsMoney()
    {
        return "$" + campusExpress.toString();
    }
    public static Account loadAccount(String filename, Context context) {
        Account a = null;
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            a = (Account) is.readObject();
            is.close();
        } catch (Exception e) {
        }
        return a;
    }
    public void saveAccount(String fileName, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, 0);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
        }
    }
    public Account(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
        transactions = new ArrayList<Transaction>();
    }

}