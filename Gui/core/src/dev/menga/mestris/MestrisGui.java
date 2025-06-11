package dev.menga.mestris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import dev.menga.metris.Field;
import dev.menga.metris.Color;

public class MestrisGui extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ShapeRenderer shapeRenderer;
	Field field;

	static final int CELL_SIZE = 32;
	static final int FIELD_WIDTH = Field.MAX_WIDTH;
	static final int FIELD_HEIGHT = Field.MAX_VISIBLE_HEIGHT;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		shapeRenderer = new ShapeRenderer();
		field = new Field(); // For now, just an empty field
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		// batch.draw(img, 0, 0); // Optionally draw background
		batch.end();

		// Draw Tetris field
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (int x = 0; x < FIELD_WIDTH; x++) {
			for (int y = 0; y < FIELD_HEIGHT; y++) {
				Color color = field.getCell(x, y + (Field.MAX_HEIGHT - FIELD_HEIGHT));
				if (color != Color.VOID) {
					setShapeColor(shapeRenderer, color);
					shapeRenderer.rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				}
			}
		}
		shapeRenderer.end();
	}

	private void setShapeColor(ShapeRenderer renderer, Color color) {
		switch (color) {
			case AQUA -> renderer.setColor(0, 1, 1, 1);
			case YELLOW -> renderer.setColor(1, 1, 0, 1);
			case MAGENTA -> renderer.setColor(1, 0, 1, 1);
			case GREEN -> renderer.setColor(0, 1, 0, 1);
			case RED -> renderer.setColor(1, 0, 0, 1);
			case BLUE -> renderer.setColor(0, 0, 1, 1);
			case ORANGE -> renderer.setColor(1, 0.5f, 0, 1);
			case GARBAGE -> renderer.setColor(0.3f, 0.3f, 0.3f, 1);
			default -> renderer.setColor(0, 0, 0, 0);
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		shapeRenderer.dispose();
	}
}
