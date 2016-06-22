package com.chess.engine.pieces.vectorPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.PieceType;

/**
 * the Bishop class
 * @author Daniel Wakefield
 * @version 1.0
 */
public final class Bishop extends VectorPiece {

    /**
     * the constructor for the Bishop class
     * initiate the VECTOR_MOVES array
     * @param position the curent position of the alliance
     * @param alliance the alliance of the piece
     * @param isFirstMove has the piece not moved yet
     */
    public Bishop(int position, Alliance alliance, boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.BISHOP);
        this.VECTOR_MOVES = new int[]{-9, -7, 7, 9};
    }

    /**
     * check to see if a move vector is valid from a certain position
     * @param coordinate the current coordinate
     * @param vector the vector to check
     * @return true if move is valid, false otherwise
     */
    @Override
    protected boolean isValidVector (int coordinate, int vector) {
        // if the vector puts the bishop off either the top or the bottom of the board
        if (!BoardUtils.isValidCoordinate(coordinate + vector)) {
            return false;
        }

        // get which column we're on
        switch (coordinate % BoardUtils.NUM_COLS) {
            // if on the leftmost don't allow a move leftward
            case 0:
                return !(vector == -9 || vector == 7);
            // if on the rightmost tile don't allow a move rightward
            case 7:
                return !(vector == -7 || vector == 9);
            // otherwise return true
            default:
                return true;
        }
    }

    /**
     * the toString method
     * @return "b" if bishop is black, "B" if bishop is white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "b" : "B";
    }

    /**
     * create a new bishop in the destination of the move
     * @param move the move to execute
     * @return the new bishop in the destination coordinate
     */
    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getEndPosition(), move.getAlliance(), false);
    }
}
