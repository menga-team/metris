package dev.menga.metris;

import dev.menga.metris.utils.Vec2i;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shape {
    private Vec2i[] tiles;

    public Shape(Vec2i[] tiles) {
        this.tiles = tiles;
    }

    public static Shape quad(Vec2i a, Vec2i b, Vec2i c, Vec2i d) {
        return new Shape(new Vec2i[]{a, b, c, d});
    }
}
