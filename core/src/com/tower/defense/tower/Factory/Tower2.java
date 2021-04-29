package com.tower.defense.tower.Factory;

public class Tower2 {

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
