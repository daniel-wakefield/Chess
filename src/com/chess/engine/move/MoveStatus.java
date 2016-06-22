package com.chess.engine.move;

/**
 * the MoveStatus enum type
 * @author Daniel Wakefield
 * @version 1.0
 */
public enum MoveStatus {
    /**
     * DONE is used whenever a move is made and it was legal, and doesn't leave the player in check
     */
    DONE {
        /**
         * the toString method
         * @return "DONE"
         */
        @Override
        public String toString() {
            return "DONE";
        }
    },
    /**
     * ILLEGAL is used whenever a move was attempted to be made, but it was not a legal move, i.e. trying to move
     * a rook diagonally
     */
    ILLEGAL {
        /**
         * the toString method
         * @return "ILLEGAL"
         */
        @Override
        public String toString() {
            return "ILLEGAL";
        }
    },
    /**
     * LEAVES_PLAYER_IN_CHECK is used when a move would otherwise be legal, but execution of that move would leave
     * the player in check
     */
    LEAVES_PLAYER_IN_CHECK {
        /**
         * the toString method
         * @return "LEAVES_PLAYER_IN_CHECK"
         */
        @Override
        public String toString() {
            return "LEAVES_PLAYER_IN_CHECK";
        }
    }
}
