package dev.menga.metris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class MetrisGuiGame extends Game {

    Batch batch;
    Resources resources;

    private MenuScreen screen;
    private RenderableGame metris;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.resources = new Resources();
        this.screen = new MenuScreen(this);
        this.setScreen(this.screen);

        ArrayList<Widget> menuWidgetList = new ArrayList() {
            {
                add(new ImageWidget(screen, new Texture(Gdx.files.internal("menutitle.png"))));
                add(new MenuButton(screen, "Start Game", () -> {
                    Metris.getLogger().info("Starting game...");

                    // TODO: isch des richtig do?
                    screen.getTitleMusic().stop();
                    //


                    screen.getGame().setScreen(new GameScreen(screen.getGame()));
                }));
                add(new MenuButton(screen, "Multiplayer", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(screen, "Scores", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(screen, "Help", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(screen, "About", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(screen, "Exit", () -> {
                    Metris.getLogger().info("Exiting game...");
                    Gdx.app.exit();
                }));
            }
        };
        this.screen.setWidgets(menuWidgetList);

        Pixmap pixmap = new Pixmap(Gdx.files.internal("cursor.png"));
// Set hotspot to the middle of it (0,0 would be the top-left corner)
        int xHotspot = 15, yHotspot = 15;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        pixmap.dispose(); // We don't need the pixmap anymore
        Gdx.graphics.setCursor(cursor);

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
