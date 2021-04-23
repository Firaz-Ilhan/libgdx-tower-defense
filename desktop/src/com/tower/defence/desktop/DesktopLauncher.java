package com.tower.defence.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tower.defence.TowerDefense;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense";
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new TowerDefense(), config);
	}
}
