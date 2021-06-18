package com.tower.defense.player;

import com.tower.defense.tower.ITower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

public class Player {

    private final static Logger log = LogManager.getLogger(Player.class);
    private boolean lost = false;
    private String name;
    private int wallet = 100;

    private int lifepoints = 200;
    private LinkedList<ITower> inventory;

    public Player(String name) {
        this.name = name;
        log.info("player: {}", name);
        inventory = new LinkedList<>();

    }

    /**
     * At the end of a wave, a Player gets a reward for surviving.
     * If they missed enemies, they get less money at the end of the round.
     * But they get at least 20.
     * The final reward is added to the wallet
     */

    public void addToWallet(int reward) {
        if (reward < 0) {
            log.info("You can not add a negative reward");
        }
        else{
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
    public void lost(){
        lost = true;
    }

    public boolean hasLost() {
        return lost;
    }
}
