package com.tower.defense.wave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tower.defense.enemy.IEnemy;
import com.tower.defense.player.Player;
import com.tower.defense.screen.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;

import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;

public class Wave {
    // Arraylist of existing Enemies
    public static Array<IEnemy> waveLeft;
    public static Array<IEnemy> waveRight;
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
    private int enemySpeed = 25;
    private long timeSinceBreak;
    private boolean pausing = false;
    private final long breaktime = 10000L;

    public Wave() {
        waveLeft = new Array<IEnemy>();
        waveRight = new Array<IEnemy>();
        spawnEnemy();
    }

    public void spawnEnemy() {
        if (!pausing) {
            if (enemiesSpawned == waveSize) {
                if (waveRight.size == 0 && waveLeft.size == 0) {
                    endOfWave();
                }
            } else {
                IEnemy enemyLeft = getEnemyInstance("easy", 522, 700);
                IEnemy enemyRight = getEnemyInstance("easy", 1022, 700);
                waveLeft.add(enemyLeft);
                waveRight.add(enemyRight);
                lastSpawnTime = TimeUtils.nanoTime();
                enemiesSpawned++;
            }
        } else {
            if (TimeUtils.millis() - timeSinceBreak > breaktime) {
                pausing = false;
                waveCount++;
            }
        }
    }

    // check if we need to create a new raindrop
    public void newEnemySpawn() {
        if (TimeUtils.nanoTime() - lastSpawnTime > waveSpeed) {
            spawnEnemy();
        }
    }

    public void renderWave(boolean playerSide, Array<IEnemy> wave, Player player, ArrayList<String> wavePattern) {
        for (Iterator<IEnemy> iter = wave.iterator(); iter.hasNext();) {
            IEnemy enemy = iter.next();
            if (player.getName() == "Tester") {
                if (enemy.getY() >= 525) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                }

                else if (enemy.getX() >= 325 && enemy.getY() > 400) {
                    int newXLocation = (int) (enemy.getX() - enemySpeed / 15);
                    enemy.setX(newXLocation);
                } else if (enemy.getY() >= 225) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                } else if (enemy.getX() <= 425) {
                    int newXLocation = (int) (enemy.getX() + enemySpeed / 15);
                    enemy.setX(newXLocation);
                } else {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                }
            }

            else if (player.getName() == "Tester2") {
                if (enemy.getY() >= 525) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                } else if (enemy.getX() < 1225 && enemy.getY() > 400) {
                    enemy.setX((int) (enemy.getX() + enemySpeed / 15));
                } else if (enemy.getY() >= 225) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                } else if (enemy.getX() >= 1125) {
                    int newXLocation = (int) (enemy.getX() - enemySpeed / 15);
                    enemy.setX(newXLocation);
                } else {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                }
            }
            if (enemy.getY() < 20) {
                player.reduceLifepoints(enemy.getDamage());
                iter.remove();
                if (playerSide) {
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

    public void endOfWave() {
        GameScreen.player1.addToWallet(waveReward, enemiesPastLeft);
        GameScreen.player2.addToWallet(waveReward, enemiesPastRight);
        waveSpeed = Math.round(waveSpeed * 0.95);
        waveSize = Math.round(waveSize * 1.2);
        waveReward = (int) Math.round(waveReward * 1.5);
        enemySpeed += 5;
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

    public void doNextStep(Player player, ArrayList<String> wavePattern, IEnemy enemy) {
        int stepAmount = wavePattern.size();
        for (int currentStepPosition = 0; currentStepPosition < stepAmount; currentStepPosition++) {
            String currentStep = wavePattern.get(currentStepPosition);

            if (player.getName() == "Tester") {
                if (enemy.getY() >= 525) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed * Gdx.graphics.getDeltaTime());
                    enemy.setY(newYLocation);
                }

                else if (enemy.getX() >= 325) {
                    int newXLocation = (int) (enemy.getX() - enemySpeed * Gdx.graphics.getDeltaTime());
                    enemy.setX(newXLocation);
                } else if (enemy.getY() >= 225) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed * Gdx.graphics.getDeltaTime());
                    enemy.setY(newYLocation);
                }
            }

            else if (player.getName() == "Tester2") {
                if (enemy.getY() >= 525) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                } else if (enemy.getX() < 1225) {
                    enemy.setX((int) (enemy.getX() + enemySpeed / 15));
                } else if (enemy.getY() >= 225) {
                    int newYLocation = (int) (enemy.getY() - enemySpeed / 15);
                    enemy.setY(newYLocation);
                }

            }
        }
    }
}
