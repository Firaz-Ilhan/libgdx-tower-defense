package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.IEnemy;

public class Enemy2 implements IEnemy{

        private int posX;
        private int posY;
        private int lifepoints = 10;
        private int damage = 5;
        //private int movementspeed = movementspeed*1; //static movementspeed, die am Anfang der Runde in der Main Klasse? gesetzt wird

        public Enemy2(int posX, int posY){
            this.posX = posX;
            this.posY = posY;
        }

        public int getY() {
            return posY;
        }

        public int getX() {
            return posX;
        }

        public int getDamage() {
            return damage;
        }
        public void setY(int posY){
            this.posY = posY;
        }
        public void setLifepoints(int damageReceived){
            lifepoints -= damageReceived;
        }
        public int getLifepoints(){
            return lifepoints;
    }

}
