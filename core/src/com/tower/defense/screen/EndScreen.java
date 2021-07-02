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
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tower.defense.helper.PacketQueue.packetQueue;
import static com.tower.defense.network.packet.PacketType.PACKETENDOFGAME;
import static com.tower.defense.screen.GameScreen.player;
import static com.tower.defense.screen.GameScreen.opponent;

public class EndScreen implements Screen {

    private final static Logger log = LogManager.getLogger(EndScreen.class);

    private final Stage stage;
    private final Skin skin;
    private final TowerDefense game;
    private String outcome;
    private Texture background;

    public EndScreen(final TowerDefense game) {
        this.game = game;
        game.getClient().getClientConnection().closeConnection();
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
        computeOutcome();
        // create gui elements
        final TextButton mainMenuButton = new TextButton("Go Back", skin, "small");
        final Label whoWon;
        if(player.hasLost()|| !opponent.hasLost() && !player.hasLost()) {
            whoWon = new Label(outcome, skin, "main");
        }
        else{
            whoWon = new Label(outcome, skin, "won");
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
    private String computeOutcome(){
        if (player.hasLost() && opponent.hasLost() ) {
            outcome = "Draw";
        } else if (player.hasLost() ) {
            outcome = "You lost";
        } else if(opponent.hasLost()) {
            outcome = "You won!";
        }
        else{
            outcome = "you quit the game";
        }

        return outcome;
    }

    @Override
    public void render(float delta) {
        handle();
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
    private void handle() {
        while (!packetQueue.isEmpty()) {
            Packet packet = packetQueue.removeFirst();
            PacketType type = packet.getPacketType();

            log.info("Traffic: New {}", type.toString());
            if (type == PACKETENDOFGAME) {
                opponent.lost();
            }
        }
    }
}
