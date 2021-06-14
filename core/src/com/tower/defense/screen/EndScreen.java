package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tower.defense.screen.GameScreen.player1;
import static com.tower.defense.screen.GameScreen.player2;

public class EndScreen implements Screen {

    private final static Logger log = LogManager.getLogger(EndScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private String winner;

    public EndScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get("skins/glassyui/glassy-ui.json");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        final Table headerTable = new Table();
        headerTable.setFillParent(true);
        headerTable.setDebug(false);
        stage.addActor(headerTable);
        if (player1.hasLost() && player2.hasLost() ) {
            winner = "Draw";
        } else if (player1.hasLost() ) {
            winner = "You lost";
        } else {
            winner = " You won";
        }

        log.info(winner);

        // create gui elements
        final TextButton mainMenuButton = new TextButton("Go Back", skin, "small");
        final Label whoWon = new Label(winner, skin, "big");

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                log.info("switched to {}", game.getScreen());
            }
        });

        headerTable.align(Align.top);
        headerTable.add(mainMenuButton);

        table.add(whoWon);
        table.defaults().pad(10f);
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
