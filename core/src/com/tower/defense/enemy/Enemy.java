package com.tower.defense.enemy;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    private int posX;
    private int posY;
    private int lifepoints;
    private int damage;
    private ArrayList<Vector2> wavePatternLeft;
    private ArrayList<Vector2> wavePatternRight;
    private ArrayList<Vector2> wavePattern;

    private Vector2 currentPosition;
    private int waypointPosition = 0;

    public Enemy(int x, int y, int lifepoints, int damage, ArrayList<Vector2> wavePatternLeft, ArrayList<Vector2> wavePatternRight) {
        this.posX = x;
        this.posY = y;
        this.lifepoints = lifepoints;
        this.damage = damage;
        this.wavePatternLeft = wavePatternLeft;
        this.wavePatternRight = wavePatternRight;
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
        currentPosition = new Vector2(getX(), getY());
        return currentPosition;
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

    public Vector2 nextWaypoint(boolean playerSide){
        if(playerSide){
            wavePattern = wavePatternLeft;
        }else{
            wavePattern = wavePatternRight;
        }
        return wavePattern.get(waypointPosition);
    }

    public void advancePattern(){
        if(waypointPosition < wavePattern.size()) {
            waypointPosition++;
        }
    }

}
