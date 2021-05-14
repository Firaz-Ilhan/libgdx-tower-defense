package com.tower.defense;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tower.defense.helper.Settings;
import com.tower.defense.screen.MainMenuScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TowerDefense extends Game {

    private final static Logger log = LogManager.getLogger(TowerDefense.class);

    public AssetManager assetManager;
    private Settings settings;

    @Override
    public void create() {
        // system info
        log.info("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));
        log.info("JRE version: {}", System.getProperty("java.version"));
        log.info("libGDX version: {}", com.badlogic.gdx.Version.VERSION);
        log.info("OpenGl version: {}.{}",
                Gdx.graphics.getGLVersion().getMajorVersion(),
                Gdx.graphics.getGLVersion().getMinorVersion());
        log.info("GPU: {}", Gdx.graphics.getGLVersion().getRendererString());
        log.info("Current Display: {}", Gdx.graphics.getDisplayMode());

        // load settings
        settings = new Settings();
        settings.toggleDisplayMode(); // can ignore the default value in DesktopLauncher

        // loading assets
        assetManager = new AssetManager();
        assetManager.load("skins/glassyui/glassy-ui.json", Skin.class);
        assetManager.finishLoading();
        log.debug("asset manager loaded: {}", assetManager.getAssetNames());

        // set first screen to main menu
        this.setScreen(new MainMenuScreen(this));
        log.info("set screen to {}", this.getScreen().getClass());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public Settings getSettings() {
        return settings;
    }
}
