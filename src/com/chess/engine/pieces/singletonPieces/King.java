package com.chess.engine.pieces.singletonPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.PieceType;

/**
 * the King class
 * @author Daniel Wakefield
 * @version 1.0
 */
public final class King extends SingletonPiece {
    /**
     * the constructor
     * initialize the SINGLETON_MOVES array
     * @param position the position of the piece
     * @param alliance the alliance of the piece
     * @param isFirstMove has the piece not moved yet
     */
    public King(int position, Alliance alliance, boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.KING);
        this.SINGLETON_MOVES = new int[] {-9,-8,-7,-1,1,7,8,9};
    }

    /**
     * is the move valid
     * @param position the current position of the piece
     * @param candidateMove the candidate move vector
     * @return true if the move is valid, false otherwise
     */
    protected boolean isValidCandidateMove(int position, int candidateMove) {
        // if the board goes off the top or bottom of the board
        if (!BoardUtils.isValidCoordinate(position + candidateMove)) {
            return false;
        }

        // get the column of the piece
        switch (position % BoardUtils.NUM_COLS) {
            // if the left most, don't allow a move leftward
            case 0:
                return !(candidateMove == -9 || candidateMove == -1 || candidateMove == 7);
            // if on the right most, don't allow a move rightward
            case 7:
                return !(candidateMove == -7 || candidateMove == 1 || candidateMove == 9);
            // otherwise return true
            default:
                return true;
        }
    }

    /**
     * the toString method
     * @return "k" if black, "K" if white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "k" : "K";
    }

    /**
     * move the piece to the new destination
     * @param move the move to execute
     * @return the new king in the destination
     */
    @Override
    public King movePiece(Move move) {
        return new King(move.getEndPosition(), move.getAlliance(), false);
    }
}
