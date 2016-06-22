package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.move.CastleType;
import com.chess.engine.move.Move;
import com.chess.engine.move.MoveStatus;
import com.chess.engine.move.MoveTransition;
import com.chess.engine.pieces.*;
import com.chess.engine.pieces.singletonPieces.*;
import com.chess.engine.pieces.vectorPieces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wakefield
 * @version 1.0
 * the class that represents the board.
 */
public class Board {
    // the tiles on the board
    private final Tile[] tileList;
    // the alliance of the next mover
    private Alliance nextMover;

    // if there is a pawn that can be taken enPassant, this is that pawn
    private Pawn enPassantPawn;

    // the kings of the players
    private King whiteKing;
    private King blackKing;

    // the pieces
    private Collection<Piece> whitePieces;
    private Collection<Piece> blackPieces;
    private Collection<Piece> allPieces;

    // the possible moves for each player
    private Collection<Move> whitePossibleMoves;
    private Collection<Move> blackPossibleMoves;

    // is the player in check
    private boolean whiteInCheck;
    private boolean blackInCheck;

    /**
     * Creates a Board object from the builder
     * @param builder the builder for the given board
     */
    private Board (Builder builder) {
        tileList = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tileList[i] = builder.getTile(i);
        }

        this.enPassantPawn = builder.getEnPassantPawn();
        this.nextMover = builder.getMover();

        this.whitePieces = builder.getPieces(Alliance.WHITE);
        this.blackPieces = builder.getPieces(Alliance.BLACK);

        this.whiteKing = calculateKing(Alliance.WHITE);
        this.blackKing = calculateKing(Alliance.BLACK);
        this.allPieces = new ArrayList<>();
        this.allPieces.addAll(this.whitePieces);
        this.allPieces.addAll(this.blackPieces);

        Collection <Move> whitePossibleMovesNoCastles = getPossibleMovesNoCastles(this.whitePieces);
        Collection <Move> blackPossibleMovesNoCastles = getPossibleMovesNoCastles(this.blackPieces);

        this.whitePossibleMoves =
                addCastlesForAlliance(whitePossibleMovesNoCastles, blackPossibleMovesNoCastles, Alliance.WHITE);
        this.blackPossibleMoves =
                addCastlesForAlliance(whitePossibleMovesNoCastles, blackPossibleMovesNoCastles, Alliance.BLACK);

