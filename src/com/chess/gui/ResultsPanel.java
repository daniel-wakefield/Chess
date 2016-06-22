package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;

import javax.swing.*;
import java.awt.*;

/**
 * the results panel, shows who wins, if there is a stalemate, and who is the next mover
 * @author Daniel Wakefield
 * @version 1.0
 */
class ResultsPanel extends JPanel {

    //the dimension of the results panel
    private static Dimension RESULTS_PANEL_DIMENSION = new Dimension(40,40);

    // the possible labels
    private JLabel whiteMove;
    private JLabel blackMove;
    private JLabel whiteInCheck;
    private JLabel blackInCheck;
    private JLabel whiteWin;
    private JLabel blackWin;
    private JLabel stalemate;

    /**
     * draw the results panel
     * @param board the board we get the results from
     */
    ResultsPanel(Board board) {
        super(new BorderLayout());
        setPreferredSize(RESULTS_PANEL_DIMENSION);
        setLayout(new FlowLayout());
        JLabel nextMove = new JLabel("Next Move:");
        add(nextMove);
        this.whiteMove = new JLabel("White");
        this.blackMove = new JLabel ("Black");
        add(this.whiteMove);
        add(this.blackMove);
        add(new JLabel("|"));
        add (new JLabel("In Check:"));
        this.blackInCheck = new JLabel("Black");
        this.whiteInCheck = new JLabel("White");
        add (this.blackInCheck);
        add (this.whiteInCheck);
        add(new JLabel("|"));
        add (new JLabel("Winner:"));
        this.blackWin = new JLabel("Black");
        this.whiteWin = new JLabel("White");
        this.stalemate = new JLabel("Stalemate");
        add(this.blackWin);
        add(this.whiteWin);
        add(this.stalemate);
        redo(board);

        setVisible(true);
    }

    /**
     * whenever we redraw the board get the results again
     * @param board the board we get the results for
     */
    void redo(Board board) {

        Alliance currentPlayerAlliance = board.getCurrentPlayerAlliance();

        if (currentPlayerAlliance.isBlack()) {
            turnOn(this.blackMove);
            turnOff(this.whiteMove);
        }
        else {
            turnOn(this.whiteMove);
            turnOff(this.blackMove);
        }
        if (board.isPlayerInCheck(Alliance.BLACK)) {
            turnOn(this.blackInCheck);
        }
        else {
            turnOff(this.blackInCheck);
        }
        if (board.isPlayerInCheck(Alliance.WHITE)){
            turnOn(this.whiteInCheck);
        }
        else {
            turnOff(this.whiteInCheck);
        }

        if (board.calculateIsPlayerInCheckmate(currentPlayerAlliance)) {
            if (currentPlayerAlliance.isWhite()) {
                turnOn(this.blackWin);
            }
            else if (currentPlayerAlliance.isBlack()) {
                turnOn(this.whiteWin);
            }
        }

        if (board.calculateIsPlayerInStalemate(currentPlayerAlliance)) {
            turnOn(this.stalemate);
        }
    }

    /**
     * highlight a label
     * @param label the lakel to turn on
     */
    private void turnOn(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
    }

    /**
     * turn off the label
     * @param label the label to turn off
     */
    private void turnOff(JLabel label) {
        label.setForeground(Color.BLACK);
        label.setOpaque(false);
    }
}
