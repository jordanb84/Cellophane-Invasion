package com.djam2.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.djam2.game.sound.MusicType;
import com.djam2.game.state.StateManager;
import com.djam2.game.state.impl.StateEditor;
import com.djam2.game.state.impl.StateMap;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private StateManager stateManager;

	@Override
	public void create () {
		this.batch = new SpriteBatch();

		this.camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		this.stateManager = new StateManager();
		//this.stateManager.registerState("map", new StateMap(this.stateManager));
		this.stateManager.registerState("editor", new StateEditor(this.stateManager));
		this.stateManager.registerState("map", new StateMap(this.stateManager));
		this.stateManager.setActiveState("map");

		Gdx.graphics.setTitle("Cellophane Invasion - Made for Discord Jam 2");

		MusicType.loop(MusicType.Background);
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

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.stateManager.resizeActiveState(width, height);
	}

}
