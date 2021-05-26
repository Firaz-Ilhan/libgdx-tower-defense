package com.tower.defense.enemy.Factory;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.enemy.Enemy;

import java.util.ArrayList;

public class EnemyFactory {

    public static Enemy getEnemyInstance(String difficulty, int posX, int posY) {

        switch (difficulty) {
            case "medium":
                return new Enemy1(posX, posY, 7, 2);
            default:
                return new Enemy1(posX, posY, 2,2);
        }
    }
}
