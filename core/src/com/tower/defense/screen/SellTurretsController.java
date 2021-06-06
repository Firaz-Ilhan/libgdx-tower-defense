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
    boolean sellModePressed, buildModePressed;
    OrthographicCamera buttonCam;

    public SellTurretsController(){
        buttonCam = new OrthographicCamera();
        buttonViewPort = new FitViewport(1600,900,buttonCam);
        buttonStage = new Stage(buttonViewPort,GameScreen.spriteBatch);
        Gdx.input.setInputProcessor(buttonStage);

        Table buttonTable = new Table();
        buttonTable.left().bottom();

        final Image sellImage = new Image(new Texture("core/assets/buttons/sellMode.png"));
        sellImage.setSize(100,100);
        sellImage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                sellModePressed = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sellModePressed = false;

            }
        });

        final Image buildImage = new Image(new Texture("core/assets/buttons/buildMode.png"));
        buildImage.setSize(100,100);
        buildImage.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buildModePressed = true;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buildModePressed = false;
            }
        });

        buttonTable.add(sellImage).size(sellImage.getWidth(), sellImage.getHeight());
        buttonTable.add(buildImage).size(buildImage.getWidth(), buildImage.getHeight());

        buttonStage.addActor(buttonTable);

    }

    public void draw(){
        buttonStage.draw();
    }

    public boolean isSellModePressed() {
        return sellModePressed;
    }

    public boolean isBuildModePressed() {
        return buildModePressed;
    }
    public void resize(int width, int height){
        buttonViewPort.update(width,height);
    }
}
