package com.tower.defense.wave;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tower.defense.TowerDefense;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.network.packet.client.PacketInEndOfWave;
import com.tower.defense.screen.GameScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;

public class Wave {

    private final static Logger log = LogManager.getLogger(Wave.class);
    private final TowerDefense game;
    // Arraylist of existing Enemies
    public Array<Enemy> waveLeft;
    public Array<Enemy> waveRight;
    // lastSpawnTime is checked before creating an enemy
    private long lastSpawnTime;
    private int enemiesPastLeft = 0;
    // w
    private int waveCount = 1;
    private int enemiesSpawned = 0;
    private double waveSize = 5;
    // waveSpeed is compared to lastSpawnTime,
    // if its high it takes longer to spawn a new enemy
    private long waveSpeed = 2000000000L;
    private int waveReward = 30;
    private float enemySpeed = 25;
    private boolean pausing = false;
    private boolean partnerIsPausing = false;

    public Wave(final TowerDefense game) {
        this.game = game;
        waveLeft = new Array<Enemy>();
        waveRight = new Array<Enemy>();
        spawnEnemy();
        log.info("wave count: {}", waveCount);
    }

    public void spawnEnemy() {
        if (!pausing) {
            if (enemiesSpawned == waveSize) {
                if (waveRight.size == 0 && waveLeft.size == 0) {
                    log.info("all enemies disappeared");
                    endOfWave();
                }
            } else {
                Enemy enemyLeft = getEnemyInstance("easy", 525, 700,enemySpeed);
                Enemy enemyRight = getEnemyInstance("easy", 1025, 700,enemySpeed);
                waveLeft.add(enemyLeft);
                waveRight.add(enemyRight);
                lastSpawnTime = TimeUtils.nanoTime();
                enemiesSpawned++;
            }
        }
        else{
            endOfWave();
        }
    }

    // check if we need to create a new enemy
    public void newEnemySpawn() {
        if (TimeUtils.nanoTime() - lastSpawnTime > waveSpeed) {
            spawnEnemy();
        }
    }


    // once a wave is over the players get money based on how many
    // enemies they were able to kill.
    // The waveSpeed, waveSize and waveReward all increase for
    // the next wave
    public void endOfWave() {
        if(!pausing) {
            int reward = calculateReward();
            GameScreen.player1.addToWallet(reward);
            game.getClient().sendPacket(new PacketInEndOfWave(reward));
            pausing = true;
            log.info("Pausing: {}",pausing);
        }
        if(partnerIsPausing){
            waveSpeed = Math.round(waveSpeed * 0.75);
            waveSize = Math.round(waveSize * 1.1);
            waveReward = (int) Math.round(waveReward * 1.5);
            log.info("wave reward: {}", waveReward);
            enemySpeed += 5;
            log.info("enemy speed: {}", enemySpeed);
            enemiesSpawned = 0;
            enemiesPastLeft = 0;
            startWave();
        }

    }
    public int calculateReward() {
        waveReward = waveReward - enemiesPastLeft * 2;
        log.info("reward: {}", waveReward);
        return waveReward;
    }

    // for displaying which wave this is
    public int getWaveCount() {
        return waveCount;
    }

    //is called by the handle() method if a EndOfWave packet was received
    public void partnerWaveEnded(int reward){
            log.info("partners wave ended");
            GameScreen.player2.addToWallet(reward);
            partnerIsPausing = true;
    }
    //is called in EndOfWave()
    public void startWave(){
        partnerIsPausing =false;
        pausing = false;
        waveCount++;
    }
    public void enemyPassed(){
        enemiesPastLeft++;
    }
    public TowerDefense getGame(){
        return game;
    }
}

