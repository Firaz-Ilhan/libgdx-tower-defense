package com.tower.defence;

import com.badlogic.gdx.Game;
import com.tower.defence.screen.MainMenuScreen;


public class TowerDefense extends Game {

    public void create() {
        //set first screen to main menu
        this.setScreen(new MainMenuScreen());
    }

    public void render() {
        super.render();
    }

    public void dispose() {
    }
}
