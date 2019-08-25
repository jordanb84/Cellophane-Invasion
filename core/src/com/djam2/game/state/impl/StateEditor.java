package com.djam2.game.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.djam2.game.assets.Assets;
import com.djam2.game.io.MapExporter;
import com.djam2.game.io.MapImporter;
import com.djam2.game.map.Map;
import com.djam2.game.map.MapDefinition;
import com.djam2.game.map.MapLayer;
import com.djam2.game.state.State;
import com.djam2.game.state.StateManager;
import com.djam2.game.tile.Tile;
import com.djam2.game.tile.TileData;
import com.djam2.game.tile.TileType;

import javax.swing.*;
import java.io.File;

public class StateEditor extends State {

    private Map map;

    private TileType placingTile = TileType.Flowers;

    private int editingLayerIndex = 0;

    private Rectangle mouseBody = new Rectangle();

    private boolean overlapping;
    private Rectangle overlappingBody = new Rectangle();

    private Sprite overlaySprite;

    private boolean selectingTile;

    private boolean testing;

    private OrthographicCamera testingCamera;

    public StateEditor(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {
        this.testingCamera = new OrthographicCamera();
        this.testingCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        MapDefinition mapDefinition = new MapDefinition(3, 60, 60, 16, 16);
        //this.map = new Map(mapDefinition, TileType.Ground);
        this.map = MapImporter.getInstance().getMapFromFile(Gdx.files.internal("map/roads25.map"));
        this.map.setStateManager(this.getManager());

        this.overlaySprite = Assets.getInstance().getSprite("tile/overlay.png");
    }

    @Override
    public void reset() {
        this.map = MapImporter.getInstance().getMapFromFile(Gdx.files.internal("map/roads25.map"));
        this.map.setStateManager(this.getManager());
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.map.render(batch, camera);

        if(this.overlapping) {
            this.overlaySprite.setSize(TileType.Air.SPRITE.getWidth(), TileType.Air.SPRITE.getHeight());
            this.overlaySprite.setPosition(this.overlappingBody.getX(), this.overlappingBody.getY());
            this.overlaySprite.setAlpha(0.5f);
            this.overlaySprite.draw(batch);
        }

        if(this.selectingTile) {
            batch.setProjectionMatrix(this.testingCamera.combined);
            this.overlaySprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.overlaySprite.setPosition(0, 0);
            this.overlaySprite.setAlpha(0.2f);
            this.overlaySprite.draw(batch);

            int row = 0;
            int column = 0;

            for(TileType tileType : TileType.values()) {
                Vector2 tileTypePosition = new Vector2(100 + column * this.map.getMapDefinition().getTileWidth(), 100 + row * this.map.getMapDefinition().getTileHeight());

                tileType.SPRITE.setPosition(tileTypePosition.x, tileTypePosition.y);
                tileType.SPRITE.draw(batch);

                Rectangle tileBody = new Rectangle(tileTypePosition.x, tileTypePosition.y, this.map.getMapDefinition().getTileWidth(), this.map.getMapDefinition().getTileHeight());

                Vector3 testingMousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                this.testingCamera.unproject(testingMousePosition);

                Rectangle testingMouseBody = new Rectangle(testingMousePosition.x, testingMousePosition.y, 0, 0);

                if(testingMouseBody.overlaps(tileBody)) {
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        this.selectingTile = false;
                        this.placingTile = tileType;
                    }
                }

                if(column >= 6) {
                    column = 0;
                    row++;
                }

                column++;
            }

            batch.setProjectionMatrix(camera.combined);
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.updateTitle();

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        this.mouseBody.set(mousePosition.x, mousePosition.y, 0, 0);

        if(!this.selectingTile && !this.testing) {
            this.updateAndPlaceTiles(camera);

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                if (this.editingLayerIndex < this.map.getMapDefinition().getMapLayers() - 1) {
                    this.editingLayerIndex++;
                    Gdx.graphics.setTitle("Layer " + this.editingLayerIndex);
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                if (this.editingLayerIndex > 0) {
                    this.editingLayerIndex--;
                    Gdx.graphics.setTitle("Layer " + this.editingLayerIndex);
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                JFileChooser fileChooser = new JFileChooser();

                int selectedOption = fileChooser.showOpenDialog(null);

                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    this.map = MapImporter.getInstance().getMapFromFile(Gdx.files.internal("map/" + selectedFile.getName()));

                    System.out.println("Replaced map");
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                JFileChooser fileChooser = new JFileChooser();

                int selectedOption = fileChooser.showOpenDialog(null);

                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    String fileName = selectedFile.getName();

                    MapExporter.getInstance().exportToFile(this.map, Gdx.files.local("core/assets/map/" + fileName));
                }
            }

            float speed = 100;
            float delta = Gdx.graphics.getDeltaTime();

            if(Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.position.add(0, speed * delta, 0);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.position.add(0, -speed * delta, 0);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.position.add(speed * delta, 0, 0);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.position.add(-speed * delta, 0, 0);
            }

            camera.update();

            MapDefinition mapDefinition = this.map.getMapDefinition();

            int index = 0;
            for(int row = 0; row < mapDefinition.getMapHeight(); row++) {
                for(int column = 0; column < mapDefinition.getMapWidth(); column++) {
                    TileType tileType = this.map.getMapLayer(this.editingLayerIndex).getLayerTiles().get(index);

                    Vector2 position = new Vector2(column * mapDefinition.getTileWidth(), row * mapDefinition.getTileHeight());

                    if(this.mouseBody.overlaps(tileType.TILE.getBody(position))) {
                        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                            this.placingTile = tileType;
                        }
                    }

                    index++;
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            this.selectingTile = !this.selectingTile;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            this.testing = !this.testing;
        }

        if(this.testing) {
            this.map.update(camera);
        }
    }

    private void updateAndPlaceTiles(OrthographicCamera camera) {
        MapDefinition mapDefinition = this.map.getMapDefinition();

        MapLayer editingLayer = this.map.getMapLayer(this.editingLayerIndex);

        boolean liveUpdate = true;

        this.overlapping = false;

        for(int row = 0; row < mapDefinition.getMapHeight(); row++) {
            for(int column = 0; column < mapDefinition.getMapWidth(); column++) {
                int tileIndex = column + row * mapDefinition.getMapHeight();

                Tile tile = editingLayer.getLayerTiles().get(tileIndex).TILE;

                Vector2 tilePosition = new Vector2(column * mapDefinition.getTileWidth(), row * mapDefinition.getTileHeight());

                Rectangle tileBody = tile.getBody(tilePosition);

                if(this.mouseBody.overlaps(tileBody)) {
                    this.overlapping = true;
                    this.overlappingBody = new Rectangle(tileBody);

                    if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        editingLayer.getLayerTiles().set(tileIndex, this.placingTile);
                    }
                }

                if(liveUpdate) {
                    TileData tileData = editingLayer.getLayerTileData().get(tileIndex);

                    tile.update(this.map, camera, tilePosition, tileData);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        this.map.resize(width, height);
    }

    public void updateTitle() {
        Gdx.graphics.setTitle("Layer " + this.editingLayerIndex + " Testing " + this.testing);
    }

}
