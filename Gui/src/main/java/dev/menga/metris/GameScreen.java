package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.menga.metris.utils.Vec2i;

public class GameScreen implements Screen {

    private MetrisGuiGame game;

    public GameScreen(MetrisGuiGame game) {
        this.game = game;
    }

    private Camera camera = new OrthographicCamera(800, 800);
    private Viewport viewport = new ExtendViewport(800, 800, camera);
    private InputHandler inputHandler;
    private RenderableGame metris;
    private Stage stage;

    @Override
    public void show() {
        this.metris = new RenderableGame(this.game.resources, Vec2i.of(0, 0), Vec2i.of(386, 160));
        this.inputHandler = new InputHandler(this.metris);
        this.stage = new Stage(this.viewport, this.game.batch);
        Gdx.input.setInputProcessor(this.inputHandler);
    }

    @Override
    public void render(float delta) {
        this.metris.update(delta);

        Gdx.gl.glClearColor(30 / 255f, 30 / 255f, 30 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.batch.begin();
        this.metris.render(this.game.batch);
        this.game.batch.end();

        this.stage.act(delta);
        this.stage.draw();
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
}
