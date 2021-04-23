package com.tower.defence.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.tower.defence.Map.TD_map;
import com.tower.defence.TestMain;

import com.tower.defence.TowerDefense;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Tower Defense";
		config.width = 1600;
		config.height = 900;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new TD_map(), config);

	}
}
