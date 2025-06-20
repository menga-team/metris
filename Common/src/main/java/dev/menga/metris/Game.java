package dev.menga.metris;

import java.util.*;

public abstract class Game {
    Field field = new Field();
    Tetromino currentTetromino = null;
    Tetromino holdingTetromino = null;
    // TODO: Custom queue impl?
    Queue<Tetromino> bagA = null;
    Queue<Tetromino> bagB = null;

    /**
     * Will move bagB into bagA and refills bagB with new tetrominos.
     * When playing online the server is tasked with generating them.
     */
    public abstract void refillBags();

    public Tetromino pollNextTetromino() {
        Tetromino poll = bagA.poll();
        if (poll != null) {
            return poll;
        } else {
            this.refillBags();
            return bagA.poll();
        }
    }

    /**
     * This method is able to preview at least the next 7 tetrominos (1 full bag).
     * @param  Amount How many elements it should list.
     * @return An array with the specified amount of tetrominos inside it.
     *         If `amount' > 7 there might be less but at least 7.
     */
    public Tetromino[] getNextTetrominos(int amount) {
        Tetromino[] preview = new Tetromino[amount];
        int i = 0;
        for (Tetromino next : this.bagA) {
            preview[i++] = next;
            if (i == preview.length) break;
        }
        if (i < preview.length) {
            for (Tetromino next : this.bagB) {
                preview[i++] = next;
                if (i == preview.length) break;
            }
        }
        return preview;
    }

    public Game() {
        this.refillBags();
        this.refillBags();
        this.currentTetromino = this.pollNextTetromino();
    }
}
