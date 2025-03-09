package dev.menga.metris;

import lombok.Getter;

public enum Color {
    GARBAGE(-1),
    VOID(0),
    AQUA(1),
    YELLOW(2),
    MAGENTA(3),
    GREEN(4),
    RED(5),
    BLUE(6),
    ORANGE(7);

    @Getter
    private byte id;

    Color(int id) {
        this.id = (byte) id;
    }
}
