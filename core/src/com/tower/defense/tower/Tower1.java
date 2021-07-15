package com.tower.defense.tower;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tower1 extends Tower {

    public Tower1(Texture turretTexture, float x, float y, int width, int height, SpriteBatch batch, Sound shootingSound) {
        super(turretTexture, x, y, width, height, batch, shootingSound);
    }

}
