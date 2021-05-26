package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.Enemy;

public class Enemy1 extends Enemy {
    public Enemy1(int posX, int posY, int lifepoints, int damage){
        super(posX,  posY, lifepoints, damage);
    }

    // private int movementspeed = movementspeed*1; //static movementspeed, die am
    // Anfang der Runde in der Main Klasse? gesetzt wird

}
