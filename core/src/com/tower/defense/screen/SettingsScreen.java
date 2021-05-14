package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;

public class SettingsScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;

    public SettingsScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get("skins/glassyui/glassy-ui.json");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.setDebug(false);
        stage.addActor(settingsTable);

        final Table headerTable = new Table();
        headerTable.setFillParent(true);
        headerTable.setDebug(false);
        stage.addActor(headerTable);

        // create gui elements
        final TextButton mainMenuButton = new TextButton("Go Back", skin, "small");
        final CheckBox fullscreenModeCheckBox = new CheckBox("Fullscreen", skin, "default");

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        if (game.getSettings().isFullscreenEnabled()) {
            fullscreenModeCheckBox.setChecked(true);
        }

        fullscreenModeCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setFullscreenMode(fullscreenModeCheckBox.isChecked());
                game.getSettings().toggleDisplayMode();
            }
        });

        headerTable.align(Align.top);
        headerTable.add(mainMenuButton);

        settingsTable.defaults().pad(10f);
        settingsTable.add(fullscreenModeCheckBox);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        game.dispose();
    }
}
