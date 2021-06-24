package com.tower.defense.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tower.defense.TowerDefense;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuitDialog {

    private final static Logger log = LogManager.getLogger(QuitDialog.class);

    private final Skin skin;
    private final TowerDefense game;
    private final Stage stage;


    public QuitDialog(TowerDefense game, Skin skin, Stage stage) {
        this.game = game;
        this.skin = skin;
        this.stage = stage;

    }

    InputProcessor inputProcessor = new InputAdapter() {
        @Override
        public boolean keyDown(int keycode) {

            if ((keycode == Input.Keys.ESCAPE)) {
                quitGameConfirm();
            }

            return false;
        }
    };

    public void quitGameConfirm() {

        Label label = new Label("Do you want to surrender and quit the game?", skin, "black");
        TextButton btnYes = new TextButton("Quit", skin, "small");
        TextButton btnNo = new TextButton("Cancel", skin, "small");

        final Dialog dialog = new Dialog("Quit the Game?", skin) {
            {
                text(label);
                button(btnYes);
                button(btnNo);
            }
        }.show(stage);

        dialog.setModal(true);
        dialog.setMovable(true);
        dialog.setResizable(false);

        btnYes.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                // remove dialog from the stage
                dialog.hide();
                dialog.cancel();
                dialog.remove();

                // switch to EndScreen
                game.setScreen(new EndScreen(game));
                log.info("set screen to {}", game.getScreen().getClass());

                if (game.getClient() != null) {
                    // close connection and stop ClientConnectionThread
                    game.getClient().getClientConnection().closeConnection();
                }
                return true;
            }

        });

        btnNo.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // return to the game and remove dialog from the stage
                dialog.cancel();
                dialog.hide();
                dialog.remove();

                return true;
            }

        });
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }
}
