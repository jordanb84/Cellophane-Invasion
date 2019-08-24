package com.djam2.game.tile;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.map.Map;

public class Tile {

    private TileType tileType;

    public Tile(TileType tileType) {
        this.tileType = tileType;
    }

    public void render(Map parentMap, SpriteBatch batch, OrthographicCamera camera, Vector2 position, TileData tileData) {
        this.getSprite().setPosition(position.x, position.y);
        this.getSprite().draw(batch);
    }

    public void update(Map parentMap, OrthographicCamera camera, Vector2 position, TileData tileData) {
        tileData.updatePhysicsBody(parentMap.getPhysicsWorld(), position, this.tileType);
    }

    public Rectangle getBody(Vector2 position) {
        return new Rectangle(position.x, position.y, TileType.Air.SPRITE.getWidth(), TileType.Air.SPRITE.getHeight());
    }

    private Sprite getSprite() {
        return this.tileType.SPRITE;
    }

}