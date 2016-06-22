package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;
import com.chess.engine.move.Move;
import com.chess.engine.move.MoveStatus;
import com.chess.engine.move.MoveTransition;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.PieceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * the Table class, the main class of the gui
 * @author Daniel Wakefield
 * @version 1.0
 */
public class Table {
    // the board to be drawn
    private Board board;

    // the tile colors
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    // the dimensions
    private static final Dimension FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static final Dimension TILE_DIMENSION = new Dimension(10,10);

    // the sourcetile for a move
    private Tile sourceTile;
    // the piece we're trying to move
    private Piece playerMovedPiece;

    // the board
    private BoardPanel boardPanel;
    // the results panel
    private ResultsPanel resultsPanel;
    // takenpieces panel
    private TakenPiecesPanel takenPiecesPanel;

    // do we want to show the legal moves
    private boolean showLegalMoves;

    /**
     * the constructor for the table
     */
    public Table() {
        // set up the gameFrame
        JFrame gameFrame;
        // set this to true
        this.showLegalMoves = true;
        // create a standard board
        this.board = Board.createStandardBoard();
        // name the JFrame chess
        gameFrame = new JFrame("Chess");
        // set so the frame closes on exit
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setSize(FRAME_DIMENSION);
        JMenuBar tableMenu = createMenuBar();
        gameFrame.setJMenuBar(tableMenu);
        this.boardPanel = new BoardPanel();
        this.boardPanel.drawBoard();
        gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.resultsPanel = new ResultsPanel(this.board);
        gameFrame.add(this.resultsPanel, BorderLayout.NORTH);
        this.takenPiecesPanel = new TakenPiecesPanel();
        gameFrame.add(this.takenPiecesPanel,BorderLayout.WEST);
        gameFrame.setVisible(true);
    }

    /**
     * create the menu bar
     * @return the menu bar
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createOptionsMenu());
        return menuBar;
    }

    /**
     * create the options menu
     * @return the options menu
     */
    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JCheckBox showMoves = new JCheckBox("Highlight Legal Moves", true);
        showMoves.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLegalMoves = showMoves.isSelected();
            }
        });
        optionsMenu.add(showMoves);
        return optionsMenu;
    }

    /**
     * get the board we are currently representing
     * @return the board we are representing
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * the BoardPanel class
     * the visual representation of the game-board itself
     */
    private class BoardPanel extends JPanel {
        List<TilePanel> boardTiles;

        /**
         * set up the board
         */
        BoardPanel () {
            super (new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            // draw the tiles
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * draw the board
         */
        void drawBoard() {
            removeAll();
            this.boardTiles = new ArrayList<> ();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            for (final TilePanel panel: boardTiles) {
                panel.drawTile();
                add(panel);
            }
            validate();
            repaint();
        }
    }

    /**
     * create the tiles
     */
    private class TilePanel extends JPanel {
        // the coordinate of the tile
        private int tileCoordinate;

        /**
         * create the tile JPanel
         * @param boardPanel the board panel
         * @param tileCoordinate the coordinate
         */
        TilePanel(BoardPanel boardPanel, int tileCoordinate) {
            super (new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_DIMENSION);
            assignColor();
            assignIcon();

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        playerMovedPiece = null;
                    }
                    else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = board.getTile(tileCoordinate);
                            if (sourceTile.isTileOccupied()) {
                                playerMovedPiece = sourceTile.getPiece();
                                if (playerMovedPiece.getAlliance() != board.getCurrentPlayerAlliance()) {
                                    playerMovedPiece = null;
                                    sourceTile = null;
                                }
                            }
                            else {
                                sourceTile = null;
                                playerMovedPiece = null;
                            }
                        }
                        else {
                            Move move = board.getMove(playerMovedPiece, tileCoordinate);
                            if (move != null) {
                                MoveTransition moveTrans = board.makeMove(move);
                                if (moveTrans.getStatus() == MoveStatus.DONE) {
                                    board = moveTrans.getTransBoard();
                                    resultsPanel.redo(board);
                                    if(move.isAttack()) {
                                        Move.AttackMove attackMove = (Move.AttackMove) move;
                                        takenPiecesPanel.addPiece(attackMove.getAttackedPiece());
                                        takenPiecesPanel.redo();
                                    }
                                }
                            }
                            sourceTile = null;
                            playerMovedPiece = null;
                        }
                    }
                    boardPanel.drawBoard();
                    checkForWinner();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        /**
         * draw the tile
         */
        void drawTile() {
            assignColor();
            assignIcon();
            highlightLegalMoves();
            validate();
            repaint();
        }

        /**
         * highlight the legal moves
         */
        private void highlightLegalMoves () {
            if (showLegalMoves) {
                if (playerMovedPiece != null) {
                    for (final Move move : board.getMovesForPiece(playerMovedPiece)) {
                        if (move.getEndPosition() == this.tileCoordinate) {
                            try {
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        /**
         * assign the color of the tile
         */
        private void assignColor() {
            if ((this.tileCoordinate / BoardUtils.NUM_ROWS) % 2 == 0) {
                setBackground(this.tileCoordinate % 2 == 0 ? lightTileColor: darkTileColor);
            }
            else {
                setBackground(this.tileCoordinate % 2 == 0 ? darkTileColor: lightTileColor);
            }
        }

        /**
         * assign the icon
         */
        private void assignIcon () {
            String imagesPath = "art/simple/";
            this.removeAll();
            if(board.getTile(this.tileCoordinate).isTileOccupied()) {
                try {
                    final Piece piece = board.getTile(this.tileCoordinate).getPiece();
                        if (piece != null) {
                            final BufferedImage image =
                                    ImageIO.read(new File(imagesPath
                                            + piece.getAlliance().toString().substring(0, 1)
                                            + piece.toString().toUpperCase()
                                            + ".gif"));
                            add(new JLabel(new ImageIcon(image)));
                            if (piece.getPieceType() == PieceType.KING && board.isPlayerInCheck(piece.getAlliance())) {
                                final BufferedImage alert =
                                        ImageIO.read(new File("art/misc/illegal.png"));
                                add (new JLabel(new ImageIcon(alert)));
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * check for a winner, show message dialogue if winner is found, or stalemate occurs
     */
    private void checkForWinner() {
        Alliance currentPlayerAlliance = board.getCurrentPlayerAlliance();
        if (board.calculateIsPlayerInCheckmate(currentPlayerAlliance)) {
            if (currentPlayerAlliance.isBlack()) {
                JOptionPane.showMessageDialog(null, "White Wins");
            }
            else if (currentPlayerAlliance.isWhite()) {
                JOptionPane.showMessageDialog(null, "Black Wins");
            }
        }

        else if (board.calculateIsPlayerInStalemate(board.getCurrentPlayerAlliance())) {
            JOptionPane.showMessageDialog(null, "Stalemate");
        }
    }

}
