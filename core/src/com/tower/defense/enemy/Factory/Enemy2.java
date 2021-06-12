package com.tower.defense.enemy.Factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tower.defense.enemy.Enemy;



public class Enemy2 extends Enemy {

    private int posX;
    private int posY;
    private int lifepoints = 10;
    private int damage = 5;
    //private int movementspeed = movementspeed*1; //static movementspeed, die am Anfang der Runde in der Main Klasse? gesetzt wird
    private final static Logger log = LogManager.getLogger(Enemy2.class);
    public Enemy2(int posX, int posY, int lifepoints, int damage){
        super(posX,  posY, lifepoints, damage);

    }

    public void setY(int posY) {
        this.posY = posY;
    }

    public void setLifepoints(int damageReceived) {
        if (damageReceived > 0) {
            lifepoints -= damageReceived;
        }else{
            log.info("You can not make a negative damage");
        }
    }

    public int getLifepoints() {
        return lifepoints;
    }

}
