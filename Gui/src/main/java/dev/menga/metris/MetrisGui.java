package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class MetrisGui {
    public static void main(String[] args) {
        Metris.getLogger().info("GUI Started.");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 800);
        config.setResizable(false);
        new Lwjgl3Application(new MetrisGuiGame(), config);
    }
}
