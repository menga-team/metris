package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

public class Resources implements Disposable {

    private final AssetManager assetManager = new AssetManager();
    private final TextureAtlas atlas;

    @Getter
    final private TextureRegion unit;
    final private TextureRegion[] colorTextures;
    final private TextureRegion[] ghostColorTextures;


    Resources() {
        this.assetManager.load("textures.atlas", TextureAtlas.class);
        this.assetManager.finishLoading();
        this.atlas = assetManager.get("textures.atlas", TextureAtlas.class);
        this.colorTextures = this.atlas.findRegion("colors").split(32, 32)[0];
        this.ghostColorTextures = this.atlas.findRegion("colors").split(32, 32)[1];
        this.unit = this.atlas.findRegion("unit");
    }

    public TextureRegion getTexture(String texture) {
        return atlas.findRegion(texture);
    }

    public TextureRegion getColor(Color color) {
        return this.colorTextures[color.getId()];
    }

    public TextureRegion getGhostColor(Color color) {
        return this.ghostColorTextures[color.getId()];
    }

    public BitmapFont getDefaultFont() {
        return new BitmapFont();
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
        this.atlas.dispose();
    }
}
