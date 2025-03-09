package dev.menga.metris;

public class Field {
    static final int MAX_WIDTH = 10;
    static final int MAX_VISIBLE_HEIGHT = 20;
    static final int MAX_HEIGHT = 40;

    private Color[][] field = new Color[MAX_WIDTH][MAX_HEIGHT];

    public Field() {
        for (int i = 0; i < MAX_WIDTH; ++i) {
            for (int j = 0; j < MAX_HEIGHT; ++j) {
                field[i][j] = Color.VOID;
            }
        }
    }
}
