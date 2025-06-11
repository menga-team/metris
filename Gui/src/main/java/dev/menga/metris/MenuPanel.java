package dev.menga.metris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Color.*;

public class MenuPanel extends JPanel {
    public MenuPanel(ActionListener singleplayerAction, ActionListener multiplayerAction, ActionListener scoreAction) {
        setLayout(new GridBagLayout());
        setBackground(BLACK);

        JButton singleplayerBtn = new JButton("Singleplayer");
        JButton multiplayerBtn = new JButton("Multiplayer");
        JButton scoreBtn = new JButton("Score");

        singleplayerBtn.addActionListener(singleplayerAction);
        multiplayerBtn.addActionListener(multiplayerAction);
        scoreBtn.addActionListener(scoreAction);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;

        gbc.gridy = 0;
        add(singleplayerBtn, gbc);
        gbc.gridy = 1;
        add(multiplayerBtn, gbc);
        gbc.gridy = 2;
        add(scoreBtn, gbc);
    }
}