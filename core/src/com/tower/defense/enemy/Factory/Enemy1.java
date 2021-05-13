package com.tower.defense.enemy.Factory;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.enemy.IEnemy;

/**
 *
 */
public class Enemy1 implements IEnemy {
    private int posX;
    private int posY;
    private int lifepoints = 7;
    private int damage = 2;
    // private int movementspeed = movementspeed*1; //static movementspeed, die am
    // Anfang der Runde in der Main Klasse? gesetzt wird

    public Enemy1(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    /**
     * @return
     */
    public int getY() {
        return posY;
    }

    /**
     * @return
     */
    public int getX() {
        return posX;
    }

    /**
     * @return current location
     */
    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    /**
     * @return
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param posY
     */
    public void setY(int posY) {
        this.posY = posY;
    }

    /**
     * @param posX
     */
    public void setX(int posX) {
        this.posX = posX;
    }

    public void setPosition(Vector2 newPos) {
        this.posX = (int) newPos.x;
        this.posY = (int) newPos.y;
    }

    /**
     * @param damageReceived The damaged dealt by towers is subtracted from the
     *                       remaining LP here
     */
    public void setLifepoints(int damageReceived) {
        lifepoints -= damageReceived;
    }

    public int getLifepoints() {
        return lifepoints;
    }

}
