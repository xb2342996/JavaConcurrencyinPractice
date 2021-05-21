package com.xxbb.jcip.applyingthreadpools.safe;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = Executors.newCachedThreadPool();
        this.seen = new ConcurrentHashMap<>();
        if (this.exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) this.exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            PuzzleNode<P, M> soluNode = solution.getValue();
            return (soluNode == null) ? null : soluNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    private Runnable newTask(P p, M m, PuzzleNode<P, M> node) {
        return new SolverTask(p, m, node);
    }

    class SolverTask extends PuzzleNode<P, M> implements Runnable {

        public SolverTask(P position, M move, PuzzleNode<P, M> prev) {
            super(position, move, prev);
        }

        @Override
        public void run() {
            if (solution.isSet() || seen.putIfAbsent(position, true) != null) {
                return;
            }
            if (puzzle.isGoal(position)) {
                solution.setValue(this);
            } else {
                for (M move : puzzle.legalMoves(position)) {
                    exec.execute(newTask(puzzle.move(position, move), move, this));
                }
            }
        }
    }
}
