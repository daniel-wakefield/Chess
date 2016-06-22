package com.chess.engine.move;

import com.chess.engine.board.Board;

/**
 * @author Daniel Wakefield
 * @version 1.0
 * the MoveTransition class
 * this class allows you to see if a move was legal or not before deciding to update the board
 * representation
 */
public class MoveTransition {
    // the new board
    private final Board transBoard;
    // the move status
    private final MoveStatus moveStatus;

    /**
     * the constructor
     * @param transBoard the board that was created by a move execution
     * @param moveStatus the status of that move
     */
    public MoveTransition (final Board transBoard, final MoveStatus moveStatus) {
        this.transBoard = transBoard;
        this.moveStatus = moveStatus;
    }

    /**
     * the getter for the move status
     * @return the move status
     */
    public MoveStatus getStatus() {
        return moveStatus;
    }

    /**
     * the getter for the new board
     * @return the new board
     */
    public Board getTransBoard() {
        return this.transBoard;
    }

}
