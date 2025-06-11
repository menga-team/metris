package dev.menga.metris;

import javax.swing.*;
import java.awt.*;

public class MetrisGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set fixed size
            final int WIDTH = 1920;
            final int HEIGHT = 1080;

            JFrame frame = new JFrame("Metris Tetris");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);

            // Load background image (already confirmed working)
            ImageIcon bgIcon = new ImageIcon("/home/vandi/Documents/uni/Season2/Programming project/metris/Gui/assets/background.png");
            Image bgImage = bgIcon.getImage();

            // Create a panel that draws the background
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bgImage, 0, 0, 1920, 1080, this);
                }
            };
            backgroundPanel.setLayout(null); // Allow absolute positioning
            backgroundPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));


            // Menu panel
            MenuPanel menuPanel = new MenuPanel(
                    e -> {
                        backgroundPanel.removeAll();

                        TetrisPanel mainTetrisPanel = new TetrisPanel(40);
                        mainTetrisPanel.setBounds(626, 141, mainTetrisPanel.getPreferredSize().width, mainTetrisPanel.getPreferredSize().height);
                        backgroundPanel.add(mainTetrisPanel);
                    },
                    e -> {
                        backgroundPanel.removeAll();

                        TetrisPanel mainTetrisPanel = new TetrisPanel(40);
                        mainTetrisPanel.setBounds(626, 141, mainTetrisPanel.getPreferredSize().width, mainTetrisPanel.getPreferredSize().height);
                        backgroundPanel.add(mainTetrisPanel);

                        TetrisPanel bot1TetrisPanel = new TetrisPanel(10);
                        bot1TetrisPanel.setBounds(1741, 81, mainTetrisPanel.getPreferredSize().width, bot1TetrisPanel.getPreferredSize().height);
                        backgroundPanel.add(bot1TetrisPanel);

                        TetrisPanel bot2TetrisPanel = new TetrisPanel(10);
                        bot2TetrisPanel.setBounds(1741, 441, mainTetrisPanel.getPreferredSize().width, bot2TetrisPanel.getPreferredSize().height);
                        backgroundPanel.add(bot2TetrisPanel);

                        TetrisPanel bot3TetrisPanel = new TetrisPanel(10);
                        bot3TetrisPanel.setBounds(1741, 801, mainTetrisPanel.getPreferredSize().width, bot3TetrisPanel.getPreferredSize().height);
                        backgroundPanel.add(bot3TetrisPanel);
                        },
                    e -> {

//                        backgroundPanel.removeAll();
                        JOptionPane.showMessageDialog(frame, "Scoreboard not implemented yet.");
                    }
            );
            menuPanel.setBounds(760, 300, 400, 400);
            backgroundPanel.add(menuPanel);

            frame.setContentPane(backgroundPanel);
            frame.pack(); // Important: triggers preferredSize
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
