package com.djam2.game.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djam2.game.io.MapImporter;
import com.djam2.game.map.Map;
import com.djam2.game.map.MapDefinition;
import com.djam2.game.state.State;
import com.djam2.game.state.StateManager;
import com.djam2.game.tile.TileType;

public class StateMap extends State {

    private Map map;

    public StateMap(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void create() {
        MapDefinition mapDefinition = new MapDefinition(3, 60, 60, 16, 16);
        //this.map = new Map(mapDefinition, TileType.Ground);

        this.map = MapImporter.getInstance().getMapFromFile(Gdx.files.internal("map/roads29.map"));
        this.map.setStateManager(this.getManager());
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.map.render(batch, camera);
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.map.update(camera);
    }

    @Override
    public void resize(int width, int height) {
        this.map.resize(width, height);
    }

    @Override
    public void reset() {

    }

}

