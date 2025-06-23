package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import lombok.Getter;

import java.awt.*;

public class MenuInputHandler implements InputProcessor {


    @Getter
    private MenuScreen menu;

    MenuInputHandler(MenuScreen menu) {
        this.menu = menu;
    }

    @Override
    public boolean keyDown(int keycode) {
        this.handleKeycode(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // Invert y coordinate for GUI
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        Metris.getLogger().debug("Left click at " + screenX + ", " + screenY);
        for (Widget widget : this.menu.getWidgets()) {
            if (screenX >= widget.getX() && screenX <= widget.getX() + widget.getWidth() && screenY >= widget.getY() && screenY <= widget.getY() + widget.getHeight()) {
                widget.click();
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        y = Gdx.graphics.getHeight() - y; // Invert y coordinate for GUI
        for (int j = 0; j < this.menu.getWidgets().size(); j++) {
            Widget widget = this.menu.getWidgets().get(j);
            if (x >= widget.getX() && x <= widget.getX() + widget.getWidth() && y >= widget.getY() && y <= widget.getY() + widget.getHeight()){
                this.menu.setFocussedWidget(j);
            }
        }
        return true;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void handleKeycode(int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> this.getMenu().previousFocussedWidget();
            case Input.Keys.DOWN -> this.getMenu().nextFocussedWidget();
            case Input.Keys.ENTER -> this.getMenu().enterFocussedWidget();
            case Input.Keys.SPACE -> this.getMenu().enterFocussedWidget();
        }
    }
}
