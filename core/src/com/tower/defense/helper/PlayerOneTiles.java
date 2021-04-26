package com.tower.defense.helper;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class PlayerOneTiles {
    private static ArrayList<Vector2> list;

    public PlayerOneTiles(){
        list = new ArrayList<Vector2>();
        for(int x = 0; x <= 9; x++){
            for(int y = 0; y <= 13; y++){
                list.add(new Vector2(x,y));
            }
        }

        for(int x = 12; x <= 14; x++){
            for(int y = 0; y <= 13; y++){
                list.add(new Vector2(x,y));
            }
        }
    }

    public boolean tileInArray(Vector2 hoveredTilePosition){
        if(list.contains(hoveredTilePosition)){
            return true;
        }
        return false;
    }
}
