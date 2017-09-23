/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.jamesb.game.MarioBros;
import com.jamesb.game.Screens.HUD;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james
 */
public class Brick extends InteractiveTileObject{

    private Sound brickBreaking;

    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this); //set the ID to brick so we can call it later
        setCategoryFilter(MarioBros.BRICK_BIT);
        
        // Load sound 
        brickBreaking = MarioBros.assetManager.get("Audio/Sounds/breakblock.wav", Sound.class);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision!");
        setCategoryFilter(MarioBros.DESTROYED_BIT); // you broke the brick so now mario can go thru it.
        getCell().setTile(null);
        HUD.addScore(200);
        brickBreaking.play();
        
    }
    
}
