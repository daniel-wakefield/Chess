package com.chess.engine;

/**
 * the Alliance class
 * @author Daniel Wakefield
 * @version 1.0
 */
public enum Alliance {
    WHITE,
    BLACK;

    /**
     * get the direction vector of the alliance
     * @return -1 if white, 1 if black
     */
    public int getDirection() {
        return this == Alliance.WHITE? -1 : 1;
    }

    /**
     * is the alliance black
     * @return true if black, false otherwise
     */
    public boolean isBlack() {
        return this == Alliance.BLACK;
    }

    /**
     * is the alliance white
     * @return true if white, false otherwise
     */
    public boolean isWhite() {
        return this == Alliance.WHITE;
    }

    /**
     * get the opposite alliance
     * @return BLACK if white, white if black
     */
    public Alliance getOpponentAlliance() {
        return this == Alliance.BLACK ? Alliance.WHITE : Alliance.BLACK;
    }
}
