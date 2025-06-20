package dev.menga.metris.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vec2i {
    public int x;
    public int y;

    public static Vec2i of(int x, int y) {
        return new Vec2i(x, y);
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
}