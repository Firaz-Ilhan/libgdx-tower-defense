package com.tower.defense.enemy;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
    private int posX;
    private int posY;
    private int lifepoints;
    private int damage;

    // ArrayLists that are used to tell an Enemy what wayPoints they have
    // to follow when navigating the map
    private ArrayList<Vector2> wavePatternLeft;
    private ArrayList<Vector2> wavePatternRight;
    private ArrayList<Vector2> wavePattern;

    // the waypointPosition is the position of that waypoint in the
    // wavePattern the enemy currently moves to
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
     * @return Y position
     */
    public float getY() {
        return posY;
    }

    /**
     * @return X position
     */
    public float getX() {
        return posX;
    }

    /**
     * @return current location as a Vector2
     */
    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    /**
     * @return the damage the enemy deals
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param posY
     * sets the enemy's Y position
     */
    public void setY(int posY) {
        this.posY = posY;
    }

    /**
     * @param posX
     * sets the enemy's X position
     */
    public void setX(int posX) {
        this.posX = posX;
    }

    /**
     *
     * @param newPos
     * sets the enemy's new position (both x and y)
     */
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

    /**
     *
     * @return lifepoints of the enemy
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     *
     * @param playerSide to determine which wavePattern has to be used
     * @return the waypoint the enemy has to move towards currently
     */
    public Vector2 nextWaypoint(boolean playerSide){
        if(playerSide){
            wavePattern = wavePatternLeft;
        }else{
            wavePattern = wavePatternRight;
        }
        return wavePattern.get(waypointPosition);
    }

    /**
     * advances the wavypointPosition by one to move to the next wayPoint
     */
    public void advancePattern(){
        if(waypointPosition < wavePattern.size() - 1) {
            waypointPosition++;
        }
    }

}
