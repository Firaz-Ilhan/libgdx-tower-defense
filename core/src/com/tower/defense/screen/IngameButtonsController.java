package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.helper.Constant;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.client.PacketInfluence;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.MipMapLinearLinear;


public class IngameButtonsController {

    private final Viewport buttonViewPort;
    private final Stage buttonStage;
    private final ImageButton buildButton;
    private final ImageButton sellButton;

    private final ImageButton controlsButton;

    private final ImageButton influenceButton;
    private final TowerDefense game;
    private GameScreen screen;





    public IngameButtonsController(TowerDefense game,GameScreen screen) {
        this.game = game;
        this.screen = screen;

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

        Texture influenceTextureDown = new Texture(Gdx.files.internal("buttons/influenceDown.png"), true);
        Texture influenceTextureUp = new Texture(Gdx.files.internal("buttons/influenceUp.png"), true);
        influenceTextureDown.setFilter(MipMapLinearLinear, Linear);
        influenceTextureUp.setFilter(MipMapLinearLinear, Linear);
        Drawable influenceImageUp = new TextureRegionDrawable(new TextureRegion(influenceTextureUp));
        Drawable influenceImageDown = new TextureRegionDrawable(new TextureRegion(influenceTextureDown));
        ImageButton.ImageButtonStyle influenceStyle = new ImageButton.ImageButtonStyle();
        influenceStyle.imageUp = influenceImageUp;
        influenceStyle.imageDown = influenceImageDown;

        influenceButton = new ImageButton(influenceStyle);

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
                influenceButton.setChecked(false);
                return true;
            }
        });
        influenceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("button pressed");
                buildButton.setChecked(false);
                sellButton.setChecked(false);
                if(game.getClient() != null && GameScreen.player.getWalletValue() >= 100 ) {
                    game.getClient().sendPacket(new PacketInfluence());
                    screen.wave.healAndBuffWave(false);
                    GameScreen.player.buyInfluence(100);
                }
            }
        });
        ButtonGroup<ImageButton> buttonGroup = new ButtonGroup<>(sellButton, buildButton);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonTable.add(sellButton);
        buttonTable.add(buildButton);
        buttonTable.add(influenceButton);

        buttonStage.addActor(buttonTable);


        //creating button to see the controls
        Table controlTable = new Table();
        controlTable.setPosition(1550,50);

        Texture controlsTexture = new Texture(Gdx.files.internal("buttons/controlButton.png"),true);
        Texture controlsDownTexture = new Texture(Gdx.files.internal("buttons/controlButtonDown.png"),true);


        controlsTexture.setFilter(MipMapLinearLinear, Linear);
        controlsDownTexture.setFilter(MipMapLinearLinear, Linear);


        Drawable controlsImage = new TextureRegionDrawable(new TextureRegion(controlsTexture));
        Drawable controlsImageDown = new TextureRegionDrawable(new TextureRegion(controlsDownTexture));

        ImageButton.ImageButtonStyle controlsStyle = new ImageButton.ImageButtonStyle();
        controlsStyle.imageUp = controlsImage;
        controlsStyle.imageDown = controlsImageDown;

        controlsButton = new ImageButton(controlsStyle);

        controlsButton.addListener(new InputListener() {


            public boolean touchDown(InputEvent event, int keycode) {
               // controlActivePressed = true;
                return true;

            }


            public boolean touchUp(InputEvent event, int keycode) {
                //controlActivePressed = false;
                return false;

          }});



        //ButtonGroup<ImageButton> controlsGroup = new ButtonGroup<>(controlsButton);
        //buttonGroup.setMaxCheckCount(1);

        controlTable.add(controlsButton);



        buttonStage.addActor(controlTable);
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
    public boolean isInfluenceModePressed() {
        return influenceButton.isChecked();
    }

    public boolean isControlsPressed(){
        return controlsButton.isPressed();
    }



    public void resize(int width, int height) {
        buttonViewPort.update(width, height);
    }

    public Stage getButtonStage() {
        return buttonStage;
    }
}
