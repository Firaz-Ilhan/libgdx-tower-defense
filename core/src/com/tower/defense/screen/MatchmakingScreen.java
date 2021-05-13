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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MatchmakingScreen implements Screen {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;

    public MatchmakingScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get("skins/glassyui/glassy-ui.json");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table connectionTable = new Table();
        connectionTable.setFillParent(true);
        connectionTable.setDebug(false);
        stage.addActor(connectionTable);

        final TextField opponentIPField = new TextField("", skin, "default");
        final Label opponentIPLabel = new Label("Opponent's IP address", skin, "default");

        final Label ownLabel = new Label("Your IP address:", skin, "default");
        final Label ownIPLabel = new Label("placeholder", skin, "default");
        // ownIPLabel.setText("192.168...");
        final Label connectionStatus = new Label("not connected", skin, "default");

        final TextButton connectButton = new TextButton("Connect...", skin, "small");

        connectionTable.defaults().pad(10f);
        connectionTable.align(Align.left);
        connectionTable.add(opponentIPLabel);
        connectionTable.add(opponentIPField);
        connectionTable.row();
        connectionTable.add(ownLabel);
        connectionTable.add(ownIPLabel);
        connectionTable.row();
        connectionTable.add(connectButton);
        connectionTable.add(connectionStatus);

        final Table chatTable = new Table();
        chatTable.setFillParent(true);
        chatTable.setDebug(false);
        stage.addActor(chatTable);

        final TextButton startGame = new TextButton("Start Game...", skin, "small");
        final TextField chatInputField = new TextField("", skin, "default");

        chatTable.defaults().pad(10f);
        chatTable.align(Align.right);
        chatTable.add(chatInputField);
        chatTable.row();
        chatTable.add(startGame);


        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // final String ip = opponentIPField.getText();
                // connect players
                // connectionStatus.setText("new status")
            }
        });

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // start the game only when the players are connected
                game.setScreen(new GameScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());
            }
        });
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
