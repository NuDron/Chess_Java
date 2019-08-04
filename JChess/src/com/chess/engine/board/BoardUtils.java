package com.chess.engine.board;

/**
 * Chessboard have a 64 'tiles' which are numbers in an arrray.
 */
public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1); // Offset of 1 as counting starts from 0.
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    /**
     * Helper method to find a particular column. Since the chessboard is just a field of 64 tiles,
     * we need this helper method to 'find' the columns - for edge cases in moves.
     * @param columnNumber
     * @return
     */
    private static boolean[] initColumn(int columnNumber) {
        //Declare an array of booleans - 64 values(NUM_TILES).
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnNumber] = true;   // will label the first tile as true
            columnNumber += NUM_TILES_PER_ROW;             // Will add 8 to tile nr, so 'first tile' of column will be labelled correctly.
        } while(columnNumber < NUM_TILES);        // Condition for 'do' so it won't exceed max val of tiles.

        return column;
    }

    private static boolean[] initRow(int rowNo) {
        final boolean[] row = new boolean[NUM_TILES];
        // The while loop breaks one first tile from another row then specified one (starting tile is passed as argument).
        do{
           row[rowNo] = true;
           rowNo++;
        }while(rowNo % NUM_TILES_PER_ROW !=0);
        return row;
    }

    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES; //Check if coordinate is not outside of the chessboard.
    }
}
