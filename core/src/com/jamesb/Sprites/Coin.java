/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.jamesb.game.MarioBros;
import com.jamesb.game.Screens.HUD;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james
 */
public class Coin extends InteractiveTileObject {

    // Check to remove coin
    private static TiledMapTileSet tileset;
    private final int BLANK_COIN = 28;
    private boolean isActive = true;
    private Sound coinSound;
    private Sound noCoinSound;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileset = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this); //pass coin to the fixture so we can call it later
        setCategoryFilter(MarioBros.COIN_BIT); // set the coins filter bit
        // Load sounds
        coinSound = MarioBros.assetManager.get("Audio/Sounds/coin.wav", Sound.class);
        noCoinSound = MarioBros.assetManager.get("Audio/Sounds/bump.wav", Sound.class);
        
    }

    @Override
    public void onHeadHit() {
        if (isActive) {
            Gdx.app.log("Coin", "Collision!");
            HUD.addScore(200);
            getCell().setTile(tileset.getTile(BLANK_COIN)); //replacing tile code.
            isActive = false;
            
            coinSound.play();
        } else {
            noCoinSound.play();
        }

    }

}
