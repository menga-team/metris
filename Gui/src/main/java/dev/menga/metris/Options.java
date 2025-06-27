package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

public class Options implements Serializable {
    @Getter
    @Setter
    private boolean SpacePlacesBlocks = true;
    @Getter
    @Setter
    private float musicVolume = 1;
    @Getter
    @Setter
    private float soundVolume = 1;

    public Options(boolean SpacePlacesBlocks, float musicVolume, float soundVolume) {
        this.SpacePlacesBlocks = SpacePlacesBlocks;
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;
    }

    public Options() {}

    public static boolean saveGameState(Options gameState) {
        return saveGameState(gameState, Gdx.files.local("gameState.dat"));
    }

    public static boolean saveGameState(Options gameState, FileHandle filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.file()))) {
            oos.writeObject(gameState);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Options loadGameState() {
        return loadGameState(Gdx.files.local("gameState.dat"));
    }

    public static Options loadGameState(FileHandle filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.file()))) {
            return (Options) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions, such as file not found or class not found.
            e.printStackTrace();
            return new Options();
        }
    }
}