package com.tower.defence.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tower.defence.TowerDefense;
import com.tower.defence.Map.TD_map;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense";
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.resizable = false;
		config.fullscreen = false;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new TowerDefense(), config);
	}
}
