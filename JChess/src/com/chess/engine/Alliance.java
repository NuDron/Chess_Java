package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1; // Returns value which correlates to direction on the chessboard.
        }

        @Override
        public boolean isWhite() {
            return true; // Basic check for Piece alliance
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer wPlayer,
                                   BlackPlayer bPlayer) {
            return wPlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true; // Basic check for Piece alliance
        }

        @Override
        public Player choosePlayer(final WhitePlayer wPlayer,
                                   BlackPlayer bPlayer) {
            return bPlayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();

    public abstract Player choosePlayer(WhitePlayer wPlayer, BlackPlayer bPlayer);
}
