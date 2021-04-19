package com.tower.defence.Player;

import com.tower.defence.Tower.ITower;

import java.util.List;

public class Player {
    private String name;
    private int wallet = 100;
    private boolean right;
    private boolean left;
    private int lifepoints = 200;
    private List<ITower> inventory;

    public Player(String name, boolean left,boolean right){
        this.name= name;
        this.left=left;
        this.right=right;
    }

    public void addToWallet(int reward, int enemiesMissed){
        reward= reward - enemiesMissed*2; //if you missed enemies, you get less money at the end of the round
        wallet+=reward;
    }



     public List<ITower> getInventory(){
     return inventory;
     }

     public void buyTower(ITower tower){
     inventory.add(tower);
     wallet = wallet - tower.getCost();
     }
     public void sellTower(int index){
        ITower tower = inventory.get(index);
        wallet = (int) (wallet + tower.getCost()*0.8); //selling Towers only regain 80% of the costs
        inventory.remove(index);

     }
     
     /* public void placeTower(int index, int x,int y){
     inventory.get(index).setX(x);
     inventory.get(index).setY(y);
     //the command for communication with the server must be implemented here
     }*/

    public int getWalletValue(){
        return wallet;
    }
    public void reduceLifepoints(int damage){
        lifepoints = lifepoints-damage;
    }
    public int getLifepoints(){
        return lifepoints;
    }
}
