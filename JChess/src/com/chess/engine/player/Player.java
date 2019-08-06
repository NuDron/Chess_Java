package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.awt.geom.Area;
import java.util.*;

// Abstract class of player
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calcAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty(); //If the list (of possible attacks for player's King) is not empty then player is in check/

    }

    // Getter method for King piece.
    public King getPlayerKing() {
        return this.playerKing;
    }

    // Getter method for Player's legal moves.
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    /**
     * Passing the king's current tile location and enemy moves, then check if any of possible enemy moves overlaps with current King's position.
     * If it does overlap then this attack attacks the King and this.player is in check (isInCheck - true).
     * @param piecePosition
     * @param moves
     * @return
     */
    private static Collection<Move> calcAttackOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        // Checking every potential enemy move for threat against King piece.
        for(final Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! Not valid board was created - NO KING ");
    }

    //Testing if move passed is in player's moves collection.
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    // Enemy pieces threaten the King figure, BUT there is possibility to escape.
    public boolean isInCheck() {
        return this.isInCheck;
    }

    // King is threatened and there is NO safe moves available.
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    // King is not in check, but has NO safe moves.
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    /**
     * Helper method for isInCheckMate method.
     * The method performs the "theoretical" moves and check if they are legal && King is not in check.
     *
     * isInCheck is not defined in constructor as it would start an infinite loop (board creation -> checking moves -> board creation ...)
     * @return
     */
    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled() {
        return false;
    }

    /**
     * The method checks if the move (passed var) is legal. If move is not legal, the same board is returned.
     * If move is legal then new board with move executed is returned.
     *
     * Should not be able to make a move that exposes the King again.
     * @param move
     * @return
     */
    public MoveTransition makeMove(final Move move) {
        // If the move is illegal(not part of player's collection of moves) then move transition does not introduce new board.
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        // Polymorphically execute the move.
        final Board transitionBoard = move.execute();
        // Calculate the attacks for current opponents King piece && calculate all current player legal moves.
        final Collection<Move> kingAttacks = Player.calcAttackOnTile(transitionBoard.getCurrentPlayer().getOppenent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());
        //If there are some attacks on King piece.
        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    // Polymorphic method
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOppenent();
}
