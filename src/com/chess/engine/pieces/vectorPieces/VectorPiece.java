package com.chess.engine.pieces.vectorPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * the VectorPiece class, vector pieces are pieces that can move an infinite number of spaces in specified directions
 * i.e. the Rook, Queen, and Bishop
 * @author Daniel Wakefield
 * @version 1.0
 */
 abstract class VectorPiece extends Piece {

    // the possible movement vectors - denoted by a move by one square in that direction, then we can
    // calculate further by adding the vector again to the new tile
    int[] VECTOR_MOVES;

    /**
     * the constructor
     * @param position the current position of the piece
     * @param alliance the alliance of the piece
     * @param isFirstMove has the piece not moved yet
     * @param type the piece type
     */
    VectorPiece(int position, Alliance alliance, boolean isFirstMove, PieceType type) {
        super(position, alliance, isFirstMove, type);
    }

    /**
     * calculate the possible moves for this piece on the input board
     * @param board the board to calculate the moves for
     * @return the possible moves
     */
    @Override
    public List<Move> getPossibleLegalMoves(Board board) {

        // the list of moves to eventually return
        List<Move> moves = new ArrayList<>();

        // for each vector
        for (int v: VECTOR_MOVES) {

            // initalize the candidate destination as the current location
            int candidateDestination = this.position;

            // while we can add the vector and have it be a valid coordinate
            while (isValidVector(candidateDestination, v)) {
                // add the vector
                candidateDestination += v;

                // get the tile
                Tile tile = board.getTile(candidateDestination);

                // if the tile is not occupied
                if (!tile.isTileOccupied()) {
                    // add the move, then repeat, adding the vector again
                    moves.add(new Move(board, this, candidateDestination));
                }
                // otherwise, if the tile is occupied
                else {
                    // get the piece
                    Piece otherPiece = tile.getPiece();
                    // if the piece is of the opponent alliance
                    if (otherPiece != null && otherPiece.getAlliance() != this.alliance) {
                        // add the attack move
                        moves.add(new Move.AttackMove(board, this, candidateDestination, otherPiece));
                    }
                    // then stop looking for moves along this vector
                    break;
                }
            }
        }
        return moves;
    }

    /**
     * to see if a move along this vector is valid - will be different for every piece
     * @param coordinate the current coordinate
     * @param vector the vector to check
     * @return is it a valid move
     */
    protected abstract boolean isValidVector (int coordinate, int vector);
}
