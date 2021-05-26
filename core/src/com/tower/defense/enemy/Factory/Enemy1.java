package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.Enemy;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;


/**
 *
 */
public class Enemy1 extends Enemy {
    ArrayList<Vector2> wavePatternLeft;
    ArrayList<Vector2> wavePatternRight;

    public Enemy1(int posX, int posY, int lifepoints, int damage, ArrayList<Vector2> wavePatternLeft, ArrayList<Vector2> wavePatternRight){
        super(posX,  posY, lifepoints, damage,wavePatternLeft,wavePatternRight);

    }

    // private int movementspeed = movementspeed*1; //static movementspeed, die am
    // Anfang der Runde in der Main Klasse? gesetzt wird

}
