package com.tower.defense.helper;

import com.badlogic.gdx.Gdx;

/**
 * Only static access allowed
 */
public final class Settings {

    //prevents class from being instantiated
    private Settings() {

    }

    public static void toggleDisplayMode() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(1600, 900);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }
}
