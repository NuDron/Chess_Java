package com.chess.gui;

import javax.swing.*;
import java.awt.*;

public class Table {

    private final JFrame gameFrame;
    // Setting dimension of GUI frame.
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);

    // Creating Singleton.
    public Table() {
        this.gameFrame = new JFrame("Java Chess");


        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }

}
