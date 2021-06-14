package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.helper.AllowedTiles;
import com.tower.defense.network.packet.Packet;
import com.tower.defense.network.packet.PacketType;
import com.tower.defense.network.packet.client.PacketInEndOfGame;
import com.tower.defense.network.packet.server.PacketOutEndOfWave;
import com.tower.defense.player.Player;
import com.tower.defense.tower.Factory.Tower1;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.Wave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.ListIterator;

import static com.tower.defense.wave.Wave.waveLeft;
import static com.tower.defense.wave.Wave.waveRight;


public class GameScreen implements Screen {

    private final static Logger log = LogManager.getLogger(GameScreen.class);

    private final TowerDefense game;
    private final static Queue<Packet> packetQueue = new Queue<>();
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

    //List to store all turrets
    private LinkedList turretsPlaced = new LinkedList<ITower>();

    //PopUP menu


    //Booleans to avoid creating multiple turrets by clicking once
    private boolean canDraw;
    private boolean canDelete;
    private boolean leftMouseButtonDown;
    private boolean rightMouseButtonDown;
    private Tower1 tower1;
    private Texture enemyImage;


    // this boolean determines which side of the map the player is on
    private boolean playerSide;

    private AllowedTiles allowedTiles;

    // WAVE
    private Wave wave;
    public static Player player1;
    public static Player player2;


