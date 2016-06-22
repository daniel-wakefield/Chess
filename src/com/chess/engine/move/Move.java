package com.chess.engine.move;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;
import com.chess.engine.pieces.vectorPieces.Rook;

/**
 * @author Daniel Wakefield
 * @version 1.0
 */
public class Move {
    protected int startPosition;
    protected int endPosition;
    protected Piece movedPiece;
    protected Board board;
    protected Alliance movedPieceAlliance;

    /**
     * the constructor for the Move class
     * @param board the board the move would happen on
     * @param piece the piece that is being moved
     * @param endPosition the end position for the piece
     */
    public Move(Board board, Piece piece, int endPosition) {
        this.board = board;
        this.startPosition = piece.getPosition();
        this.endPosition = endPosition;
        this.movedPiece = piece;
        this.movedPieceAlliance = piece.getAlliance();
    }

    /**
     * get the start position
     * @return the start position of the move
     */
    public int getStartPosition() {
        return this.startPosition;
    }

    /**
     * get the end position of the move
     * @return the end position of the move
     */
    public int getEndPosition() {
        return this.endPosition;
    }

    /**
     * get the alliance of the move
     * @return the alliance of the move
     */
    public Alliance getAlliance() {
        return this.movedPieceAlliance;
    }

    /**
     * get the piece that is being moved
     * @return the piece that is being moved
     */
    public Piece getPiece () {
        return this.movedPiece;
    }

    /**
     * execute the move, return the new board
     * @return the board that results from executing the move
     */
    public Board execute() {
        Board.Builder builder = new Board.Builder();
        // put all the pieces that are not the moved piece onto the board
        for (Piece piece: board.getAllPieces()) {
            if (!piece.equals(movedPiece)) {
                builder.addPiece(piece);
            }
        }
        // add the moved piece in the new position
        builder.addPiece(movedPiece.movePiece(this));
        // switch the mover to the other alliance
        builder.setMover(board.getCurrentPlayerAlliance().getOpponentAlliance());
        // return the resulting board
        return builder.build();
    }

    /**
     * is the move an attack move
     * @return false, a Move is not an AttackMove
     */
    public boolean isAttack() {
        return false;
    }

    /**
     * the equals method
     * @param other the object to check the equals of
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }

        Move otherMove = (Move) other;

        return this.movedPiece.equals(otherMove.getPiece())
                && this.endPosition == otherMove.getEndPosition();

    }

    /**
     * a hash method
     * @return the hashcode for the move
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = result*31 + this.movedPiece.hashCode();
        result = result*31 + this.endPosition;
        return result;
    }

    /**
     * the toString method
     * @return the String representation of the move
     */
    @Override
    public String toString() {
        return "Move: " + movedPiece.toString() + endPosition;
    }

    /**
     * the AttackMove class
     */
    public static class AttackMove extends Move {
        // the attacked piece
        private Piece attackedPiece;

        /**
         * the constructor for the AttackMove class
         * @param board the board the move happens on
         * @param piece the piece that is attacking
         * @param endPosition the end position of the move
         * @param attackedPiece the piece that is getting attacked
         */
        public AttackMove (Board board, Piece piece, int endPosition, Piece attackedPiece) {
            super(board, piece, endPosition);
            this.attackedPiece = attackedPiece;
        }

        /**
         * return the board that would result from executing the move
         * @return the board that results from executing the move
         */
        public Board execute () {
            Board.Builder builder = new Board.Builder();
            // put all the pieces onto the board except the moved and attacked piece
            for (Piece piece: board.getAllPieces()) {
                if (!piece.equals(movedPiece) && !piece.equals(attackedPiece)) {
                    builder.addPiece(piece);
                }
            }
            // put the moved piece on the board in the new position
            builder.addPiece(movedPiece.movePiece(this));
            // set the mover to the other alliance
            builder.setMover(board.getCurrentPlayerAlliance().getOpponentAlliance());
            // return the resulting board
            return builder.build();
        }

        /**
         * checks to see if the move is an attack
         * @return true
         */
        @Override
        public boolean isAttack() {
            return true;
        }

        /**
         * get the attacked piece
         * @return the attacked piece
         */
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        /**
         * return the string representation of the attack
         * @return the string representation of the attack
         */
        @Override
        public String toString() {
            return "AttackMove: " + movedPiece.toString() + " catures "
                    + attackedPiece.toString() + " at " + endPosition;
        }

