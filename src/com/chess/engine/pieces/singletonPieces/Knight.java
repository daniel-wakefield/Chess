package com.chess.engine.pieces.singletonPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.PieceType;

/**
 * the Knight class
 * @author Daniel Wakefield
 * @version 1.0
 */
public final class Knight extends SingletonPiece {

    /**
     * the constructor
     * initializes the SINGLETON_MOVES array
     * @param position the current position of the piece
     * @param alliance the alliance of the piece
     * @param isFirstMove has the piece not moved yet
     */
    public Knight(final int position, final Alliance alliance, final boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.KNIGHT);
        this.SINGLETON_MOVES = new int[] {-17, -15, -10, -6, 6, 10, 15, 17};
    }

    /**
     * see if the move is valid
     * @param position the current position of the piece
     * @param candidateMove the candidate move vector
     * @return is the move valid
     */
    protected boolean isValidCandidateMove (int position, int candidateMove) {
        // does the move put the piece off the top or bottom of the board
        if (!BoardUtils.isValidCoordinate(position + candidateMove)) {
            return false;
        }

        // get the current column
        switch (position % 8) {
            // if on the right two most columns and the move puts it off the right, return false
            case 0:
                return !(candidateMove == -10 || candidateMove == -17 ||
                            candidateMove == 6 || candidateMove == 15);
            case 1:
                return !(candidateMove == -10 || candidateMove == 6);
            // if on the left two most columns and the move puts it off the left, return false
            case 6:
                return !(candidateMove == -6 || candidateMove == 10);
            case 7:
                return !(candidateMove == -15 || candidateMove == -6 ||
                            candidateMove == 10 || candidateMove == 17);
            default:
                return true;
        }
    }

    /**
     * the toString method
     * @return "n" if black, "N" if white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "n" : "N";
    }

    /**
     * move the knight to the move's end position
     * @param move the move to execute
     * @return the new knight
     */
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getEndPosition(), move.getAlliance(), false);
    }
}
