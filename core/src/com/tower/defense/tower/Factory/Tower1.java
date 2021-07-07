package com.tower.defense.tower.Factory;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.player.Player;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.Wave;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class Tower1 implements ITower {

    private float x;
    private float y;
    boolean is_attacking;
    private double damage = 1;
    private double firerate = 1;
    private double range = 250;
    private int cost = 20;
    private Texture turretTexture;
    private float width, height;
    private SpriteBatch spriteBatch;
    protected HashMap <Enemy,Float> enemyMap;
    long startTime;
    long endTime;
    Sound shootingSound;

    public Tower1(Texture turretTexture, float x, float y, int width, int height, SpriteBatch batch, Sound shootingSound) {
        this.x = x;
        this.y = y;
        this.is_attacking = false;
        this.turretTexture = turretTexture;
        this.width = width;
        this.height = height;
        this.spriteBatch = batch;
        this.startTime = System.nanoTime();
        this.shootingSound = shootingSound;
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

    //Shooting function
    //get Shaperenderer and draw a line between the tower and the current enemy
    public void shooting(ShapeRenderer shapeRenderer) {
        float xTarget =  enemyMap.keySet().stream().findFirst().get().getX();
        float yTarget =  enemyMap.keySet().stream().findFirst().get().getY();

        enemyMap.keySet().stream().findFirst().get().setLifepoints((int) damage);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1,0,0,1);
        shapeRenderer.rectLine(x,y,xTarget,yTarget,5, Color.BLUE,Color.BLUE);
        shapeRenderer.end();
        shootingSound.play();
        startTime = System.nanoTime();
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

    //Create an Hashmap whitin all enemies which are at the current time in the tower-range
    //also remove all enemies who arent in the range anymore
    //the hashmap contains the enemie and the distance between enemy and tower
    public void updateTargetarray(Wave waveClass, Player player){
            enemyMap = new HashMap<>();
            Array<Enemy> wave;
        if (player.getPlayer()) {
            wave = waveClass.waveLeft;
        } else {
            wave = waveClass.waveRight;
        }
        for (int i = 0; i < wave.size; i++) {
            int PosX = (int) wave.get(i).getX();
            int PosY = (int) wave.get(i).getY();
            float distance = (float) Point2D.distance(getX(),getY(),PosX,PosY);
            if (distance < range){
                enemyMap.put(wave.get(i),distance);
            }else {
                enemyMap.remove(wave.get(i));
            }

        }

    }

    //the update method gets called every loop
    public void update(ShapeRenderer shapeRenderer){
        endTime = System.nanoTime();
        double difference = (endTime-startTime)/1e9;

        if (enemyMap.size()>0 && difference>firerate){
            shooting(shapeRenderer);

        }
    }


    /**
     * draw-method for Tower1
     */
    public void draw() {
        spriteBatch.draw(turretTexture, x, y, width, height);
    }
}
