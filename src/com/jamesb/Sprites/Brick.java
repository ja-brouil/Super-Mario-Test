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
public class Brick extends InteractiveTileObject{
 

    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this); //set the ID to brick so we can call it later
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision!");
        setCategoryFilter(MarioBros.DESTROYED_BIT); // you broke the brick so now mario can go thru it.
        getCell().setTile(null);
    }
    
}
