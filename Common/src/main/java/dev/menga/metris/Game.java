package dev.menga.metris;

import java.util.*;

import dev.menga.metris.utils.Vec2i;

public abstract class Game {

    Field field = new Field();

    // The amount of `elapsed' (milliseconds) that needs to pass for the
    // gravity to pull the current tetromino 1 block.
    long gravityStrengh = 1000;

    Vec2i position;
    boolean canExchangeHeld = true;
    Tetromino currentTetromino = null;
    Tetromino holdingTetromino = null;

    // TODO: Custom queue impl?
    Queue<Tetromino> bagA = null;
    Queue<Tetromino> bagB = null;

    long elapsed = 0;
    // TODO: Maybe create Timer classes?
    long gravityTimer = 0;

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

    public Vec2i getSpawnPosition() {
        // TODO: When field is overfilled, give player a last chance by
        // moving the spawn position up a bit.
        return Vec2i.of(4, 19);
    }

    public boolean testPlacement(Tetromino tet, Vec2i coords) {
        Vec2i[] tiles = tet.getShape().getTiles();
        boolean fits = true;
        for (int i = 0; i < tiles.length; ++i) {
            Vec2i testPos = coords.add(tiles[i]);
            if (this.field.colors[testPos.getY()][testPos.getX()].isVisible()) {
                fits = false;
                break;
            }
        }
        return fits;
    }

    public boolean move(Vec2i offset) {
        Vec2i newPos = this.position.add(offset);
        if (testPlacement(this.currentTetromino, newPos)) {
            this.position = newPos;
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        return this.move(Vec2i.of(1, 0));
    }

    public boolean moveLeft() {
        return this.move(Vec2i.of(-1, 0));
    }

    public boolean moveDown() {
        boolean ok = this.move(Vec2i.of(0, 1));
        if (!ok) {
            // TODO: Place?
        }
        return ok;
    }

    // TODO: Right now delta gets passed as milliseconds, investigate if
    // that is OK.
    public void update(long delta) {
        this.elapsed += delta;
        this.gravityTimer += delta;

        if (this.gravityTimer >= this.gravityStrengh) {
            this.gravityTimer -= this.gravityStrengh;
            this.moveDown();
        }
    }

    public Game() {
        this.refillBags();
        this.refillBags();
        this.position = this.getSpawnPosition();
        this.currentTetromino = this.pollNextTetromino();
    }
}
