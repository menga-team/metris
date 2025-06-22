package dev.menga.metris;

import java.util.*;

import dev.menga.metris.utils.Vec2i;
import lombok.Getter;

public abstract class Game {

    @Getter
    Field field = new Field();

    // The amount of `elapsed' (milliseconds) that needs to pass for the
    // gravity to pull the current tetromino 1 block.
    long gravityStrengh = 1000;

    @Getter
    boolean canExchangeHeld = true;

    @Getter
    Vec2i position;

    @Getter
    Tetromino currentTetromino = null;

    @Getter
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

    public boolean testPlacement(Vec2i coords) {
        return this.testPlacement(this.currentTetromino, coords);
    }

    public boolean testPlacement(Tetromino tet, Vec2i coords) {
        Vec2i[] tiles = tet.getShape().getTiles();
        for (int i = 0; i < tiles.length; ++i) {
            Vec2i testPos = coords.add(tiles[i]);
            if (!(testPos.getY() >= 0 && testPos.getY() < Field.MAX_HEIGHT &&
                  testPos.getX() >= 0 && testPos.getX() < Field.MAX_WIDTH)) {
                return false;
            }
            if (this.getField().getAt(testPos).isVisible()) {
                return false;
            }
        }
        return true;
    }

    public Vec2i getHardDropPosition() {
        Vec2i testPos = new Vec2i(this.position).add(0, 1);
        while(testPos.y-- > 0) {
            if (!this.testPlacement(testPos.add(0, -1))) {
                break;
            }
        }
        return testPos;
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
        boolean ok = this.move(Vec2i.of(0, -1));
        if (!ok) {
            this.place();
        }
        return ok;
    }

    public void nextTetromino() {
        this.currentTetromino = this.pollNextTetromino();
        this.position = this.getSpawnPosition();
        this.gravityTimer = 0;
    }

    public void hardDrop() {
        if (!this.testPlacement(this.getPosition().add(0, -1))) {
            this.place();
        } else {
            this.position = this.getHardDropPosition();
            this.gravityTimer = 0;
        }
    }

    public void place(Tetromino tet, Vec2i pos) {
        this.field.rasterizeTetromino(tet, pos);
        this.nextTetromino();
    }

    public void place() {
        this.place(this.getCurrentTetromino(), this.getPosition());
        clearLines();
    }

    public void clearLines() {
        for (int i = 0; i < Field.MAX_HEIGHT; ++i) {
            if (this.field.isLineFull(i)) {
                this.field.moveLinesDown(i + 1);
            }
        }
    }

    public boolean rotate(Rotation rot) {
        Vec2i[][] offsetData;
        Rotation currentRot = this.getCurrentTetromino().getRotation();
        switch (this.getCurrentTetromino()) {
        case Tetromino.I:
            offsetData = Tetromino.OFFSET_DATA_I;
            break;
        case Tetromino.J:
        case Tetromino.L:
        case Tetromino.S:
        case Tetromino.T:
        case Tetromino.Z:
            offsetData = Tetromino.OFFSET_DATA_JLSTZ;
            break;
        case Tetromino.O:
            Vec2i kick = Tetromino.OFFSET_DATA_O[currentRot.getIndex()]
                         .sub(Tetromino.OFFSET_DATA_O[rot.getIndex()]);
            this.position.addMut(kick);
            this.currentTetromino.setRotation(rot);
            return true;
        default:
            return false;
        }

        Vec2i[] tests = {
            offsetData[currentRot.getIndex()][0].sub(offsetData[rot.getIndex()][0]),
            offsetData[currentRot.getIndex()][1].sub(offsetData[rot.getIndex()][1]),
            offsetData[currentRot.getIndex()][2].sub(offsetData[rot.getIndex()][2]),
            offsetData[currentRot.getIndex()][3].sub(offsetData[rot.getIndex()][3]),
            offsetData[currentRot.getIndex()][4].sub(offsetData[rot.getIndex()][4]),
        };

        int kick = 0;
        boolean fits = false;
        for(; kick < 5; ++kick) {
            if (this.testPlacement(this.getPosition().add(tests[kick]))){
                fits = true;
                break;
            }
        }

        if (fits) {
            this.position.addMut(tests[kick]);
            this.currentTetromino.setRotation(rot);
        }
        return fits;
    }

    public boolean rotateCW() {
        return this.rotate(this.getCurrentTetromino().getRotation().next());
    }

    public boolean rotateCCW() {
        return this.rotate(this.getCurrentTetromino().getRotation().previous());
    }

    // TODO: Right now delta gets passed as milliseconds, investigate if
    // that is OK.
    public void tick(long delta) {
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
