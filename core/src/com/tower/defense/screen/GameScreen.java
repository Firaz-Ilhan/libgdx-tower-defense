package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.enemy.Factory.Enemy1;
import com.tower.defense.enemy.Factory.EnemyFactory;
import com.tower.defense.helper.AllowedTiles;
import com.tower.defense.helper.Constant;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.*;
import com.tower.defense.player.Player;
import com.tower.defense.tower.Factory.Tower1;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.RenderWave;
import com.tower.defense.wave.Wave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.Line;
import java.util.LinkedList;
import java.util.ListIterator;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.*;
import static com.tower.defense.helper.PacketQueue.packetQueue;


public class GameScreen implements Screen {

    private final static Logger log = LogManager.getLogger(GameScreen.class);

    private final TowerDefense game;

    private final Stage stage;
    private TiledMap map;
    private TiledMapTileLayer groundLayer;
    private int[] decorationLayerIndices;

    private final OrthographicCamera camera;
    private final ScalingViewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch spriteBatch;

    private Vector2 hoveredTilePosition;
    private Vector2 mousePosition;

    // Help variables to show mouse position
    private BitmapFont font;

    private Texture hoveredTileTexture;
    private Texture hoveredTileNotAllowed;

    private Texture turret1Texture;
    private Texture turret2Texture;

    // list to store own towers
    private final LinkedList turretsPlaced = new LinkedList<ITower>();
    private ListIterator<Tower1> tower1ListIterator1;

    // list to store towers of the opponent
    private final LinkedList enemyTowersPlaced = new LinkedList<ITower>();


    private boolean turretIsHovered;
    private Texture turret1RangeIndicator;


    private final IngameButtonsController sellTurretsController;
    private boolean sellMode;
    private boolean buildMode;


    //Alert that it is not allowed to delete the last owning turret
    private boolean zeroTowerAlert;


    //Booleans to avoid creating multiple turrets by clicking once
    private boolean canDraw;
    private boolean canDelete;
    private boolean leftMouseButtonDown;
    private boolean rightMouseButtonDown;

    private Tower1 tower1;

    private Texture enemyImage;
    private Texture healthBarBG;
    private Texture healthBar;

    //get the lifepoints of the current enemy type.
    // The multiplicator is required to correctly scale the healthbar
    private int lifepointsAtStart = EnemyFactory.getEnemyInstance("easy", 0, 0, 0).getLifepoints();
    float multiplicator = (float) 100 / lifepointsAtStart;

    // this boolean determines which side of the map the player is on
    private boolean playerSide;

    private AllowedTiles allowedTiles;

    // WAVE
    private Wave wave;
    public static Player player1;
    public static Player player2;

    private final Skin skin;
    private FreeTypeFontGenerator generator;

    public GameScreen(TowerDefense game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT);
        // create stage and set it as input processor
        this.stage = new Stage(viewport);
        this.skin = game.assetManager.get(Constant.SKIN_PATH);

        // sell and buy towers
        this.sellTurretsController = new IngameButtonsController();

        QuitDialog quitDialog = new QuitDialog(game, skin, stage);

        // allows multiple input processors
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(quitDialog.getInputProcessor());
        multiplexer.addProcessor(sellTurretsController.getButtonStage());
        Gdx.input.setInputProcessor(multiplexer);

