package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.Enemy;

public class EnemyFactory {

    public static Enemy getEnemyInstance(String difficulty, int posX, int posY,float speed) {

        switch (difficulty) {
            case "medium":
                return new Enemy2(posX, posY, 7, 2,speed);
            default:
                return new Enemy1(posX, posY, 2,10,speed);
        }
    }
}
