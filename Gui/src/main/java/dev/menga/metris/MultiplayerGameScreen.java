package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import dev.menga.metris.utils.Vec2i;
import lombok.Getter;

public class MultiplayerGameScreen extends GameScreen {

    @Getter
    private RenderableGame me;

    @Getter
    private RenderableGame opponent;

    public MultiplayerGameScreen(MetrisGuiGame game) {
        super(game, new GameInputHandler());
        this.me = new RenderableGame(
                this.getGui().getResources(),
                Vec2i.of(160, 0),
                Vec2i.of(546, 160),
                Vec2i.of(60, 560)
        );
        this.opponent = new RenderableGame(
                this.getGui().getResources(),
                Vec2i.of(600+160, 0),
                Vec2i.of(600+546, 160),
                Vec2i.of(600+60, 560)
        );
        ((GameInputHandler) this.getInputProcessor()).setGame(this.getMe());
    }

    @Override
    public void present() {

    }

    @Override
    public void compute(long delta) {
        this.getMe().tick(delta);
        this.getOpponent().tick(delta);
    }

    @Override
    public void render(long delta) {
        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.getMe().render(this.getGui().getBatch());
        this.getOpponent().render(this.getGui().getBatch());
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
    public void cleanup() {

    }
}
