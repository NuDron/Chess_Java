package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

/**
 * TODO DOCUMENTATION
 */
public abstract class Move {

    //Constructor
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    // Subclasses

    /**
     * Move without attacking.
     */
    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                  final Piece movedPiece,
                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    /**
     * Move with attack, need to keep track of the Piece that is attacked.
     */
    public static final class AttackMove extends Move {

        final Piece attackedPiece; // Need to keep track of the Piece that is attacked.

        public AttackMove(final Board board,
                   final Piece movedPiece,
                   final int destinationCoordinate,
                   final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }
}
