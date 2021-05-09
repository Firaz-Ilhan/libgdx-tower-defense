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
import com.tower.defense.helper.AllowedTiles;
import com.tower.defense.tower.Factory.Tower1;
import com.tower.defense.tower.Factory.Tower2;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    private final TowerDefense game;
    private final Stage stage;
    private TiledMap map;
    private TiledMapTileLayer groundLayer;
    private int[] decorationLayerIndices;

    private OrthographicCamera camera;
    private final ScalingViewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch spriteBatch;

    private Vector2 mousePosition;
    private Vector2 hoveredTilePosition;

    //Help variables to show mouse position
    private BitmapFont font;

    private Texture hoveredTileTexture;
    private Texture hoveredTileNotAllowed;
    private Texture turret1Texture;
    private Texture turret2Texture;

    private LinkedList turretsPlaced = new LinkedList();

    //Booleans to avoid creating multiple turrets by clicking once
    private boolean canDraw;
    private boolean leftMouseButtonDown;

    private Tower1 tower1;
    private Tower2 tower2;



    private boolean playerSide;

    private AllowedTiles allowedTiles;

    private int screenHeight;
    private int screenWidth;

    public GameScreen(TowerDefense game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1600, 900);
        //create stage and set it as input processor
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        this.leftMouseButtonDown = false;
        this.canDraw = false;

    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");
        hoveredTileTexture = new Texture(Gdx.files.internal("hovered_tile.png"));
        hoveredTileNotAllowed = new Texture(Gdx.files.internal("hovered_tile_not_allowed.png"));
        //temporary
        //turret = new Texture(Gdx.files.internal("turret.png"));

        //getting the layers of the map
        MapLayers mapLayers = map.getLayers();
        groundLayer = (TiledMapTileLayer) mapLayers.get("ground");
        decorationLayerIndices = new int[]{
                mapLayers.getIndex("decoration")
        };

        //setting up the camera
        float width = 1600;
        float height = 900;


        camera.setToOrtho(false, width, height);
        camera.update();

        //creating the renderer
        renderer = new OrthogonalTiledMapRenderer(map);

        spriteBatch = new SpriteBatch();

        //checks which side the player is on based on boolean value
        playerSide = true;

        //setting up the font for the helper variables that show the mouse position
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2, 2);

        allowedTiles = new AllowedTiles();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //setting the render view to the camera
        camera.update();
        renderer.setView(camera);

        //getting the current mouse position
        mousePosition = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        //position of the hovered tile
        hoveredTilePosition = new Vector2((int) mousePosition.x / 50, (int) mousePosition.y / 50);

        renderer.getBatch().begin();

        //rendering the groundLayer
        renderer.renderTileLayer(groundLayer);

        //temporary help
        font.draw(renderer.getBatch(), String.valueOf((int)mousePosition.x), 0, 40);
        font.draw(renderer.getBatch(), String.valueOf((int)mousePosition.y), 100, 40);
        font.draw(renderer.getBatch(), String.valueOf((int)hoveredTilePosition.x), 0, 100);
        font.draw(renderer.getBatch(), String.valueOf((int)hoveredTilePosition.y), 100, 100);
        font.draw(renderer.getBatch(), String.valueOf(screenWidth), 0, 160);
        font.draw(renderer.getBatch(), String.valueOf(screenHeight), 100, 160);

        renderer.getBatch().end();

        //rendering the decocation on top of the ground tiles
        renderer.render(decorationLayerIndices);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);


        //creating the textures of the turrets
        turret1Texture = new Texture(Gdx.files.internal("turrets/turret1Texture.png"));
        turret2Texture = new Texture(Gdx.files.internal("turrets/turret2Texture.png"));


        //drawing the hoveredTile based on what player side you are on and whether you allowed to or not
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



        //if statement to avoid multiple spawning of turrets by clicking the left mousebutton once;
        if (Gdx.input.isButtonPressed(0) && !leftMouseButtonDown) {
            canDraw = true;
        }


        //drawing the turret at the selected tile and avoid turret-stacking by removing the used tile-position from the AllowedTiles-list
        if (canDraw && !leftMouseButtonDown && allowedTiles.tileInArray(hoveredTilePosition, AllowedTiles.playerOneAllowedTiles)) {
            spawnTurret1();
            System.out.println("tower placed");
            AllowedTiles.playerOneAllowedTiles.remove(hoveredTilePosition);
        } else {
            canDraw = false;
        }

        //iterator to draw all currently stored turrets of kind 1

        ListIterator<Tower1> tower1ListIterator = turretsPlaced.listIterator();

        while (tower1ListIterator.hasNext()) {
            tower1 = tower1ListIterator.next();
            tower1.draw();
        }

        leftMouseButtonDown = Gdx.input.isButtonPressed(0);


        spriteBatch.end();
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
        map.dispose();
        game.dispose();
        stage.dispose();
    }

    /**
     * Method to spawn a Turret1 and add him to the turretsPlaced list
     */
    public void spawnTurret1() {
        turretsPlaced.add(new Tower1(turret1Texture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50, 50, 50, spriteBatch));
    }

    public void spawnTurret2() {

    }
}
