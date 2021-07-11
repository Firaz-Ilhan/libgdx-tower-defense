package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsScreen implements Screen {

    private final static Logger log = LogManager.getLogger(SettingsScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;

    public SettingsScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get(Constant.SKIN_PATH);
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
        final CheckBox musicCheckBox = new CheckBox("Music", skin, "default");
        final Slider musicVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        final CheckBox soundEffectsCheckBox = new CheckBox("Sound effects", skin, "default");
        final Slider soundEffectsVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });

        fullscreenModeCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setFullscreenMode(fullscreenModeCheckBox.isChecked());
                game.getSettings().toggleDisplayMode();
            }
        });

        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setMusicState(musicCheckBox.isChecked());
            }
        });

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setMusicVolume(musicVolumeSlider.getValue());
            }
        });

        soundEffectsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setSoundState(soundEffectsCheckBox.isChecked());
            }
        });

        soundEffectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSettings().setSoundVolume(soundEffectsVolumeSlider.getValue());
            }
        });

        // check for current state in persistent setting file and change gui state
        if (game.getSettings().isFullscreenEnabled()) {
            fullscreenModeCheckBox.setChecked(true);
        }

        if (game.getSettings().isMusicEnabled()) {
            musicCheckBox.setChecked(true);
        }

        if (game.getSettings().isSoundEnabled()) {
            soundEffectsCheckBox.setChecked(true);
        }

        musicVolumeSlider.setValue(game.getSettings().getMusicVolume());
        soundEffectsVolumeSlider.setValue(game.getSettings().getSoundVolume());

        headerTable.align(Align.top);
        headerTable.add(mainMenuButton);

        settingsTable.defaults().pad(10f);
        settingsTable.add(musicCheckBox);
        settingsTable.add(musicVolumeSlider);
        settingsTable.row();
        settingsTable.add(soundEffectsCheckBox);
        settingsTable.add(soundEffectsVolumeSlider);
        settingsTable.row();
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
