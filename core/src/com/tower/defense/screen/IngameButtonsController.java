package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.logging.FileHandler;


public class IngameButtonsController {

    private Viewport buttonViewPort;
    private Stage buttonStage;
    private boolean sellModePressed, buildModePressed;
    private OrthographicCamera buttonCam;
    private Table buttonTable;


    public IngameButtonsController() {
        buttonCam = new OrthographicCamera();
        buttonViewPort = new FitViewport(1600, 900, buttonCam);
        buttonStage = new Stage(buttonViewPort, GameScreen.spriteBatch);
        Gdx.input.setInputProcessor(buttonStage);

        buttonTable = new Table();
        buttonTable.left().bottom();


        Texture sellTexture = new Texture("core/assets/buttons/sellMode.png");
        Texture sellDownTexture = new Texture("core/assets/buttons/sellModeDown.png");
        Drawable sellImage = new TextureRegionDrawable(new TextureRegion(sellTexture));
        Drawable sellDownImage = new TextureRegionDrawable(new TextureRegion(sellDownTexture));

        ImageButton sellButton = new ImageButton(sellImage, sellDownImage);

        sellButton.addListener(new InputListener() {


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


        Texture buildTexture = new Texture("core/assets/buttons/buildMode.png");
        Texture buildDownTexture = new Texture("core/assets/buttons/buildModeDown.png");
        Drawable buildDownImage = new TextureRegionDrawable(new TextureRegion(buildDownTexture));
        Drawable buildImage = new TextureRegionDrawable(new TextureRegion(buildTexture));

        ImageButton buildButton = new ImageButton(buildImage, buildDownImage);

        buildButton.addListener(new InputListener() {


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

        //buttonTable.add(sellImage).size(sellImage.getWidth(), sellImage.getHeight());
        //buttonTable.add(buildImage).size(buildImage.getWidth(), buildImage.getHeight());
        buttonTable.add(sellButton);
        buttonTable.add(buildButton);

        buttonStage.addActor(buttonTable);


    }

    public void draw() {
        buttonStage.draw();

    }

    public boolean isSellModePressed() {
        return sellModePressed;
    }

    public boolean isBuildModePressed() {
        return buildModePressed;
    }

    public void resize(int width, int height) {
        buttonViewPort.update(width, height);
    }


}
