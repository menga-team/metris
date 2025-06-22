package dev.menga.metris;

import dev.menga.metris.utils.Vec2i;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Tetromino needs to be a instantiated class because we don't want to mix
// rotations, as there is only 1 instance for any given Enum.  Maybe also
// instantiate color for use in mini-games?

@Getter
public class Tetromino {
    private final TetrominoType type;
    @Setter
    private Rotation rotation;

    public Tetromino(TetrominoType type) {
        this.type = type;
        this.rotation = Rotation.DEG0;
    }

    public Shape getShape() {
        return this.getType().getShapes()[this.getRotation().getIndex()];
    }

    public Color getColor() {
        return this.getType().getColor();
    }

    public static List<Tetromino> randomBag() {
        return TetrominoType.getRandomValues().stream().map(Tetromino::new).toList();
    }
}

/* 0,0 is the center of every piece.  Every specified coordinate will
 * be filled with one tile.  4 Coordinates make up 1 tetromino.
 *
 *	-2,2	-1,2	0,2	1,2	2,2
 *	-2,1	-1,1	0,1	1,1	2,1
 *	-2,0	-1,0	0,0	1,0	2,0
 *	-2,-1	-1,-1	0,1	1,2	2,1
 *	-2,-2	-1,-2	0,2	1,2	2,2
 *
 * E.g. the I tetromino will be spawned like this:
 * {{-1, 0}, {0, 0}, {1, 0}, {2, 0}}
 *
 *	-2,2	-1,2	0,2	1,2	2,2
 *	-2,1	-1,1	0,1	1,1	2,1
 *	-2,0	[-1,0]	[0,0]	[1,0]	[2,0]
 *	-2,-1	-1,-1	0,1	1,2	2,1
 *	-2,-2	-1,-2	0,2	1,2	2,2
 *
 * The rotation matrix was copied from this source:
 * https://harddrop.com/wiki/SRS
 */

@Getter
enum TetrominoType {
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

    // Used for rotating (kicks).  See Game.java
    final static Vec2i[][] OFFSET_DATA_JLSTZ = {
        {Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0)},
        {Vec2i.of(0, 0), Vec2i.of(1, 0), Vec2i.of(1, -1), Vec2i.of(0, 2), Vec2i.of(1, 2)},
        {Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 0)},
        {Vec2i.of(0, 0), Vec2i.of(-1, 0), Vec2i.of(-1, -1), Vec2i.of(0, 2), Vec2i.of(-1, 2)},
    };

    final static Vec2i[][] OFFSET_DATA_I = {
        {Vec2i.of(0, 0), Vec2i.of(-1, 0), Vec2i.of(2, 0), Vec2i.of(-1, 0), Vec2i.of(2, 0)},
        {Vec2i.of(-1, 0), Vec2i.of(0, 0), Vec2i.of(0, 0), Vec2i.of(0, 1), Vec2i.of(0, -2)},
        {Vec2i.of(-1, 1), Vec2i.of(1, 1), Vec2i.of(-2, 1), Vec2i.of(1, 0), Vec2i.of(-2, 0)},
        {Vec2i.of(0, 1), Vec2i.of(0, 1), Vec2i.of(0, 1), Vec2i.of(0, -1), Vec2i.of(0, 2)},
    };

    final static Vec2i[] OFFSET_DATA_O = {
        Vec2i.of(0, 0),
        Vec2i.of(0, -1),
        Vec2i.of(-1, -1),
        Vec2i.of(-1, 0),
    };

    private final Shape[] shapes;
    private final Color color;

    TetrominoType(Shape[] shapes, Color color) {
        this.shapes = shapes;
        this.color = color;
    }

    public static List<TetrominoType> getRandomValues() {
        List<TetrominoType> list = Arrays.asList(TetrominoType.values());
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

    public Rotation next() {
        final Rotation[] rotationTranslation = { DEG90, DEG180, DEG270, DEG0 };
        return rotationTranslation[this.getIndex()];
    }

    public Rotation previous() {
        final Rotation[] rotationTranslation = { DEG270, DEG0, DEG90, DEG180 };
        return rotationTranslation[this.getIndex()];
    }

    Rotation(int index) {
        this.index = index;
    }
}
