package com.tower.defense.screen_helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tower.defense.helper.Constant;

public class ControlsWindow {

    private final Window controlsWindow;
    private final Texture controlWindowTexture;
    private final Window.WindowStyle controlWindowStyle;
    private final Stage windowStage;
    private final Viewport windowViewPort;
    private final OrthographicCamera windowCam;

    public ControlsWindow() {
        windowCam = new OrthographicCamera();
        windowViewPort = new FitViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT, windowCam);
        windowStage = new Stage(windowViewPort);


        controlWindowTexture = new Texture(Gdx.files.internal("controlMenu.png"));
        controlWindowStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(new TextureRegion(controlWindowTexture)));


        controlsWindow = new Window("", controlWindowStyle);
        controlsWindow.setPosition(625, 350);
        controlsWindow.setSize(350, 200);


        windowStage.addActor(controlsWindow);


    }

    public void draw(){
        windowStage.draw();
    }
}