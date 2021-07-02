package com.tower.defense.tower;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tower.defense.player.Player;
import com.tower.defense.wave.Wave;

public interface ITower {

    float getY();

    float getX();

    void setX(int x);

    void setY(int y);

    void shooting(ShapeRenderer shapeRenderer);

    double getDamage();

    double getRange();

    double getFirerate();

    int getCost();
    void updateTargetarray(Wave wave, Player player);
    void update(ShapeRenderer shapeRenderer);


}
