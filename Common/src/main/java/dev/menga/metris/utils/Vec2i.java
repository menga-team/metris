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

    public void addMut(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public Vec2i add(Vec2i rhs) {
        return this.add(rhs.getX(), rhs.getY());
    }

    public Vec2i add(int x, int y) {
        return Vec2i.of(this.getX() + x, this.getY() + y);
    }

    public void subMut(Vec2i rhs) {
        this.x -= rhs.x;
        this.y -= rhs.y;
    }

    public Vec2i sub(Vec2i rhs) {
        return this.sub(rhs.getX(), rhs.getY());
    }

    public Vec2i sub(int x, int y) {
        return Vec2i.of(this.getX() - x, this.getY() - y);
    }

    public void scaleMut(int by) {
        this.x *= by;
        this.y *= by;
    }

    public Vec2i scale(int by) {
        return Vec2i.of(by * this.getX(), by * this.getY());
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i from) {
        this.x = from.getX();
        this.y = from.getY();
    }

    @Override
    public String toString() {
        return "X: " + this.getX() + " Y: " + this.getY();
    }
}
