package com.tower.defense;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import com.tower.defense.enemy.IEnemy;
import com.tower.defense.player.Player;
import com.tower.defense.wave.Wave;

import java.util.Iterator;


import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;
import static com.tower.defense.wave.Wave.waveLeft;
import static com.tower.defense.wave.Wave.waveRight;

public class TestMain extends ApplicationAdapter {
    private Texture enemyImage;
    private Texture towerImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Wave wave;
    public static Player player1;
    public static Player player2;

    @Override
    public void create() {
        player1 = new Player("Tester",true, false);
        player1 = new Player("Tester2",false, true);
        // load the images for the enemy and the Towers
        enemyImage = new Texture(Gdx.files.internal("core/assets/drop.png"));
        towerImage = new Texture(Gdx.files.internal("core/assets/drop.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // create the enemy array and spawn the first enemy
        waveLeft = new Array<IEnemy>();
        waveRight = new Array<IEnemy>();
        wave.spawnEnemy();
    }

    /**
     * If all enemies of the current wave spawned,
     * the wave size and speed is increasing by 50% as well as the waveReward
     * after all parameters regarding the new wave is set to 0 again
     * This method must be started from a second thread
     */


    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();
        for(IEnemy enemy: waveRight) {
            batch.draw(enemyImage, enemy.getX(), enemy.getY());
        }
        for(IEnemy enemy: waveLeft) {
            batch.draw(enemyImage, enemy.getX(), enemy.getY());
        }
        batch.end();

        // process user input

        // check if we need to create a new raindrop
        wave.newEnemySpawn();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the latter case we play back
        // a sound effect as well.
        wave.renderWave(true,waveLeft,player1);
        wave.renderWave(false,waveRight,player2);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        enemyImage.dispose();
        batch.dispose();
    }
}

