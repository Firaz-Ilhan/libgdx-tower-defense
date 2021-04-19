package com.tower.defence.Enemy.Factory;

import com.tower.defence.Enemy.IEnemy;

public class EnemyFactory {
    public static IEnemy getInstance(String difficulty, int x, int y){

        switch (difficulty){
            case "medium":
                return new Enemy2(x,y);
            default:
                return new Enemy1(x,y);
        }
    }
}
