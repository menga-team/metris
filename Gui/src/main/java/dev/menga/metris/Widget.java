package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

public class Widget {

    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    @Getter
    @Setter
    private int width = 600;

    @Getter
    @Setter
    private int height = 50;

    @Getter
    @Setter
    private MenuScreen menuScreen;

    Widget(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
    }

    public void click() {
        // Default implementation does nothing
        // Override in subclasses if needed
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void show() {
        // Default implementation does nothing
        // Override in subclasses if needed
    }

    public void render(Batch batch, ShapeRenderer lineShapeRenderer, ShapeRenderer filledShapeRenderer){
        // Default implementation does nothing
        // Override in subclasses to provide rendering logic
    };

}
