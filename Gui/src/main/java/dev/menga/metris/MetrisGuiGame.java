package dev.menga.metris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MetrisGuiGame extends Game {

    Batch batch;
    Resources resources;

    private GameScreen screen;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.resources = new Resources();
        this.screen = new GameScreen(this);
        this.setScreen(this.screen);
    }

    @Override
    public void render() {
        Gdx.graphics.setTitle("METRIS GUI (" + Gdx.graphics.getFramesPerSecond() + " fps)");
        super.render();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.resources.dispose();
        super.dispose();
    }

}
