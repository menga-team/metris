package dev.menga.metris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

// TODO: Cleanup
public class Resources implements Disposable {

    private final AssetManager assetManager = new AssetManager();
    private final TextureAtlas tileAtlas;
    private final TextureAtlas ghostAtlas;
    private final TextureAtlas backgroundAtlas;

    @Getter
    final private TextureRegion[][] tileTextures;
    final private TextureRegion[][] ghostTextures;
    final private TextureRegion backgroundTexture;

    Resources() {
        this.assetManager.load("textures.atlas", TextureAtlas.class);
        this.assetManager.load("tiles.atlas", TextureAtlas.class);
        this.assetManager.load("ghosts.atlas", TextureAtlas.class);
        this.assetManager.load("background.atlas", TextureAtlas.class);
        this.assetManager.finishLoading();

        this.tileAtlas = assetManager.get("tiles.atlas", TextureAtlas.class);
        this.ghostAtlas = assetManager.get("ghosts.atlas", TextureAtlas.class);
        this.backgroundAtlas = assetManager.get("background.atlas", TextureAtlas.class);

        this.tileTextures = this.tileAtlas.findRegion("everything").split(32, 32);
        this.ghostTextures = this.ghostAtlas.findRegion("everything").split(32, 32);
        this.backgroundTexture = this.backgroundAtlas.findRegion("everything").split(320, 640)[0][0];
    }

    public TextureRegion getTile(Color color, int index) {
        return this.tileTextures[color.getId()-2][index];
    }

    public TextureRegion getGhost(Color color, int index) {
        return this.ghostTextures[color.getId()-2][index];
    }

    public TextureRegion getBackground() {
        return this.backgroundTexture;
    }

    public BitmapFont getDefaultFont() {
        return new BitmapFont();
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
        this.tileAtlas.dispose();
        this.ghostAtlas.dispose();
        this.backgroundAtlas.dispose();
    }
}
