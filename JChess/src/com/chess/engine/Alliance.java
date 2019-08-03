package com.chess.engine;

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
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
}
