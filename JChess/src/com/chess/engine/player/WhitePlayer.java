package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    // Method returns the Alliance var
    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOppenent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves, final Collection<Move> opponentsLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        // Check if it is King first move and King is not in check.
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            // King side castle move.
            // Check if Tile 61 && 62 is not occupied - those between King and Rook.
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                // Check if rook is on the tile (rook default spawn place) AND if it is this piece(rook) first move.
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // Make sure that opponent won't be able to attack the pieces after the Castle Move.
                    if(Player.calcAttackOnTile(61, opponentsLegalMoves).isEmpty() &&
                            Player.calcAttackOnTile(62, opponentsLegalMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        kingCastles.add(new Move.KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    62,
                                                                    (Rook)rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    61));
                    }
                }
            }
            // Queen side castle move
            // Check if tiles between King and Rook(on Queen side) are empty.
            if(!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                // Get reference to Queen side Rook piece.
                final Tile rookTile = this.board.getTile(56);
                // Check if Tile is occupied AND it is first move of the Piece(only Rook can satisfy this).
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,
                                                                 this.playerKing,
                                                                58,
                                                                 (Rook)rookTile.getPiece(),
                                                                 rookTile.getTileCoordinate(),
                                                                59));
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
