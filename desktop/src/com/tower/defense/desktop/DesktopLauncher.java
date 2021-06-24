package com.tower.defense.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tower.defense.TowerDefense;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Tower Defense");
        config.setWindowedMode(1600, 900);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setIdleFPS(30);
        config.setWindowIcon("virus.png");
        new Lwjgl3Application(new TowerDefense(), config);
    }
}

