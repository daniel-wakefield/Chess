package com.chess.tests;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;
import com.chess.engine.move.MoveStatus;
import com.chess.engine.move.MoveTransition;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.singletonPieces.King;
import com.chess.engine.pieces.singletonPieces.Knight;
import com.chess.engine.pieces.vectorPieces.Bishop;
import com.chess.engine.pieces.vectorPieces.Queen;
import com.chess.engine.pieces.vectorPieces.Rook;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * the Piece test class
 * test the movement of pieces
 * @author Daniel Wakefield
 * @version 1.0
 */
public class PieceTests {

    private King whiteKing = new King(60, Alliance.WHITE, true);
    private King blackKing = new King(5, Alliance.BLACK, true);

    /**
     * test the movement of the pawn object
     */
    @Test
    public void pawnMovementTest() {
        // test for pawn move and pawnJump
        Pawn whitePawn = new Pawn(52, Alliance.WHITE, true);
        Pawn blackPawn = new Pawn(16, Alliance.BLACK, false);
        Board.Builder builder = new Board.Builder();
        builder.addPiece(whitePawn);
        builder.addPiece(blackPawn);
        builder.addPiece(this.whiteKing);
        builder.addPiece(this.blackKing);
        Board board = builder.build();
        ArrayList <Move> possibleMoves = (ArrayList<Move>) whitePawn.getPossibleLegalMoves(board);
        ArrayList <Move> possibleMovesCheck = new ArrayList<>();
        possibleMovesCheck.add(new Move(board, whitePawn, 44));
        possibleMovesCheck.add(new Move.PawnJump(board, whitePawn, 36));
        assertTrue(possibleMoves.containsAll(possibleMovesCheck));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
        possibleMovesCheck.clear();
        possibleMoves = (ArrayList<Move>) blackPawn.getPossibleLegalMoves(board);
        possibleMovesCheck.add(new Move(board, blackPawn, 24));
        assertTrue(possibleMoves.containsAll(possibleMovesCheck));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
        possibleMovesCheck.clear();

        // test for pawn attacks
        Pawn blackPawn1 = new Pawn (43, Alliance.BLACK, false);
        Pawn blackPawn2 = new Pawn (45, Alliance.BLACK, false);
        builder.addPiece(blackPawn1);
        builder.addPiece(blackPawn2);
        board = builder.build();
        possibleMoves = (ArrayList<Move>)whitePawn.getPossibleLegalMoves(board);
        possibleMovesCheck.add(new Move(board, whitePawn, 44));
        possibleMovesCheck.add(new Move.PawnJump(board, whitePawn, 36));
        possibleMovesCheck.add(new Move.AttackMove(board, whitePawn, 43, blackPawn1));
        possibleMovesCheck.add(new Move.AttackMove(board, whitePawn, 45, blackPawn2));
        assertTrue(possibleMovesCheck.containsAll(possibleMoves));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
        possibleMovesCheck.clear();
        possibleMoves = (ArrayList<Move>) blackPawn1.getPossibleLegalMoves(board);
        possibleMovesCheck.add(new Move(board, blackPawn1, 51));
        possibleMovesCheck.add(new Move.AttackMove(board, blackPawn1, 52, whitePawn));
        assertTrue(possibleMoves.containsAll(possibleMovesCheck));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
        possibleMovesCheck.clear();
        possibleMoves = (ArrayList<Move>) blackPawn2.getPossibleLegalMoves(board);
        possibleMovesCheck.add(new Move(board, blackPawn2, 53));
        possibleMovesCheck.add(new Move.AttackMove(board, blackPawn2, 52, whitePawn));
        assertTrue(possibleMoves.containsAll(possibleMovesCheck));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
        possibleMovesCheck.clear();

        // test edge cases
        builder = new Board.Builder();
        Pawn whitePawn1 = new Pawn (32, Alliance.WHITE, false);
        Pawn whitePawn2 = new Pawn (39, Alliance.WHITE, false);
        blackPawn1 = new Pawn (24, Alliance.BLACK, false);
        blackPawn2 = new Pawn (31, Alliance.BLACK, false);
        builder.addPiece(whitePawn1);
        builder.addPiece(whitePawn2);
        builder.addPiece(blackPawn1);
        builder.addPiece(blackPawn2);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        board = builder.build();
        assertTrue(whitePawn1.getPossibleLegalMoves(board).isEmpty());
        assertTrue(whitePawn2.getPossibleLegalMoves(board).isEmpty());
        assertTrue(blackPawn1.getPossibleLegalMoves(board).isEmpty());
        assertTrue(blackPawn2.getPossibleLegalMoves(board).isEmpty());

        //test enPassant
        builder = new Board.Builder();
        whitePawn1 = new Pawn (53, Alliance.WHITE, true);
        blackPawn1 = new Pawn (38, Alliance.BLACK, false);
        whitePawn2 = new Pawn (25, Alliance.WHITE, false);
        blackPawn2 = new Pawn (10, Alliance.BLACK, true);
        builder.addPiece(whitePawn1);
        builder.addPiece(blackPawn1);
        builder.addPiece(whitePawn2);
        builder.addPiece(blackPawn2);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        builder.setMover(Alliance.WHITE);
        board = builder.build();
        Move white1Jump = new Move.PawnJump(board, whitePawn1, 37);
        assertTrue(whitePawn1.getPossibleLegalMoves(board).contains(white1Jump));
        MoveTransition trans = board.makeMove(white1Jump);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        whitePawn1 = (Pawn) whitePawn1.movePiece(white1Jump);
        assertTrue(board.getEnPassantPawn() != null);
        Move enPassantCapture = new Move.AttackMove(board, blackPawn1, 45, whitePawn1);
        assertTrue(blackPawn1.getPossibleLegalMoves(board).contains(enPassantCapture));
        Move black2Jump = new Move.PawnJump(board, blackPawn2, 26);
        assertTrue(blackPawn2.getPossibleLegalMoves(board).contains(black2Jump));
        trans = board.makeMove(black2Jump);
        assertTrue(trans.getStatus() == MoveStatus.DONE);
        board = trans.getTransBoard();
        blackPawn2 = (Pawn) blackPawn2.movePiece(black2Jump);
        assertTrue(board.getEnPassantPawn() != null);
        enPassantCapture = new Move.AttackMove(board, whitePawn2, 18, blackPawn2);
        assertTrue(whitePawn2.getPossibleLegalMoves(board).contains(enPassantCapture));
    }