    public GameScreen(TowerDefense game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1600, 900);
        // create stage and set it as input processor
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        this.leftMouseButtonDown = false;
        this.rightMouseButtonDown = false;
        this.canDraw = false;
        this.canDelete = false;
    }

    @Override
    public void show() {
        // loading Textures
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");
        hoveredTileTexture = new Texture(Gdx.files.internal("hovered_tile.png"));
        hoveredTileNotAllowed = new Texture(Gdx.files.internal("hovered_tile_not_allowed.png"));

        // loading the textures of the turrets
        turret1Texture = new Texture(Gdx.files.internal("turrets/turret1Texture.png"));
        turret2Texture = new Texture(Gdx.files.internal("turrets/turret2Texture.png"));

        enemyImage = new Texture(Gdx.files.internal("virus.png"));

        // getting the layers of the map
        MapLayers mapLayers = map.getLayers();
        groundLayer = (TiledMapTileLayer) mapLayers.get("ground");
        decorationLayerIndices = new int[]{mapLayers.getIndex("decoration")};

        // setting up the camera
        float width = 1600;
        float height = 900;

        camera.setToOrtho(false, width, height);
        camera.update();

        // creating the renderer
        renderer = new OrthogonalTiledMapRenderer(map);

        spriteBatch = new SpriteBatch();

        // checks which side the player is on based on boolean value
        playerSide = true;

        // setting up the font for the helper variables that show the mouse position
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2, 2);

        allowedTiles = new AllowedTiles();
        // WAVE: initiating Players and Wave


        player1 = new Player("Player1");
        player2 = new Player("Player2");
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

        // getting the current mouse position
        mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // position of the hovered tile
        hoveredTilePosition = new Vector2((int) mousePosition.x / 50, (int) mousePosition.y / 50);

        renderer.getBatch().begin();

        // rendering the groundLayer
        renderer.renderTileLayer(groundLayer);

        // temporary help
        font.draw(renderer.getBatch(), String.valueOf((int) mousePosition.x), 0, 40);
        font.draw(renderer.getBatch(), String.valueOf((int) mousePosition.y), 100, 40);
        font.draw(renderer.getBatch(), String.valueOf((int) hoveredTilePosition.x), 0, 100);
        font.draw(renderer.getBatch(), String.valueOf((int) hoveredTilePosition.y), 100, 100);
        font.draw(renderer.getBatch(), String.valueOf(screenWidth), 0, 160);
        font.draw(renderer.getBatch(), String.valueOf(screenHeight), 100, 160);
        font.draw(renderer.getBatch(), "LP: " + player1.getLifepoints(), 0, 900);
        font.draw(renderer.getBatch(), "LP: " + player2.getLifepoints(), 1400, 900);
        font.draw(renderer.getBatch(), "Money: " + player1.getWalletValue(), 0, 850);
        font.draw(renderer.getBatch(), "Money: " + player2.getWalletValue(), 1400, 850);
        font.draw(renderer.getBatch(), "Wave: " + wave.getWaveCount(), 800, 900);

        renderer.getBatch().end();

        // rendering the decoration on top of the ground tiles
        renderer.render(decorationLayerIndices);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        //creating the textures of the turrets
        turret1Texture = new Texture(Gdx.files.internal("turrets/turret1Texture.png"));
        turret2Texture = new Texture(Gdx.files.internal("turrets/turret2Texture.png"));

        //drawing the hoveredTile based on what player side you are on and whether you allowed to or not

        // drawing the hoveredTile based on what player side you are on and whether you
        // allowed to or not

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
        // WAVE: drawing the enemies
        for (Enemy enemy : waveRight) {
            spriteBatch.draw(enemyImage, enemy.getX(), enemy.getY());
        }
        for (Enemy enemy : waveLeft) {
            spriteBatch.draw(enemyImage, enemy.getX(), enemy.getY());
        }


        /**
         *Creating PopUp menu to confirm that the turret gets selled
         */


        /**
         * if statement to avoid multiple spawning of turrets by clicking the left mousebutton once
         */

        if (Gdx.input.isButtonPressed(0) && !leftMouseButtonDown) {
            canDraw = true;
        }
        /**
         * avoid multiple rightclicking
         */

        if (Gdx.input.isButtonPressed(1) && !rightMouseButtonDown) {
            canDelete = true;
        }

        /**
         * drawing the turret at the selected tile and avoid turret-stacking by removing the used tile-position from the AllowedTiles-list
         */

        if (canDraw && !leftMouseButtonDown && allowedTiles.tileInArray(hoveredTilePosition, AllowedTiles.playerOneAllowedTiles)) {

            spawnTurret1();
            AllowedTiles.playerOneAllowedTiles.remove(hoveredTilePosition);


        } else {
            canDraw = false;
        }

        /**
         * List iterator that draws all placed turrets and handles "turret removing"
         */

        ListIterator<Tower1> tower1ListIterator1 = turretsPlaced.listIterator();

        while (tower1ListIterator1.hasNext()) {


            tower1 = tower1ListIterator1.next();
            tower1.draw();

            //Output if player tries to delete the last turret

            if (canDelete && !rightMouseButtonDown && turretsPlaced.size() == 1) {
                System.out.println("You cant own 0 turrets");
            }


            if (canDelete && !rightMouseButtonDown && turretsPlaced.size() > 1) {


                System.out.println(hoveredTilePosition.x * 50 + "," + hoveredTilePosition.y * 50);


                if (tower1.getX() == hoveredTilePosition.x * 50 && tower1.getY() == hoveredTilePosition.y * 50) {


                    tower1ListIterator1.remove();
                    AllowedTiles.playerOneAllowedTiles.add(hoveredTilePosition);
                    System.out.println(turretsPlaced);

                }


            } else {
                canDelete = false;

            }


        }


        leftMouseButtonDown = Gdx.input.isButtonPressed(0);
        rightMouseButtonDown = Gdx.input.isButtonPressed(1);

        spriteBatch.end();


        // WAVE: check if we need to create a enemy
        wave.newEnemySpawn();

        // WAVE:
        // move the enemy, remove any that are beneath the bottom edge of
        // the screen or that have no more LP.

        wave.renderWave(waveLeft, player1, true);
        wave.renderWave(waveRight, player2, false);

        // END OF GAME
        if (player1.getLifepoints() <= 0) {
            player1.lost();
            game.getClient().sendPacket(new PacketInEndOfGame());
            game.setScreen(new EndScreen(game));
            log.info("set screen to {}", game.getScreen().getClass());
        }
        stage.getViewport().apply();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
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
        renderer.dispose();
    }

    /**
     * Method to spawn a Turret1 and add him to the turretsPlaced list
     */
    public void spawnTurret1() {
        Vector2 mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        tower1 = new Tower1(turret1Texture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50, 50, 50, spriteBatch);
        turretsPlaced.add(tower1);

        //turretsPlacedArray.add(tower1);

        //player1TurretsPlaced.add(tower1);
    }


    public void spawnTurret2() {

    }

    public void handlePackets() {
        if(packetQueue.isEmpty()){
            return;
        }
        while(!packetQueue.isEmpty()) {
            Packet packet = packetQueue.removeFirst();
            PacketType type = packet.getPacketType();

            log.info("Traffic: New {}", type.toString());

            switch (type) {
                case PACKETOUTSEARCHMATCH:
                case PACKETOUTMATCHFOUND:
                    break;
                case PACKETOUTCHATMESSAGE:
//                PacketOutChatMessage packetOutChatMessage = (PacketOutChatMessage) packet;
//                addMessageToBox(false, packetOutChatMessage.getText());
                    break;
                case PACKETOUTSTARTMATCH:
                    break;
                case PACKETOUTENDOFWAVE:
                    PacketOutEndOfWave packetOutEndOfWave = (PacketOutEndOfWave) packet;
                    wave.partnerWaveEnded(packetOutEndOfWave.getReward());
                    break;
                case PACKETOUTENDOFGAME:
                    player2.lost();
                    game.setScreen(new EndScreen(game));
                    log.info("set screen to {}", game.getScreen().getClass());
                    break;
                default:
                    break;

            }
        }
    }
    public static void handle(Packet packet){
        packetQueue.addFirst(packet);
    }

}
