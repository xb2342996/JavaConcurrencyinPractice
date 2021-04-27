package com.xxbb.jcip.buildingblocks.safe;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, new Runnable() {
            @Override
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = new Worker(mainBoard.getSubBoard(count, i) );
        }
    }

    public void start() {
        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i]).start();
        }
        mainBoard.waitForConverage();
    }

    private interface Board {
        Board getSubBoard(int count, int i);
        void waitForConverage();
        void commitNewValues();
        boolean hasConverged();
        void setNewValue();
        int getMaxX();
        int getMaxY();
    }

    private class Worker implements Runnable {

        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++) {
                    for (int y = 0; y < board.getMaxY(); y++) {
                        board.setNewValue();
                    }
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        return;
                    } catch (BrokenBarrierException e) {
                        return;
                    }
                }
            }
        }
    }
}
