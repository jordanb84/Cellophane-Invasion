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
        this.loadTexture("tile/start.png");
        this.loadTexture("tile/end.png");
        this.loadTexture("tile/road0.png");
        this.loadTexture("tile/road1.png");
        this.loadTexture("tile/road2.png");
        this.loadTexture("tile/road3.png");
        this.loadTexture("tile/road4.png");
        this.loadTexture("tile/road5.png");
        this.loadTexture("tile/road6.png");
        this.loadTexture("tile/road7.png");
        this.loadTexture("tile/road8.png");
        this.loadTexture("tile/road9.png");
        this.loadTexture("tile/road10.png");
        this.loadTexture("tile/road11.png");
        this.loadTexture("tile/road12.png");
        this.loadTexture("tile/road13.png");
        this.loadTexture("tile/road14.png");
        this.loadTexture("tile/road15.png");
        this.loadTexture("tile/road16.png");
        this.loadTexture("tile/road17.png");
        this.loadTexture("tile/road18.png");
        this.loadTexture("tile/road19.png");
        this.loadTexture("tile/road20.png");
        this.loadTexture("tile/road21.png");
        this.loadTexture("tile/road22.png");
        this.loadTexture("tile/road23.png");
        this.loadTexture("tile/road24.png");
        this.loadTexture("tile/road25.png");
        this.loadTexture("tile/road26.png");
        this.loadTexture("tile/road27.png");
        this.loadTexture("tile/road28.png");
        this.loadTexture("tile/road29.png");
        this.loadTexture("tile/road29.png");
        this.loadTexture("tile/road30.png");
        this.loadTexture("tile/road31.png");
        this.loadTexture("tile/road32.png");
        this.loadTexture("tile/road33.png");
        this.loadTexture("tile/road34.png");
        this.loadTexture("tile/road35.png");
        this.loadTexture("tile/road36.png");
        this.loadTexture("tile/road37.png");
        this.loadTexture("tile/road38.png");
        this.loadTexture("tile/road39.png");
        this.loadTexture("tile/road40.png");
        this.loadTexture("tile/road41.png");
        this.loadTexture("tile/road42.png");
        this.loadTexture("tile/road43.png");
        this.loadTexture("tile/road44.png");
        this.loadTexture("tile/road45.png");
        this.loadTexture("tile/road46.png");
        this.loadTexture("tile/road47.png");
        this.loadTexture("tile/road48.png");
        this.loadTexture("tile/road49.png");
        this.loadTexture("tile/road49.png");
        this.loadTexture("tile/road50.png");
        this.loadTexture("tile/road51.png");
        this.loadTexture("tile/road52.png");
        this.loadTexture("tile/wall0.png");
        this.loadTexture("tile/wall1.png");
        this.loadTexture("tile/wall2.png");
        this.loadTexture("tile/wall3.png");
        this.loadTexture("tile/wall4.png");
        this.loadTexture("tile/wall5.png");
        this.loadTexture("tile/wall6.png");
        this.loadTexture("tile/stonewall0.png");
        this.loadTexture("tile/stonewall1.png");
        this.loadTexture("tile/stonewall2.png");
        this.loadTexture("tile/stonewall3.png");
        this.loadTexture("tile/stonewall4.png");
        this.loadTexture("tile/stonewall5.png");
        this.loadTexture("tile/stonewall6.png");
        this.loadTexture("tile/stonewall7.png");
        this.loadTexture("tile/stonewall8.png");
        this.loadTexture("tile/grave0.png");
        this.loadTexture("tile/grave1.png");
        this.loadTexture("tile/gas0.png");
        this.loadTexture("tile/gas1.png");
        this.loadTexture("tile/gas2.png");
        this.loadTexture("tile/fire.png");
        this.loadTexture("tile/pathblock.png");
        this.loadTexture("tile/blue0.png");
        this.loadTexture("tile/blue1.png");
        this.loadTexture("tile/blue2.png");
        this.loadTexture("tile/blue3.png");
        this.loadTexture("tile/blue4.png");
        this.loadTexture("tile/blue5.png");
        this.loadTexture("tile/blue6.png");
        this.loadTexture("tile/blue7.png");
        this.loadTexture("tile/blue8.png");
        this.loadTexture("tile/blue9.png");
        this.loadTexture("tile/blue10.png");
        this.loadTexture("tile/empty0.png");
        this.loadTexture("tile/empty1.png");
        this.loadTexture("tile/empty2.png");
        this.loadTexture("tile/empty3.png");
        this.loadTexture("tile/empty4.png");
        this.loadTexture("tile/empty5.png");
        this.loadTexture("tile/empty6.png");
        this.loadTexture("tile/empty7.png");
        this.loadTexture("tile/empty8.png");
        this.loadTexture("tile/empty9.png");
        this.loadTexture("tile/fence0.png");
        this.loadTexture("tile/fence1.png");
        this.loadTexture("tile/fence2.png");
        this.loadTexture("tile/fence3.png");
        this.loadTexture("tile/fence4.png");
        this.loadTexture("tile/fence5.png");
        this.loadTexture("tile/fence6.png");
        this.loadTexture("tile/fence7.png");
        this.loadTexture("tile/tire.png");
        this.loadTexture("tile/soda.png");
        this.loadTexture("tile/sign.png");
        this.loadTexture("tile/barrel.png");
        this.loadTexture("tile/skulls0.png");
        this.loadTexture("tile/skulls1.png");
        this.loadTexture("tile/box.png");
        this.loadTexture("tile/car0.png");
        this.loadTexture("tile/car1.png");
        this.loadTexture("tile/car2.png");
        this.loadTexture("tile/car3.png");
        this.loadTexture("tile/shrub.png");
        this.loadTexture("tile/barrel1.png");
        this.loadTexture("tile/govern0.png");
        this.loadTexture("tile/govern1.png");
        this.loadTexture("tile/govern2.png");
        this.loadTexture("tile/govern3.png");
        this.loadTexture("tile/green0.png");
        this.loadTexture("tile/green1.png");
        this.loadTexture("tile/green2.png");
        this.loadTexture("tile/green3.png");
        this.loadTexture("tile/green4.png");
        this.loadTexture("tile/green5.png");
        this.loadTexture("tile/green6.png");
        this.loadTexture("tile/green7.png");
        this.loadTexture("tile/green8.png");
        this.loadTexture("tile/green9.png");
        this.loadTexture("tile/green10.png");
        this.loadTexture("tile/green11.png");
        this.loadTexture("tile/green12.png");
        this.loadTexture("tile/green13.png");
        this.loadTexture("tile/green14.png");

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
        this.loadTexture("entity/bat_blood0.png");
        this.loadTexture("entity/zombie.png");
        this.loadTexture("entity/zombie_blood0.png");

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