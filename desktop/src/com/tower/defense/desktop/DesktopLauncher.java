package com.tower.defense.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tower.defense.TowerDefense;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense";
		config.width = 1600;
		config.height = 900;
		config.fullscreen = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new TowerDefense(), config);
	}
}
