package dev.menga.metris;

import lombok.Getter;

public enum Color {
    VOID(-1),
    SOLID(0),
    GARBAGE(1),
    RED(2),
    ORANGE(3),
    YELLOW(4),
    GREEN(5),
    AQUA(6),
    BLUE(7),
    MAGENTA(8);

    @Getter
    private byte id;

    public boolean isVisible() {
        return this != VOID;
    }

    Color(int id) {
        this.id = (byte) id;
    }
}
