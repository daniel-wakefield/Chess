package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;

import java.util.List;

/**
 * The abstract Piece class from which all other extend
 * @author Daniel Wakefield
 * @version 1.0
 */
public abstract class Piece {
    // the alliance of the piece
    protected Alliance alliance;
    // the piece's position
    protected int position;
    // is it the piece's first move
    protected boolean isFirstMove;
    // the piecetype
    private PieceType pieceType;
    // the cached hashcode
    private int hashCode;

    /**
     * the Constructor for the piece class
     * @param position the piece's position
     * @param alliance the piece's alliance
     * @param isFirstMove has the piece not moved yet
     * @param pieceType the piece type
     */
    public Piece(int position, Alliance alliance, boolean isFirstMove, PieceType pieceType) {
        this.position = position;
        this.alliance = alliance;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
        this.hashCode = computeHashCode();
    }

    /**
     * get the piece's position
     * @return the piece's position
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * get the alliance of the piece
     * @return the piece's alliance
     */
    public Alliance getAlliance() {
        return this.alliance;
    }

    /**
     * givin a board, calculate the piece's possible legal moves
     * @param board the board to calculate the moves for
     * @return a list of all the possible moves
     */
    public abstract List<Move> getPossibleLegalMoves (Board board);

    /**
     * move the piece by returning a new piece in the new position
     * @param move the move to execute
     * @return the moved piece
     */
    public abstract Piece movePiece(Move move);

    /**
     * is the piece unmoved
     * @return if the piece has not moved yet return true, else false
     */
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * get the piece's piece type
     * @return the piece's piece type
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * get the piece's piece value, as defined in the PieceType class
     * @return the piece's PieceType value
     */
    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    /**
     * overide the equals method
     * @param other the object we are comparing
     * @return if other is equal to this return true, else false
     */
    @Override
    public boolean equals (Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }

        Piece otherPiece = (Piece) other;

        return alliance == otherPiece.getAlliance()
                && position == otherPiece.getPosition()
                && isFirstMove == otherPiece.isFirstMove()
                && pieceType == otherPiece.getPieceType();
    }

    /**
     * get the hashcode for the piece
     * @return the piece's hashcode
     */
    @Override
    public int hashCode () {
        return hashCode;
    }

    /**
     * compute the piece's hashcode to be cached
     * @return the piece's hashcode
     */
    private int computeHashCode(){
        int result = pieceType.hashCode();
        result = result*31 + alliance.hashCode();
        result = result*31 + position;
        result = result*31 + (isFirstMove? 1:0);
        return result;
    }

}
