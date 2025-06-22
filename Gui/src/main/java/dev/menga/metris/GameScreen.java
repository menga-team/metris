package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.menga.metris.utils.Vec2i;

import java.util.Arrays;
import java.util.Random;

public class GameScreen implements Screen {

    private MetrisGuiGame game;

    private float deltaAccumulator = 0f;

    public GameScreen(MetrisGuiGame game) {
        this.game = game;
    }

    private Camera camera = new OrthographicCamera(800, 800);
    private Viewport viewport = new ExtendViewport(800, 800, camera);
    private InputHandler inputHandler;
    private RenderableGame metris;
    private Stage stage;
    private final Color[][] randomFieldColors = new Color[Field.MAX_VISIBLE_HEIGHT][Field.MAX_WIDTH];

    @Override
    public void show() {
        this.metris = new RenderableGame(this.game.resources, Vec2i.of(160, 0), Vec2i.of(546, 160), Vec2i.of(60, 560));
        this.inputHandler = new InputHandler(this.metris);
        this.stage = new Stage(this.viewport, this.game.batch);
        Gdx.input.setInputProcessor(this.inputHandler);

        // Generate random field colors for game over
        Random rand = new Random();
        Color[] visibleColors = Arrays.stream(Color.values()).filter(c -> c.getId() >= 2).toArray(Color[]::new);
        for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
            for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                this.randomFieldColors[y][x] = visibleColors[rand.nextInt(visibleColors.length)];
            }
        }
    }

    @Override
    public void render(float delta) {
        long deltaInMs = (long) (delta * 1000f);
        this.deltaAccumulator += delta % (1 / 1000f);
        if (this.deltaAccumulator >= (1 / 1000f)) {
            deltaInMs += (long) (deltaAccumulator / (1 / 1000f));
            this.deltaAccumulator %= (1 / 1000f);
        }
        this.inputHandler.update(deltaInMs);
        this.metris.update(deltaInMs);

        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.batch.begin();
        this.metris.render(this.game.batch);
        this.game.batch.end();

        this.stage.act(delta);
        this.stage.draw();

        if (this.metris.isGameOver()) {
            this.gameOver();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void gameOver() {
        // show a game over text on the existing screen
        LabelStyle labelStyle = new LabelStyle(this.game.resources.getDefaultFont(),
                                               com.badlogic.gdx.graphics.Color.RED);

        Label gameOverLabel = new Label("Game Over", labelStyle);
        gameOverLabel.setFontScale(3f);

        float x = (this.stage.getWidth() - gameOverLabel.getWidth() * gameOverLabel.getFontScaleX()) / 2f;
        float y = (this.stage.getHeight() - gameOverLabel.getHeight() * gameOverLabel.getFontScaleY()) / 2f;
        gameOverLabel.setPosition(x, y);

        this.stage.addActor(gameOverLabel);

        if (this.randomFieldColors != null) {
            for (int i = 0; i < Field.MAX_VISIBLE_HEIGHT; ++i) {
                System.arraycopy(this.randomFieldColors[i], 0, this.metris.field.getColors()[i], 0, Field.MAX_WIDTH);
            }
        }
    }
}
