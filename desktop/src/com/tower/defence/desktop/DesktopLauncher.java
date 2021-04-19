package com.tower.defence.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tower.defence.MyGdxGame;
import com.tower.defence.Map.TiledMap;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense";
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new TiledMap(), config);
	}
}
