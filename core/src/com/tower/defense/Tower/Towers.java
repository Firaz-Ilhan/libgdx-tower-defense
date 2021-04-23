package com.tower.defense.Tower;

public abstract class Towers implements ITower {
    int cost;
    int range;
    double damage;
    double firerate;



    public void Upgrade_dmg(){
        damage++;
    }


    public int getCost() {
        return cost;
    }
    public int getRange() {
        return range;
    }
    public double getFirerate() {
        return firerate;
    }
    public double getDamage() {
        return damage;
    }


}
