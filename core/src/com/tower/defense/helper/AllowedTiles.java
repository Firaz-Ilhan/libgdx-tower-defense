package com.tower.defense.helper;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class AllowedTiles {
    public static ArrayList<Vector2> playerOneAllowedTiles;
    public static ArrayList<Vector2> playerTwoAllowedTiles;
    public AllowedTiles(){
        playerOneAllowedTiles = new ArrayList<Vector2>();
        for(int x = 0; x <= 9; x++){
            for(int y = 0; y <= 13; y++){
                playerOneAllowedTiles.add(new Vector2(x,y));
            }
        }

        for(int x = 12; x <= 14; x++){
            for(int y = 0; y <= 13; y++){
                playerOneAllowedTiles.add(new Vector2(x,y));
            }
        }

        playerTwoAllowedTiles = new ArrayList<Vector2>();
        for(int x = 17; x <= 19; x++){
            for(int y = 0; y <= 13; y++){
                playerTwoAllowedTiles.add(new Vector2(x,y));
            }
        }

        for(int x = 22; x <= 31; x++){
            for(int y = 0; y <= 13; y++){
                playerTwoAllowedTiles.add(new Vector2(x,y));
            }
        }

    }

    public boolean tileInArray(Vector2 hoveredTilePosition, ArrayList<Vector2> list){
        if(list.contains(hoveredTilePosition)){
            return true;
        }
        return false;
    }
    
}
