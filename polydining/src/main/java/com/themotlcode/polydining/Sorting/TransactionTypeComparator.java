package com.themotlcode.polydining.Sorting;

import com.themotlcode.polydining.models.AccountTransaction;

import java.util.Comparator;

/**
 * Used to compare the types of two transactions to determines their natural ordering.
 */
public class TransactionTypeComparator implements Comparator<AccountTransaction> {
    @Override
    public int compare(AccountTransaction a, AccountTransaction b) {
        return a.getType().compareTo(b.getType());
    }
}
