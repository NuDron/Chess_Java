package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO DOCUMENTATION
 */
public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16}; // 16 is for first 'jump' move. 9 & 7 is for attack.

    public Pawn(final int piecePos, final Alliance pieceAll) {
        super(PieceType.PAWN, piecePos, pieceAll);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            //If Pawn moves 1 tile forward and tile is not occupied.
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate)); //TODO implement promotion mechanic
            } else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                final int behindCandidateDestionationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                // If the tile behind the candidate and the destination candidate is not occupied.
                if(!board.getTile(behindCandidateDestionationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                // FOR ATTACK - When white piece is on the 8th column or Black piece is on the 1st column.
                }else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite())
                            || (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
                        if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                            final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                            if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                                //TODO
                                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                            }
                        }
                } else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite())
                    || (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack()))) {
                }
            }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
