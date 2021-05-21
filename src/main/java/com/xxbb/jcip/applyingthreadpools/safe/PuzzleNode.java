package com.xxbb.jcip.applyingthreadpools.safe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PuzzleNode<P, M> {
    final P position;
    final M move;
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P position, M move, PuzzleNode<P, M> prev) {
        this.position = position;
        this.move = move;
        this.prev = prev;
    }

    public List<M> asMoveList() {
        List<M> solution = new ArrayList<>();
        for (PuzzleNode<P, M> node = this; node.move != null; node = node.prev) {
            solution.add(0, node.move);
        }
        return solution;
    }
}
