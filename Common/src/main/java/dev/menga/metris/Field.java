package dev.menga.metris;

import dev.menga.metris.utils.Vec2i;
import lombok.Getter;

public class Field {

    static final int MAX_WIDTH = 10;
    static final int MAX_VISIBLE_HEIGHT = 20;
    static final int MAX_HEIGHT = 40;

    @Getter
    Color[][] colors = new Color[MAX_HEIGHT][MAX_WIDTH];

    public Field() {
        for (int i = 0; i < MAX_HEIGHT; ++i) {
            for (int j = 0; j < MAX_WIDTH; ++j) {
                colors[i][j] = Color.VOID;
            }
        }
    }

    public Color getAt(Vec2i pos) {
        return this.colors[pos.getY()][pos.getX()];
    }

    public void setAt(Vec2i pos, Color color) {
        this.colors[pos.getY()][pos.getX()] = color;
    }

    public void rasterizeTetromino(Tetromino tet, Vec2i pos) {
        Vec2i[] tiles = tet.getShape().getTiles();
        for (int i = 0; i < tiles.length; ++i) {
            this.setAt(pos.add(tiles[i]), tet.getColor());
        }
    }
}
