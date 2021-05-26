package com.tower.defense.enemy.Factory;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.enemy.Enemy;

import java.util.ArrayList;

public class EnemyFactory {

    public static Enemy getEnemyInstance(String difficulty, int posX, int posY, ArrayList<Vector2> wavePatternLeft, ArrayList<Vector2> wavePatternRight) {

        switch (difficulty) {
            case "medium":
                return new Enemy1(posX, posY, 7, 2, wavePatternLeft, wavePatternRight);
            default:
                return new Enemy1(posX, posY, 2,2,wavePatternLeft, wavePatternRight);
        }
    }
}
