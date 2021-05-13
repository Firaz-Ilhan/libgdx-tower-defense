package com.tower.defense;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tower.defense.helper.Settings;
import com.tower.defense.screen.MainMenuScreen;

public class TowerDefense extends Game {

    public AssetManager assetManager;
    private Settings settings;

    @Override
    public void create() {
        // load settings
        settings = new Settings();
        settings.toggleDisplayMode(); // can ignore the default value in DesktopLauncher

        // loading assets
        assetManager = new AssetManager();
        assetManager.load("skins/glassyui/glassy-ui.json", Skin.class);
        assetManager.finishLoading();

        // set first screen to main menu
        this.setScreen(new MainMenuScreen(this));
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
