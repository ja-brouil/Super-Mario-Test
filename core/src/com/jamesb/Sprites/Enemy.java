/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james
 */
public abstract class Enemy extends Sprite{
    
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    
    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1 , 0);
        b2body.setActive(false);
    }
    
    
    protected abstract void defineEnemy();
    public abstract void update(float dt);
    
    
    // Collision detection
    public abstract void hitOnHead();
    
    // Reverse velocity if you hit an object
    
    public void reverseVelocity(Boolean x, Boolean y){
        if (x){
            velocity.x = -velocity.x;
        }
        if (y){
            velocity.y = -velocity.y;
        }
    }
    
}
