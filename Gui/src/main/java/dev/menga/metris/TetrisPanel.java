package dev.menga.metris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisPanel extends JPanel {
    private int cell_size;
    private final dev.menga.metris.MetrisClient gameClient;

    public TetrisPanel(int cellsize) {
        this.gameClient = new dev.menga.metris.MetrisClient();
        cell_size = cellsize;
        setPreferredSize(new Dimension(
                gameClient.getFieldWidth() * cell_size,
                gameClient.getFieldVisibleHeight() * cell_size
        ));
        setBackground(java.awt.Color.BLACK);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> gameClient.moveLeft();
                    case KeyEvent.VK_RIGHT -> gameClient.moveRight();
                    case KeyEvent.VK_DOWN -> gameClient.softDrop();
                    case KeyEvent.VK_UP -> gameClient.rotate();
                }
                repaint();
            }
        });

        Timer timer = new Timer(500, e -> {
            gameClient.tick();
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw field
        for (int x = 0; x < gameClient.getFieldWidth(); x++) {
            for (int y = 0; y < gameClient.getFieldVisibleHeight(); y++) {
                dev.menga.metris.Color color = gameClient.getFieldColor(x, y + gameClient.getFieldHeight() - gameClient.getFieldVisibleHeight());
                if (color != dev.menga.metris.Color.VOID) {
                    g.setColor(toAwtColor(color));
                    g.fillRect(x * cell_size, y * cell_size, cell_size, cell_size);
                }
            }
        }
        // Draw current tetromino
        for (var tile : gameClient.getCurrentTetrominoTiles()) {
            int x = tile.x;
            int y = tile.y - 20 ;
            if (y >= 0 && y < gameClient.getFieldVisibleHeight()) {
                g.setColor(toAwtColor(gameClient.getCurrentTetrominoColor()));
                g.fillRect(x * cell_size, (y) * cell_size, cell_size, cell_size);
            }
        }
    }

    private java.awt.Color toAwtColor(dev.menga.metris.Color color) {
        return switch (color) {
            case AQUA -> java.awt.Color.CYAN;
            case YELLOW -> java.awt.Color.YELLOW;
            case MAGENTA -> java.awt.Color.MAGENTA;
            case GREEN -> java.awt.Color.GREEN;
            case RED -> java.awt.Color.RED;
            case BLUE -> java.awt.Color.BLUE;
            case ORANGE -> java.awt.Color.ORANGE;
            case GARBAGE -> java.awt.Color.GRAY;
            default -> java.awt.Color.BLACK;
        };
    }
}