        /**
         * see if two attacks are equal
         * @param other the object to check the equals of
         * @return true if the attacks are equal, false otherwise
         */
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }

            AttackMove otherMove = (AttackMove) other;

            return otherMove.getAttackedPiece().equals(attackedPiece) && super.equals(other);
        }

        /**
         * the hashcode method
         * @return the hashcode for the attack move
         */
        @Override
        public int hashCode() {
            return super.hashCode() * 31 + attackedPiece.hashCode();
        }
    }

    /**
     * the CatleMove class, the class for castles
     */
    public static class CastleMove extends Move {

        // the rook we're moving
        private Piece rook;
        // where the rook moves to
        private int rookEndPosition;
        // the castle type
        private CastleType castleType;

        /**
         * the constructor
         * @param board the board the castle happens on
         * @param piece the piece that is moving (always a king)
         * @param endPosition the location the king moves to
         * @param rook the rook that is castling
         * @param rookEndPosition the location the castle moves to
         * @param castleType the type of the castle
         */
        public CastleMove(Board board,
                          Piece piece,
                          int endPosition,
                          Piece rook,
                          int rookEndPosition,
                          CastleType castleType) {
            super(board, piece, endPosition);
            this.castleType = castleType;
            // if the piece is not a king and rook is not a rook, throw an exception
            if (piece.getPieceType() != PieceType.KING) {
                throw new IllegalArgumentException("castleMove must contain a King");
            }
            if (rook.getPieceType() != PieceType.ROOK) {
                throw new IllegalArgumentException("castle move must contain a rook");
            }
            this.rook = rook;
            this.rookEndPosition = rookEndPosition;
        }

        public int getRookEndPosition() {
            return this.rookEndPosition;
        }

        /**
         * get the rook that is part of the castle
         * @return the rook
         */
        Piece getRook() {
            return this.rook;
        }

        /**
         * execute the castle
         * @return the board that would result from the castle being executed
         */
        public Board execute() {
            Board.Builder builder = new Board.Builder();
            // place all the pieces except the king and the rook
            for (Piece piece: board.getAllPieces()) {
                if (!piece.equals(movedPiece) && !piece.equals(rook)) {
                    builder.addPiece(piece);
                }
            }

            // add the rook and king in their new positions
            builder.addPiece(movedPiece.movePiece(this));
            builder.addPiece(rook.movePiece(this));
            // set mover to other player
            builder.setMover(board.getCurrentPlayerAlliance().getOpponentAlliance());
            // return the new board
            return builder.build();
        }

        /**
         * the toString method
         * @return the string representation of the move
         */
        @Override
        public String toString() {
            return this.castleType == CastleType.QUEEN_SIDE? "Queenside Castle" : "Kingside Castle";
        }

        /**
         * the equals method
         * @param other the object to check the equals of
         * @return return true if other equals this, false otherwise
         */
        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }

            CastleMove otherCastle = (CastleMove) other;

            return super.equals(other)
                    && otherCastle.getRook().equals(this.rook)
                    && otherCastle.getRookEndPosition() == this.rookEndPosition;
        }

        /**
         * the hashCode method for the castleMove class
         * @return the hashCode for this object
         */
        @Override
        public int hashCode() {
            int value = super.hashCode();
            value = value*31 + this.rook.hashCode();
            value = value*31 + this.rookEndPosition;
            return value;
        }
    }

    /**
     * the PawnJump class
     */
    public static class PawnJump extends Move {

        /**
         * the constructor
         * @param board the board the PawnJump happens on
         * @param piece the piece that is jumping (always a pawn)
         * @param endPosition the ending position of the pawn
         */
        public PawnJump(Board board, Piece piece, int endPosition) {
            super(board, piece, endPosition);
        }

        /**
         * exactly the same as the Move.execute() method, except this time we set the enPassantPawn to be the piece
         * that me are moving
         * @return the board that is created after the pawn jump is executed
         */
        @Override
        public Board execute() {
            Board.Builder builder = new Board.Builder();
            for (Piece piece: board.getAllPieces()) {
                if (!piece.equals(movedPiece)) {
                    builder.addPiece(piece);
                }
            }
            Pawn movedPawn = (Pawn) movedPiece.movePiece(this);
            builder.addPiece(movedPawn);
            builder.setMover(board.getCurrentPlayerAlliance().getOpponentAlliance());
            builder.setEnPassantPawn(movedPawn);
            return builder.build();
        }
    }

}
