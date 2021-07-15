package com.tower.defense.enemy.factory;

import com.tower.defense.enemy.Enemy;

public class EnemyFactory {
    //prevents the class from being instantiated
    private EnemyFactory() {
    }

    /**
     * to get a enemy you usually call an instance
     * with a given difficulty and a speed
     *
     * @param difficulty
     * @param posX
     * @param posY
     * @param speed
     * @return
     */

    public static Enemy getEnemyInstance(String difficulty, int posX, int posY, float speed) {

        switch (difficulty) {
            case "medium":
                return new Enemy1(posX, posY, 10, 20, speed);
            default:
                return new Enemy1(posX, posY, 5, 10, speed);
        }
    }
}
