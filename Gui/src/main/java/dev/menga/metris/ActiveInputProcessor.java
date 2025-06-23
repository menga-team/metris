package dev.menga.metris;

import com.badlogic.gdx.InputProcessor;

public interface ActiveInputProcessor extends InputProcessor {

    void update(long delta);

}