    /**
     * test the movement of the bishop object
     */
    @Test
    public void BishopMovementTest (){
        // basic movement
        Bishop blackBishop = new Bishop (28, Alliance.BLACK, false);
        Bishop whiteBishop = new Bishop (33, Alliance.WHITE, false);
        Board.Builder builder = new Board.Builder();
        builder.addPiece(blackBishop);
        builder.addPiece(whiteBishop);
        builder.addPiece(this.blackKing);
        builder.addPiece(this.whiteKing);
        Board board = builder.build();
        testMoveAndAttackMove(board, blackBishop, new int[]{19,10,1,37,46,55,21,14,7,35,42,49,56}, new int[] {} );
        testMoveAndAttackMove(board, whiteBishop, new int[]{24, 42, 51, 40, 26, 19, 12}, new int[] {5} );

        int[] whiteBishopPawnLocations = {24, 26, 40, 42};
        surroundWithPawns(whiteBishop, Alliance.WHITE, whiteBishopPawnLocations);
        surroundWithPawns(whiteBishop, Alliance.BLACK, whiteBishopPawnLocations);

        int[] blackBishopPawnLocation = {19, 21, 35, 37};
        surroundWithPawns(blackBishop, Alliance.BLACK, blackBishopPawnLocation);
        surroundWithPawns(blackBishop, Alliance.WHITE, blackBishopPawnLocation);

        builder.addPiece(new Knight(10, Alliance.BLACK, false));
        builder.addPiece(new Queen(55, Alliance.WHITE, false));
        builder.addPiece(new Pawn(7, Alliance.BLACK, false));
        builder.addPiece(new Rook(42, Alliance.WHITE, false));
        board = builder.build();
        testMoveAndAttackMove(board, blackBishop, new int[] {19, 37, 46, 21, 14, 35}, new int[] {55, 42});

        builder.addPiece(new Knight(24, Alliance.BLACK, false));
        builder.addPiece(new Pawn(12, Alliance.WHITE, false));
        builder.addPiece(new Rook (40, Alliance.BLACK, false));
        board = builder.build();
        testMoveAndAttackMove(board, whiteBishop, new int[] {26, 19}, new int[] {24, 40});

    }

