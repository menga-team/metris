package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.menga.metris.utils.Vec2i;
import lombok.Setter;

import java.util.*;

public class RenderableGame extends Game {

    static final int FIELD_RENDER_UNIT = 32;
    static final int NEXT_TETROMINOS = 4;

    static final int[] TEXTURE_LOOKUP = {
            1, 5, 1, 5, 7, 13, 7, 9, 1, 5, 1, 5, 7, 13, 7, 9, 4, 3, 4, 3, 12, 31, 12, 24, 4, 3, 4, 3, 8, 25, 8, 19, 1,
            5, 1, 5, 7, 13, 7, 9, 1, 5, 1, 5, 7, 13, 7, 9, 4, 3, 4, 3, 12, 31, 12, 24, 4, 3, 4, 3, 8, 25, 8, 19, 6,
            15, 6, 15, 2, 29, 2, 23, 6, 15, 6, 15, 2, 29, 2, 23, 14, 30, 14, 30, 28, 46, 28, 44, 14, 30, 14, 30, 20,
            45, 20, 39, 6, 15, 6, 15, 2, 29, 2, 23, 6, 15, 6, 15, 2, 29, 2, 23, 10, 27, 10, 27, 21, 43, 21, 41, 10,
            27, 10, 27, 16, 37, 16, 35, 1, 5, 1, 5, 7, 13, 7, 9, 1, 5, 1, 5, 7, 13, 7, 9, 4, 3, 4, 3, 12, 31, 12, 24,
            4, 3, 4, 3, 8, 25, 8, 19, 1, 5, 1, 5, 7, 13, 7, 9, 1, 5, 1, 5, 7, 13, 7, 9, 4, 3, 4, 3, 12, 31, 12, 24,
            4, 3, 4, 3, 8, 25, 8, 19, 6, 11, 6, 11, 2, 22, 2, 17, 6, 11, 6, 11, 2, 22, 2, 17, 14, 26, 14, 26, 28, 42,
            28, 36, 14, 26, 14, 26, 20, 40, 20, 34, 6, 11, 6, 11, 2, 22, 2, 17, 6, 11, 6, 11, 2, 22, 2, 17, 10, 18,
            10, 18, 21, 38, 21, 32, 10, 18, 10, 18, 16, 33, 16, 0};

/*
        block texturing process
        +------------+-------------------------------+-------------------+-----------------------+-------------------+
        |neighbors   |   bit-array                   |              int  |   lookup table        |   texture index   |
        |            |                               |                   |                       |                   |
        |0 1 1       |                               |                   |                       |                   |
        |0 x 0       |   [0, 1, 1, 0, 1, 1, 1, 0]    |   110             |   array[110]          |   2               |
        |1 1 1       |                               |                   |                       |                   |
        +------------+-------------------------------+-------------------+-----------------------+-------------------+
*/

    private Resources resources;
    private final Vec2i gridOff;
    private final Vec2i nextOff;
    private final Vec2i heldOff;
    @Setter
    private Color[][] randomFieldColors;

    // TODO: RenderConfig class ?
    public RenderableGame(Resources resources, Vec2i gridOff, Vec2i nextOff, Vec2i heldOff) {
        super();
        this.resources = resources;
        this.gridOff = gridOff;
        this.nextOff = nextOff;
        this.heldOff = heldOff;
    }

