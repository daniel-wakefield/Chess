package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.vectorPieces.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * The Pawn class
 * @author Daniel Wakefield
 * @version 1.0
 */
public class Pawn extends Piece{
    // possible move vectors from current location
    private int[] moveVectors = {7,8,9,16};

    /**
     * the constructor for the Pawn class
     * @param position the position of the pawn
     * @param alliance the alliance of the pawn
     * @param isFirstMove has the pawn not previously been moved
     */
    public Pawn(int position, Alliance alliance, boolean isFirstMove) {
        super(position, alliance, isFirstMove, PieceType.PAWN);
    }

    /**
     * get all of the possible legal moves for this pawn given a specific board
     * @param board the board to calculate the moves for
     * @return a list of the possible moves
     */
    @Override
    public List<Move> getPossibleLegalMoves(Board board) {
        // the list of moves to return eventually
        List<Move> moves = new ArrayList<>();

        // variables used throughout function
        int destination;
        int direction = this.alliance.getDirection();
        Tile destinationTile;

        // for each of the vectors
        for (int v: moveVectors) {
            // the vectors have different logic
            switch(v) {
                // normal pawn move
                case 8:
                    // if destination is valid
                    destination = this.position + v * direction;
                    if (BoardUtils.isValidCoordinate(destination)) {
                        // if destination isn't occupied
                        destinationTile = board.getTile(destination);
                        if (!destinationTile.isTileOccupied()) {
                            // add a move to that coordinate
                            moves.add(new Move(board, this, destination));
                        }
                    }
                    break;
                // pawn jump case
                case 16:
                    // can only do pawn jump on first move
                    if (!this.isFirstMove) {
                        break;
                    }
                    // if destination is valid
                    destination = this.position + v * direction;
                    int intermediateCoordinate = this.position + 8 * direction;
                    if (BoardUtils.isValidCoordinate(destination)) {
                        // if destination and intermediate tile are not occupied
                        destinationTile = board.getTile(destination);
                        Tile intermediateTile = board.getTile(intermediateCoordinate);
                        if (!destinationTile.isTileOccupied() && !intermediateTile.isTileOccupied()) {
                            // add the pawn jump
                            moves.add(new Move.PawnJump(board, this, destination));
                        }
                    }
                    break;
                // if is an attack, either normal or enpassant
                case 7:case 9:
                    // if is a legal destination
                    if (isLegalMove (this.position, v, direction)) {
                        destination = this.position + v * direction;
                        destinationTile = board.getTile(destination);
                        // for attack destination must be occupied
                        if (destinationTile.isTileOccupied()) {
                            // get attacked piece and add attack move, if attacked piece is opponont alliance
                            Piece attackedPiece = destinationTile.getPiece();
                            if (attackedPiece!=null && attackedPiece.getAlliance() != this.alliance) {
                                moves.add(new Move.AttackMove(board, this, destination, attackedPiece));
                            }
                        }
                        // if there is an enpassant pawn, of opposite alliance in destination tile
                        Pawn enPassantPawn = board.getEnPassantPawn();
                        if (enPassantPawn != null && enPassantPawn.getAlliance() != this.alliance) {
                            if (v == 7 && enPassantPawn.getPosition() == -1*this.alliance.getDirection() + this.position) {
                                moves.add(new Move.AttackMove(board, this, destination, enPassantPawn ));
                            }
                            else if (v == 9 && enPassantPawn.getPosition() == this.alliance.getDirection() + this.position) {
                                moves.add(new Move.AttackMove(board, this, destination, enPassantPawn));
                            }
                        }

                    }
                    break;
            }
        }

        return moves;
    }

    /**
     * test to see if a candidate destination is a legal move destination
     * @param position current position of pawn
     * @param vector vector to test
     * @param direction direction of pawn
     * @return true if move is valid, false otherwise
     */
    private static boolean isLegalMove (final int position, final int vector, final int direction) {
        if (!BoardUtils.isValidCoordinate(position + vector * direction)) {
            return false;
        }

        switch (position % BoardUtils.NUM_COLS) {
            case 0:
                return !((direction == -1 && vector == 9) || (direction == 1 && vector == 7));
            case 7:
                return !((direction == -1 && vector == 7) || (direction == 1 && vector == 9));
            default:
                return true;
        }
    }

    /**
     * the toString method
     * @return String representation of the pawn "p" if black, "P" if white
     */
    @Override
    public String toString() {
        return this.alliance.isBlack() ? "p" : "P";
    }

    /**
     * the move method, returns a new piece in new location
     * if it is a pawn promotion return a queen, otherwise a pawn
     * @param move the move to execute
     * @return the moved piece in the new location
     */
    @Override
    public Piece movePiece(Move move) {
        // if it is a pawn promotion return a queen
        if (move.getEndPosition() / BoardUtils.NUM_ROWS == 0
                || move.getEndPosition() / BoardUtils.NUM_ROWS == 7) {
            return new Queen(move.getEndPosition(), move.getAlliance(), false);
        }
        // otherwise return a new pawn
        return new Pawn(move.getEndPosition(), move.getAlliance(), false);
    }

}
