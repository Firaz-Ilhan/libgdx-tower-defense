package com.tower.defense.enemy;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.enemy.Factory.Enemy1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public abstract class Enemy {
    private float posX;
    private float posY;
    private int lifepoints;
    private float speed;
    private int damage;

    // ArrayLists that are used to tell an Enemy what wayPoints they have
    // to follow when navigating the map
    private ArrayList<Vector2> wavePatternLeft;
    private ArrayList<Vector2> wavePatternRight;
    private ArrayList<Vector2> wavePattern;

    // the waypointPosition is the position of that waypoint in the
    // wavePattern the enemy currently moves to
    private int waypointPosition = 0;
    private final static Logger log = (Logger) LogManager.getLogger(Enemy.class);

    public Enemy(float x, float y, int lifepoints, int damage, float speed) {
        this.posX = x;
        this.posY = y;
        this.lifepoints = lifepoints;
        this.damage = damage;
        this.speed = speed;
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
     * @param posY sets the enemy's Y position
     */
    public void setY(float posY) {
        this.posY = posY;
    }

    /**
     * @param posX sets the enemy's X position
     */
    public void setX(float posX) {
        this.posX = posX;
    }

    /**
     * @param newPos sets the enemy's new position (both x and y)
     */
    public void setPosition(Vector2 newPos) {
        this.posX = newPos.x;
        this.posY = newPos.y;
    }

    /**
     * @param damageReceived The damaged dealt by towers is subtracted from the
     *                       remaining LP here
     */
    public void setLifepoints(int damageReceived) {
        if (damageReceived > 0) {
            lifepoints -= damageReceived;
        } else {
            log.info("You can not deal negative damage");
        }

    }

    /**
     * @return lifepoints of the enemy
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     *
     * @return this.speed
     */
    public float getSpeed(){
        return speed;
    }

    /**
     * @param playerSide to determine which wavePattern has to be used
     * @return
     * the waypoint the enemy has to move towards currently
     */
    public Vector2 nextWaypoint(boolean playerSide) {
        if (playerSide) {
            wavePattern = wavePatternLeft;
        } else {
            wavePattern = wavePatternRight;
        }
        return wavePattern.get(waypointPosition);
    }

    /**
     * advances the wavypointPosition by one to move to the next wayPoint
     */
    public void advancePattern() {
        if (waypointPosition < wavePattern.size() - 1) {
            waypointPosition++;
        }
    }

}
