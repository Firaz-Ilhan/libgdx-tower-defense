package com.tower.defense.tower.Factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tower.defense.tower.ITower;

import java.util.concurrent.TimeoutException;

public class Tower1 implements ITower {

    private float x;
    private float y;
    boolean is_attacking;
    private double damage = 1;
    private double firerate = 1;
    private double range = 50;
    private int cost = 20;
    private Texture turretTexture;
    private float width, height;
    private SpriteBatch spriteBatch;

    public Tower1(Texture turretTexture,float x, float y, int width, int height, SpriteBatch batch) {
        this.x = x;
        this.y = y;
        this.is_attacking = false;
        this.turretTexture = turretTexture;
        this.width = width;
        this.height = height;
        this.spriteBatch = batch;
    }



    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setIs_attacking(boolean is_attacking) {
        this.is_attacking = is_attacking;
    }

    public double getDamage() {
        return damage;
    }

    public double getFirerate() {
        return firerate;
    }

    public double getRange() {
        return range;
    }

    public int getCost() {
        return cost;
    }


    /**
     * draw-method for Tower1
     */
    public void draw() {
        spriteBatch.draw(turretTexture, x, y, width, height);
    }
}
