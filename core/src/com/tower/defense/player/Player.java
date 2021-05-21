package com.tower.defense.player;

import com.tower.defense.tower.ITower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Player {

    private final static Logger log = LogManager.getLogger(Player.class);

    private String name;
    private int wallet = 100;
    private boolean left;
    private int lifepoints = 200;
    private List<ITower> inventory;

    public Player(String name, boolean left) {
        this.name = name;
        this.left = left;
        log.info("player: {}", name);
    }

    /**
     * At the end of a wave, a Player gets a reward for surviving.
     * If they missed enemies, they get less money at the end of the round.
     * But they get at least 20.
     * The final reward is added to the wallet
     */
    public void addToWallet(int reward, int enemiesMissed) {
        if (reward >= 0) {
            reward = reward - enemiesMissed * 2;
            if(reward>20){
                log.info("reward: {}", reward);
                wallet += reward;
            }
            else{
                reward = 20;
                log.info("reward: {}", reward);
                wallet += reward;
            }


        }
        else{
            log.info("You can not add a negative reward");
        }
    }

    /**
     * @return inventory
     */
    public List<ITower> getInventory() {
        return inventory;
    }

    /**
     * If a Tower was bought it is added to the list of the players towers(inventory)
     * The cost will be deducted from the wallet
     */
    public void buyTower(ITower tower) {
        inventory.add(tower);
        log.info("tower {} was added to the inventory", tower.toString());
        wallet = wallet - tower.getCost();
        log.info("wallet: {}", wallet);
    }

    /**
     * selling Towers only regain 80% of the costs. The result is rounded to an Integer
     * At the end the Tower is removed from the list
     */
    public void sellTower(int index) {
        ITower tower = inventory.get(index);
        wallet = (int) (wallet + Math.round(tower.getCost() * 0.8));
        log.info("wallet: {}", wallet);
        inventory.remove(index);
        log.info("tower at index {} was sold", index);
    }

     /* public void placeTower(int index, int x,int y){
     inventory.get(index).setX(x);
     inventory.get(index).setY(y);
     //the command for communication with the server must be implemented here
     }*/

    /**
     * This method is called when an Enemy passed the Map without dying,
     * the damage it deals is subtracted from the Players Life
     */
    public void reduceLifepoints(int damage) {
        lifepoints = lifepoints - damage;
        log.debug("lifepoints: {}", lifepoints);
    }

    /**
     * @return wallet
     */
    public int getWalletValue() {
        return wallet;
    }

    /**
     * @return lifepoints
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    public boolean isLeft() {
        return left;
    }
}
