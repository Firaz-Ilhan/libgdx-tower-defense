package com.tower.defense.wave;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.player.Player;
import com.tower.defense.screen.GameScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
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

    ArrayList<Vector2> wavePatternLeft;
    ArrayList<Vector2> wavePatternRight;
    ArrayList<Vector2> wavePattern;



    public Wave() {
        waveLeft = new Array<Enemy>();
        waveRight = new Array<Enemy>();
        spawnEnemy();
        log.info("wave count: {}", waveCount);
        wavePatternLeft = new ArrayList<>(Arrays.asList(new Vector2(525, 525), new Vector2(325, 525),
                new Vector2(325, 225), new Vector2(425, 225), new Vector2(425, -10)));
        wavePatternRight = new ArrayList<>(Arrays.asList(new Vector2(1025, 525), new Vector2(1225, 525),
                new Vector2(1225, 225), new Vector2(1125, 225), new Vector2(1225, -10)));
    }

    public void spawnEnemy() {
        if (!pausing) {
            if (enemiesSpawned == waveSize) {
                if (waveRight.size == 0 && waveLeft.size == 0) {
                    endOfWave();
                }
            } else {
                Enemy enemyLeft = getEnemyInstance("easy", 525, 700, wavePatternLeft, wavePatternRight);
                Enemy enemyRight = getEnemyInstance("easy", 1025, 700, wavePatternLeft, wavePatternRight);
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

    // check if we need to create a new raindrop
    public void newEnemySpawn() {
        if (TimeUtils.nanoTime() - lastSpawnTime > waveSpeed) {
            spawnEnemy();
        }
    }

    public void renderWave(Array<Enemy> wave, Player player, boolean playerSide) {
        for (Iterator<Enemy> iter = wave.iterator(); iter.hasNext();) {
            Enemy enemy = iter.next();
            float positionAddAmount = enemySpeed / 25;

            Vector2 currPosition = enemy.getPosition();
            Vector2 currWaypoint = enemy.nextWaypoint(playerSide);

            if (currPosition != currWaypoint) {
                if (currPosition.y != currWaypoint.y) {
                    if (currPosition.y > currWaypoint.y) {
                        currPosition.y = enemy.getY() - positionAddAmount;
                        enemy.setPosition(currPosition);
                    } else {
                        currPosition.y = enemy.getY() + positionAddAmount;
                        enemy.setPosition(currPosition);
                    }
                } else if (currPosition.x != currWaypoint.x) {
                    if (currPosition.x > currWaypoint.x) {
                        currPosition.x = enemy.getX() - positionAddAmount;
                        enemy.setPosition(currPosition);
                        ;
                    } else {
                        currPosition.x = enemy.getX() + positionAddAmount;
                        enemy.setPosition(currPosition);
                    }
                } else {
                    enemy.advancePattern();
                }
                if (enemy.getY() < -10) {
                    player.reduceLifepoints(enemy.getDamage());
                    iter.remove();
                    if (player.getName().equals("Player1")) {
                        enemiesPastLeft++;
                    } else {
                        enemiesPastRight++;
                    }
                }
                if (enemy.getLifepoints() <= 0) {
                    iter.remove();
                }
            }
        }
        }


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
