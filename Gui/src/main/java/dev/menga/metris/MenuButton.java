package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.Color;

import static java.awt.SystemColor.text;

public class MenuButton extends Widget {

    @Getter
    private String label;
    private GlyphLayout layout;
    private float textWidth;
    private float textHeight;

    @Getter
    @Setter
    private Runnable action;

    private BitmapFont font;

    public MenuButton(MenuScreen menuScreen, String label, Runnable action) {
        super(menuScreen);
        this.setMenuScreen(menuScreen);
        this.label = label;
        this.action = action;
    }

    public void show() {
        font = new BitmapFont();
//        Metris.getLogger().info(label + "button shown");
        this.setLabel(label); // Ensures the label is set and measured
    }

    public void setLabel(String label) {
        this.label = label;
        layout = new GlyphLayout(font, label); // Measures the text
        textWidth = layout.width;
        textHeight = layout.height;
    }

    public boolean isFocussed() {
        return this.getMenuScreen().getWidgets().get(this.getMenuScreen().focussedWidget) == this;
    }

    public void click() {
        if (action != null) {
            action.run();
        }
    }

    public void render(Batch batch, ShapeRenderer shapeRenderer) {

        if (this.isFocussed()) {
            shapeRenderer.setColor(192/255f, 192/255f, 192/255f, 1);
        } else {
            shapeRenderer.setColor(128/255f, 128/255f, 128/255f, 1);
        }
        if (font != null) {
            font.draw(batch, label, this.getX() + (this.getWidth() / 2) - (this.textWidth / 2), this.getY() + (this.getHeight() / 2) + (this.textHeight / 2));
        }
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
    }
}
