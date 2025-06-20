package dev.menga.metris;

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
}
