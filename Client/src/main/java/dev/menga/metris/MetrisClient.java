package dev.menga.metris;

import dev.menga.metris.*;

import java.util.*;

public class MetrisClient extends Game {
    private int currentX = Field.MAX_WIDTH / 2;
    private int currentY = 20;

    public MetrisClient() {
        bagA = new LinkedList<>(Tetromino.getRandomValues());
        bagB = new LinkedList<>(Tetromino.getRandomValues());
        refillBags();
        currentTetromino = pollNextTetromino();
    }

    @Override
    public void refillBags() {
        if (bagA == null) bagA = new LinkedList<>();
        if (bagB == null) bagB = new LinkedList<>();
        bagA.addAll(bagB);
        bagB.clear();
        bagB.addAll(Tetromino.getRandomValues());
    }

    public int getFieldWidth() {
        return Field.MAX_WIDTH;
    }

    public int getFieldVisibleHeight() {
        return Field.MAX_VISIBLE_HEIGHT;
    }

    public int getFieldHeight() {
        return Field.MAX_HEIGHT;
    }

    public dev.menga.metris.Color getFieldColor(int x, int y) {
        // Expose field color for GUI
        try {
            var fieldArr = Field.class.getDeclaredField("field");
            fieldArr.setAccessible(true);
            dev.menga.metris.Color[][] arr = (dev.menga.metris.Color[][]) fieldArr.get(field);
            return arr[x][y];
        } catch (Exception e) {
            return dev.menga.metris.Color.VOID;
        }
    }

    public List<Vec2i> getCurrentTetrominoTiles() {
        List<Vec2i> tiles = new ArrayList<>();
        if (currentTetromino == null) return tiles;
        for (Vec2i v : currentTetromino.getShape().getTiles()) {
            tiles.add(new Vec2i(currentX + v.x, currentY + v.y));
        }
        return tiles;
    }

    public dev.menga.metris.Color getCurrentTetrominoColor() {
        return currentTetromino != null ? currentTetromino.getColor() : dev.menga.metris.Color.VOID;
    }

    public void moveLeft() {
        if (canMove(currentX - 1, currentY, currentTetromino.getShape())) currentX--;
    }

    public void moveRight() {
        if (canMove(currentX + 1, currentY, currentTetromino.getShape())) currentX++;
    }

    public void softDrop() {
        if (canMove(currentX, currentY + 1, currentTetromino.getShape())) {
            currentY++;
        } else {
            lockTetromino();
        }
    }

    public void rotate() {
        var oldRot = currentTetromino.getRotation();
        currentTetromino.setRotation(Rotation.values()[(oldRot.getIndex() + 1) % 4]);
        if (!canMove(currentX, currentY, currentTetromino.getShape())) {
            currentTetromino.setRotation(oldRot); // revert
        }
    }

    public void tick() {
        softDrop();
    }

    private boolean canMove(int x, int y, Shape shape) {
        for (Vec2i v : shape.getTiles()) {
            int nx = x + v.x, ny = y + v.y;
            if (nx < 0 || nx >= Field.MAX_WIDTH || ny < 0 || ny >= Field.MAX_HEIGHT) return false;
            if (getFieldColor(nx, ny) != dev.menga.metris.Color.VOID) return false;
        }
        return true;
    }

    private void lockTetromino() {
        try {
            var fieldArr = Field.class.getDeclaredField("field");
            fieldArr.setAccessible(true);
            dev.menga.metris.Color[][] arr = (dev.menga.metris.Color[][]) fieldArr.get(field);
            for (Vec2i v : currentTetromino.getShape().getTiles()) {
                int nx = currentX + v.x, ny = currentY + v.y;
                if (nx >= 0 && nx < Field.MAX_WIDTH && ny >= 0 && ny < Field.MAX_HEIGHT) {
                    arr[nx][ny] = currentTetromino.getColor();
                }
            }
        } catch (Exception ignored) {}
        clearLines();
        currentTetromino = pollNextTetromino();
        currentX = Field.MAX_WIDTH / 2;
        currentY = 20;
    }

    private void clearLines() {
        try {
            var fieldArr = Field.class.getDeclaredField("field");
            fieldArr.setAccessible(true);
            dev.menga.metris.Color[][] arr = (dev.menga.metris.Color[][]) fieldArr.get(field);
            for (int y = 0; y < Field.MAX_HEIGHT; y++) {
                boolean full = true;
                for (int x = 0; x < Field.MAX_WIDTH; x++) {
                    if (arr[x][y] == dev.menga.metris.Color.VOID) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    for (int ty = y; ty > 0; ty--) {
                        for (int x = 0; x < Field.MAX_WIDTH; x++) {
                            arr[x][ty] = arr[x][ty - 1];
                        }
                    }
                    for (int x = 0; x < Field.MAX_WIDTH; x++) {
                        arr[x][0] = dev.menga.metris.Color.VOID;
                    }
                }
            }
        } catch (Exception ignored) {}
    }
}