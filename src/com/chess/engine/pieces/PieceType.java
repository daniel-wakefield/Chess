package com.chess.engine.pieces;

/**
 * Created by jamesdavison on 5/26/16.
 */
public enum PieceType {
    PAWN {
        public int getPieceValue() {
            return 100;
        }
    },
    KNIGHT {
        public int getPieceValue() {
            return 300;
        }
    },
    BISHOP {
        public int getPieceValue() {
            return 300;
        }
    },
    ROOK {
        public int getPieceValue() {
            return 500;
        }
    },
    QUEEN {
        public int getPieceValue() {
            return 900;
        }
    },
    KING {
        public int getPieceValue() {
            return 10000;
        }
    };

    public abstract int getPieceValue();
}
