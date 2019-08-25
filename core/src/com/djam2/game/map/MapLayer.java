package com.djam2.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.djam2.game.tile.TileData;
import com.djam2.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapLayer {

    private MapDefinition mapDefinition;

    private List<TileType> layerTiles = new ArrayList<TileType>();

    private List<TileData> layerTileData = new ArrayList<TileData>();

    public MapLayer(MapDefinition mapDefinition, List<TileType> layerTiles) {
        this.mapDefinition = mapDefinition;
        this.layerTiles = layerTiles;
        this.createTileData();
    }

    public MapLayer(MapDefinition mapDefinition, TileType fillType) {
        this.mapDefinition = mapDefinition;
        this.fillWithType(fillType);
        this.createTileData();
    }

    public void render(Map parentMap, SpriteBatch batch, OrthographicCamera camera) {
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                int tileIndex = column + row * this.mapDefinition.getMapHeight();

                Vector2 tilePosition = new Vector2(column * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());
                TileData tileData = this.layerTileData.get(tileIndex);

                this.layerTiles.get(tileIndex).TILE.render(parentMap, batch, camera, tilePosition, tileData);
            }
        }
    }

    public void update(Map parentMap, OrthographicCamera camera) {
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                int tileIndex = column + row * this.mapDefinition.getMapHeight();

                Vector2 tilePosition = new Vector2(column * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());
                TileData tileData = this.layerTileData.get(tileIndex);

                this.layerTiles.get(tileIndex).TILE.update(parentMap, camera, tilePosition, tileData);
            }
        }
    }

    public void renderDebugBodies(ShapeRenderer shapeRenderer) {
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                int tileIndex = column + row * this.mapDefinition.getMapHeight();

                if(this.getLayerTiles().get(tileIndex).SOLID) {

                    Vector2 tilePosition = new Vector2(column * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                    shapeRenderer.rect(tilePosition.x, tilePosition.y, this.mapDefinition.getTileWidth(), this.mapDefinition.getTileHeight());
                }
            }
        }
    }

    public boolean collisionAt(Rectangle body) {
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < this.mapDefinition.getMapWidth(); column++) {
                int tileIndex = column + row * this.mapDefinition.getMapHeight();

                Vector2 tilePosition = new Vector2(column * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                Rectangle tileBody = this.layerTiles.get(tileIndex).TILE.getBody(tilePosition);

                if(tileBody.overlaps(body) && this.layerTiles.get(tileIndex).SOLID) {
                    return true;
                }
            }
        }

        return false;
    }

    public void fillWithType(TileType tileType) {
        this.layerTiles.clear();

        for(int filledTiles = 0; filledTiles < this.mapDefinition.getTilesPerLayer(); filledTiles++) {
            this.layerTiles.add(tileType);
        }
    }

    private void createTileData() {
        this.layerTileData.clear();

        for(int tileDataCreated = 0; tileDataCreated < this.mapDefinition.getTilesPerLayer(); tileDataCreated++) {
            this.layerTileData.add(new TileData());
        }
    }

    public List<TileType> getLayerTiles() {
        return this.layerTiles;
    }

    public List<TileData> getLayerTileData() {
        return this.layerTileData;
    }

}
