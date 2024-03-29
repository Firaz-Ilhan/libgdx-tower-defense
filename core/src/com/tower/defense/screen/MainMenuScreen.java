package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainMenuScreen implements Screen {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private Texture background;

    public MainMenuScreen(TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get(Constant.SKIN_PATH);
        //create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.setDebug(false);
        stage.addActor(menuTable);
        //create buttons
        background = new Texture(Gdx.files.internal("backgroundTitle.png"));
        TextureRegionDrawable backgroundRegion =
                new TextureRegionDrawable(new TextureRegion(background));
        final TextButton multiplayerButton = new TextButton("Multiplayer", skin, "default");
        final TextButton settingsButton = new TextButton("Settings", skin, "default");
        final TextButton aboutButton = new TextButton("About", skin, "default");
        final TextButton exitButton = new TextButton("Exit", skin, "default");

        //add buttons to table
        menuTable.defaults().pad(10f);
        menuTable.add(multiplayerButton);
        menuTable.row();
        menuTable.add(settingsButton);
        menuTable.row();
        menuTable.add(aboutButton);
        menuTable.row();
        menuTable.add(exitButton);
        menuTable.background(backgroundRegion);
        //create button listeners
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                log.info("user has closed the program with the exit button");
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });

        multiplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MatchmakingScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });
        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new AboutScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.45f, 0.63f, 0.76f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // updates the stage's viewport when the screen size is changed
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
