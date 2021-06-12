package com.tower.defense.tower.Factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.Wave;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Tower1 implements ITower {

    private float x;
    private float y;
    boolean is_attacking;
    private double damage = 1;
    private double firerate = 1;
    private double range = 150;
    private int cost = 100;
    private Texture turretTexture;
    private float width, height;
    private SpriteBatch spriteBatch;
    protected List<Enemy> Targetlist;
    protected HashMap <Enemy,Float> enemyMap;
    private float firedelay;

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


    public void updateTargetarray(){
        enemyMap = new HashMap<>();
        for (int i = 0; i < Wave.waveLeft.size; i++) {
            int PosX = (int) Wave.waveLeft.get(i).getX();
            int PosY = (int) Wave.waveLeft.get(i).getY();
            float distance = (float) Point2D.distance(getX(),getY(),PosX,PosY);
            if (distance < range){
                enemyMap.put(Wave.waveLeft.get(i),distance);
            }else {
                enemyMap.remove(Wave.waveLeft.get(i));
            }

        }
        System.out.println(enemyMap.size());


    }

    @Override
    public void findtarget() {

    }

    @Override
    public void update(float time) {
        updateTargetarray();
        System.out.println(time);
        if (enemyMap.size()>0){
            firedelay -= time;
            if (firedelay <= 0){
                enemyMap.keySet().stream().findFirst().get().setLifepoints((int) damage);
                firedelay += 10;
            }


        }

    }


    /**
     * draw-method for Tower1
     */
    public void draw() {
        spriteBatch.draw(turretTexture, x, y, width, height);
    }
}
