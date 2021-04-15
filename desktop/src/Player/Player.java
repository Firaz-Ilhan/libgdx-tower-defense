package Player;

public class Player {
    String name;
    int wallet = 0;
    boolean right;
    boolean left;

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
     public void placeTower(ITower tower, int x,int y){
        wallet = wallet - tower.getCost;
        //the command for communication with the server must be implemented here
    }
 */
    public int getWalletValue(){
        return wallet;
    }
}
