package enemy;

import com.tower.defense.enemy.Factory.Enemy1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testEnemy {
    @Test
    public void testDamageReceiving(){
        Enemy1 enemy = new Enemy1(0,0);
        int lifepointsFull = enemy.getLifepoints();
        enemy.setLifepoints(2);

        assertEquals(lifepointsFull-2,enemy.getLifepoints());
    }
    @Test
    public void testNegativeDamageReceiving(){
        Enemy1 enemy = new Enemy1(0,0);
        int lifepointsFull = enemy.getLifepoints();
        enemy.setLifepoints(-2);

        assertEquals(lifepointsFull,enemy.getLifepoints());
    }
}
