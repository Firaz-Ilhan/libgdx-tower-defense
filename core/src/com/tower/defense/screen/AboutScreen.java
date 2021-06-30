package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AboutScreen implements Screen {

    private final static Logger log = LogManager.getLogger(MainMenuScreen.class);
    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private Texture background;
    private final String ABOUT_TEXT= "This Game was developed by the following people: \n" +
                                        "Firaz Ilhan \n" +
                                        "Lea Gutierrez \n" +
                                        "Luca Baur \n" +
                                        "Niklas Mäckle \n" +
                                        "Niko Dangel \n" +
                                        "Musik by : 'Platzhalter' ";
    public AboutScreen(TowerDefense game) {
        this.game = game;
        skin = game.assetManager.get(Constant.SKIN_PATH);
        //create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        //create buttons
        background = new Texture(Gdx.files.internal("backgroundTitle.png"));
        TextureRegionDrawable backgroundRegion =
                new TextureRegionDrawable(new TextureRegion(background));
        final TextButton mainMenuButton = new TextButton("Back", skin, "small");
        final TextButton aboutText = new TextButton(ABOUT_TEXT,skin,"small");
        aboutText.setDisabled(true);
        //add buttons to table
        table.defaults().pad(10f);
        table.background(backgroundRegion);
        table.add(aboutText);
        table.row();
        table.add(mainMenuButton);
        aboutText.setBounds(0,0,200,500);
        //create button listeners
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                log.info("switched to {}", game.getScreen());
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.45f,0.63f,0.76f,1f);
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
