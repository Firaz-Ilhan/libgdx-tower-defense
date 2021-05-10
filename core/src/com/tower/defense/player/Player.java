package com.tower.defense.player;

import com.tower.defense.tower.ITower;

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

    /**
     *
     * @param reward
     * @param enemiesMissed
     *
     * At the end of a wave, a Player gets a reward for surviving.
     * If they missed enemies, they get less money at the end of the round.
     * The final reward is added to the wallet
     */
    public void addToWallet(int reward, int enemiesMissed){
        reward= reward - enemiesMissed*2;
        wallet+=reward;
    }

    /**
     *
     * @return inventory
     */
     public List<ITower> getInventory(){
     return inventory;
     }

    /**
     *
     * @param tower
     * If a Tower was bought it is added to the list of the players towers(inventory)
     * The cost will be deducted from the wallet
     */
     public void buyTower(ITower tower){
     inventory.add(tower);
     wallet = wallet - tower.getCost();
     }

    /**
     *
     * @param index
     * selling Towers only regain 80% of the costs. The result is rounded to an Integer
     * At the end the Tower is removed from the list
     */
     public void sellTower(int index){
        ITower tower = inventory.get(index);
        wallet = (int) (wallet + Math.round(tower.getCost()*0.8));
        inventory.remove(index);

     }

     /* public void placeTower(int index, int x,int y){
     inventory.get(index).setX(x);
     inventory.get(index).setY(y);
     //the command for communication with the server must be implemented here
     }*/

    /**
     *
     * @return wallet
     */
    public int getWalletValue(){
        return wallet;
    }

    /**
     *
     * @param damage
     * This method is called when an Enemy passed the Map without dying, The damage it deals is subtracted from the Players Life
     */
    public void reduceLifepoints(int damage){
        lifepoints = lifepoints-damage;
    }
    /**
     *
     * @return lifepoints
     */
    public int getLifepoints(){
        return lifepoints;
    }

    /**
     *
     * @return name
     */
    public String getName(){ return name; }

}
