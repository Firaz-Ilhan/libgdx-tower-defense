package com.tower.defense.enemy.Factory;

import com.tower.defense.enemy.IEnemy;

public class Enemy2 implements IEnemy{

        private int x;
        private int y;
        private int lifepoints = 10;
        private int damage = 5;
        //private int movementspeed = movementspeed*1; //static movementspeed, die am Anfang der Runde in der Main Klasse? gesetzt wird

        public Enemy2(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        public int getDamage() {
            return damage;
        }
        public void setY(int y){
            this.y = y;
        }
        public void setLifepoints(int damageReceived){
            lifepoints -= damageReceived;
        }

}
