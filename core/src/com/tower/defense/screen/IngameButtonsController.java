package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tower.defense.helper.Constant;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.MipMapLinearLinear;


public class IngameButtonsController {

    private final Viewport buttonViewPort;
    private final Stage buttonStage;
    private final ImageButton buildButton;
    private final ImageButton sellButton;


    public IngameButtonsController() {
        OrthographicCamera buttonCam = new OrthographicCamera();
        buttonViewPort = new FitViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT, buttonCam);
        buttonStage = new Stage(buttonViewPort);
        Table buttonTable = new Table();
        buttonTable.left().bottom();


        Texture sellTexture = new Texture(Gdx.files.internal("buttons/sellMode.png"), true);
        Texture sellDownTexture = new Texture(Gdx.files.internal("buttons/sellModeDown.png"), true);

        // improves texture scaling for low resolution
        sellDownTexture.setFilter(MipMapLinearLinear, Linear);
        sellTexture.setFilter(MipMapLinearLinear, Linear);

        Drawable sellImage = new TextureRegionDrawable(new TextureRegion(sellTexture));
        Drawable sellDownImage = new TextureRegionDrawable(new TextureRegion(sellDownTexture));

        ImageButton.ImageButtonStyle sellStyle = new ImageButton.ImageButtonStyle();
        sellStyle.imageUp = sellImage;
        sellStyle.imageChecked = sellDownImage;

        sellButton = new ImageButton(sellStyle);

        Texture buildTexture = new Texture(Gdx.files.internal("buttons/buildMode.png"), true);
        Texture buildDownTexture = new Texture(Gdx.files.internal("buttons/buildModeDown.png"), true);

        // improves texture scaling for low resolution
        buildTexture.setFilter(MipMapLinearLinear, Linear);
        buildDownTexture.setFilter(MipMapLinearLinear, Linear);

        Drawable buildDownImage = new TextureRegionDrawable(new TextureRegion(buildDownTexture));
        Drawable buildImage = new TextureRegionDrawable(new TextureRegion(buildTexture));

        ImageButton.ImageButtonStyle buildStyle = new ImageButton.ImageButtonStyle();
        buildStyle.imageUp = buildImage;
        buildStyle.imageChecked = buildDownImage;

        buildButton = new ImageButton(buildStyle);
        buildButton.setChecked(true);

        buildButton.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                buildButton.setChecked(true);
                sellButton.setChecked(false);
                return true;
            }
        });


        sellButton.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                sellButton.setChecked(true);
                buildButton.setChecked(false);
                return true;
            }
        });

        ButtonGroup<ImageButton> buttonGroup = new ButtonGroup<>(sellButton, buildButton);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonTable.add(sellButton);
        buttonTable.add(buildButton);

        buttonStage.addActor(buttonTable);


    }

    public void draw() {
        buttonStage.draw();

    }

    public boolean isSellModePressed() {
        return sellButton.isChecked();
    }

    public boolean isBuildModePressed() {
        return buildButton.isChecked();
    }

    public void resize(int width, int height) {
        buttonViewPort.update(width, height);
    }

    public Stage getButtonStage() {
        return buttonStage;
    }
}
