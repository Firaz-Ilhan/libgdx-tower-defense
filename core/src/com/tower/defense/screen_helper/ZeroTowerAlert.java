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

public class ZeroTowerAlert {

    private final Window alertWindow;
    private final Texture alertTexture;
    private final Window.WindowStyle alertStyle;
    private final Stage alertStage;
    private final Viewport alertViewPort;
    private final OrthographicCamera alertCam;

    public ZeroTowerAlert() {
        alertCam = new OrthographicCamera();
        alertViewPort = new FitViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT, alertCam);
        alertStage = new Stage(alertViewPort);


        alertTexture = new Texture(Gdx.files.internal("zeroTowerAlert.png"));
        alertStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(new TextureRegion(alertTexture)));


        alertWindow = new Window("", alertStyle);
        alertWindow.setPosition(625, 350);
        alertWindow.setSize(200, 75);


        alertStage.addActor(alertWindow);


    }

    public void draw() {
        alertStage.draw();
    }
}

