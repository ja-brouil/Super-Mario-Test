/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.jamesb.game.MarioBros;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james
 */
public class Goomba extends Enemy{
    
    // Variables
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Sound stompedSound;
    

    public Goomba(PlayScreen screen, float x, float y){
        super(screen, x, y);
        
        // Build Goomba
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        
        // Set bounds
        setBounds(getX(), getY(), 16 / MarioBros.PPM,  16 / MarioBros.PPM);
        

        
        // Set destroyed initial
        setToDestroy = false;
        destroyed = false;
        
        // Load Destroyed sound
        stompedSound = MarioBros.assetManager.get("Audio/Sounds/stomp.wav", Sound.class);
        
        velocity = new Vector2(0.4f, 0);
    }
    
    
    
    @Override
    protected void defineEnemy() {
        
        // Copy mario code to speed things up
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody; //dynamic so he can move
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape(); //create circles
        shape.setRadius(6 / MarioBros.PPM);
        // Set category
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        // Set Mask | What can he collide with?
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.BRICK_BIT | MarioBros.COIN_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        
        // Create head fixture for Goomba
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-5 /MarioBros.PPM, 8 / MarioBros.PPM), new Vector2( 5 / MarioBros.PPM, 8 / MarioBros.PPM));
        
        fdef.shape = head;
        // Add bounce on head
        fdef.restitution = 0.5f; // 0 no bounce, 1 means you bounce back the amount you dropped from so 100%
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this); // set to class itself so we can access it
    }
    
    // update method
    
    public void update(float dt){
        stateTime += dt;
        // Check to remove Goomba
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stompedSound.play();
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0 , 16, 16));
            stateTime = 0;
        }
        else if (!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2); // Don't forget Box2D objects start the the CENTER. So to get the out edge, you must get the position, subtract the width and divide by 2
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
        
    }
    
    // Hit on head
    
    public void hitOnHead(){
        setToDestroy = true;
    }
    
    // Override the main render method to draw an empty batch for the goomba only.
    public void draw(Batch batch){
        if (!destroyed || stateTime  < 1){
            super.draw(batch);
        }
    }
    
}
