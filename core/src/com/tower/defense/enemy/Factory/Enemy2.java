package com.tower.defense.enemy.Factory;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.enemy.Enemy;

import java.util.ArrayList;

public class Enemy2 extends Enemy {
    ArrayList<Vector2> wavePatternLeft;
    ArrayList<Vector2> wavePatternRight;

    public Enemy2(int posX, int posY, int lifepoints, int damage, ArrayList<Vector2> wavePatternLeft, ArrayList<Vector2> wavePatternRight){
        super(posX,  posY, lifepoints, damage);

    }


    // private int movementspeed = movementspeed*1; //static movementspeed, die am
    // Anfang der Runde in der Main Klasse? gesetzt wird

}
