package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.menga.metris.utils.Vec2i;

import java.util.*;

public class RenderableGame extends Game {

    static final int FIELD_RENDER_UNIT = 32;
    static final int NEXT_TETROMINOS = 4;

    private Resources resources;
    private final Vec2i gridOff;
    private final Vec2i nextOff;
    private final Vec2i heldOff;

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
                // TODO: Maybe rethink visible sprites and keep the void
                // a normal sprite as any other color/garbage?
                batch.draw(this.resources.getUnit(), this.gridOff.getX() + (FIELD_RENDER_UNIT * x), this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
                if (color.isVisible()) {
                    batch.draw(this.resources.getColor(color), this.gridOff.getX() + (FIELD_RENDER_UNIT * x), this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
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
            texture = this.resources.getColor(toRender.getColor());
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
}
