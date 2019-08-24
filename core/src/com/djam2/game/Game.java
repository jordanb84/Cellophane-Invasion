package com.djam2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djam2.game.state.StateManager;
import com.djam2.game.state.impl.StateMap;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private StateManager stateManager;

	@Override
	public void create () {
		this.batch = new SpriteBatch();

		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		this.stateManager = new StateManager();
		this.stateManager.registerState("map", new StateMap(this.stateManager));
		this.stateManager.setActiveState("map");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.stateManager.updateActiveState(this.camera);

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		this.stateManager.renderActiveState(this.batch, this.camera);
		this.batch.end();
	}

	@Override
	public void dispose () {

	}
}
