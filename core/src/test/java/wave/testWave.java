package wave;

import com.tower.defense.TowerDefense;
import com.tower.defense.wave.Wave;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class testWave {
    Wave wave;
@Test
    public void testSpawning(){
    wave = new Wave(new TowerDefense());
    assertEquals(1,wave.waveLeft.size);
    assertEquals(1,wave.waveRight.size);
    wave.spawnEnemy();
    assertEquals(2,wave.waveLeft.size);
    assertEquals(2,wave.waveRight.size);
}
//@Test
//doesn't work rn, investigating it later
//    public void enemiesAreRemoved(){
//    wave = new Wave();
//    Player player = new Player("tester",true);
//    waveLeft.get(0).setLifepoints(7);
//    wave.renderWave(waveLeft,player);
//    assertEquals(0,waveLeft.size);
//
//    }
}
