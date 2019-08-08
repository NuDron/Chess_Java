package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/**
 * TODO DOCUMENTATION
 * Every time a Piece is moved(majorMove, AttackMove, PawnMove etc.) a new Board object will be constructed
 * with all the pieces (as they were in last turn) and moved Pieces updated.
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        // If both references point to the same memory location return true.
        if(this == other) {
            return true;
        }
        // If 'other' is not an instance of Move class return false.
        if(!(other instanceof Move)) {
            return false;
        }
        // Cast 'other' variable to Move class - instanceof check must be done beforehand.
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
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

    public boolean isAttack() {
        return false;   // To be overwritten in subclasses.
    }

    public boolean isCastleMove() {
        return false;   // To be overwritten in subclasses.
    }

    public Piece getAttackedPiece() {
        return null;    // To be overwritten in subclasses.
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

    // Subclasses of Move.

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
        public int hashCode() {
            // return attacked piece hashCode + Move hashCode value.
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if(this == other) {
                return true;
            }
            if(!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
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

        /**
         * Execution fo Pawn jump Move.
         * @return
         */
        @Override
        public Board execute() {
            // Build new board.
            final Board.Builder builder = new Board.Builder();
            // Set all 'untouched' Pieces on new board.
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            // Set all of opponent's pieces on the new board.
            for(final Piece piece : this.board.getCurrentPlayer().getOppenent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // Change selected Pawn location on new board.
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            // Add Pawn to new board.
            builder.setPiece(movedPawn);
            // Calculate if 'Pawn Jump' allows en passant(pawn capture) action.
            builder.setEnPassantPawn(movedPawn);
            // Execute en passant move - on opponent's turn.
            builder.setMoveMaker(this.board.getCurrentPlayer().getOppenent().getAlliance());
            // return new board (for render).
            return builder.build();
        }
    }

    // Uberclass for King side castle move
    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCastleRook() {
            return this.castleRook;
        }
        @Override
        public boolean isCastleMove() {
            return true;
        }
        @Override
        public Board execute() {

            final Board.Builder builder= new Board.Builder();
            for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                // If Piece is not "a moved Piece" AND "a castle Rook piece" then set the Player's pieces on new board.
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            // Set all of opponent's pieces on the new board.
            for(final Piece piece : this.board.getCurrentPlayer().getOppenent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // Move King Piece.
            builder.setPiece((this.movedPiece.movePiece(this)));
            // Make new rook Piece
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance())); //  TODO SET NEW ROOK isFirstMove to false.
            // As move has been made set the other player to active.
            builder.setMoveMaker(this.board.getCurrentPlayer().getOppenent().getAlliance());
            return builder.build();
        }

    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        // Visible showcase of tile involved into castle move.
        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        // Visible showcase of tile involved into castle move.
        @Override
        public String toString() {
            return "0-0-0";
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
