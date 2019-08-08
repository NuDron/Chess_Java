package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.List;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;

    //Constructor
    Piece(final PieceType pieceType, final int piecePos, final Alliance pieceAll) {
        this.piecePosition = piecePos;
        this.pieceAlliance = pieceAll;
        this.pieceType = pieceType;
        this.isFirstMove = false; // TODO
        this.cachedHashCode = calcHashCode();
    }

    // For better performance store the HashCode - so it is not calculated each time.
    private int calcHashCode(){
        int result = pieceType.hashCode();
        result = 31* result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0 );
        return result;
    }

    @Override // Does not want reference equality, but object equality.
    public boolean equals(final Object other) {
        // If objects are equal by reference return true.
        if(this == other) {
            return true;
        }
        // If compared object is not instance of Piece class then return false.
        if(!(other instanceof Piece)) {
            return false;
        }
        // This cast can be performed as [instance of] check was done beforehand.
        final Piece otherPiece = (Piece) other;
        // Return boolean value if position, type, alliance and firstMove values are same.
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove() == otherPiece.isFirstMove();
    }

    // Simply returns stored HashCode created when constructor is executed.
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    /**
     * Method used for getting type of piece. Like when board is searched through.
     */
    public enum PieceType {

        BISHOP("B") {
            @Override
            public boolean isKing(){
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K"){
            @Override
            public boolean isKing(){
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N"){
            @Override
            public boolean isKing(){
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        PAWN("P"){
            @Override
            public boolean isKing(){
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing(){
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing(){
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        };

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        // Making abstract method saves casting.
        public abstract boolean isKing();
        public abstract boolean isRook();
    }

}
