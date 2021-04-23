package com.tower.defence.Enemy.Factory;

import com.tower.defence.Enemy.IEnemy;

/**
 * 
 */
public class Enemy1 implements IEnemy {
    private int x;
    private int y;
    private int lifepoints = 7;
    private int damage = 2;
    //private int movementspeed = movementspeed*1; //static movementspeed, die am Anfang der Runde in der Main Klasse? gesetzt wird

    public Enemy1(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }
    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }
    /**
     *
     * @return
     */
    public int getDamage() {
        return damage;
    }

    /**
     *
     * @param y
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     *
     * @param damageReceived
     * The damaged dealt by towers is subtracted from the remaining LP here
     */
    public void setLifepoints(int damageReceived){
        lifepoints -= damageReceived;
    }

    public int getLifepoints(){
        return lifepoints;
    }
}
