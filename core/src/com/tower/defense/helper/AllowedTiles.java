package com.tower.defense.helper;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class AllowedTiles {
    public static ArrayList<Vector2> playerOneAllowedTiles;
    public static ArrayList<Vector2> playerTwoAllowedTiles;

    public AllowedTiles() {
        playerOneAllowedTiles = new ArrayList<>();
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 3; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 0; x <= 5; x++) {
            for (int y = 4; y <= 11; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 0; x <= 9; x++) {
            for (int y = 12; y <= 13; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 10; x <= 14; x++) {
            for (int y = 0; y <= 5; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 8; x <= 14; x++) {
            for (int y = 6; y <= 9; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 12; x <= 14; x++) {
            for (int y = 10; y <= 13; y++) {
                playerOneAllowedTiles.add(new Vector2(x, y));
            }
        }

        ////////////////////////////////////////////////////////

        playerTwoAllowedTiles = new ArrayList<>();
        for (int x = 17; x <= 21; x++) {
            for (int y = 0; y <= 5; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 17; x <= 23; x++) {
            for (int y = 6; y <= 9; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 17; x <= 19; x++) {
            for (int y = 10; y <= 13; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 24; x <= 31; x++) {
            for (int y = 0; y <= 3; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 26; x <= 31; x++) {
            for (int y = 4; y <= 11; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

        for (int x = 22; x <= 31; x++) {
            for (int y = 12; y <= 13; y++) {
                playerTwoAllowedTiles.add(new Vector2(x, y));
            }
        }

    }

    public boolean tileInArray(Vector2 hoveredTilePosition, ArrayList<Vector2> list) {
        if (list.contains(hoveredTilePosition)) {
            return true;
        }
        return false;
    }

}
