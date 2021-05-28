package com.tower.defense.wave;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.player.Player;
import com.tower.defense.screen.GameScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;

public class Wave {

    private final static Logger log = LogManager.getLogger(Wave.class);

    // Arraylist of existing Enemies
    public static Array<Enemy> waveLeft;
    public static Array<Enemy> waveRight;
    // lastSpawnTime is checked before creating an enemy
    private long lastSpawnTime;
    private int enemiesPastLeft = 0;
    private int enemiesPastRight = 0;
    // w
    private int waveCount = 1;
    private int enemiesSpawned = 0;
    private double waveSize = 5;
    // waveSpeed is compared to lastSpawnTime,
    // if its high it takes longer to spawn a new enemy
    private long waveSpeed = 2000000000L;
    private int waveReward = 30;
    private float enemySpeed = 25;
    private long timeSinceBreak;
    private boolean pausing = false;
    private final long breaktime = 10000L;

    public Wave() {
        waveLeft = new Array<Enemy>();
        waveRight = new Array<Enemy>();
        spawnEnemy();
        log.info("wave count: {}", waveCount);
    }

    public void spawnEnemy() {
        if (!pausing) {
            if (enemiesSpawned == waveSize) {
                if (waveRight.size == 0 && waveLeft.size == 0) {
                    endOfWave();
                }
            } else {
                Enemy enemyLeft = getEnemyInstance("easy", 525, 700);
                Enemy enemyRight = getEnemyInstance("easy", 1025, 700);
                waveLeft.add(enemyLeft);
                waveRight.add(enemyRight);
                lastSpawnTime = TimeUtils.nanoTime();
                enemiesSpawned++;
            }
        } else {
            if (TimeUtils.millis() - timeSinceBreak > breaktime) {
                pausing = false;
                waveCount++;
                log.info("wave count: {}", waveCount);
            }
        }
    }

    // check if we need to create a new enemy
    public void newEnemySpawn() {
        if (TimeUtils.nanoTime() - lastSpawnTime > waveSpeed) {
            spawnEnemy();
        }
    }

    // renders each wave of enemies based on the player side
    public void renderWave(Array<Enemy> wave, Player player, boolean playerSide) {

        // with an iterator it goes through each wave step by step
        for (Iterator<Enemy> iter = wave.iterator(); iter.hasNext(); ) {
            Enemy enemy = iter.next();

            // distance added with each frame
            float positionAddAmount = enemySpeed / 25;

            Vector2 currentEnemyPosition = enemy.getPosition();

            // the next waypoint the enemy will move to
            Vector2 nextWantedWaypoint = enemy.nextWaypoint(playerSide);

            // only if the enemy's current position isn't the same as the desired waypoint
            // it will move towards it based on which coordinate (x and/or y) is wrong
            if (currentEnemyPosition.y != nextWantedWaypoint.y) {
                if (currentEnemyPosition.y > nextWantedWaypoint.y) {
                    if(currentEnemyPosition.y - positionAddAmount < nextWantedWaypoint.y){
                        currentEnemyPosition.y = nextWantedWaypoint.y;
                        enemy.setPosition(currentEnemyPosition);
                    }
                    else {
                        currentEnemyPosition.y = enemy.getY() - positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                } else {
                    if(currentEnemyPosition.y + positionAddAmount > nextWantedWaypoint.y){
                        currentEnemyPosition.y = nextWantedWaypoint.y;
                        enemy.setPosition(currentEnemyPosition);
                    }else {
                        currentEnemyPosition.y = enemy.getY() + positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                }
            } else if (currentEnemyPosition.x != nextWantedWaypoint.x) {
                if (currentEnemyPosition.x > nextWantedWaypoint.x) {
                    if(currentEnemyPosition.x - positionAddAmount < nextWantedWaypoint.x){
                        currentEnemyPosition.x = nextWantedWaypoint.x;
                        enemy.setPosition(currentEnemyPosition);
                    }else {
                        currentEnemyPosition.x = enemy.getX() - positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                } else {
                    if(currentEnemyPosition.x + positionAddAmount > nextWantedWaypoint.x){
                        currentEnemyPosition.x = nextWantedWaypoint.x;
                        enemy.setPosition(currentEnemyPosition);
                    }else {
                        currentEnemyPosition.x = enemy.getX() + positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                }

                // if the positions of enemy and waypoint are the same
                // the wavePattern will progress to the next waypoint
            } else {
                enemy.advancePattern();
            }

            // if an enemy reaches the bottom edge of the map it gets
            // removed and the player looses health points based
            // on the enemy's damage
            if (enemy.getY() < -10) {
                player.reduceLifepoints(enemy.getDamage());
                iter.remove();
                if (player.getName().equals("Player1")) {
                    enemiesPastLeft++;
                } else {
                    enemiesPastRight++;
                }
            }

            // if the lifepoints of an enemy are reduced
            // to 0 it get's removed
            if (enemy.getLifepoints() <= 0) {
                iter.remove();
            }
        }
    }


    // once a wave is over the players get money based on how many
    // enemies they were able to kill.
    // The waveSpeed, waveSize and waveReward all increase for
    // the next wave
    public void endOfWave() {
        GameScreen.player1.addToWallet(waveReward, enemiesPastLeft);
        GameScreen.player2.addToWallet(waveReward, enemiesPastRight);
        waveSpeed = Math.round(waveSpeed * 0.95);
        waveSize = Math.round(waveSize * 1.2);
        waveReward = (int) Math.round(waveReward * 1.5);
        log.info("wave reward: {}", waveReward);
        enemySpeed += 5;
        log.info("enemy speed: {}", enemySpeed);
        enemiesSpawned = 0;
        enemiesPastLeft = 0;
        enemiesPastRight = 0;
        pausing = true;
        timeSinceBreak = TimeUtils.millis();
    }

    // for displaying which wave this is
    public int getWaveCount() {
        return waveCount;
    }
}
