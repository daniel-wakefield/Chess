package com.chess.engine.pieces.singletonPieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * The SingletonPiece class
 * a singleton piece is a piece that can only move to one specific place, specifically the Knight and the King
 * @author Daniel Wakefield
 * @version 1.0
 */
abstract class SingletonPiece extends Piece {

    // the possible move vectors
    int[] SINGLETON_MOVES;

    /**
     * the constructor
     * @param position the position of the piece
     * @param alliance the alliance of the piece
     * @param isFirstMove has the piece not moved yet
     * @param type the type of the piece
     */
    SingletonPiece(final int position, final Alliance alliance,boolean isFirstMove, final PieceType type) {
        super(position, alliance, isFirstMove, type);
    }

    /**
     * given a specific board, return a list of all the possible moves for this piece
     * @param board the board to calculate the moves for
     * @return the list of possible moves
     */
    @Override
    public List<Move> getPossibleLegalMoves (final Board board) {

        // uninitialized variable to hold possible destination
        int candidateDestination;

        // the list of moves to return eventually
        final List <Move> legalMoves = new ArrayList<>();

        // for each of the move vectors
        for (int i: SINGLETON_MOVES) {
            // get the candidate destination
            candidateDestination = this.position + i;

            // if the candidate destination is valid
            if (isValidCandidateMove(this.position, i)) {
                // get the destination tile
                Tile tile = board.getTile(candidateDestination);

                // if the tile is not occupied
                if (!tile.isTileOccupied()) {
                    // add non-attack move
                    legalMoves.add(new Move(board, this, candidateDestination));
                }
                // otherwise
                else {
                    // get the piece
                    Piece otherPiece = tile.getPiece();

                    // if the alliance of the piece is the opponent's alliance
                    if (otherPiece != null && this.alliance != otherPiece.getAlliance()) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestination, otherPiece));
                    }
                }
            }
        }

        // return the list
        return legalMoves;
    }

    /**
     * see if the move is valid
     * @param position the current position of the piece
     * @param candidateMove the candidate move vector
     * @return is the move valid
     */
    protected abstract boolean isValidCandidateMove (int position, int candidateMove);


}
