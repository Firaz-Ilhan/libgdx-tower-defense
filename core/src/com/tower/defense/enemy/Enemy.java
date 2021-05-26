package com.tower.defense.enemy;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    private int posX;
    private int posY;
    private int lifepoints;
    private int damage;
    private ArrayList<Vector2> wavePatternLeft;
    private ArrayList<Vector2> wavePatternRight;
    private ArrayList<Vector2> wavePattern;

    private int waypointPosition = 0;

    public Enemy(int x, int y, int lifepoints, int damage) {
        this.posX = x;
        this.posY = y;
        this.lifepoints = lifepoints;
        this.damage = damage;
        wavePatternLeft = new ArrayList<>(Arrays.asList(new Vector2(525, 525), new Vector2(325, 525),
                new Vector2(325, 225), new Vector2(425, 225), new Vector2(425, -20)));
        wavePatternRight = new ArrayList<>(Arrays.asList(new Vector2(1025, 525), new Vector2(1225, 525),
                new Vector2(1225, 225), new Vector2(1125, 225), new Vector2(1225, -20)));
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

    public Vector2 nextWaypoint(boolean playerSide){
        if(playerSide){
            wavePattern = wavePatternLeft;
        }else{
            wavePattern = wavePatternRight;
        }
        return wavePattern.get(waypointPosition);
    }

    public void advancePattern(){
        if(waypointPosition < wavePattern.size() - 1) {
            waypointPosition++;
        }
    }

}
