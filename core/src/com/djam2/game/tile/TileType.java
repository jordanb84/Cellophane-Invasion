package com.djam2.game.tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.djam2.game.assets.Assets;
import com.djam2.game.tile.impl.BasicTile;

public enum TileType {
    Air, Ground, Flowers, Crate(true), Start(), End()
    ;

    TileType(String spritePath, boolean solid) {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + spritePath));
        this.SOLID = solid;
        this.TILE = new BasicTile(this);
    }

    TileType() {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + this.name().toLowerCase() + ".png"));
        this.SOLID = false;
        this.TILE = new BasicTile(this);
    }

    TileType(boolean solid) {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + this.name().toLowerCase() + ".png"));
        this.SOLID = solid;
        this.TILE = new BasicTile(this);
    }

    public final Sprite SPRITE;
    public final boolean SOLID;
    public final Tile TILE;

}