package com.tower.defence.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TD_map extends ApplicationAdapter{

    private TiledMap map;
    private TiledMapTileLayer groundLayer;
    private int[] decorationLayerIndices;
    
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    
    @Override
    public void create(){
        //loading the map
        map = new TmxMapLoader().load("map/TowerDefenseMapPrototype.tmx");

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
        camera.setToOrtho(false,width,height);
        camera.update();

        //creating the renderer
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(){
        //clearing the screen
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //setting the render view to the camera
        camera.update();
        renderer.setView(camera);

        //rendering the map
        renderer.getBatch().begin();
        renderer.renderTileLayer(groundLayer);
        renderer.getBatch().end();
        renderer.render(decorationLayerIndices);
    }

}
