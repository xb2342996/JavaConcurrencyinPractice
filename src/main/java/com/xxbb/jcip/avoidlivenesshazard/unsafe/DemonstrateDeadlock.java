package com.xxbb.jcip.avoidlivenesshazard.unsafe;

import com.xxbb.jcip.avoidlivenesshazard.safe.Account;
import com.xxbb.jcip.avoidlivenesshazard.safe.DollarAmount;
import com.xxbb.jcip.avoidlivenesshazard.safe.InsufficientFundsException;

import java.util.Random;

public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 100000;

    public static void main(String[] args) {
        final Random rd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account();
        }

        class TransferThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int from = rd.nextInt(NUM_ACCOUNTS);
                    int to = rd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(rd.nextInt(1000));
                    try {
                        DynamicOrderDeadlock.transferMoney(accounts[from], accounts[to], amount);
                    } catch (InsufficientFundsException ignored) {
                    }
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }
}
