package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.menga.metris.utils.Vec2i;

import java.util.*;

public class RenderableGame extends Game {

    static final int FIELD_RENDER_UNIT = 32;
    static final int NEXT_TETROMINOS = 4;

    private Resources resources;
    private final Vec2i gridOff;
    private final Vec2i nextOff;


    // TODO: RenderConfig class ?
    public RenderableGame(Resources resources, Vec2i gridOff, Vec2i nextOff) {
        super();
        this.field.colors[19][0] = Color.AQUA;
        this.resources = resources;
        this.gridOff = gridOff;
        this.nextOff = nextOff;
    }

    public void render(Batch batch) {
        // Render field.
        for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
            for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                Color color = this.field.getColors()[y][x];
                batch.draw(this.resources.getUnit(),
                        this.gridOff.getX() + (FIELD_RENDER_UNIT * x),
                        this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
                if (color.isVisible()) {
                    batch.draw(this.resources.getColor(color),
                            this.gridOff.getX() + (FIELD_RENDER_UNIT * x),
                            this.gridOff.getY() + (FIELD_RENDER_UNIT * y));
                }
            }
        }
        // Render upcoming tetrominos.
        Tetromino[] preview = this.getNextTetrominos(NEXT_TETROMINOS);
        for (int i = 0; i < NEXT_TETROMINOS; ++i) {
            for (Vec2i tile : preview[i].getShape().getTiles()) {
                batch.draw(this.resources.getColor(preview[i].getColor()),
                        this.nextOff.getX() + (FIELD_RENDER_UNIT * tile.getX()),
                        this.nextOff.getY() + i * FIELD_RENDER_UNIT * 4 + (FIELD_RENDER_UNIT * tile.getY()));
            }
        }
    }

    public void update(float delta) {

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
