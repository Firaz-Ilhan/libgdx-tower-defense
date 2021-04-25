package com.tower.defense.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tower.defense.TowerDefense;

public class GameScreen implements Screen{

    private final TowerDefense game;
    private final Stage stage;
    private TiledMap map;
    private TiledMapTileLayer groundLayer;
    private int[] decorationLayerIndices;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch spriteBatch;

    private Vector2 mousePosition;

    private int tileX;
    private int tileY;

    //Help variables to show mouse position
    private BitmapFont font;

    private Texture hoveredTile;
    private Texture turret;

    public GameScreen(TowerDefense game) {
        this.game = game;
        //create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");
        hoveredTile = new Texture(Gdx.files.internal("hovered_tile.png"));
        //temporary
        turret = new Texture(Gdx.files.internal("turret.png"));

        //getting the layers of the map
        MapLayers mapLayers = map.getLayers();
        groundLayer = (TiledMapTileLayer) mapLayers.get("ground");
        decorationLayerIndices = new int[]{
                mapLayers.getIndex("decoration")
        };

        //setting up the camera
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();

        //creating the renderer
        renderer = new OrthogonalTiledMapRenderer(map);

        spriteBatch = new SpriteBatch();

        //setting up the font for the helper variables that show the mouse position
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2, 2);
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
        mousePosition = new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY());

        //x position of hovered tile
        tileX = (int) mousePosition.x / 50;
        //y position of hovered tile
        tileY = (int) mousePosition.y / 50;

        renderer.getBatch().begin();

        //rendering the groundLayer
        renderer.renderTileLayer(groundLayer);
        //temporary help
        font.draw(renderer.getBatch(),String.valueOf(mousePosition.x),0,40);
        font.draw(renderer.getBatch(),String.valueOf(mousePosition.y),100,40);
        font.draw(renderer.getBatch(),String.valueOf(tileX),0,100);
        font.draw(renderer.getBatch(),String.valueOf(tileY),100,100);

        renderer.getBatch().end();
        
        //rendering the decocation on top of the ground tiles
        renderer.render(decorationLayerIndices);

        //rendering the hoveredTile visually on top of all tiles
        spriteBatch.begin();
        spriteBatch.draw(hoveredTile, tileX * 50, tileY * 50);
        spriteBatch.end();

        //temporary left click method to show the turret
        if(Gdx.input.isButtonPressed(Buttons.LEFT)){
            spriteBatch.begin();
            spriteBatch.draw(turret, tileX * 50, tileY * 50);
            spriteBatch.end();
        }  

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
