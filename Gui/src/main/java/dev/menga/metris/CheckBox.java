package dev.menga.metris;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class CheckBox extends MenuButton{

    @Getter
    @Setter
    private boolean checked = false;

    private Consumer<Boolean> action;

    public CheckBox(MenuScreen menuScreen, String label, boolean checked, Consumer<Boolean> action) {
        super(menuScreen, label, () -> {});
        this.action = action;
        this.checked = checked;
    }

    public CheckBox(MenuScreen menuScreen, String label) {
        super(menuScreen, label);
    }

    public CheckBox(MenuScreen menuScreen) {
        super(menuScreen);
    }

    @Override
    public void click() {
        this.checked = !this.checked;
        if (this.action != null) {
            this.action.accept(this.checked);
        }
    }

    @Override
    public void render(Batch batch, ShapeRenderer lineShapeRenderer, ShapeRenderer filledShapeRenderer) {
        super.render(batch, lineShapeRenderer, filledShapeRenderer);
        // Render the checkbox state
        lineShapeRenderer.rect(getX() + 10, getY() + 10, 30, 30);
        if (checked) {
            filledShapeRenderer.rect(getX() + 15, getY() + 15, 20, 20);
        }
    }
}
