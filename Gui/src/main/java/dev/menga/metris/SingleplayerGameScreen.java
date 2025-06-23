package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import dev.menga.metris.utils.Vec2i;
import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

public class SingleplayerGameScreen extends GameScreen {

    private RenderableGame metris;

    public SingleplayerGameScreen(MetrisGuiGame game) {
        super(game, new GameInputHandler());
        this.metris = new RenderableGame(
                this.getGui().getResources(),
                Vec2i.of(160, 0),
                Vec2i.of(546, 160),
                Vec2i.of(60, 560)
        );
        ((GameInputHandler) this.getInputProcessor()).setGame(this.metris);
    }

    private final Color[][] randomFieldColors = new Color[Field.MAX_VISIBLE_HEIGHT][Field.MAX_WIDTH];
    private boolean gameOverHandled = false;

    @Getter
    Sound mainMusic;
    @Getter
    Sound gameOverSound;
    @Getter
    long bgmId;

    // TODO: Should be in RenderableGame with configurable offsets.
    private Label scoreLabel;
    private Label levelLabel;

    @Override
    public void present() {
        // Setup music
        this.mainMusic = Gdx.audio.newSound(Gdx.files.internal("simabito.ogg"));
        this.bgmId = this.mainMusic.play(1f);
        this.mainMusic.setLooping(this.getBgmId(), true);
        this.mainMusic.setPitch(this.getBgmId(), 1f);

        this.gameOverSound = Gdx.audio.newSound(Gdx.files.internal("death.mp3"));

        // Generate random field colors for game over
        Random rand = new Random();
        Color[] visibleColors = Arrays.stream(Color.values()).filter(c -> c.getId() >= 2).toArray(Color[]::new);
        for (int y = 0; y < Field.MAX_VISIBLE_HEIGHT; ++y) {
            for (int x = 0; x < Field.MAX_WIDTH; ++x) {
                this.randomFieldColors[y][x] = visibleColors[rand.nextInt(visibleColors.length)];
            }
        }

        LabelStyle labelStyle = new LabelStyle(this.getGui().getResources().getDefaultFont(), com.badlogic.gdx.graphics.Color.WHITE);
        this.scoreLabel = new Label("Score: 0", labelStyle);
        this.levelLabel = new Label("Level: 0", labelStyle);

        this.scoreLabel.setFontScale(2f);
        this.levelLabel.setFontScale(2f);

        this.scoreLabel.setPosition(20, 750);
        this.levelLabel.setPosition(20, 700);

        this.stage.addActor(this.scoreLabel);
        this.stage.addActor(this.levelLabel);
    }

    @Override
    public void compute(long delta) {
        this.metris.tick(delta);
        final float musicSpeed = (float) Math.sqrt(1000f) / (float) Math.sqrt(this.metris.getGravityStrength());
        this.getMainMusic().setPitch(this.getBgmId(), musicSpeed);
    }

    @Override
    public void render(long delta) {
        Gdx.gl.glClearColor(50 / 255f, 50 / 255f, 50 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.metris.render(this.getGui().getBatch());

        // Update score and level labels
        this.scoreLabel.setText("Score: " + this.metris.getScore());
        this.levelLabel.setText("Level: " + this.metris.getLevel());

        if (this.metris.isGameOver()) {
            if (!gameOverHandled) {
                gameOverHandled = true;
                this.getMainMusic().stop();

                this.bgmId = this.gameOverSound.play(1f);
                this.gameOverSound.setLooping(this.getBgmId(), false);
                this.gameOverSound.setPitch(this.getBgmId(), 1f);
            }
            this.gameOver();
        } else {
            gameOverHandled = false;
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
    public void cleanup() {
        this.mainMusic.dispose();
    }

    public void gameOver() {
        // show a game over text on the existing screen
        LabelStyle labelStyle = new LabelStyle(this.getGui().getResources().getDefaultFont(), com.badlogic.gdx.graphics.Color.RED);

        Label gameOverLabel = new Label("Game Over", labelStyle);
        gameOverLabel.setFontScale(3f);

        float x = (this.stage.getWidth() - gameOverLabel.getWidth() * gameOverLabel.getFontScaleX()) / 2f;
        float y = (this.stage.getHeight() - gameOverLabel.getHeight() * gameOverLabel.getFontScaleY()) / 2f;
        gameOverLabel.setPosition(x, y);

        this.stage.addActor(gameOverLabel);

        for (int i = 0; i < Field.MAX_VISIBLE_HEIGHT; ++i) {
            System.arraycopy(this.randomFieldColors[i], 0, this.metris.field.getColors()[i], 0, Field.MAX_WIDTH);
        }
    }
}
