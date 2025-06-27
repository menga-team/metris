package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class MenuScreen implements Screen {

    @Getter
    private MetrisGuiGame game;

    @Getter
    @Setter
    private ArrayList<Widget> widgets = new ArrayList<>();

    public int focusedWidget = 0;

    BitmapFont font = new BitmapFont();

    @Setter
    @Getter
    private int yPadding = 32;

    private final Camera camera = new OrthographicCamera(800, 800);
    private final Viewport viewport = new ExtendViewport(800, 800, camera);
    private Stage stage;

    private final ShapeRenderer lineShapeRenderer = new ShapeRenderer();
    private final ShapeRenderer filledShapeRenderer = new ShapeRenderer();

    @Getter
    Sound titleMusic;

    @Getter
    long titleMusicId;


    public MenuScreen(MetrisGuiGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        MenuInputHandler menuInputHandler = new MenuInputHandler(this);
        lineShapeRenderer.setAutoShapeType(true);
        filledShapeRenderer.setAutoShapeType(true);
        this.stage = new Stage(this.viewport, this.game.getBatch());
        Gdx.input.setInputProcessor(menuInputHandler);
        this.font = new BitmapFont();

        // Setup music
        // TODO: Fix long starting/loading time -> compress the music file ig?
        this.titleMusic = Gdx.audio.newSound(Gdx.files.internal("titleMusic.mp3"));
        this.titleMusicId = this.titleMusic.play(1f);
        this.titleMusic.setLooping(this.getTitleMusicId(), true);
        this.titleMusic.setPitch(this.getTitleMusicId(), 1f);
    }

    public void setWidgets(ArrayList<Widget> widgets) {
        this.widgets = widgets;
        for (Widget widget : this.widgets) {
            widget.setMenuScreen(this);
            widget.show();
        }
        if (!this.widgets.isEmpty()) {
            this.focusedWidget = 0;
        } else {
            this.focusedWidget = -1; // No widgets available
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.getBatch().setProjectionMatrix(this.camera.combined);
        this.game.getBatch().begin();
        lineShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        filledShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.renderWidgets(this.game.getBatch());

        lineShapeRenderer.end();
        filledShapeRenderer.end();
        this.game.getBatch().end();

        this.stage.act(delta);
        this.stage.draw();
    }

    void renderWidgets(Batch batch) {
        int current_y = Gdx.graphics.getHeight();
        for (Widget widget : this.widgets) {
            current_y -= this.yPadding;
            current_y -= widget.getHeight();
            int x = Gdx.graphics.getWidth() / 2 - widget.getWidth() / 2;

            widget.setPosition(x, current_y);
            widget.render(batch, lineShapeRenderer, filledShapeRenderer);
        }
        // FIXME: Vandini....
        font.draw(batch, "Metris", -100, -100); // For some reason the last draw text is not rendered, so we add a dummy text here
    }

    @Override
    public void resize(int i, int i1) {

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
        lineShapeRenderer.dispose();
        filledShapeRenderer.dispose();
    }

    public void nextFocussedWidget() {
        this.focusedWidget++;
        if (this.focusedWidget >= this.widgets.size()) {
            this.focusedWidget = 0;
        }
       Metris.getLogger().debug("Next focused: " + this.focusedWidget);
    }

    public void previousFocussedWidget() {
        this.focusedWidget--;
        if (this.focusedWidget < 0) {
            this.focusedWidget = widgets.size() - 1;
        }
       Metris.getLogger().debug("Previous focused: " + this.focusedWidget);
    }

    public void enterFocussedWidget() {
        if (this.focusedWidget >= 0 && this.focusedWidget < this.widgets.size()) {
            widgets.get(this.focusedWidget).click();
        }
    }

    public void setFocussedWidget(int j) {
        if (j >= 0 && j < this.widgets.size()) {
            this.focusedWidget = j;
        } else {
            Metris.getLogger().warn("Tried to set focussed widget to {}, but only {} widgets are available.", j, widgets.size());
        }
       Metris.getLogger().debug("Focused: " + this.focusedWidget);
    }
}
