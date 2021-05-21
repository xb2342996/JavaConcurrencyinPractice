package com.xxbb.jcip.applyingthreadpools.safe;

import java.util.concurrent.atomic.AtomicInteger;

public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M> {

    private final AtomicInteger taskCount = new AtomicInteger(0);

    public PuzzleSolver(Puzzle<P, M> puzzle) {
        super(puzzle);
    }

    protected Runnable newTask(P p, M m, PuzzleNode<P, M> node) {
        return new CountingSolverTask(p, m, node);    }

    class CountingSolverTask extends SolverTask {

        public CountingSolverTask(P position, M move, PuzzleNode<P, M> prev) {
            super(position, move, prev);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0) {
                    solution.setValue(null);
                }
            }
        }
    }
}
