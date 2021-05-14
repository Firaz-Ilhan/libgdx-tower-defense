package com.tower.defense.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Settings are stored in...
 * Windows: %UserProfile%/.prefs/Tower_Defense_Settings
 * Linux and OS X: ~/.prefs/Tower_Defense_Settings
 */
public class Settings {

    private final String fullscreen = "fullscreen";

    public Preferences getPreferences() {
        final String fileName = "Tower_Defense_Settings";
        return Gdx.app.getPreferences(fileName);
    }

    /**
     * activates or deactivates the full screen mode in the settings file
     *
     * @param value true to activate the full screen mode
     */
    public void setFullscreenMode(boolean value) {
        getPreferences().putBoolean(fullscreen, value);
        getPreferences().flush();
    }

    /**
     * checks whether the full screen mode in the settings file is enabled or not
     *
     * @return true if fullscreen is enabled in the settings file
     */
    public boolean isFullscreenEnabled() {
        return getPreferences().getBoolean(fullscreen);
    }

    /**
     * switches between full screen and windowed mode, depending on the
     * current value in the settings file
     */
    public void toggleDisplayMode() {
        if (isFullscreenEnabled()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(1600, 900);
        }
    }
}
