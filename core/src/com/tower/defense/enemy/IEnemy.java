package com.tower.defense.enemy;

public interface IEnemy {
     int getY();

     int getX();

     int getDamage();

     int getLifepoints();

     void setY(int y);

     void setLifepoints(int damageReceived);

}
