package com.tower.defense.wave;

import static com.tower.defense.TestMain.*;

public class WaveThread extends Thread {
    private int waveCount;
    private int waveReward

    private void endOfWave(){
        while(waveRight.size!=0 && waveLeft.size != 0);
        player1.addToWallet(waveReward,enemiesPastLeft);
        player2.addToWallet(waveReward,enemiesPastRight);
        waveCount++;
        waveSpeed*=1.5;
        waveSize= Math.round(waveSize*1.5);
        waveReward =(int) Math.round(waveReward*1.5);
        enemiesSpawned=0;
        enemiesPastLeft=0;
        enemiesPastRight=0;
    }
    @Override
    public void run(){


    }
}
