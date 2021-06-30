package com.tower.defense.player;

import com.tower.defense.tower.ITower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

    private final static Logger log = LogManager.getLogger(Player.class);
    private boolean lost = false;
    private final String name;
    private int wallet = 50;
    private final boolean isPlayer;

    private int lifepoints = 200;

    public Player(String name, boolean isPlayer) {
        this.name = name;
        this.isPlayer = isPlayer;
        log.info("player: {}", name);

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
     * If a Tower was bought it is added to the list of the players towers(inventory)
     * The cost will be deducted from the wallet
     */
    public void buyTower(ITower tower) {
        log.info("tower {} was added to the inventory", tower.toString());
        wallet = wallet - tower.getCost();
        log.info("wallet: {}", wallet);
    }

    /**
     * Triggered if a "Heal opponent Wave" was bought
     * The cost will be deducted from the wallet
     */
    public void buyInfluence(int cost) {
        wallet = wallet - cost;
        log.info("wallet: {}", wallet);
    }
    /**
     * selling Towers only regain 80% of the costs. The result is rounded to an Integer
     * At the end the Tower is removed from the list
     */
    public void sellTower(ITower tower) {
        wallet = (int) (wallet + Math.round(tower.getCost() * 0.8));
        log.info("wallet: {}", wallet);
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
     * can be called to update opponents lifepoints
     * @param lifepoints
     */
    public void setLifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
    }

    /**
     * sets boolean lost to true
     */
    public void lost(){
        lost = true;
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


    public boolean hasLost() {
        return lost;
    }

    public boolean getPlayer() {
        return isPlayer;
    }

}
