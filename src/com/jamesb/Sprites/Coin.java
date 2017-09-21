/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.jamesb.game.MarioBros;

/**
 *
 * @author james
 */
public class Coin extends InteractiveTileObject {

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this); //pass coin to the fixture so we can call it later
        setCategoryFilter(MarioBros.COIN_BIT); // set the coins filter bit
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision!");
    }

}
