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
        for (Map.Entry<Integer, Integer> entry : this.held.entrySet()) {
            int kc = entry.getKey();
            int timer = entry.getValue();
            if (timer >= 300) {
                if (lastTrigger.getOrDefault(kc, 0) >= 100) {
                    lastTrigger.merge(kc, -100, Integer::sum);
                    this.handleKeycode(kc);
                }
                this.lastTrigger.merge(kc, (int) delta, Integer::sum);
            }
            entry.setValue(timer + (int) delta);
        }
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
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
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
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void handleKeycode(int keycode) {
        switch (keycode) {
        case Input.Keys.RIGHT -> this.getGame().moveRight();
        case Input.Keys.LEFT -> this.getGame().moveLeft();
        case Input.Keys.DOWN -> this.getGame().moveDown();
        case Input.Keys.SPACE -> this.getGame().hardDrop();
        case Input.Keys.X -> this.getGame().rotateCW();
        case Input.Keys.Z -> this.getGame().rotateCCW();
        }
    }
}
