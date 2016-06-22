package com.chess.gui;

import com.chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * the TakenPiecesPanel class
 * this is the panel that will show which pieces have been taken
 * @author Daniel Wakefield
 * @version 1.0
 */
class TakenPiecesPanel extends JPanel{
    // the two panels, to separate the pieces of the two players
    private JPanel northPanel;
    private JPanel southPanel;

    // panel color, dimension, and border
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    // the two lists of pieces
    private final List<Piece> blackPieces;
    private final List<Piece> whitePieces;

    /**
     * the constructor for the panel
     */
    TakenPiecesPanel () {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
        this.blackPieces = new ArrayList<>();
        this.whitePieces = new ArrayList<>();
    }

    /**
     * add a piece to the panel
     * @param piece the piece we are adding
     */
    void addPiece(Piece piece) {
        // add to the correct panel
        if (piece.getAlliance().isBlack()) {
            this.blackPieces.add(piece);
        }
        else {
            this.whitePieces.add(piece);
        }
    }

    /**
     * redo the panel with every move
     */
    void redo () {
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        Collections.sort(this.whitePieces, new Comparator<Piece>() {
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(this.blackPieces, new Comparator<Piece>() {
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (final Piece piece: this.whitePieces) {
            try {
                final BufferedImage image = ImageIO.read (new File("art/simple/"
                        + piece.getAlliance().toString().substring(0,1)
                        + piece.toString().toUpperCase()
                        + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            }catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece piece: this.blackPieces) {
            try {
                final BufferedImage image = ImageIO.read (new File("art/simple/"
                        + piece.getAlliance().toString().substring(0,1)
                        + piece.toString().toUpperCase()
                        + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 15, icon.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            }catch (final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