    public void render(Batch batch) {
        // Render field.
        for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
            for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                Color color = this.field.getColors()[y][x];
                batch.draw(this.resources.getUnit(), this.gridOff.getX() + (FIELD_RENDER_UNIT * x), this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
                if (color.isVisible()) {

                    int[] inp = new int[8];
                    for (int i = 0; i < 8; i++) {
                        inp[i] = 0;
                    }
                    int ly = this.field.MAX_VISIBLE_HEIGHT-1;
                    int lx = this.field.MAX_WIDTH-1;

                    if (x > 0 && y < ly && this.field.getAt(Vec2i.of(x - 1, y + 1)).equals(color)) {
                        inp[0] = 1;
                    }
                    if (y < ly && this.field.getAt(Vec2i.of(x, y + 1)).equals(color)) {
                        inp[1] = 1;
                    }
                    if (x < lx && y < ly && this.field.getAt(Vec2i.of(x + 1, y + 1)).equals(color)) {
                        inp[2] = 1;
                    }
                    if (x < lx && this.field.getAt(Vec2i.of(x + 1, y)).equals(color)) {
                        inp[3] = 1;
                    }
                    if (x < lx && y > 0 && this.field.getAt(Vec2i.of(x + 1, y - 1)).equals(color)) {
                        inp[4] = 1;
                    }
                    if (y > 0 && this.field.getAt(Vec2i.of(x, y - 1)).equals(color)) {
                        inp[5] = 1;
                    }
                    if (x > 0 && y > 0 && this.field.getAt(Vec2i.of(x - 1, y - 1)).equals(color)) {
                        inp[6] = 1;
                    }
                    if (x > 0 && this.field.getAt(Vec2i.of(x - 1, y)).equals(color)) {
                        inp[7] = 1;
                    }

                    int result = 0;
                    for (int i = 0; i < 8; i++) {
                        result = (result << 1) | inp[i];
                    }

                    int index = TEXTURE_LOOKUP[result];

                    Metris.getLogger().info("{} {} {}", inp, result, index);

                    batch.draw(this.resources.getTile(color, index), this.gridOff.getX() + (FIELD_RENDER_UNIT * x), this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
                }
            }
        }

        // Render current tetromino.
        this.renderTetromino(batch, this.getCurrentTetromino(), this.pixelPos(this.getPosition()));
        // Render ghost of current tetromino.
        this.renderTetromino(batch, this.getCurrentTetromino(), this.pixelPos(this.getHardDropPosition()), true);

        // Render upcoming tetrominos.
        Tetromino[] preview = this.getNextTetrominos(NEXT_TETROMINOS);

        for (int i = 0; i < NEXT_TETROMINOS; ++i) {
            this.renderTetromino(batch, preview[NEXT_TETROMINOS - 1 - i], Vec2i.of(this.nextOff.getX(), this.nextOff.getY() + i * FIELD_RENDER_UNIT * 4));
        }

        // Render held tetromino.
        if (this.getHoldingTetromino() != null) {
            this.renderTetromino(batch, this.getHoldingTetromino(), Vec2i.of(this.heldOff.getX(), this.heldOff.getY()));
        }
    }

    public Vec2i pixelPos(Vec2i fieldPos) {
        Vec2i pixelPos = new Vec2i(fieldPos);
        pixelPos.scaleMut(FIELD_RENDER_UNIT);
        pixelPos.addMut(this.gridOff);
        return pixelPos;
    }

    public void renderTetromino(Batch batch, Tetromino toRender, Vec2i position) {
        this.renderTetromino(batch, toRender, position, false);
    }

    public void renderTetromino(Batch batch, Tetromino toRender, Vec2i position, boolean ghost) {
        TextureRegion texture;
        if (ghost) {
            texture = this.resources.getGhostColor(toRender.getColor());
        } else {








            texture = this.resources.getTile(toRender.getColor(), 0);
        }

        for (Vec2i tile : toRender.getShape().getTiles()) {
            batch.draw(texture, position.getX() + (FIELD_RENDER_UNIT * tile.getX()), position.getY() + (FIELD_RENDER_UNIT * tile.getY()));
        }
    }

    public void update(long delta) {
        this.tick(delta);
    }

    @Override
    public void refillBags() {
        // TODO: Online
        this.bagA = this.bagB;
        List<Tetromino> temp = Arrays.asList(Tetromino.values());
        Collections.shuffle(temp);
        this.bagB = new LinkedList<>(temp);
    }

    public void fillField() {
        // Fill the fields with random colors generated in show
        if (this.randomFieldColors != null) {
            for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
                for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                    this.field.getColors()[y][x] = randomFieldColors[y][x];
                }
            }
        }
    }
}
