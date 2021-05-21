package com.xxbb.jcip.avoidlivenesshazard.safe;

public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public void transferMoney(Account from, Account to, DollarAmount amount) throws InsufficientFundsException {

        class Helper {
            public void transfer() throws InsufficientFundsException {
                if (from.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    from.debit(amount);
                    to.credit(amount);
                }
            }
        }

        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);

        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (to) {
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
