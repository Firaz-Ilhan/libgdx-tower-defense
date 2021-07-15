package player;

import com.tower.defense.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testPlayer {
    @Test
    public void testAddToWallet() {
        Player player = new Player("Test", true);
        int moneyAtBeginning = player.getWalletValue();
        player.addToWallet(30);
        assertEquals(moneyAtBeginning + 30, player.getWalletValue());

        moneyAtBeginning = player.getWalletValue();
        player.addToWallet(30);
        assertEquals(moneyAtBeginning + 30, player.getWalletValue());

    }
}
