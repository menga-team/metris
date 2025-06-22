package dev.menga.metris;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import lombok.Getter;

public class InputHandler implements InputProcessor {

    @Getter
    private Game game;
    private Map<Integer, Integer> held;
    private Map<Integer, Integer> lastTrigger;

    public InputHandler(Game game) {
        this.held = new HashMap<Integer, Integer>();
        this.lastTrigger = new HashMap<Integer, Integer>();
        this.game = game;
    }

    private boolean isRepeatable(int kc) {
        final Set<Integer> illegal = Set.of(Input.Keys.SPACE, Input.Keys.X, Input.Keys.Z);
        return !illegal.contains(kc);
    }

    public void update(long delta) {
        this.held.forEach(
            (kc, timer) -> {
                if (timer >= 500) {
                    if (lastTrigger.getOrDefault(kc, 0) >= 200) {
                        lastTrigger.merge(kc, -200, Integer::sum);
                        this.handleKeycode(kc);
                    }
                    lastTrigger.merge(kc, (int) delta, Integer::sum);
                }
            }
        );
    }

    @Override
    public boolean keyDown(int keycode) {
        this.handleKeycode(keycode);
        if (this.isRepeatable(keycode)) {
            this.held.put(keycode, 0);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (this.isRepeatable(keycode)) {
            if (this.lastTrigger.containsKey(keycode)) {
                this.lastTrigger.remove(keycode);
            }
            if (this.held.containsKey(keycode)) {
                this.held.remove(keycode);
            } else {
                Metris.getLogger().warn("Key ({}) was apparently depressed but unknown to the game.", keycode);
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void handleKeycode(int keycode) {
        switch (keycode) {
        case Input.Keys.RIGHT -> this.getGame().moveRight();
        case Input.Keys.LEFT -> this.getGame().moveLeft();
        case Input.Keys.DOWN -> this.getGame().moveDown();
        case Input.Keys.SPACE -> this.getGame().hardDrop();
        }
    }
}
