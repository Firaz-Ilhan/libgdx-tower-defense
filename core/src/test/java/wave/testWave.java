package wave;

import com.tower.defense.TowerDefense;
import com.tower.defense.wave.Wave;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class testWave {
    Wave wave;

    @Test
    public void testSpawning() {
        wave = new Wave(new TowerDefense());
        assertEquals(1, wave.waveLeft.size);
        assertEquals(1, wave.waveRight.size);
        wave.spawnEnemy();
        assertEquals(2, wave.waveLeft.size);
        assertEquals(2, wave.waveRight.size);
    }
}
