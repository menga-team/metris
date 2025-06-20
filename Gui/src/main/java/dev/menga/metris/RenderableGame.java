package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.menga.metris.utils.Vec2i;

import java.util.Arrays;
import java.util.LinkedList;

public class RenderableGame extends Game {

    static final int FIELD_RENDER_UNIT = 32;

    private Resources resources;
    private Vec2i screenOff;

    public RenderableGame(Resources resources, Vec2i screenOff) {
        super();
        this.field.colors[19][0] = Color.AQUA;
        this.resources = resources;
        this.screenOff = screenOff;
    }

    public void render(Batch batch) {
        for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
            for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                Color color = this.field.getColors()[y][x];
                batch.draw(this.resources.getUnit(),
                        this.screenOff.getX() + (FIELD_RENDER_UNIT * x),
                        this.screenOff.getY() + (FIELD_RENDER_UNIT * y));
                if (color.isVisible()) {
                    batch.draw(this.resources.getColor(color),
                            this.screenOff.getX() + (FIELD_RENDER_UNIT * x),
                            this.screenOff.getY() + (FIELD_RENDER_UNIT * y));
                }
            }
        }
    }

    public void update(float delta) {

    }

    @Override
    public void refillBags() {
        // TODO: Online, Random
        this.bagA = this.bagB;
        this.bagB = new LinkedList<>(Arrays.asList(Tetromino.values()));
    }
}
