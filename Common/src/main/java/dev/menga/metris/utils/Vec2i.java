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

    public void addMut(Vec2i rhs) {
        this.x += rhs.x;
        this.y += rhs.y;
    }

    public Vec2i add(Vec2i rhs) {
        return Vec2i.of(rhs.getX() + this.getX(), rhs.getY() + this.getY());
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
