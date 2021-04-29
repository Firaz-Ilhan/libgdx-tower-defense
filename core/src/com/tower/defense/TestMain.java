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

import java.util.Iterator;


import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;

public class TestMain extends ApplicationAdapter {
    private Texture enemyImage;
    private Texture towerImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    public static Array<IEnemy> waveLeft;
    public static Array<IEnemy> waveRight;
    private long lastSpawnTime;
    public static Player player1;
    public static Player player2;
    public static int enemiesPastLeft = 0;
    public static int enemiesPastRight = 0;
    private int waveCount = 0;
    private int enemiesSpawned = 1;
    private double waveSize = 7;
    private long waveSpeed = 1000000000L;
    private int waveReward = 30;
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            endOfWave();

        }
    });
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

    /**
     * If all enemies of the current wave spawned,
     * the wave size and speed is increasing by 50% as well as the waveReward
     * after all parameters regarding the new wave is set to 0 again
     * This method must be started from a second thread
     */
    private void endOfWave(){
        while(waveRight.size!=0 && waveLeft.size != 0);
        player1.addToWallet(waveReward,enemiesPastLeft);
        player2.addToWallet(waveReward,enemiesPastRight);
        waveCount++;
        waveSpeed*=1.5;
        waveSize= Math.round(waveSize*1.5);
        waveReward =(int) Math.round(waveReward*1.5);
        enemiesSpawned=0;
        enemiesPastLeft=0;
        enemiesPastRight=0;
    }

    private void spawnEnemy() {
        if (thread.getState()== Thread.State.RUNNABLE){

        }
        else if (enemiesSpawned == waveSize) {
            thread.run();
        }else{
            IEnemy enemyLeft = getEnemyInstance("easy", 220, 480);
            IEnemy enemyRight = getEnemyInstance("easy", 500, 480);
            //enemy.width = 64;
            //enemy.height = 64;
            waveLeft.add(enemyLeft);
            waveRight.add(enemyRight);
            lastSpawnTime = TimeUtils.nanoTime();
            enemiesSpawned ++;
        }
    }
        private void renderWave(boolean left, Array<IEnemy> wave, Player player){
            for (Iterator<IEnemy> iter = wave.iterator(); iter.hasNext(); ) {
                IEnemy enemy = iter.next();
                int newYLocation = (int)(enemy.getY()-30 * Gdx.graphics.getDeltaTime());
                enemy.setY(newYLocation);
                if(enemy.getY() < 20) {
                    player.reduceLifepoints(enemy.getDamage());
                    iter.remove();
                    if(left) {
                        enemiesPastLeft++;
                    }
                    else{
                        enemiesPastRight++;
                    }
                }
                if(enemy.getLifepoints() <= 0) {
                    iter.remove();
                }
            }
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
        if(TimeUtils.nanoTime() - lastSpawnTime > waveSpeed){
            spawnEnemy();
        }

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the latter case we play back
        // a sound effect as well.
        renderWave(true,waveLeft,player1);
        renderWave(false,waveRight,player2);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        enemyImage.dispose();
        batch.dispose();
    }
}

