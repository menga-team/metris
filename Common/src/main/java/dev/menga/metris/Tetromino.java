package dev.menga.metris;

import dev.menga.metris.utils.Vec2i;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum Tetromino {
    I(new Shape[] {
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(2, 0)),    // 0°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1), Vec2i.of(0, -2)),   // 90°
          Shape.quad(Vec2i.of(-2, 0), Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0)),   // 180°
          Shape.quad(Vec2i.of(0, 2), Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1))     // 270°
      }, Color.AQUA),
    O(new Shape[] {
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(1, 1), Vec2i.of(0, 0), Vec2i.of(1, 0)),     // 0°
          Shape.quad(Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(0, -1), Vec2i.of(1, -1)),   // 90°
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(-1, -1), Vec2i.of(0, -1)), // 180°
          Shape.quad(Vec2i.of(-1, 1), Vec2i.of(0, 1), Vec2i.of(-1, 0), Vec2i.of(0, 0)),   // 270°
      }, Color.YELLOW),
    T(new Shape[] {
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, 1), Vec2i.of(1, 0)),    // 0°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1), Vec2i.of(1, 0)),    // 90°
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, -1), Vec2i.of(1, 0)),   // 180°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(-1, 0), Vec2i.of(0, -1)),   // 270°
      }, Color.MAGENTA),
    S(new Shape[] {
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, 1), Vec2i.of(1, 1)),    // 0°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(1, -1)),    // 90°
          Shape.quad(Vec2i.of(-1, -1), Vec2i.of(0, -1), Vec2i.of(0, 0), Vec2i.of(1, 0)),  // 180°
          Shape.quad(Vec2i.of(-1, 1), Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, -1)),  // 270°
      }, Color.GREEN),
    Z(new Shape[] {
          Shape.quad(Vec2i.of(-1, 1), Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(1, 0)),    // 0°
          Shape.quad(Vec2i.of(1, 1), Vec2i.of(1, 0), Vec2i.of(0, 0), Vec2i.of(0, -1)),    // 90°
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, -1), Vec2i.of(1, -1)),  // 180°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(-1, 0), Vec2i.of(-1, -1)),  // 270°
      }, Color.RED),
    J(new Shape[] {
          Shape.quad(Vec2i.of(-1, 1), Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0)),   // 0°
          Shape.quad(Vec2i.of(1, 1), Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1)),    // 90°
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(1, -1)),   // 180°
          Shape.quad(Vec2i.of(-1, -1), Vec2i.of(0, -1), Vec2i.of(0, 0), Vec2i.of(0, 1)),  // 270°
      }, Color.BLUE),
    L(new Shape[] {
          Shape.quad(Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(1, 1)),    // 0°
          Shape.quad(Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1), Vec2i.of(1, -1)),   // 90°
          Shape.quad(Vec2i.of(-1, -1), Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(1, 0)),  // 180°
          Shape.quad(Vec2i.of(-1, 1), Vec2i.of(0, 1), Vec2i.of(0, 0), Vec2i.of(0, -1)),   // 270°
      }, Color.ORANGE);

    private final Shape[] shapes;
    private final Color color;

    @Setter
    private Rotation rotation = Rotation.DEG0;

    Tetromino(Shape[] shapes, Color color) {
        this.shapes = shapes;
        this.color = color;
    }

    // TODO: Any better way to handle rotation?
    public void rotateCW() {
        final Rotation[] rotationTranslation = { Rotation.DEG90, Rotation.DEG180, Rotation.DEG270, Rotation.DEG0 };
        this.setRotation(rotationTranslation[this.getRotation().getIndex()]);
    }

    public void rotateCCW() {
        final Rotation[] rotationTranslation = { Rotation.DEG270, Rotation.DEG0, Rotation.DEG90, Rotation.DEG180 };
        this.setRotation(rotationTranslation[this.getRotation().getIndex()]);
    }

    public Shape getShape() {
        return this.getShapes()[this.getRotation().getIndex()];
    }

    public static List<Tetromino> getRandomValues() {
        List<Tetromino> list =  Arrays.asList(Tetromino.values());
        Collections.shuffle(list);
        return list;
    }
}

enum Rotation {
    DEG0(0),
    DEG90(1),
    DEG180(2),
    DEG270(3);

    @Getter
    private final int index;

    Rotation(int index) {
        this.index = index;
    }
}

@Getter @Setter
class Shape {
    private Vec2i[] tiles;

    public Shape(Vec2i[] tiles) {
        this.tiles = tiles;
    }

    public static Shape quad(Vec2i a, Vec2i b, Vec2i c, Vec2i d) {
        return new Shape(new Vec2i[]{a, b, c, d});
    }
}
