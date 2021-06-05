package wave;

import com.tower.defense.player.Player;
import com.tower.defense.wave.Wave;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tower.defense.wave.Wave.waveLeft;
import static com.tower.defense.wave.Wave.waveRight;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class testWave {
    Wave wave;
@Test
    public void testSpawning(){
    wave = new Wave();
    assertEquals(1,waveLeft.size);
    assertEquals(1,waveRight.size);
    wave.spawnEnemy();
    assertEquals(2,waveLeft.size);
    assertEquals(2,waveRight.size);
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
