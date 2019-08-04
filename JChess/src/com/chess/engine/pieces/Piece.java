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

    Piece(final PieceType pieceType, final int piecePos, final Alliance pieceAll) {
        this.piecePosition = piecePos;
        this.pieceAlliance = pieceAll;
        this.pieceType = pieceType;
        this.isFirstMove = false; // TODO
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

    /**
     * Method used for getting type of piece. Like when board is searched through.
     */
    public enum PieceType {

        BISHOP("B") {
            @Override
            public boolean isKing(){
                return false;
            }
        },
        KING("K"){
            @Override
            public boolean isKing(){
                return true;
            }
        },
        KNIGHT("N"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        PAWN("P"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing(){
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing(){
                return false;
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
    }

}
