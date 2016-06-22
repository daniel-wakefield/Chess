package com.chess.tests;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.move.MoveStatus;
import com.chess.engine.move.MoveTransition;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * test various well known checkmates
 * @author Daniel Wakefield
 * @version 1.0
 */
public class CheckmateTests {

    /**
     * test foolsmate
     */
    @Test
    public void foolsmate() {
        Board board = Board.createStandardBoard();
        Piece pieceToMove = board.getTile(54).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        Move move = new Move.PawnJump(board, pieceToMove, 38);
        MoveTransition trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(12).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 28);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(53).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move(board, pieceToMove, 45);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(3).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 39);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertTrue(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertTrue(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.WHITE));
    }

    /**
     * test scholarsmate
     */
    @Test
    public void scholarsmate() {
        Board board = Board.createStandardBoard();
        Piece pieceToMove = board.getTile(52).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        Move move = new Move.PawnJump(board, pieceToMove, 36);
        MoveTransition trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(12).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 28);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(61).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.BISHOP);
        move = new Move(board, pieceToMove, 34);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(5).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.BISHOP);
        move = new Move(board, pieceToMove, 26);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(59).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 31);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(6).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 21);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(31).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 13);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertTrue(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertTrue(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertFalse(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.BLACK));
    }

    /**
     * test smotheredmate
     */
    @Test
    public void smotheredMate() {
        Board board = Board.createStandardBoard();
        Piece pieceToMove = board.getTile(52).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        Move move = new Move.PawnJump(board, pieceToMove, 36);
        MoveTransition trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(12).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 28);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(62).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 52);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(1).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 18);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(57).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 42);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(18).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 35);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(54).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move(board, pieceToMove, 46);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(35).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 45);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertTrue(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertTrue(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.WHITE));
    }

    /**
     * test hippopotamusmate
     */
    @Test
    public void hippopotamusMate() {
        Board board = Board.createStandardBoard();
        Piece pieceToMove = board.getTile(52).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        Move move = new Move.PawnJump(board, pieceToMove, 36);
        MoveTransition trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(12).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 28);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(62).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 52);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(3).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 39);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(57).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 42);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(1).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 18);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(54).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move(board, pieceToMove, 46);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(39).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 30);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(51).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 35);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(18).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        Piece attackedPiece = board.getTile(35).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 35, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(58).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.BISHOP);
        attackedPiece = board.getTile(30).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 30, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(35).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 45);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertTrue(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertTrue(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.WHITE));
    }

    /**
     * test blackburne shilling mate
     */
    @Test
    public void blackburneShillingMate() {
        Board board = Board.createStandardBoard();
        Piece pieceToMove = board.getTile(52).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        Move move = new Move.PawnJump(board, pieceToMove, 36);
        MoveTransition trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(12).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.PAWN);
        move = new Move.PawnJump(board, pieceToMove, 28);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(62).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 45);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(1).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 18);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(61).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.BISHOP);
        move = new Move(board, pieceToMove, 34);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(18).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 35);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(45).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        Piece attackedPiece = board.getTile(28).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 28, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(3).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        move = new Move(board, pieceToMove, 30);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(28).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        attackedPiece = board.getTile(13).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 13, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(30).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        attackedPiece = board.getTile(54).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 54, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(63).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.ROOK);
        move = new Move(board, pieceToMove, 61);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(54).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.QUEEN);
        attackedPiece = board.getTile(36).getPiece();
        move = new Move.AttackMove(board, pieceToMove, 36, attackedPiece);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertTrue(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.WHITE));
        pieceToMove = board.getTile(34).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.BISHOP);
        move = new Move(board, pieceToMove, 52);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        checkCheckAndMate(board);
        pieceToMove = board.getTile(35).getPiece();
        assertTrue(pieceToMove.getPieceType() == PieceType.KNIGHT);
        move = new Move(board, pieceToMove, 45);
        trans = board.makeMove(move);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        assertTrue(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertTrue(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(Alliance.WHITE));
    }

    /**
     * a test to make sure neither of the kings are in check, checkmate, or stalemate
     * @param board
     */
    private void checkCheckAndMate(Board board) {
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInCheckmate(Alliance.WHITE));
        assertFalse(board.isPlayerInCheck(Alliance.WHITE));
        assertFalse(board.isPlayerInCheck(Alliance.BLACK));
        assertFalse(board.calculateIsPlayerInStalemate(board.getCurrentPlayerAlliance()));
    }
}
