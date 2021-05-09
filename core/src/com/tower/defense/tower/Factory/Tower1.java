package com.tower.defense.tower.Factory;

import com.tower.defense.tower.ITower;

public class Tower1 implements ITower {

    private int x;
    private int y;
    boolean is_attacking;
    private double damage = 1;
    private double firerate = 1;
    private double range = 50;
    private int cost  = 100;

    public Tower1(int x, int y){
        this.x = x;
        this.y = y;
        this.is_attacking = false;
    }



    public int getY() {
        return y;
    }

    public int getX() {
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
}
