package helper;

import com.badlogic.gdx.math.Vector2;
import com.tower.defense.helper.AllowedTiles;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//tests used for checking that certain tiles are allowed to be used for a player and
//the player's opponent, while also checking for edge cases like negative values
public class testAllowedTiles {
    private final AllowedTiles allowedTiles = new AllowedTiles();
    ArrayList<Vector2> playerOneAllowedTiles = allowedTiles.playerOneAllowedTiles;
    ArrayList<Vector2> playerTwoAllowedTiles = allowedTiles.playerTwoAllowedTiles;

    @Test
    public void testIfTileIsAllowedForPlayer() {

        Vector2 tileAllowed1 = new Vector2(5, 5);
        Vector2 tileAllowed2 = new Vector2(13, 2);
        Vector2 tileAllowed3 = new Vector2(10, 2);
        Vector2 tileAllowed4 = new Vector2(14, 13);

        Vector2 tileNotAllowed1 = new Vector2(0, 0);
        Vector2 tileNotAllowed2 = new Vector2(-1, 5);
        Vector2 tileNotAllowed3 = new Vector2(32, 32);
        Vector2 tileNotAllowed4 = new Vector2(-2, -5);

        assertTrue(tileInArrayList(tileAllowed1, playerOneAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed2, playerOneAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed3, playerOneAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed4, playerOneAllowedTiles));

        assertFalse(tileInArrayList(tileNotAllowed1, playerOneAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed2, playerOneAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed3, playerOneAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed4, playerOneAllowedTiles));
    }

    @Test
    public void testIfTileIsAllowedForOpponent() {

        Vector2 tileAllowed1 = new Vector2(24, 13);
        Vector2 tileAllowed2 = new Vector2(28, 2);
        Vector2 tileAllowed3 = new Vector2(20, 6);
        Vector2 tileAllowed4 = new Vector2(19, 1);

        Vector2 tileNotAllowed1 = new Vector2(0, 0);
        Vector2 tileNotAllowed2 = new Vector2(-1, 5);
        Vector2 tileNotAllowed3 = new Vector2(32, 32);
        Vector2 tileNotAllowed4 = new Vector2(-2, -5);

        assertTrue(tileInArrayList(tileAllowed1, playerTwoAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed2, playerTwoAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed3, playerTwoAllowedTiles));
        assertTrue(tileInArrayList(tileAllowed4, playerTwoAllowedTiles));

        assertFalse(tileInArrayList(tileNotAllowed1, playerTwoAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed2, playerTwoAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed3, playerTwoAllowedTiles));
        assertFalse(tileInArrayList(tileNotAllowed4, playerTwoAllowedTiles));
    }

    private boolean tileInArrayList(Vector2 tile, ArrayList<Vector2> allowedTiles) {
        for (Vector2 allowedTile : allowedTiles) {
            if (allowedTile.x == tile.x && allowedTile.y == tile.y) {
                return true;
            }
        }
        return false;
    }


}