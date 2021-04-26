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

public class GameScreen implements Screen{

    private final TowerDefense game;
    private final Stage stage;
    private TiledMap map;
    private TiledMapTileLayer groundLayer;
    private int[] decorationLayerIndices;

    private OrthographicCamera camera;
    private ScalingViewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch spriteBatch;

    private Vector2 mousePosition;
    private Vector2 hoveredTilePosition;

    //Help variables to show mouse position
    private BitmapFont font;

    private Texture hoveredTileTexture;
    private Texture hoveredTileNotAllowed;
    //private Texture turret;

    private boolean playerSide;

    private AllowedTiles allowedTiles;

    private int screenHeight;
    private int screenWidth;

    public GameScreen(TowerDefense game) {
        this.game = game;
        viewport = new FitViewport(1600, 900);
        //create stage and set it as input processor
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
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
        
        camera = new OrthographicCamera();
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

        //position is required to properly calculate the hovered tile
        Vector2 position = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        //getting the current mouse position                  
        mousePosition = new Vector2((int)position.x - (screenWidth - 1600) / 1600,
                                     (int)position.y - (screenHeight - 900) / 900);

        //position of the hovered tile
        hoveredTilePosition = new Vector2((int) mousePosition.x / 50, (int) mousePosition.y / 50);

        renderer.getBatch().begin();

        //rendering the groundLayer
        renderer.renderTileLayer(groundLayer);
        
        //temporary help
        font.draw(renderer.getBatch(),String.valueOf(mousePosition.x),0,40);
        font.draw(renderer.getBatch(),String.valueOf(mousePosition.y),100,40);
        font.draw(renderer.getBatch(),String.valueOf(hoveredTilePosition.x),0,100);
        font.draw(renderer.getBatch(),String.valueOf(hoveredTilePosition.y),100,100);
        font.draw(renderer.getBatch(),String.valueOf(screenWidth),0,160);
        font.draw(renderer.getBatch(),String.valueOf(screenHeight),100,160);

        renderer.getBatch().end();
        
        //rendering the decocation on top of the ground tiles
        renderer.render(decorationLayerIndices);

        //drawing the hoveredTile based on what player side you are on and whether you allowed to or not
        spriteBatch.begin();

        if(playerSide == true){
            if(allowedTiles.tileInArray(hoveredTilePosition,AllowedTiles.playerOneAllowedTiles) == true){
                spriteBatch.draw(hoveredTileTexture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }

            else{
                spriteBatch.draw(hoveredTileNotAllowed, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }
        }
        else{
            if(allowedTiles.tileInArray(hoveredTilePosition,AllowedTiles.playerTwoAllowedTiles) == true){
                spriteBatch.draw(hoveredTileTexture, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }
            else{
                spriteBatch.draw(hoveredTileNotAllowed, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            }
        }

        spriteBatch.end();

        //temporary left click method to show the turret
        /*if(Gdx.input.isButtonPressed(Buttons.LEFT)){
            spriteBatch.begin();
            spriteBatch.draw(turret, hoveredTilePosition.x * 50, hoveredTilePosition.y * 50);
            spriteBatch.end();
        } */ 

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
}
