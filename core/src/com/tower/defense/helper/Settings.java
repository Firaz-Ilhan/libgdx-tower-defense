package com.tower.defense.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tower.defense.screen.MainMenuScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Settings are stored in...
 * Windows: %UserProfile%/.prefs/Tower_Defense_Settings
 * Linux and OS X: ~/.prefs/Tower_Defense_Settings
 */
public class Settings {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);

    private final String fullscreen = "fullscreen";
    private final String MUSIC_ENABLED = "music_enabled";
    private final String MUSIC_VOLUME = "music_volume";

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
        log.info("player set fullscreen to: {}", value);
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
            log.info("display was set to full screen mode");
        } else {
            Gdx.graphics.setWindowedMode(1600, 900);
            log.info("display was set to windowed screen mode");
        }
    }

    /**
     * enables or disables background music
     *
     * @param value true to enable background music
     */
    public void setMusicState(boolean value) {
        getPreferences().putBoolean(MUSIC_ENABLED, value);
        getPreferences().flush();
        log.info("music is set to: {}", value);
    }

    /**
     * checks whether the music in the settings file is enabled or not.
     * Returns a default value(true) if the settings file was not created yet
     *
     * @return true if music is enabled
     */
    public boolean isMusicEnabled() {
        return getPreferences().getBoolean(MUSIC_ENABLED, true);
    }

    /**
     * sets the background music volume
     *
     * @param volume a float between 0 and 1
     */
    public void setMusicVolume(float volume) {
        getPreferences().putFloat(MUSIC_VOLUME, volume);
        getPreferences().flush();
        log.debug("music volume is set to: {}", volume);
    }

    /**
     * checks the settings file and return its value.
     * Returns a default value(0.2f) if the settings file has not been created yet
     *
     * @return a float between 0 and 1
     */
    public float getVolume() {
        return getPreferences().getFloat(MUSIC_VOLUME, 0.2f);
    }
}
