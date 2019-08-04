package com.chess.engine;

import com.chess.engine.board.Board;

public class JChess {

    public static void main(String[] args) {

        Board board = Board.createStandardBoard();


        /**
         * Should output:
         *   r  n  b  q  k  b  n  r
         *   p  p  p  p  p  p  p  p
         *   -  -  -  -  -  -  -  -
         *   -  -  -  -  -  -  -  -
         *   -  -  -  -  -  -  -  -
         *   -  -  -  -  -  -  -  -
         *   R  N  B  Q  K  B  N  R
         *   P  P  P  P  P  P  P  P
         */
        System.out.println(board);
    }
}
