package com.tower.defense.wave;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tower.defense.TowerDefense;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.network.packet.client.PacketEndOfWave;
import com.tower.defense.screen.GameScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

import static com.tower.defense.enemy.factory.EnemyFactory.getEnemyInstance;

public class Wave {

    // Arraylist of existing Enemies
    public Array<Enemy> waveLeft;
    public Array<Enemy> waveRight;
    private final static Logger log = LogManager.getLogger(Wave.class);
    private final TowerDefense game;


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
    }

    /**
     * checks whether wave is pausing or not.
     * If not, enemies are spawning as long as the
     * max waveSize is reached
     */
    public void spawnEnemy() {
        if (!pausing) {
            if (enemiesSpawned == waveSize) {
                if (waveRight.size == 0 && waveLeft.size == 0) {
                    log.info("all enemies disappeared");
                    endOfWave();
                }
            } else {
                Enemy enemyLeft = getEnemyInstance("easy", 525, 700, enemySpeed);
                Enemy enemyRight = getEnemyInstance("easy", 1025, 700, enemySpeed);
                waveLeft.add(enemyLeft);
                waveRight.add(enemyRight);
                lastSpawnTime = TimeUtils.nanoTime();
                enemiesSpawned++;
            }
        } else {
            endOfWave();
        }
    }

    /**
     * check if we need to create a new enemy
     */

    public void newEnemySpawn() {
        if (TimeUtils.nanoTime() - lastSpawnTime > waveSpeed) {
            spawnEnemy();
        }
    }

    /**
     * once a wave is over the players get money based on how many
     * enemies they were able to kill.
     * The waveSpeed, waveSize and waveReward all increase for
     * the next wave
     */
    public void endOfWave() {
        if (!pausing) {
            int reward = calculateReward();
            GameScreen.player.addToWallet(reward);
            if (game.getClient() != null) {
                game.getClient().sendPacket(new PacketEndOfWave(reward));
            }
            pausing = true;
            log.info("Pausing: {}", pausing);
        }
        if (partnerIsPausing) {
            waveSpeed = Math.round(waveSpeed * 0.75);
            waveSize = Math.round(waveSize * 1.1);
            waveReward = (int) Math.round(waveReward * 1.1);
            log.info("wave reward: {}", waveReward);
            enemySpeed += 4;
            log.info("enemy speed: {}", enemySpeed);
            enemiesSpawned = 0;
            enemiesPastLeft = 0;
            startWave();
        }

    }

    /**
     * Calculate earned reward
     *
     * @return
     */
    public int calculateReward() {
        waveReward = waveReward - enemiesPastLeft * 2;
        log.info("reward: {}", waveReward);
        return waveReward;
    }

    /**
     * is called by Influence
     * iterates through wave and calls healAndBuff()
     * in Enemy.Class
     *
     * @param own whether it's your own wave or not
     */
    public void healAndBuffWave(boolean own) {
        Array<Enemy> wave;
        if (own) {
            wave = waveLeft;
        } else {
            wave = waveRight;
        }
        for (Iterator<Enemy> iter = wave.iterator(); iter.hasNext(); ) {
            Enemy enemy = iter.next();
            enemy.healAndBuff();
        }
    }

    /**
     * for displaying which wave this is
     *
     * @return waveCount
     */
    public int getWaveCount() {
        return waveCount;
    }

    /**
     * is called by the handle() method if a EndOfWave packet was received
     *
     * @param reward int: End of Wave REward of opponent
     */
    public void partnerWaveEnded(int reward) {
        log.info("partners wave ended");
        GameScreen.opponent.addToWallet(reward);
        partnerIsPausing = true;
    }

    /**
     * is called in EndOfWave()
     * sets pausing false and increases WaveCount
     */
    public void startWave() {
        partnerIsPausing = false;
        pausing = false;
        waveCount++;
    }

    public void enemyPassed() {
        enemiesPastLeft++;
    }

    public TowerDefense getGame() {
        return game;
    }
}

