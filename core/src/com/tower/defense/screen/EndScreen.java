package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
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
    private Texture background;

    public EndScreen(final TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get(Constant.SKIN_PATH);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("background.png"));
        TextureRegionDrawable backgroundRegion =
                new TextureRegionDrawable(new TextureRegion(background));
        final Table table = new Table();
        table.setFillParent(true);
        table.background(backgroundRegion);
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
        } else if(player1.hasLost()) {
            winner = "You won!";
        }
        else{
            winner = "you quit the game";
        }

        log.info(winner);
        // create gui elements
        final TextButton mainMenuButton = new TextButton("Go Back", skin, "small");
        final Label whoWon;
        if(player1.hasLost()|| !player2.hasLost() && !player1.hasLost()) {
            whoWon = new Label(winner, skin, "main");
        }
        else{
            whoWon = new Label(winner, skin, "won");
        }
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                log.info("switched to {}", game.getScreen());
            }
        });

        headerTable.align(Align.top);
        headerTable.defaults().pad(10f);
        headerTable.row();
        headerTable.add(mainMenuButton);
        table.align(Align.center);
        table.add(whoWon);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.45f,0.63f,0.76f,1f);
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
