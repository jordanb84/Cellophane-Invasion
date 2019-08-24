package com.djam2.game.state.impl;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
        MapDefinition mapDefinition = new MapDefinition(3, 20, 20, 16, 16);
        this.map = new Map(mapDefinition, TileType.Ground);
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

    }

}