    /**
     * test the movement of the queen object
     */
    @Test
    public void queenMovementTest() {
        Queen blackQueen = new Queen(38, Alliance.BLACK, false);
        Queen whiteQueen = new Queen(18, Alliance.WHITE, false);
        Board.Builder builder = new Board.Builder();
        builder.addPiece(blackQueen);
        builder.addPiece(whiteQueen);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        Board board = builder.build();
        testMoveAndAttackMove(board,
                blackQueen,
                new int[] {29, 20, 11, 2, 47, 31, 45, 52, 59, 6, 14, 22, 30, 46, 54, 62, 39, 37, 36, 35, 34, 33, 32},
                new int[] {});
        testMoveAndAttackMove(board,
                whiteQueen,
                new int[] {16, 17, 0, 9, 2, 10, 11, 4, 19, 20, 21, 22,
                        23, 27, 36, 45, 54, 63, 26, 34, 42, 50, 58, 25, 32},
                new int[] {});

        surroundWithPawns(blackQueen, Alliance.WHITE, new int[] {29, 30, 31, 37, 45, 46, 47, 39});
        surroundWithPawns(blackQueen, Alliance.BLACK, new int[] {29, 30, 31, 37, 45, 46, 47, 39});
        surroundWithPawns(whiteQueen, Alliance.BLACK, new int[] {9, 10, 11, 19, 27, 26, 25, 17});
        surroundWithPawns(whiteQueen, Alliance.WHITE, new int[] {9, 10, 11, 19, 27, 26, 25, 17});

        builder.addPiece(new Rook(0, Alliance.WHITE, false));
        builder.addPiece(new Knight(10, Alliance.BLACK, false));
        builder.addPiece(new Pawn(23, Alliance.BLACK, false));
        builder.addPiece(new Pawn(36, Alliance.WHITE, false));
        builder.addPiece(new Bishop(58, Alliance.BLACK, false));
        builder.addPiece(new Knight(32, Alliance.BLACK, false));
        builder.addPiece(new Queen(16, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board,
                whiteQueen,
                new int[] {9, 19, 20, 21, 22, 11, 4, 27, 26, 34, 42, 50, 25, 17},
                new int[] {10, 23, 58, 32});

        builder.addPiece(new Pawn(11, Alliance.WHITE, false));
        builder.addPiece(new Knight(14, Alliance.BLACK, false));
        builder.addPiece(new Rook(31, Alliance.WHITE, false));
        builder.addPiece(new Bishop(39, Alliance.WHITE, false));
        builder.addPiece(new Bishop (47, Alliance.BLACK, false));
        builder.addPiece(new Knight(54, Alliance.WHITE, false));
        builder.addPiece(new Pawn (59, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board,
                blackQueen,
                new int[] {37, 29, 20, 46, 45, 52, 30, 22},
                new int[] {11, 31, 39, 54, 59, 36});
    }

    /**
     * test the movement of the rook movement
     */
    @Test
    public void rookMovementTest () {
        Rook blackRook = new Rook(49, Alliance.BLACK, false);
        Rook whiteRook = new Rook(20, Alliance.WHITE, false);

        Board.Builder builder = new Board.Builder();
        builder.addPiece(blackRook);
        builder.addPiece(whiteRook);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);

        Board board = builder.build();
        testMoveAndAttackMove(board,
                whiteRook,
                new int[] {21, 22, 23, 19, 18, 17, 16, 4, 12, 28, 36, 44, 52},
                new int[] {} );
        testMoveAndAttackMove(board,
                blackRook,
                new int[] {48, 50, 51, 52, 53, 54, 55, 41, 33, 25, 17, 9, 1, 57},
                new int[] {});

        surroundWithPawns(whiteRook, Alliance.BLACK, new int[] {12, 21, 28, 19});
        surroundWithPawns(whiteRook, Alliance.WHITE, new int[] {12, 21, 28, 19});
        surroundWithPawns(blackRook, Alliance.WHITE, new int[] {41, 50, 57, 48});
        surroundWithPawns(blackRook, Alliance.BLACK, new int[] {41, 50, 57, 48});

        builder.addPiece(new Rook(4, Alliance.WHITE, false));
        builder.addPiece(new Knight(22, Alliance.BLACK, false));
        builder.addPiece(new Bishop(44, Alliance.BLACK, false));
        builder.addPiece(new Pawn (16, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board,
                whiteRook,
                new int[] {12, 21, 28, 36, 17, 18, 19},
                new int[] {22, 44});

        builder.addPiece(new Pawn(48, Alliance.BLACK, false));
        builder.addPiece(new Knight(57, Alliance.BLACK, false));
        builder.addPiece(new Knight(17, Alliance.WHITE, false));
        builder.addPiece(new Pawn(54, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board,
                blackRook,
                new int[] {50, 51, 52, 53, 41, 33, 25},
                new int[] {54, 17});
    }

    /**
     * test the movement of the knight object
     */
    @Test
    public void knightMovementTest() {
        Knight whiteKnight = new Knight(44, Alliance.WHITE, false);
        Knight blackKnight = new Knight(26, Alliance.BLACK, false);
        Board.Builder builder = new Board.Builder();

        builder.addPiece(whiteKnight);
        builder.addPiece(blackKnight);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        Board board = builder.build();

        testMoveAndAttackMove(board, whiteKnight, new int[] {27, 29, 38, 54, 61, 59, 50, 34}, new int[] {});
        testMoveAndAttackMove(board, blackKnight, new int[] {9, 11, 20, 36, 43, 41, 32, 16}, new int[] {});

        surroundWithPawns(whiteKnight, Alliance.WHITE, new int[] {27, 29, 38, 54, 61, 59, 50, 34});
        surroundWithPawns(whiteKnight, Alliance.BLACK, new int[] {27, 29, 38, 54, 61, 59, 50, 34});
        surroundWithPawns(blackKnight, Alliance.BLACK, new int[] {9, 11, 20, 36, 43, 41, 32, 16});
        surroundWithPawns(blackKnight, Alliance.WHITE, new int[] {9, 11, 20, 36, 43, 41, 32, 16});

        builder.addPiece(new Bishop(38, Alliance.BLACK, false));
        builder.addPiece(new Knight(54, Alliance.WHITE, false));
        builder.addPiece(new Pawn(61, Alliance.BLACK, false));
        builder.addPiece(new Pawn(59, Alliance.WHITE, false));
        builder.addPiece(new Rook(50, Alliance.BLACK, false));
        builder.addPiece(new Rook(34, Alliance.WHITE, false));
        builder.addPiece(new Knight(27, Alliance.BLACK, false));
        builder.addPiece(new Knight(29, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board, whiteKnight, new int[]{}, new int[] {38, 61, 50, 27});

        builder.addPiece(new Bishop(9, Alliance.BLACK, false));
        builder.addPiece(new Knight(11, Alliance.WHITE, false));
        builder.addPiece(new Pawn(20, Alliance.BLACK, false));
        builder.addPiece(new Pawn(36, Alliance.WHITE, false));
        builder.addPiece(new Rook(43, Alliance.BLACK, false));
        builder.addPiece(new Rook(41, Alliance.WHITE, false));
        builder.addPiece(new Knight(32, Alliance.BLACK, false));
        builder.addPiece(new Knight(16, Alliance.WHITE, false));
        board = builder.build();

        testMoveAndAttackMove(board, blackKnight, new int[]{}, new int[] {11, 36, 41, 16});

        //test corner cases
        Knight knight0 = new Knight(0, Alliance.WHITE, false);
        Knight knight7 = new Knight(7, Alliance.WHITE, false);
        Knight knight56 = new Knight(56, Alliance.WHITE, false);
        Knight knight63 = new Knight(63, Alliance.WHITE, false);
        builder = new Board.Builder();
        builder.addPiece(knight0);
        builder.addPiece(knight7);
        builder.addPiece(knight56);
        builder.addPiece(knight63);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        board = builder.build();

        testMoveAndAttackMove(board, knight0, new int[]{10,17}, new int[]{} );
        testMoveAndAttackMove(board, knight7, new int[]{13,22}, new int[]{} );
        testMoveAndAttackMove(board, knight56, new int[]{41,50}, new int[]{} );
        testMoveAndAttackMove(board, knight63, new int[]{46,53}, new int[]{} );

        knight0 = new Knight(0, Alliance.BLACK, false);
        knight7 = new Knight(7, Alliance.BLACK, false);
        knight56 = new Knight(56, Alliance.BLACK, false);
        knight63 = new Knight(63, Alliance.BLACK, false);
        builder = new Board.Builder();
        builder.addPiece(knight0);
        builder.addPiece(knight7);
        builder.addPiece(knight56);
        builder.addPiece(knight63);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        board = builder.build();

        testMoveAndAttackMove(board, knight0, new int[]{10,17}, new int[]{} );
        testMoveAndAttackMove(board, knight7, new int[]{13,22}, new int[]{} );
        testMoveAndAttackMove(board, knight56, new int[]{41,50}, new int[]{} );
        testMoveAndAttackMove(board, knight63, new int[]{46,53}, new int[]{} );
    }

    /**
     * test the movement of the king object
     */
    @Test
    public void kingMovementTest() {
        King whiteKing = new King(21, Alliance.WHITE, false);
        King blackKing = new King(52, Alliance.BLACK, false);
        Board.Builder builder = new Board.Builder();

        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        Board board = builder.build();

        testMoveAndAttackMove(board, whiteKing, new int[] {12, 13, 14, 22, 30, 29, 28, 20}, new int[]{});
        testMoveAndAttackMove(board, blackKing, new int[] {43, 44, 45, 53, 61, 60, 59, 51}, new int[] {});

        surroundWithPawns(whiteKing, Alliance.BLACK, new int[] {12, 13, 14, 22, 30, 29, 28, 20} );
        surroundWithPawns(whiteKing, Alliance.WHITE, new int[] {12, 13, 14, 22, 30, 29, 28, 20} );
        surroundWithPawns(blackKing, Alliance.BLACK, new int[] {43, 44, 45, 53, 61, 60, 59, 51});
        surroundWithPawns(blackKing, Alliance.WHITE, new int[] {43, 44, 45, 53, 61, 60, 59, 51} );
    }

    /**
     * surround a piece with pawns, if the alliance of the pawns is the same as as the piece, make sure the piece
     * can't move, if the alliance of the pawns is opposite make sure all the pawns are attacked
     * @param piece the piece to surround
     * @param pawnAlliance the alliance of the surrounding pawns
     * @param pawnLocations the locations of the pawns
     */
    private void surroundWithPawns (Piece piece, Alliance pawnAlliance, int[] pawnLocations) {
        Pawn[] pawns = new Pawn[pawnLocations.length];
        for (int i = 0; i < pawnLocations.length; i++) {
            pawns[i] = new Pawn(pawnLocations[i], pawnAlliance, false);
        }
        Board.Builder builder = new Board.Builder();
        builder.addPiece(piece);
        builder.addPiece(whiteKing);
        builder.addPiece(blackKing);
        for (Pawn pawn: pawns) {
            builder.addPiece(pawn);
        }
        Board board = builder.build();
        if (pawnAlliance == piece.getAlliance()) {
            assertTrue(piece.getPossibleLegalMoves(board).isEmpty());
        }
        else {
            List<Move> attacks = new ArrayList<>();
            for (Pawn pawn: pawns) {
                attacks.add (new Move.AttackMove(board, piece, pawn.getPosition(), pawn));
            }
            List<Move> pieceMoves = piece.getPossibleLegalMoves(board);
            assertTrue(pieceMoves.containsAll(attacks));
            pieceMoves.removeAll(attacks);
            assertTrue(pieceMoves.isEmpty());
        }
    }

    /**
     * given a piece and a board, make sure all the moves[] destinations are regular moves, and the attacks[]
     * destinations are all attacks
     * @param board the board to check the moves for
     * @param piece the piece to check
     * @param moves the moves array
     * @param attacks the attacks array
     */
    private void testMoveAndAttackMove (Board board, Piece piece, int[] moves, int[] attacks) {
        List <Move> possibleMovesCheck = new ArrayList<>();
        for (int i: moves) {
            possibleMovesCheck.add(new Move(board, piece, i));
        }
        for (int i: attacks) {
            Piece attackedPiece = board.getTile(i).getPiece();
            possibleMovesCheck.add(new Move.AttackMove(board, piece, i, attackedPiece));
        }
        List<Move> possibleMoves = piece.getPossibleLegalMoves(board);
        assertTrue(possibleMoves.containsAll(possibleMovesCheck));
        possibleMoves.removeAll(possibleMovesCheck);
        assertTrue(possibleMoves.isEmpty());
    }

}
