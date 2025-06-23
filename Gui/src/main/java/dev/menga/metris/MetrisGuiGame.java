package dev.menga.metris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;

import java.util.ArrayList;

public class MetrisGuiGame extends Game {

    @Getter
    private Batch batch;
    @Getter
    private Resources resources;
    private MenuScreen titleMenu;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.resources = new Resources();
        this.titleMenu = new MenuScreen(this);
        this.setScreen(this.titleMenu);

        ArrayList<Widget> menuWidgetList = new ArrayList() {
            {
                add(new ImageWidget(titleMenu, new Texture(Gdx.files.internal("menutitle.png"))));
                add(new MenuButton(titleMenu, "Start Game", () -> {
                    Metris.getLogger().info("Starting game...");
                    titleMenu.getTitleMusic().stop();
                    titleMenu.getGame().setScreen(new SingleplayerGameScreen(titleMenu.getGame()));
                }));
                add(new MenuButton(titleMenu, "Multiplayer", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(titleMenu, "Scores", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(titleMenu, "Help", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(titleMenu, "About", () -> {
                    Metris.getLogger().info("Opening settings...");
                }));
                add(new MenuButton(titleMenu, "Exit", () -> {
                    Metris.getLogger().info("Exiting game...");
                    Gdx.app.exit();
                }));
            }
        };
        this.titleMenu.setWidgets(menuWidgetList);

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
