package com.djam2.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.djam2.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1300;
		config.height = 731;
		//config.backgroundFPS = 20;
		//config.foregroundFPS = 20;
		new LwjglApplication(new Game(), config);
	}
}
