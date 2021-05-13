package com.tower.defense.enemy;

import com.badlogic.gdx.math.Vector2;

public interface IEnemy {
    int getY();

    int getX();

    Vector2 getPosition();

    void setPosition(Vector2 newPos);

    int getDamage();

    int getLifepoints();

    void setY(int y);

    void setX(int x);

    void setLifepoints(int damageReceived);

}
