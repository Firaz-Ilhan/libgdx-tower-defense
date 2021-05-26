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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.tower.defense.TowerDefense;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.helper.AllowedTiles;
import com.tower.defense.player.Player;
import com.tower.defense.wave.Wave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tower.defense.wave.Wave.waveLeft;
import static com.tower.defense.wave.Wave.waveRight;

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

    // Help variables to show mouse position
    private BitmapFont font;

    private Texture hoveredTileTexture;
    private Texture hoveredTileNotAllowed;

    private Texture enemyImage;

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
    }

    @Override
    public void show() {
        // Loading Textures
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");
        hoveredTileTexture = new Texture(Gdx.files.internal("hovered_tile.png"));
        hoveredTileNotAllowed = new Texture(Gdx.files.internal("hovered_tile_not_allowed.png"));

        enemyImage = new Texture(Gdx.files.internal("virus.png"));

        // getting the layers of the map
        MapLayers mapLayers = map.getLayers();
        groundLayer = (TiledMapTileLayer) mapLayers.get("ground");
        decorationLayerIndices = new int[] { mapLayers.getIndex("decoration") };

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
        wave = new Wave();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // setting the render view to the camera
        camera.update();
        renderer.setView(camera);

        // getting the current mouse position
        Vector2 mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // position of the hovered tile
        Vector2 hoveredTilePosition = new Vector2((int) mousePosition.x / 50, (int) mousePosition.y / 50);

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

        // rendering the decocation on top of the ground tiles
        renderer.render(decorationLayerIndices);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

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

        spriteBatch.end();

        // WAVE: check if we need to create a enemy
        wave.newEnemySpawn();

        // WAVE:
        // move the enemy, remove any that are beneath the bottom edge of
        // the screen or that have no more LP.

        if (playerSide) {
            wave.renderWave(waveLeft, player1, playerSide);
        }
        wave.renderWave(waveRight, player2, playerSide);

        // END OF GAME
        if (player1.getLifepoints() <= 0 || player2.getLifepoints() <= 0) {
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
    }
}
