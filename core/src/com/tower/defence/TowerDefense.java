package com.tower.defence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tower.defence.screen.MainMenuScreen;


public class TowerDefense extends Game {

    public AssetManager assetManager;

    @Override
    public void create() {
        //loading assets
        assetManager = new AssetManager();
        assetManager.load("skins/glassyui/glassy-ui.json", Skin.class);
        assetManager.finishLoading();

        //set first screen to main menu
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
}
