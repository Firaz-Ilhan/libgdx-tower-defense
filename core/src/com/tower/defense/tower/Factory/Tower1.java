package com.tower.defense.tower.Factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.Wave;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class Tower1 implements ITower {

    private float x;
    private float y;
    boolean is_attacking;
    private double damage = 1;
    private double firerate = 1;
    private double range = 250;
    private int cost = 20;
    private Texture turretTexture;
    private TextureRegion projectile;
    private float width, height;
    private SpriteBatch spriteBatch;
    protected HashMap <Enemy,Float> enemyMap;
    long startTime;
    long endTime;




    public Tower1(Texture turretTexture, TextureRegion projectile,float x, float y, int width, int height, SpriteBatch batch) {
        this.x = x;
        this.y = y;
        this.is_attacking = false;
        this.turretTexture = turretTexture;
        this.projectile = projectile;
        this.width = width;
        this.height = height;
        this.spriteBatch = batch;
        this.startTime = System.nanoTime();

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

    public void shooting() {

        enemyMap.keySet().stream().findFirst().get().setLifepoints((int) damage);
        float length = (float) enemyMap.values().toArray()[0];

        float xTarget = enemyMap.keySet().stream().findFirst().get().getX();
        double angle = Math.asin((xTarget-getX())/(float) length);
        angle = Math.toDegrees(angle);
        //System.out.println(angle);
        spriteBatch.draw(projectile,x,y,width/2,height/2,length,10,1,1,(float)angle+180);


    }

    public void updateTargetarray(Wave wave){
            enemyMap = new HashMap<>();

        for (int i = 0; i < wave.waveLeft.size; i++) {
            int PosX = (int) wave.waveLeft.get(i).getX();
            int PosY = (int) wave.waveLeft.get(i).getY();
            float distance = (float) Point2D.distance(getX(),getY(),PosX,PosY);


            if (distance < range){
                enemyMap.put(wave.waveLeft.get(i),distance);
            }else {
                enemyMap.remove(wave.waveLeft.get(i));
            }

        }

    }

    public void update(){

        endTime = System.nanoTime();
        double difference = (endTime-startTime)/1e9;

        if (enemyMap.size()>0 && difference>firerate){
            shooting();
            startTime = System.nanoTime();
        }
    }


    /**
     * draw-method for Tower1
     */
    public void draw() {
        spriteBatch.draw(turretTexture, x, y, width, height);

    }
}
