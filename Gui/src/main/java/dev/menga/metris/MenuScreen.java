package dev.menga.metris;

import com.badlogic.gdx.Game;
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

    public int focussedWidget = 0;

    BitmapFont font = new BitmapFont();

    @Setter
    @Getter
    private int yPadding = 32;

    private Camera camera = new OrthographicCamera(800, 800);
    private Viewport viewport = new ExtendViewport(800, 800, camera);
    private MenuInputHandler MenuInputHandler;
    private Stage stage;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Getter
    Sound titleMusic;

    @Getter
    long titleMusicId;


    public MenuScreen(MetrisGuiGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.MenuInputHandler = new MenuInputHandler(this);
        shapeRenderer.setAutoShapeType(true);
        this.stage = new Stage(this.viewport, this.game.batch);
        Gdx.input.setInputProcessor(this.MenuInputHandler);
        this.font = new BitmapFont();

//        Setup music
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
        if (this.widgets.size() > 0) {
            this.focussedWidget = 0;
        } else {
            this.focussedWidget = -1; // No widgets available
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.batch.setProjectionMatrix(this.camera.combined);
        this.game.batch.begin();
        shapeRenderer.begin();
        this.renderWidgets(this.game.batch);

//        final float musicSpeed = 1000f / this.metris.getGravityStrength();
//        this.getTitleMusic().setPitch(this.getTitleMusicId(), musicSpeed);


        shapeRenderer.end();
        this.game.batch.end();

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
            widget.render(batch, shapeRenderer);
        }
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
        shapeRenderer.dispose();
    }

    public void nextFocussedWidget() {
        focussedWidget++;
        if (focussedWidget >= widgets.size()) {
            focussedWidget = 0;
        }
//        Metris.getLogger().info("Focussed: " + this.focussedWidget);
    }

    public void previousFocussedWidget() {
        focussedWidget--;
        if (focussedWidget < 0) {
            focussedWidget = widgets.size() - 1;
        }
//        Metris.getLogger().info("Focussed: " + this.focussedWidget);
    }

    public void enterFocussedWidget() {
        if (focussedWidget >= 0 && focussedWidget < widgets.size()) {
            widgets.get(focussedWidget).click();
        }
    }

    public void setFocussedWidget(int j) {
        if (j >= 0 && j < widgets.size()) {
            this.focussedWidget = j;
        } else {
            Metris.getLogger().warn("Tried to set focussed widget to " + j + ", but only " + widgets.size() + " widgets are available.");
        }
//        Metris.getLogger().info("Focussed: " + this.focussedWidget);
    }
}
