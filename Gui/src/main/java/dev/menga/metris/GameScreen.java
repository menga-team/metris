package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import lombok.Setter;

public abstract class GameScreen implements Screen {

    private float deltaAccumulator = 0f;


    @Getter
    private MetrisGuiGame gui;

    @Getter
    protected Camera camera = new OrthographicCamera(800, 800);

    @Getter
    protected Viewport viewport = new ExtendViewport(800, 800, camera);

    @Getter
    protected ActiveInputProcessor inputProcessor;

    @Getter
    protected Stage stage;

    protected GameScreen(MetrisGuiGame gui, ActiveInputProcessor inputProcessor) {
        this.gui = gui;
        this.inputProcessor = inputProcessor;
    }

    @Override
    public void show() {
        this.stage = new Stage(this.getViewport(), this.getGui().getBatch());
        Gdx.input.setInputProcessor(this.getInputProcessor());
        this.present();
    }

    public abstract void present();

    @Override
    public void render(float delta) {
        long deltaInMs = (long) (delta * 1000f);
        this.deltaAccumulator += delta % (1 / 1000f);
        if (this.deltaAccumulator >= (1 / 1000f)) {
            deltaInMs += (long) (deltaAccumulator / (1 / 1000f));
            this.deltaAccumulator %= (1 / 1000f);
        }
        
        this.inputProcessor.update(deltaInMs);
        this.compute(deltaInMs);

        this.getGui().getBatch().setProjectionMatrix(this.camera.combined);
        this.getGui().getBatch().begin();
        this.render(deltaInMs);
        this.getGui().getBatch().end();

        this.stage.act(delta);
        this.stage.draw();
    }
    
    public abstract void render(long delta);
    public abstract void compute(long delta);

    @Override
    public void dispose() {
        this.getStage().dispose();
        this.cleanup();
    }

    public abstract void cleanup();
}
