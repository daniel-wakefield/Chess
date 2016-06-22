package com.chess.tests;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.CastleType;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.singletonPieces.King;
import com.chess.engine.pieces.singletonPieces.Knight;
import com.chess.engine.pieces.vectorPieces.Bishop;
import com.chess.engine.pieces.vectorPieces.Queen;
import com.chess.engine.pieces.vectorPieces.Rook;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * the test class for the board class
 */
public class BoardTests {
    /**
     * tests the castles function in the board class
     */
    @Test
    public void castleTest() {
        Rook br1 = new Rook (0, Alliance.BLACK, true);
        Rook br2 = new Rook (7, Alliance.BLACK, true);
        King bk = new King (4, Alliance.BLACK, true);
        Rook wr1 = new Rook (56, Alliance.WHITE, true);
        Rook wr2 = new Rook (63, Alliance.WHITE, true);
        King wk = new King (60, Alliance.WHITE, true);
        Board.Builder builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(wk);
        builder.setMover(Alliance.WHITE);
        Board board = builder.build();
        Move bKingside = new Move.CastleMove(board, bk, 6, br2, 5, CastleType.KING_SIDE);
        Move bQueenside = new Move.CastleMove(board, bk, 2, br1, 3, CastleType.QUEEN_SIDE);
        Move wKingside = new Move.CastleMove(board, wk, 62, wr2, 61,CastleType.KING_SIDE);
        Move wQueenside = new Move.CastleMove(board, wk, 58, wr1, 59, CastleType.QUEEN_SIDE);

        Collection<Move> movesCheck = wk.getPossibleLegalMoves(board);
        movesCheck.addAll(wr1.getPossibleLegalMoves(board));
        movesCheck.addAll(wr2.getPossibleLegalMoves(board));
        movesCheck.add(wKingside);
        movesCheck.add(wQueenside);

        Collection<Move> moves = board.getMovesByAlliance(Alliance.WHITE);

        assertTrue(moves.containsAll(movesCheck));
        moves.removeAll(movesCheck);
        assertTrue(moves.isEmpty());

        movesCheck = bk.getPossibleLegalMoves(board);
        movesCheck.addAll(br1.getPossibleLegalMoves(board));
        movesCheck.addAll(br2.getPossibleLegalMoves(board));
        movesCheck.add(bKingside);
        movesCheck.add(bQueenside);

        moves = board.getMovesByAlliance(Alliance.BLACK);

        assertTrue(moves.containsAll(movesCheck));
        moves.removeAll(movesCheck);
        assertTrue(moves.isEmpty());

        // test obstructions
        castleObstruction(new int[] {1,2,3}, Alliance.BLACK, bQueenside);
        castleObstruction(new int[] {5, 6}, Alliance.BLACK, bKingside);
        castleObstruction(new int[] {57, 58, 59}, Alliance.WHITE, wQueenside);
        castleObstruction(new int[] {61, 62}, Alliance.WHITE, wKingside);

        // test moves through check
        castlesThroughCheck(new int[] {18, 19, 20}, Alliance.WHITE, bQueenside);
        castlesThroughCheck(new int[] {20, 21, 22}, Alliance.WHITE, bKingside);
        castlesThroughCheck(new int[] {34, 35, 36}, Alliance.BLACK, wQueenside);
        castlesThroughCheck(new int[] {36, 37, 38}, Alliance.BLACK, wKingside);

        // test for not first move
        builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(wk);
        builder.addPiece(new Rook(7, Alliance.BLACK, false));
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.BLACK).contains(bKingside));

        builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(wk);
        builder.addPiece(new King (4, Alliance.BLACK, false));
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.BLACK).contains(bKingside));
        assertFalse(board.getMovesByAlliance(Alliance.BLACK).contains(bQueenside));


        builder = new Board.Builder();
        builder.addPiece(new Rook(0, Alliance.BLACK, false));
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(wk);
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.BLACK).contains(bQueenside));

        builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(new Rook(56, Alliance.WHITE, false));
        builder.addPiece(wr2);
        builder.addPiece(wk);
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.WHITE).contains(wQueenside));


        builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(new Rook(63, Alliance.WHITE, false));
        builder.addPiece(wk);
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.WHITE).contains(wKingside));


        builder = new Board.Builder();
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(new King(60, Alliance.WHITE, false));
        board = builder.build();
        assertFalse(board.getMovesByAlliance(Alliance.WHITE).contains(wKingside));
        assertFalse(board.getMovesByAlliance(Alliance.WHITE).contains(wQueenside));
    }

    /**
     * check to make sure that when a king moves through an attacked tile that it won't work
     * @param coordinates the coordinates to attack
     * @param opponentAlliance the alliance of the opponent
     * @param obstructedCastle the type of the obstructed castle
     */
    private void castlesThroughCheck(int[] coordinates, Alliance opponentAlliance, Move obstructedCastle) {
        for (int coordinate: coordinates) {
            Board.Builder builder = getBasicCastleBuilder();
            builder.addPiece(new Rook(coordinate, opponentAlliance, false));
            Board board = builder.build();
            assertFalse(board.getMovesByAlliance(opponentAlliance.getOpponentAlliance()).contains(obstructedCastle));
        }
    }

    /**
     * test to make sure that when there is a piece in the way of a castle that it won't work
     * @param coordinates the coordinates to put a piece to obstruct the castle
     * @param alliance the alliance of the pieces moving
     * @param obstructedCastle the castle that will be blocked
     */
    private void castleObstruction(int[] coordinates, Alliance alliance, Move obstructedCastle) {
        for (int coordinate: coordinates) {
            Board.Builder builder = getBasicCastleBuilder();
            builder.addPiece(new Bishop(coordinate, alliance, false));
            Board board = builder.build();
            assertFalse(board.getMovesByAlliance(alliance).contains(obstructedCastle));
        }
    }

    /**
     * get a builder only with rooks and kings
     * @return the resulting builder
     */
    private Board.Builder getBasicCastleBuilder() {
        Board.Builder builder = new Board.Builder();
        Rook br1 = new Rook (0, Alliance.BLACK, true);
        Rook br2 = new Rook (7, Alliance.BLACK, true);
        King bk = new King (4, Alliance.BLACK, true);
        Rook wr1 = new Rook (56, Alliance.WHITE, true);
        Rook wr2 = new Rook (63, Alliance.WHITE, true);
        King wk = new King (60, Alliance.WHITE, true);
        builder.addPiece(br1);
        builder.addPiece(br2);
        builder.addPiece(bk);
        builder.addPiece(wr1);
        builder.addPiece(wr2);
        builder.addPiece(wk);
        return builder;
    }

    /**
     * build a board and check to make sure that all those pieces are on the board, and all of their moves are
     * listed by the board
     */
    @Test
    public void getPieceAndMoveTest() {
        Board.Builder builder = new Board.Builder();
        Collection<Piece> whitePiecesCheck = new ArrayList<>();
        Collection<Piece> blackPiecesCheck = new ArrayList<>();
        whitePiecesCheck.add(new Pawn(35, Alliance.WHITE, false));
        whitePiecesCheck.add(new Rook(20, Alliance.WHITE, false));
        whitePiecesCheck.add(new King(13, Alliance.WHITE, false));
        whitePiecesCheck.add(new Knight(2, Alliance.WHITE, false));
        whitePiecesCheck.add(new Bishop(7, Alliance.WHITE, false));
        whitePiecesCheck.add(new Pawn(39, Alliance.WHITE, false));
        whitePiecesCheck.add(new Queen(40, Alliance.WHITE, false));
        whitePiecesCheck.add(new Pawn(22, Alliance.WHITE, false));

        blackPiecesCheck.add(new King(60, Alliance.BLACK, false));
        blackPiecesCheck.add(new Rook(62, Alliance.BLACK, false));
        blackPiecesCheck.add(new Bishop(0, Alliance.BLACK, false));
        blackPiecesCheck.add(new Knight(33, Alliance.BLACK, false));
        blackPiecesCheck.add(new Queen(57, Alliance.BLACK, false));
        blackPiecesCheck.add(new Pawn(31, Alliance.BLACK, false));
        blackPiecesCheck.add(new Bishop(32, Alliance.BLACK, false));
        blackPiecesCheck.add(new Pawn(41, Alliance.BLACK, false));

        for (Piece piece: whitePiecesCheck) {
            builder.addPiece(piece);
        }

        for (Piece piece: blackPiecesCheck) {
            builder.addPiece(piece);
        }

        builder.setMover(Alliance.WHITE);
        Board board = builder.build();

        Collection<Piece> pieces = board.getPiecesByAlliance(Alliance.BLACK);
        assertTrue(pieces.containsAll(blackPiecesCheck));
        pieces.removeAll(blackPiecesCheck);
        assertTrue(pieces.isEmpty());

        pieces = board.getPiecesByAlliance(Alliance.WHITE);
        assertTrue(pieces.containsAll(whitePiecesCheck));
        pieces.removeAll(whitePiecesCheck);
        assertTrue(pieces.isEmpty());

        Collection<Move> whiteMovesCheck = new ArrayList<>();
        for (Piece piece: whitePiecesCheck) {
            whiteMovesCheck.addAll(piece.getPossibleLegalMoves(board));
        }

        Collection<Move> blackMovesCheck = new ArrayList<>();
        for (Piece piece: blackPiecesCheck) {
            blackMovesCheck.addAll(piece.getPossibleLegalMoves(board));
        }

        Collection<Move> moves = board.getMovesByAlliance(Alliance.BLACK);
        assertTrue(moves.containsAll(blackMovesCheck));
        moves.removeAll(blackMovesCheck);
        assertTrue(moves.isEmpty());

        moves = board.getMovesByAlliance(Alliance.WHITE);
        assertTrue(moves.containsAll(whiteMovesCheck));
        moves.removeAll(whiteMovesCheck);
        assertTrue(moves.isEmpty());

    }

}
