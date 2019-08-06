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
    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    // To supply destinationCoordinate in Player class.
    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    /**
     * The main method for building and returning new board with all of the pieces & moved piece(updated position).
     * Only for making move without attack.
     * @return
     */
    public Board execute() {
        // Create new board.
        final Board.Builder builder = new Board.Builder();
        // Traverse through & set on new board all non-moved pieces.
        for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if(!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        // Do same again for other player pieces.
        for(final Piece piece : this.board.getCurrentPlayer().getOppenent().getActivePieces()) {
            builder.setPiece(piece);
        }
        // Move the 'moved' piece.
        builder.setPiece(this.movedPiece.movePiece(this));
        // Get the other player to be active now (make a move).
        builder.setMoveMaker(this.board.getCurrentPlayer().getOppenent().getAlliance());
        // Return new board with all the changes.
        return builder.build();
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
    public static class AttackMove extends Move {

        final Piece attackedPiece; // Need to keep track of the Piece that is attacked.

        public AttackMove(final Board board,
                   final Piece movedPiece,
                   final int destinationCoordinate,
                   final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttMove extends PawnAttackMove {
        public PawnEnPassantAttMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    //TODO : REFACTOR
    public static final class PawnJump extends Move {
        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    // Uberclass for King side castle move
    static abstract class CastleMove extends Move {
        public CastleMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QuennSideCastleMove extends CastleMove {

        public QuennSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {

        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move!");
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw  new RuntimeException("Not instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for(final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
