package com.chess.engine.pieces.vectorPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.PieceType;

/**
 * the Queen Class
 * @author Daniel Wakefield
 * @version 1.0
 */
public final class Queen extends VectorPiece {
    /**
     * the contructor for the Queen
     * also initializes VECTOR_MOVES
     * @param position the position of this queen
     * @param alliance the alliance of this queen
     * @param isFirstMove has the queen not yet moved
     */
    public Queen(int position, Alliance alliance, boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.QUEEN);
        this.VECTOR_MOVES = new int[] {-9,-8,-7,-1,1,7,8,9};

    }

    /**
     * move the Queen to the destination of the move, a new queen in that destination is returned
     * @param move the move to execute
     * @return a new Queen in the move's destination
     */
    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getEndPosition(), move.getAlliance(), false);
    }

    /**
     * given the current coordinate and a movement vector make sure that the resultant destination is valid
     * @param coordinate the current coordinate
     * @param vector the vector to check
     * @return is the resultant destination valid, true or false
     */
    @Override
    protected boolean isValidVector(int coordinate, int vector) {
        // if the destination is off the board
        if (!BoardUtils.isValidCoordinate(coordinate + vector)) {
            return false;
        }

        // if we are on either the left or right side of the board
        switch (coordinate % BoardUtils.NUM_COLS) {
            // if on the left side, do not allow any move to the left
            case 0:
                return !(vector == -9 || vector == -1 || vector == 7);
            // if on the right side, do not allow any move to the right
            case 7:
                return !(vector == -7 || vector == 1 || vector == 9);
            // note: moves off the top and bottom are handled by the first if condition
            // otherwise return true
            default:
                return true;
        }
    }

    /**
     * the toString method for a queen
     * @return "q" if queen is black, "Q" if quen is white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "q" : "Q";
    }
}
