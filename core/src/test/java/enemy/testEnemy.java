package enemy;

import com.tower.defense.enemy.Enemy;
import com.tower.defense.enemy.Factory.Enemy1;
import org.junit.jupiter.api.Test;

import static com.tower.defense.enemy.Factory.EnemyFactory.getEnemyInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testEnemy {
    @Test
    public void testDamageReceiving(){
        Enemy enemy = getEnemyInstance("easy",0,100,5);
        int lifepointsFull = enemy.getLifepoints();
        enemy.setLifepoints(2);

        assertEquals(lifepointsFull-2,enemy.getLifepoints());
    }
    @Test
    public void testNegativeDamageReceiving(){
        Enemy enemy = getEnemyInstance("easy",0,100,5);
        int lifepointsFull = enemy.getLifepoints();
        enemy.setLifepoints(-2);

        assertEquals(lifepointsFull,enemy.getLifepoints());
    }

    @Test
    public void testHealAndBuff(){
        Enemy enemy = getEnemyInstance("easy",0,100,5);
        int lifepointsFull = enemy.getLifepoints();
        enemy.setLifepoints(2);
        enemy.healAndBuff();
        assertEquals(lifepointsFull + 5 ,enemy.getLifepoints());
    }
}
