package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

/**
 * Created by jamesdavison on 6/5/16
 */
public class Tile {

    private final int coordinate;
    private final Piece piece;

    Tile(int coordinate) {
        this.coordinate = coordinate;
        this.piece = null;
    }

    Tile (int coordinate, Piece piece) {
        this.coordinate = coordinate;
        this.piece = piece;
    }

    public boolean isTileOccupied () {
        return this.piece != null;
    }

    public Piece getPiece() {
        return this.piece;
    }

    @Override
    public String toString() {
        if (piece == null) {
            return "-";
        }
        else {
            return this.piece.toString();
        }
    }
}
