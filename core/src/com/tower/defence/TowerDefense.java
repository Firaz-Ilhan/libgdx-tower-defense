package com.tower.defence;

import com.badlogic.gdx.Game;
import com.tower.defence.screen.MainMenuScreen;


public class MyGdxGame extends Game {

    public void create() {
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
    }
}
