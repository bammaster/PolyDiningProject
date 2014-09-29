package com.mustangexchange.polymeal.Sorting;

import com.mustangexchange.polymeal.models.AccountTransaction;

import java.util.Comparator;

/**
 * Used to compare the dates of two transactions to determines their natual ordering.
 */
public class TransactionDateComparator implements Comparator<AccountTransaction> {
    @Override
    public int compare(AccountTransaction a, AccountTransaction b) {
        //Poor implementation. Needs to compare Date Times. Involves parsing String.
        return a.getDate().compareTo(b.getDate());
    }
}
