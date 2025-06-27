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
import java.util.function.Consumer;

public class MetrisGuiGame extends Game {

    @Getter
    private Batch batch;
    @Getter
    private Resources resources;
    private MenuScreen titleMenu;
    private ArrayList<Widget> menuWidgetList;

    public Options options = new Options();
//    private MenuScreen settingsMenu;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.resources = new Resources();
        this.titleMenu = new MenuScreen(this);
//        this.options = new Options();
//        this.settingsMenu = new MenuScreen(this);
        this.setScreen(this.titleMenu);

        options = Options.loadGameState();

        ArrayList<Widget> settingsWidgetList = new ArrayList() {
            {
                add(new CheckBox(titleMenu, "Drop places blocks on the ground", options.isSpacePlacesBlocks (), (Boolean checked) -> {
                    Metris.getLogger().info("Toggled dropping blocks on the ground");
                    options.setSpacePlacesBlocks(checked);
                    Options.saveGameState(options);
                }));
//                add(new MenuButton(titleMenu, "Back to Main Menu", () -> {
//                    Metris.getLogger().info("Returning to main menu...");
//                    titleMenu.setWidgets(menuWidgetList);
//                }));
            }
        };

//        this.settingsMenu.setWidgets(settingsWidgetList);

        ArrayList<Widget> menuWidgetList = new ArrayList() {
            {
                add(new ImageWidget(titleMenu, new Texture(Gdx.files.internal("menutitle.png"))));
                add(new MenuButton(titleMenu, "Start Game", () -> {
                    Metris.getLogger().info("Starting game...");
                    titleMenu.getTitleMusic().stop();
                    titleMenu.getGame().setScreen(new SingleplayerGameScreen(titleMenu.getGame()));
                }));
                add(new MenuButton(titleMenu, "Multiplayer", () -> {
                    Metris.getLogger().info("Opening multiplayer...");
                }));
                add(new MenuButton(titleMenu, "Settings", () -> {
                    Metris.getLogger().info("Opening settings...");
                    titleMenu.setWidgets(settingsWidgetList);
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

        settingsWidgetList.addLast(new MenuButton(titleMenu, "Back to Main Menu", () -> {
            Metris.getLogger().info("Returning to main menu...");
            titleMenu.setWidgets(menuWidgetList);
        }));

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
