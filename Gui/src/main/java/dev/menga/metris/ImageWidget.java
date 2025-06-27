package dev.menga.metris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Getter;
import lombok.Setter;

public class ImageWidget extends  Widget {

    @Getter
    @Setter
    private Texture texture;

    ImageWidget(MenuScreen menuScreen, Texture texture) {
        super(menuScreen);
        this.texture = texture;
        this.setHeight(texture.getHeight());
        this.setWidth(texture.getWidth());
    }

    public void render(Batch batch, ShapeRenderer lineShapeRenderer, ShapeRenderer filledShapeRenderer) {
        if (texture != null) {
            batch.draw(texture, getX(), getY());
        }
    }
}
