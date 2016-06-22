package com.chess.engine.pieces.vectorPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.PieceType;

/**
 * The rook class
 * @author Daniel Wakefield
 * @version 1.0
 */
public final class Rook extends VectorPiece {

    /**
     * the constructor
     * it also initializes the movement vector array for the rook
     * @param position the current position of the rook
     * @param alliance the alliance of the rook
     * @param isFirstMove has the rook not been moved yet - important for castling
     */
    public Rook(int position, Alliance alliance, boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.ROOK);
        // the possible move vectors for the rook
        this.VECTOR_MOVES = new int[] {-8, -1, 1, 8};
    }

    /**
     * a method to check to see if a move along a vector is legal
     * @param coordinate the current coordinate
     * @param vector the vector to check
     * @return is the move legal
     */
    @Override
    protected boolean isValidVector(int coordinate, int vector) {
        // if the vector puts the piece off the board return false
        if (!BoardUtils.isValidCoordinate(coordinate + vector)){
            return false;
        }

        // if the piece is on either of the side edges
        switch (coordinate % BoardUtils.NUM_COLS) {
            // if on the left side, you can't move further left
            case 0:
                return vector != -1;
            // if on the right side, you can't move further right
            case 7:
                return vector != 1;
            // otherwise it is true
            // note: we do not check the top because that case is handled by Board.isValidCoordinate()
            default:
                return true;
        }
    }

    /**
     * execute the move by returning a new rook in the destination location of the move
     * @param move the move to execute
     * @return the new rook
     */
    @Override
    public Rook movePiece(Move move) {
        // if it's a castle move
        if (move instanceof Move.CastleMove) {
            // cast the move to a castle move
            Move.CastleMove castleMove = (Move.CastleMove) move;
            // return a new rook in the rook end position of the castle move
            return new Rook(castleMove.getRookEndPosition(), castleMove.getAlliance(), false);
        }

        // otherwise return a new rook in the move's end position
        return new Rook(move.getEndPosition(), move.getAlliance(), false);
    }

    /**
     * the toString() method for the rook
     * @return "r" if rook is black, "R" if rook is white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "r" : "R";
    }

}
