package player;

import com.tower.defense.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testPlayer {
    @Test
    public void testAddToWallet() {
        Player player = new Player("Test");
        int moneyAtBeginning = player.getWalletValue();
        player.addToWallet(30, 1);
        assertEquals(moneyAtBeginning + 28, player.getWalletValue());

        moneyAtBeginning = player.getWalletValue();
        player.addToWallet(30, 10);
        assertEquals(moneyAtBeginning + 20, player.getWalletValue());

    }
}
