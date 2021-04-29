package com.tower.defense.tower;

public interface ITower {

    int getY();

    int getX();

    void setX(int x);

    void setY(int y);

    void setIs_attacking(boolean is_attacking);

    double getDamage();

    double getRange();

    double getFirerate();

    int getCost();

}
