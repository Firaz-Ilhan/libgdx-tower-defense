package Player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int wallet = 0;
    private boolean right;
    private boolean left;
    private int lifepoints = 200;
   // private List<ITower> inventory;

    public Player(String name, boolean left,boolean right){
        this.name= name;
        this.left=left;
        this.right=right;
    }

    public void addToWallet(int reward, int enemiesMissed){
        reward= reward - enemiesMissed*2; //if you missed enemies, you get less money at the end of the round
        wallet+=reward;
    }


    /**
     * Because there is no ITower by now, I commented this method
     *
     public List<ITower> getInventory(){
     return List.copyOf(inventory);
     }

    public void buyTower(ITower tower){
        inventory.add(tower);
        wallet = wallet - tower.getCost;
    }
    public void sellTower(int i){
        inventory.remove(i);
        wallet = wallet + tower.getCost*0.8; //selling Towers only regain 80% of the costs
    }

     public void placeTower(ITower tower, int x,int y){
        //the command for communication with the server must be implemented here
    }
 */
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
