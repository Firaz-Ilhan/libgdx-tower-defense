package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.IEnemy;

public class EnemyFactory {
    public static IEnemy getEnemyInstance(String difficulty, int x, int y) {

        switch (difficulty) {
            case "medium":
                return new Enemy2(x, y);
            default:
                return new Enemy1(x, y);
        }
    }
}
