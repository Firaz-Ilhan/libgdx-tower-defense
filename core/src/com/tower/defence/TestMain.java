package com.tower.defence;

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
import com.tower.defence.Enemy.IEnemy;
import com.tower.defence.Player.Player;

import java.util.Iterator;

import static com.tower.defence.Enemy.Factory.EnemyFactory.getEnemyInstance;

public class TestMain extends ApplicationAdapter {
    private Texture enemyImage;
    private Texture towerImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Array<IEnemy> waveLeft;
    private Array<IEnemy> waveRight;
    private long lastSpawnTime;
    private Player player1;
    private Player player2;
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
        spawnEnemy();
    }

    private void spawnEnemy() {
        IEnemy enemyLeft = getEnemyInstance("easy",220,480);
        IEnemy enemyRight = getEnemyInstance("easy",500,480);
        //enemy.width = 64;
        //enemy.height = 64;
        waveLeft.add(enemyLeft);
        waveRight.add(enemyRight);
        lastSpawnTime = TimeUtils.nanoTime();
    }

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
        if(TimeUtils.nanoTime() - lastSpawnTime > 1500000000l){
            spawnEnemy();
        }

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the latter case we play back
        // a sound effect as well.
        for (Iterator<IEnemy> iter = waveLeft.iterator(); iter.hasNext(); ) {
            IEnemy enemy = iter.next();
            int newYLocation = (int)(enemy.getY()-30 * Gdx.graphics.getDeltaTime());
            enemy.setY(newYLocation);
            if(enemy.getY() < 0 + 20) {
                iter.remove();
            }
            if(enemy.getLifepoints() <= 0) {
                iter.remove();
            }
        }
        for (Iterator<IEnemy> iter = waveRight.iterator(); iter.hasNext(); ) {
            IEnemy enemy = iter.next();
            int newYLocation = (int)(enemy.getY()-30 * Gdx.graphics.getDeltaTime());
            enemy.setY(newYLocation);
            if(enemy.getY() < 0 + 20) {
                iter.remove();
            }
            if(enemy.getLifepoints() <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        enemyImage.dispose();
        batch.dispose();
    }
}

