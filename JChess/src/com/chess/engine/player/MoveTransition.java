package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.awt.geom.PathIterator;

public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus movestatus;

    //Constructor
    public MoveTransition(final Board transitionBoard,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.movestatus = moveStatus;
    }

    //Sharing information for Player class.
    public MoveStatus getMoveStatus() {
        return this.movestatus;
    }
}
