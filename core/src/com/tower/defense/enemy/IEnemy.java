package com.tower.defense.enemy;

public interface IEnemy {
     int getY();

     int getX();

     int getDamage();

     void setY(int y);

     void setLifepoints(int damageReceived);

}
