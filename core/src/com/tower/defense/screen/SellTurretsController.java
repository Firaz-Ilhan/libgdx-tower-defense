package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class SellTurretsController {

    Viewport buttonViewPort;
    Stage buttonStage;
    boolean sellPressed, abortPressed;
    OrthographicCamera buttonCam;

    public SellTurretsController(){
        buttonCam = new OrthographicCamera();
        buttonViewPort = new FitViewport(1600,900,buttonCam);
        buttonStage = new Stage(buttonViewPort,GameScreen.spriteBatch);
        Gdx.input.setInputProcessor(buttonStage);

        Table buttonTable = new Table();
        buttonTable.left().bottom();

        final Image sellImage = new Image(new Texture("core/assets/buttons/sellSign.png"));
        sellImage.setSize(100,100);
        sellImage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                sellPressed = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sellPressed = false;

            }
        });

        final Image abortImage = new Image(new Texture("core/assets/buttons/abortSign.png"));
        abortImage.setSize(100,100);
        abortImage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                abortPressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                abortPressed = false;
            }
        });

        buttonTable.add(sellImage).size(sellImage.getWidth(), sellImage.getHeight());
        buttonTable.add(abortImage).size(abortImage.getWidth(), abortImage.getHeight());

        buttonStage.addActor(buttonTable);

    }

    public void draw(){
        buttonStage.draw();
    }

    public boolean isSellPressed() {
        return sellPressed;
    }

    public boolean isAbortPressed() {
        return abortPressed;
    }
    public void resize(int width, int height){
        buttonViewPort.update(width,height);
    }
}