        this.leftMouseButtonDown = false;
        this.rightMouseButtonDown = false;
        this.canDraw = false;
        this.canDelete = false;
        this.zeroTowerAlert = false;
        this.buildMode = false;
        this.sellMode = false;
        this.turretIsHovered = false;
    }

    @Override
    public void show() {
        // loading Textures
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");
        hoveredTileTexture = new Texture(Gdx.files.internal("hovered_tile.png"));
        hoveredTileNotAllowed = new Texture(Gdx.files.internal("hovered_tile_not_allowed.png"));

        enemyImage = new Texture(Gdx.files.internal(Constant.VIRUS_ENEMY_PATH));
        healthBarBG = new Texture(Gdx.files.internal("healthBarBG.png"));
        healthBar = new Texture(Gdx.files.internal("healthBar.png"));

        // getting the layers of the map
        MapLayers mapLayers = map.getLayers();
        groundLayer = (TiledMapTileLayer) mapLayers.get("ground");
        decorationLayerIndices = new int[]{mapLayers.getIndex("decoration")};

        //creating the textures of the turrets
        turret1Texture = new Texture(Gdx.files.internal(Constant.TOWER1_PATH), true);
        turret2Texture = new Texture(Gdx.files.internal(Constant.TOWER2_PATH), true);

        turret1Texture.setFilter(MipMapLinearLinear, Linear);

        camera.setToOrtho(false, Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT);
        camera.update();

        // creating the renderer
        renderer = new OrthogonalTiledMapRenderer(map);

        spriteBatch = new SpriteBatch();

        // checks which side the player is on based on boolean value
        playerSide = true;

        // setting up the font for the helper variables that show the mouse position

        generator = new FreeTypeFontGenerator(Gdx.files.internal(Constant.FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.size = 30;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = -3;
        parameter.magFilter = Linear;
        parameter.minFilter = Linear;
        font = generator.generateFont(parameter);

        allowedTiles = new AllowedTiles();
        // WAVE: initiating Players and Wave

        player1 = new Player("Player", playerSide);
        player2 = new Player("Opponent", !playerSide);
        // for testing
        // player2.reduceLifepoints(40);

        wave = new Wave(game);
    }

    @Override
    public void render(float delta) {
        handlePackets();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // setting the render view to the camera
        camera.update();
        renderer.setView(camera);

        // Check if a button was clicked
        handleInput();

        // getting the current mouse position
        mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // position of the hovered tile
        hoveredTilePosition = new Vector2((int) mousePosition.x / 50, (int) mousePosition.y / 50);

        renderer.getBatch().begin();

        // rendering the groundLayer
        renderer.renderTileLayer(groundLayer);

        // temporary help
//        font.draw(renderer.getBatch(), String.valueOf((int) mousePosition.x), 1375, 40);
//        font.draw(renderer.getBatch(), String.valueOf((int) mousePosition.y), 1475, 40);
//        font.draw(renderer.getBatch(), String.valueOf((int) hoveredTilePosition.x), 1375, 100);
//        font.draw(renderer.getBatch(), String.valueOf((int) hoveredTilePosition.y), 1475, 100);
//        font.draw(renderer.getBatch(), String.valueOf(screenWidth), 25, 160);
//        font.draw(renderer.getBatch(), String.valueOf(screenHeight), 125, 160);
        font.draw(renderer.getBatch(), "LP: " + player1.getLifepoints(), 25, 890);
        font.draw(renderer.getBatch(), "LP: " + player2.getLifepoints(), 1375, 890);
        font.draw(renderer.getBatch(), "Money: " + player1.getWalletValue(), 25, 840);
        font.draw(renderer.getBatch(), "Money: " + player2.getWalletValue(), 1375, 840);
        font.draw(renderer.getBatch(), "Wave: " + wave.getWaveCount(), 725, 890);

        if (zeroTowerAlert) {
            font.draw(renderer.getBatch(), "You can't own 0 turrets !", hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
        }


        renderer.getBatch().end();

        // rendering the decoration on top of the ground tiles
        renderer.render(decorationLayerIndices);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        stage.act();

        //drawing the hoveredTile based on what player side you are on and whether you
        //allowed to or not
        spriteBatch.begin();

        if (playerSide) {
            if (allowedTiles.tileInArray(hoveredTilePosition, AllowedTiles.playerOneAllowedTiles)) {
                spriteBatch.draw(hoveredTileTexture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            } else {
                spriteBatch.draw(hoveredTileNotAllowed, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }
        } else {
            if (allowedTiles.tileInArray(hoveredTilePosition, AllowedTiles.playerTwoAllowedTiles)) {
                spriteBatch.draw(hoveredTileTexture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            } else {
                spriteBatch.draw(hoveredTileNotAllowed, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }
        }
        // WAVE: drawing the enemies and a healthbar above them to
        //       show that an enemy got hit by a turret
        for (Enemy enemy : wave.waveRight) {
            spriteBatch.draw(enemyImage, enemy.getX(), enemy.getY());
            float currentLifepoints = (lifepointsAtStart - (lifepointsAtStart - enemy.getLifepoints())) * multiplicator;
            if (currentLifepoints != lifepointsAtStart * multiplicator) {
                spriteBatch.draw(healthBarBG, enemy.getX() - 25, enemy.getY() + 50, 100, 10);
                spriteBatch.draw(healthBar, enemy.getX() - 25, enemy.getY() + 50, currentLifepoints, 10);
            }
        }
        for (Enemy enemy : wave.waveLeft) {
            spriteBatch.draw(enemyImage, enemy.getX(), enemy.getY());
            float currentLifepoints = (lifepointsAtStart - (lifepointsAtStart - enemy.getLifepoints())) * multiplicator;
            if (currentLifepoints != lifepointsAtStart * multiplicator) {
                spriteBatch.draw(healthBarBG, enemy.getX() - 25, enemy.getY() + 50, 100, 10);
                spriteBatch.draw(healthBar, enemy.getX() - 25, enemy.getY() + 50, currentLifepoints, 10);
            }
        }

        //if statement to avoid multiple spawning of turrets by clicking the left mousebutton once

        if (Gdx.input.isButtonPressed(0) && !leftMouseButtonDown) {
            canDraw = true;
        }

        //avoid multiple right-clicking


        if (Gdx.input.isButtonPressed(1) && !rightMouseButtonDown) {
            canDelete = true;
        }


        //drawing the turret at the selected tile and avoid turret-stacking by removing the used tile-position from the AllowedTiles-list


        if (canDraw && !leftMouseButtonDown && allowedTiles.tileInArray(hoveredTilePosition, AllowedTiles.playerOneAllowedTiles) && buildMode && player1.getWalletValue() >= 20) {

            spawnTurret1();
            AllowedTiles.playerOneAllowedTiles.remove(hoveredTilePosition);

            player1.buyTower(tower1);


        } else {
            canDraw = false;
        }


        //List iterator that draws all placed turrets and handles "turret removing"


        tower1ListIterator1 = turretsPlaced.listIterator();

        while (tower1ListIterator1.hasNext()) {


            tower1 = tower1ListIterator1.next();
            tower1.draw();
            tower1.updateTargetarray(wave, player1);
            tower1.update();


            //Output if player tries to delete the last turret
            if (canDelete && !rightMouseButtonDown && turretsPlaced.size() > 1) {


                if (tower1.getX() == hoveredTilePosition.x * 50 && tower1.getY() == hoveredTilePosition.y * 50 && sellMode) {

                    tower1ListIterator1.remove();
                    AllowedTiles.playerOneAllowedTiles.add(hoveredTilePosition);
                    player1.sellTower(tower1);
                    System.out.println(turretsPlaced);

                    if (game.getClient() != null) {
                        game.getClient().sendPacket(
                                new PacketRemoveTower(hoveredTilePosition.x, hoveredTilePosition.y));
                    }

                }


            } else {
                canDelete = false;
                //sellState = false;

            }


        }
        //draw indicator for Turretrange while mouse is hoverd over turret
        turret1RangeIndicator = new Texture(Gdx.files.internal("turrets/turret1RangeIndicator.png"));


        if (Gdx.input.isKeyPressed(57)) {
            turretIsHovered = true;
        } else {
            turretIsHovered = false;
        }

        if (turretIsHovered) {

            //spriteBatch.draw(turret1RangeIndicator, tower1.getX() - 75 , tower1.getY() - 75);
            spriteBatch.draw(turret1RangeIndicator, hoveredTilePosition.x * 50 - 175, hoveredTilePosition.y * 50 - 175);      //zur not auf Mausposition
        }


        tower1ListIterator1 = enemyTowersPlaced.listIterator();


        while (tower1ListIterator1.hasNext()) {

            tower1 = tower1ListIterator1.next();
            tower1.draw();
            tower1.updateTargetarray(wave, player2);
            tower1.update();

        }

        leftMouseButtonDown = Gdx.input.isButtonPressed(0);
        rightMouseButtonDown = Gdx.input.isButtonPressed(1);


        spriteBatch.end();


        // WAVE: check if we need to create a enemy
        wave.newEnemySpawn();

        // WAVE:
        // move the enemy, remove any that are beneath the bottom edge of
        // the screen or that have no more LP.

        RenderWave.renderWave(player1, wave);
        RenderWave.renderWave(player2, wave);

        // END OF GAME
        if (player1.getLifepoints() <= 0) {
            player1.lost();

            if (game.getClient() != null) {
                game.getClient().sendPacket(new PacketEndOfGame());
            }
            dispose();
            game.setScreen(new EndScreen(game));
            log.info("set screen to {}", game.getScreen().getClass());

        }
        stage.getViewport().apply();
        stage.draw();


        //Draw Build-/SellMode Menu
        sellTurretsController.draw();


    }

    /**
     * Method to spawn a Turret1 and add him to the turretsPlaced list
     */
    public void spawnTurret1() {
        //Vector2 mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        tower1 = new Tower1(turret1Texture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50, 50, 50, spriteBatch);
        turretsPlaced.add(tower1);

        if (game.getClient() != null) {
            game.getClient().sendPacket(new PacketAddTower(hoveredTilePosition.x, hoveredTilePosition.y));
        }


    }


    public void spawnTurret2() {

    }


    /**
     * Switch between build and sell mode with the buttons provided for this
     */
    public void handleInput() {
        if (sellTurretsController.isSellModePressed()) {
            sellMode = true;
            buildMode = false;


        } else if (sellTurretsController.isBuildModePressed()) {
            buildMode = true;
            sellMode = false;
            zeroTowerAlert = false;


        } else if (sellTurretsController.isBuildModePressed()) {
            buildMode = true;
            sellMode = false;
            zeroTowerAlert = false;

        }
    }

    public void handlePackets() {
        if (packetQueue.isEmpty()) {
            return;
        }
        while (!packetQueue.isEmpty()) {
            Packet packet = packetQueue.removeFirst();
            PacketType type = packet.getPacketType();

            log.info("Traffic: New {}", type.toString());
            final float mapWidth = 31f;

            switch (type) {
                case PACKETLIFEPOINTS:
                    PacketLifepoints packetLifepoints = (PacketLifepoints) packet;
                    player2.setLifepoints(packetLifepoints.getLP());
                    break;
                case PACKETENDOFWAVE:
                    PacketEndOfWave packetEndOfWave = (PacketEndOfWave) packet;
                    wave.partnerWaveEnded(packetEndOfWave.getReward());
                    break;
                case PACKETENDOFGAME:
                    player2.lost();
                    dispose();
                    game.setScreen(new EndScreen(game));
                    log.info("set screen to {}", game.getScreen().getClass());
                    break;
                case PACKETADDTOWER:
                    log.info("packetoutnewtower received");
                    PacketAddTower packetAddTower = (PacketAddTower) packet;
                    float xCordAdd = packetAddTower.getX();
                    float yCordAdd = packetAddTower.getY();

                    tower1 = new Tower1(turret2Texture, (mapWidth - xCordAdd) * 50,
                            yCordAdd * 50, 50, 50, spriteBatch);
                    enemyTowersPlaced.add(tower1);
                    player2.buyTower(tower1);
                    break;
                case PACKETREMOVETOWER:
                    log.info("packetoutremovetower received");
                    PacketRemoveTower packetOutRemoveTower = (PacketRemoveTower) packet;
                    float xCordRemove = packetOutRemoveTower.getX();
                    float yCordRemove = packetOutRemoveTower.getY();
                    tower1ListIterator1 = enemyTowersPlaced.listIterator();
                    while (tower1ListIterator1.hasNext()) {
                        tower1 = tower1ListIterator1.next();
                        if (enemyTowersPlaced.size() > 1) {
                            if (tower1.getX() == (mapWidth - xCordRemove) * 50 && tower1.getY() == yCordRemove * 50) {
                                tower1ListIterator1.remove();
                                player2.sellTower(tower1);
                            }
                        }
                    }
                    break;
                default:
                    break;

            }
        }
    }

    public static void handle(Packet packet) {
        packetQueue.addFirst(packet);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
        sellTurretsController.resize(width, height);
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
        enemyImage.dispose();
        map.dispose();
        game.dispose();
        stage.dispose();
        turret1Texture.dispose();
        turret2Texture.dispose();
        enemyImage.dispose();
        hoveredTileTexture.dispose();
        hoveredTileNotAllowed.dispose();
        spriteBatch.dispose();
        font.dispose();
        generator.dispose();
        renderer.dispose();
        skin.dispose();
        healthBarBG.dispose();
        healthBar.dispose();
    }
}
