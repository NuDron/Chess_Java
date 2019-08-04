package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;

import java.util.*;

/**
 * General Board outlook is:
 *  8 x 8 = 64 tiles
 *
 * 1     | 0| 1| 2| 3| 4| 5| 6| 7|
 * 2     | 8| 9|10|11|12|13|14|15|
 * 3     |16|17|18|19|20|21|22|23|
 * 4     |24|25|26|27|28|29|30|31|
 * 5     |32|33|34|35|36|37|38|39|
 * 6     |40|41|42|43|44|45|46|47|
 * 7     |48|49|50|51|52|53|54|55|
 * 8     |56|57|58|59|60|61|62|63|
 *
 *         1  2  3  4  5  6  7  8
 */
public class Board {

        private final List<Tile> gameBoard;
        private final Collection<Piece> whitePieces;
        private final Collection<Piece> blackPieces;

        private Board(Builder builder) {
            this.gameBoard = createGameBoard(builder);
            this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
            this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);

            final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
            final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final String tileText = this.gameBoard.get(i).toString();
                builder.append(String.format("%3s", tileText));
                if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                    builder.append("\n");
                }
            }
            return builder.toString();
        }


        private Collection<Move> calculateLegalMoves(final Collection<Piece> piecesCollection) {
            final List<Move> legalMoves = new ArrayList<>();
            for(final Piece piece : piecesCollection) {
                legalMoves.addAll(piece.calculateLegalMoves(this));
            }
            return Collections.unmodifiableList(legalMoves);
        }

        private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {

            final List<Piece> activePieces = new ArrayList<>();

            // Go through every tile on the board and check if it is occupied, then add piece of same alliance as passed value.
            for(final Tile tile : gameBoard) {
                if(tile.isTileOccupied()) {
                    final Piece piece = tile.getPiece();
                    if(piece.getPieceAlliance() == alliance) {
                        activePieces.add(piece);
                    }
                }
            }
                return Collections.unmodifiableList(activePieces);
        }

        public Tile getTile(final int tileCoordinate) {
            return gameBoard.get(tileCoordinate);
            }

         private static List<Tile> createGameBoard(final Builder builder) {
            final Tile[] tiles = new Tile[BoardUtils.NUM_TILES]; //Creates a 64 tiles.
             for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
                 tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
             }
             return Collections.unmodifiableList(Arrays.asList(tiles));
         }

         public static Board createStandardBoard() {
            final Builder builder = new Builder();
            // Black Pieces Layout
             builder.setPiece(new Rook(0, Alliance.BLACK ));
             builder.setPiece(new Knight(1, Alliance.BLACK));
             builder.setPiece(new Bishop(2, Alliance.BLACK));
             builder.setPiece(new Queen(3, Alliance.BLACK));
             builder.setPiece(new King(4, Alliance.BLACK));
             builder.setPiece(new Bishop(5, Alliance.BLACK));
             builder.setPiece(new Knight(6,Alliance.BLACK));
             builder.setPiece(new Rook(7, Alliance.BLACK));
             for(int i = 8; i < 16; i++) {
                 builder.setPiece(new Pawn(i, Alliance.BLACK));
             }
             // White Piece Layout
             builder.setPiece(new Rook(48, Alliance.WHITE));
             builder.setPiece(new Knight(49, Alliance.WHITE));
             builder.setPiece(new Bishop(50, Alliance.WHITE));
             builder.setPiece(new Queen(51, Alliance.WHITE));
             builder.setPiece(new King(52, Alliance.WHITE));
             builder.setPiece(new Bishop(53, Alliance.WHITE));
             builder.setPiece(new Knight(54, Alliance.WHITE));
             builder.setPiece(new Rook(55, Alliance.WHITE));
             for(int i = 56; i < 64; i++) {
                 builder.setPiece(new Pawn(i, Alliance.WHITE));
             }
             //Set white pieces to move first
             builder.setMoveMaker(Alliance.WHITE);

             return builder.build();
         }

        //Constructor
        public static class Builder {
             Map<Integer, Piece> boardConfig;
             Alliance nextMoveMaker;

            public Builder() {
                this.boardConfig = new HashMap<>();
            }

            public Builder setPiece(final Piece piece) {
                this.boardConfig.put(piece.getPiecePosition(), piece);
                return this;

            }

            public Builder setMoveMaker(final Alliance alliance) {
                this.nextMoveMaker = nextMoveMaker;
                return this;
            }

            public Board build() {
            return new Board(this);
        }
    }
}