        this.whiteInCheck = isTileAttacked(this.whiteKing.getPosition(), this.blackPossibleMoves);
        this.blackInCheck = isTileAttacked(this.blackKing.getPosition(), this.whitePossibleMoves);
    }

    /**
     * getter for the enPassantPawn
     * @return the enPassantPawn for this board object
     */
    public Pawn getEnPassantPawn() {
        return this.enPassantPawn;
    }

    /**
     * getter for the alliance of the next mover
     * @return the alliance of the next mover
     */
    public Alliance getCurrentPlayerAlliance () {
        return this.nextMover;
    }

    /**
     * get the tile for the given coordinate
     * @param coordinate the coordinate of the tile you want
     * @return the requested tile
     */
    public Tile getTile(int coordinate) {
        return this.tileList[coordinate];
    }

    /**
     * get all the pieces for the given alliance
     * @param alliance the alliance you want the pieces for
     * @return the pieces of the input alliance
     */
    public Collection<Piece> getPiecesByAlliance (Alliance alliance) {
        return alliance.isBlack() ? blackPieces : whitePieces;
    }

    /**
     * get all of the moves for the input piece
     * @param piece the pieces you want the moves of
     * @return the moves for that piece
     */
    public Collection<Move> getMovesForPiece (Piece piece) {
        Alliance pieceAlliance = piece.getAlliance();
        // get all the moves for that piece's alliance
        Collection <Move> moves = pieceAlliance.isBlack() ? this.blackPossibleMoves : this.whitePossibleMoves;
        Collection<Move> toReturn = new ArrayList<>();
        // find all the moves that correspond to the input piece
        for (Move move: moves) {
            if (move.getPiece().equals(piece)) {
                toReturn.add(move);
            }
        }
        return toReturn;
    }

    /**
     * get all of the moves possible for a given alliance
     * @param alliance the alliance you want the moves for
     * @return the moves for the input alliance
     */
    public Collection<Move> getMovesByAlliance(Alliance alliance) {
        return alliance.isWhite() ? this.whitePossibleMoves : this.blackPossibleMoves;
    }

    /**
     * see if a specific player is in check
     * @param alliance the alliance of the player
     * @return is that player in check
     */
    public boolean isPlayerInCheck (Alliance alliance) {
        return alliance.isBlack() ? this.blackInCheck : this.whiteInCheck;
    }

    /**
     * get a specific move for a specific piece
     * @param piece the piece you want the move for
     * @param destination the destination of the move
     * @return the desired move
     */
    public Move getMove(Piece piece, int destination) {
        // get the possible moves for the piece's alliance
        Collection<Move> movesToCheck =
                piece.getAlliance().isBlack() ? this.blackPossibleMoves : this.whitePossibleMoves;
        // for each move
        for (Move move: movesToCheck) {
            // if the move is for the desired piece and destination
            if (move.getPiece().equals(piece) && move.getAlliance() == piece.getAlliance()
                    && move.getEndPosition() == destination && move.getStartPosition() == piece.getPosition()) {
                // return the move
                return move;
            }
        }
        return null;
    }

    /**
     * is one of the possible moves for a given alliance a legal move (a move that doesn't move the player into check)
     * @param alliance the alliance of the player to check
     * @return does that player have legal moves
     */
    private boolean hasLegalMoves (Alliance alliance) {
        // get the correct collection
        Collection<Move> possibleEscapes = alliance.isBlack() ? this.blackPossibleMoves : this.whitePossibleMoves;
        // for each move
        for (Move move: possibleEscapes) {
            // make the move
            MoveTransition trans = makeMove(move);
            // if it is successful return true
            if (trans.getStatus() == MoveStatus.DONE) {
                return true;
            }
        }
        // if no move was found return false
        return false;
    }

    /**
     * calculate if the input alliance is in check
     * @param alliance the alliance to check
     * @return is that alliance in check
     */
    public boolean calculateIsPlayerInCheckmate (Alliance alliance) {
        return isPlayerInCheck(alliance) && !hasLegalMoves(alliance);
    }

    /**
     * calculate if the input alliance is in stalemate
     * @param alliance the alliance to calculate stalemate for
     * @return is that alliance in stalemate
     */
    public boolean calculateIsPlayerInStalemate (Alliance alliance) {
        return !isPlayerInCheck(alliance) && !hasLegalMoves(alliance);
    }

    /**
     * get all the pieces on the board (pieces of both alliances)
     * @return all the pieces on the board
     */
    public Collection<Piece> getAllPieces () {
        return this.allPieces;
    }

    /**
     * get all possible moves for the input collection of pieces, without calculating castle moves
     * @param pieces the collection of pieces
     * @return all possible moves for the input collection of pieces
     */
    private Collection<Move> getPossibleMovesNoCastles (Collection<Piece> pieces) {

        Collection<Move> moves = new ArrayList<>();

        for (final Piece piece: pieces) {
            moves.addAll(piece.getPossibleLegalMoves(this));
        }

        return moves;
    }

    /**
     * givin a coordinate to attack, and a collection of moves, find all moves that attack that coordinate
     * @param coordinate the coordinate we are attacking
     * @param possibleAttacks the collection of moves to search for the aattack
     * @return all of the moves that attack the input coordinate
     */
    private Collection <Move> attacksOnCoordinate (int coordinate, Collection<Move> possibleAttacks) {
        Collection <Move> attacks = new ArrayList<>();

        // for each move in possibleAttacks, add those that attack coordinate
        for (final Move move: possibleAttacks) {
            if (move.getEndPosition() == coordinate) {
                attacks.add(move);
            }
        }

        return attacks;
    }

    /**
     * returns a boolean if there is an attack on coordinate in the move collection opponentMoves
     * @param coordinate the coordinate we are checking for attacks on
     * @param opponentMoves the collection of moves to check for attacks on coordinate
     * @return true if opponentMoves contains an attack on coordinate, false otherwise
     */
    private boolean isTileAttacked (int coordinate, Collection<Move> opponentMoves) {
        return !attacksOnCoordinate(coordinate, opponentMoves).isEmpty();
    }

    /**
     * given an alliance find and return the king for that alliance
     * @param alliance the alliance to find the king for
     * @return the King object for that alliance
     */
    private King calculateKing(Alliance alliance) {
        Collection <Piece> pieces;
        if (alliance.isBlack()) {
            pieces = blackPieces;
        }
        else {
            pieces = whitePieces;
        }

        for (Piece piece: pieces) {
            if (piece.getPieceType() == PieceType.KING) {
                return (King) piece;
            }
        }

        throw new RuntimeException("no king for " + alliance);
    }

    /**
     * calculate the possible castles for a given alliance the add it to the proper collection
     * @param whiteMoves possible white moves with no castles
     * @param blackMoves possible black moves with no castles
     * @param alliance the alliance we are calculating the castles for
     * @return the collection of castle moves added to the appropriate collection of moves
     */
    private Collection<Move> addCastlesForAlliance(Collection<Move> whiteMoves,
                                                   Collection <Move> blackMoves,
                                                   Alliance alliance) {
        final Collection<Move> castles = new ArrayList<>();
        Collection<Move> selfMoves;
        Collection<Move> opponentMoves;
        King king;

        // set up the local variables appropriately
        if (alliance.isBlack()) {
            selfMoves = blackMoves;
            opponentMoves = whiteMoves;
            king = this.blackKing;
        }
        else {
            selfMoves = whiteMoves;
            opponentMoves = blackMoves;
            king = this.whiteKing;
        }

        int[] tiles = alliance.isBlack() ? new int[]{5,6,7} : new int[] {61,62,63};

        // if it's the king's first move and the king is not under attack
        if(king.isFirstMove() && !isTileAttacked(king.getPosition(), opponentMoves)) {
            Tile rookTile;
            // if the intermediate tiles are not occupied
            if (!getTile(tiles[0]).isTileOccupied()
                    && !getTile(tiles[1]).isTileOccupied()) {
                rookTile = getTile(tiles[2]);
                // if the rook's tile is occupied by a rook, and it's it's first move
                if (rookTile.isTileOccupied()) {
                    final Piece rook = rookTile.getPiece();
                    if (rook.getPieceType() == PieceType.ROOK && rook.isFirstMove()) {
                        // if none if the intermediate tiles are attacked
                        if (!isTileAttacked(tiles[0], opponentMoves)
                                && !isTileAttacked(tiles[1], opponentMoves)) {
                            castles.add( new Move.CastleMove(this,
                                    king,
                                    tiles[1],
                                    rook,
                                    tiles[0],
                                    CastleType.KING_SIDE));
                        }
                    }
                }
            }

            tiles = alliance.isBlack() ? new int[] {0,1,2,3} : new int[] {56, 57, 58, 59};

            if (!getTile(tiles[1]).isTileOccupied()
                    && !getTile(tiles[2]).isTileOccupied()
                    && !getTile(tiles[3]).isTileOccupied()) {
                rookTile = getTile(tiles[0]);
                if (rookTile.isTileOccupied()) {
                    final Piece rook = rookTile.getPiece();
                    if (rook.getPieceType() == PieceType.ROOK && rook.isFirstMove()) {
                        if (!isTileAttacked(tiles[2], opponentMoves)
                                && !isTileAttacked(tiles[3], opponentMoves)) {
                            castles.add( new Move.CastleMove( this,
                                    king,
                                    tiles[2],
                                    rook,
                                    tiles[3],
                                    CastleType.QUEEN_SIDE));
                        }
                    }
                }
            }
        }

        // add the castles to the collection and return it
        selfMoves.addAll(castles);
        return selfMoves;
    }

    /**
     * create a standard board
     * @return a standard chess board
     */
    public static Board createStandardBoard () {
        Builder builder = new Builder();
        builder.addPiece(new Rook(0, Alliance.BLACK, true));
        builder.addPiece(new Knight(1, Alliance.BLACK, true));
        builder.addPiece(new Bishop(2, Alliance.BLACK, true));
        builder.addPiece(new Queen(3, Alliance.BLACK, true));
        builder.addPiece(new King(4, Alliance.BLACK, true));
        builder.addPiece(new Bishop(5, Alliance.BLACK, true));
        builder.addPiece(new Knight(6, Alliance.BLACK, true));
        builder.addPiece(new Rook(7, Alliance.BLACK, true));
        for (int i = 8; i < 16; i++) {
            builder.addPiece(new Pawn(i, Alliance.BLACK, true));
        }

        for (int i = 48; i < 56; i++) {
            builder.addPiece(new Pawn(i, Alliance.WHITE, true));
        }
        builder.addPiece(new Rook(56, Alliance.WHITE, true));
        builder.addPiece(new Knight(57, Alliance.WHITE, true));
        builder.addPiece(new Bishop(58, Alliance.WHITE, true));
        builder.addPiece(new Queen(59, Alliance.WHITE, true));
        builder.addPiece(new King(60, Alliance.WHITE, true));
        builder.addPiece(new Bishop(61, Alliance.WHITE, true));
        builder.addPiece(new Knight(62, Alliance.WHITE, true));
        builder.addPiece(new Rook(63, Alliance.WHITE, true));

        builder.setMover(Alliance.WHITE);

        return builder.build();
    }

    /**
     * see if a move is possible
     * @param move the move to see is possible
     * @return true if possible, false otherwise
     */
    private boolean isPossibleMove (Move move) {
        // is the alliance the proper alliance
        Alliance moveAlliance = move.getAlliance();
        if (moveAlliance != this.nextMover) {
            return false;
        }
        // look at all possible moves
        Collection <Move> moves = moveAlliance.isBlack() ? this.blackPossibleMoves : this.whitePossibleMoves;
        // is the move contained in moves
        return moves.contains(move);
    }

    /**
     * try to make the move, create that new board, and find the status
     * @param move the move to try to make
     * @return the appropriate move transition
     */
    public MoveTransition makeMove (Move move) {
        // get the alliance of the move
        Alliance moveAlliance = move.getAlliance();
        // if the move isn't possible return a move transition with the status ILLEGAL
        if (!isPossibleMove(move)) {
            return new MoveTransition(this, MoveStatus.ILLEGAL);
        }
        // get the board that would result from the move
        Board board = move.execute();
        // make sure the next mover alliance has changed
        Alliance nextPlayerAlliance = board.getCurrentPlayerAlliance();
        assert (moveAlliance.getOpponentAlliance() == nextPlayerAlliance);
        // if player is in check
        if (board.isPlayerInCheck(moveAlliance)) {
            return new MoveTransition(board, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        // otherwise return status as DONE
        else {
            return new MoveTransition(board, MoveStatus.DONE);
        }

    }

    /**
     * the toString method
     * @return the String representation of the board
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.tileList[i].toString();
            builder.append(String.format("%3s", tileText));
            if ((i+1) %BoardUtils.NUM_COLS == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * the builder class for the board
     */
    public static class Builder {
        Map<Integer, Tile> tempBoard;
        Pawn enPassantPawn;
        Alliance nextMover;
        Collection<Piece> whitePieces;
        Collection<Piece> blackPieces;

        /**
         * the constructor
         */
        public Builder() {
            tempBoard = new HashMap<>();
            tempBoard.putAll(BoardUtils.emptyTiles);
            enPassantPawn = null;
            whitePieces = new ArrayList<>();
            blackPieces = new ArrayList<>();
        }

        /**
         * add a piece to the builder
         * @param piece the piece to add
         */
        public void addPiece(Piece piece) {
            tempBoard.put(piece.getPosition(), new Tile(piece.getPosition(), piece));
            if (piece.getAlliance().isBlack()) {
                blackPieces.add(piece);
            }
            else {
                whitePieces.add(piece);
            }
        }

        /**
         * get the Board for this Builder
         * @return the Board made by the builder
         */
        public Board build() {
            return new Board(this);
        }

        /**
         * get the tile at the specified coordinate
         * @param i the tile coordinate
         * @return the Tile at coordinate i
         */
        Tile getTile(int i) {
            return tempBoard.get(i);
        }

        /**
         * set the enPassantPawn
         * @param enPassantPawn the Pawn to set
         */
        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        /**
         * get the enPassantPawn
         * @return the enPassantPawn
         */
        Pawn getEnPassantPawn () {
            return this.enPassantPawn;
        }

        /**
         * set the next mover for the board
         * @param alliance the alliance of the next mover
         */
        public void setMover (Alliance alliance) {
            this.nextMover = alliance;
        }

        /**
         * get the next mover
         * @return the alliance of the next mover
         */
        Alliance getMover() {
            return this.nextMover;
        }

        /**
         * get all the pieces for the given alliance
         * @param alliance the alliance of the pieces to get
         * @return all the pieces for that alliance
         */
        Collection<Piece> getPieces(Alliance alliance) {
            if (alliance.isBlack()) {
                return this.blackPieces;
            }
            else {
                return this.whitePieces;
            }
        }
    }

}
