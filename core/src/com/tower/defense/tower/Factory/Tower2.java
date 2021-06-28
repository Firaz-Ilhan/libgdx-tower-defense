package com.tower.defense.tower.Factory;

import com.tower.defense.player.Player;
import com.tower.defense.tower.ITower;
import com.tower.defense.wave.Wave;

public class Tower2 implements ITower {

    private int x;
    private int y;
    boolean is_attacking;
    private double damage = 5;
    private double firerate = 1;
    private double range = 100;
    private int cost  = 200;

    public Tower2(int x, int y){
        this.x = x;
        this.y = y;
        this.is_attacking = false;
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

    public void shooting() {
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

    @Override
    public void updateTargetarray(Wave wave, Player player) {

    }

    @Override
    public void update() {

    }
}
