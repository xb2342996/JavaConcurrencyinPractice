package com.xxbb.jcip.applyingthreadpools.safe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SequentialPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {
        P p = puzzle.initialPosition();
        return search(new PuzzleNode<>(p, null, null));
    }

    private List<M> search(PuzzleNode<P, M> node) {
        if (!seen.contains(node.position)) {
            seen.add(node.position);
            if (puzzle.isGoal(node.position)) {
                return node.asMoveList();
            } else {
                for (M move : puzzle.legalMoves(node.position)) {
                    P pos = puzzle.move(node.position, move);
                    PuzzleNode<P, M> child = new PuzzleNode<P, M>(pos, move, node);
                    List<M> result = search(child);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }
}
