package com.djam2.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Assets {

    private static final Assets instance = new Assets();

    private AssetManager assetManager = new AssetManager();

    public Assets() {
        this.loadTexture("tile/air.png");
        this.loadTexture("tile/ground.png");
        this.loadTexture("tile/flowers.png");
        this.loadTexture("tile/crate.png");
        this.loadTexture("tile/overlay.png");

        this.loadTexture("entity/player_up0.png");
        this.loadTexture("entity/player_up1.png");
        this.loadTexture("entity/player_down0.png");
        this.loadTexture("entity/player_down1.png");
        this.loadTexture("entity/player_right0.png");
        this.loadTexture("entity/player_right1.png");
        this.loadTexture("entity/player_left0.png");
        this.loadTexture("entity/player_left1.png");
        this.loadTexture("entity/bullet.png");
        this.loadTexture("entity/explosion0.png");
        this.loadTexture("entity/explosion1.png");
        this.loadTexture("entity/explosion2.png");
        this.loadTexture("entity/explosion3.png");
        this.loadTexture("entity/explosion4.png");
        this.loadTexture("entity/explosion5.png");
        this.loadTexture("entity/explosion6.png");
        this.loadTexture("entity/explosion7.png");
        this.loadTexture("entity/explosion8.png");
        this.loadTexture("entity/explosion9.png");
        this.loadTexture("entity/explosion10.png");
        this.loadTexture("entity/explosion11.png");
        this.loadTexture("entity/bat.png");

        this.load();
    }

    public void load() {
        this.assetManager.finishLoading();
    }

    public void loadTexture(String path) {
        this.assetManager.load(path, Texture.class);
    }

    public Texture getTexture(String path) {
        return this.assetManager.get(path, Texture.class);
    }

    public Sprite getSprite(String path) {
        return new Sprite(this.getTexture(path));
    }

    public static Assets getInstance() {
        return instance;
    }

}