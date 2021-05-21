package com.xxbb.jcip.avoidlivenesshazard.unsafe;

import com.xxbb.jcip.avoidlivenesshazard.safe.Account;
import com.xxbb.jcip.avoidlivenesshazard.safe.DollarAmount;
import com.xxbb.jcip.avoidlivenesshazard.safe.InsufficientFundsException;

public class DynamicOrderDeadlock {
    public static void transferMoney(Account from, Account to, DollarAmount amount) throws InsufficientFundsException {
        synchronized (from) {
            synchronized (to) {
                if (from.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException();
                } else {
                    from.debit(amount);
                    to.credit(amount);
                }
            }
        }
    }
}
