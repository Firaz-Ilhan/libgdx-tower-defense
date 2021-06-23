package com.tower.defense.tower;

import com.tower.defense.wave.Wave;

public interface ITower {

    float getY();

    float getX();

    void setX(int x);

    void setY(int y);

    void setIs_attacking(boolean is_attacking);

    double getDamage();

    double getRange();

    double getFirerate();

    int getCost();
    void updateTargetarray(Wave wave);
    void update();

}
