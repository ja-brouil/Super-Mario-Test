 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jamesb.game.MarioBros;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james
 */
public class Mario extends Sprite {

    // Create an enum for each state that your sprite could be in
    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private boolean runningRight;
    private float stateTimer;

    // bounds
    public float xmin, ymin = 0;

    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        // Initiate world + create mario + sprite
        // Super call can take the region to generate mario
        this.world = screen.getWorld();
        
        // Start animations variables
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // Create array to store all the animations
        // Run
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
            marioRun = new Animation<TextureRegion>(0.1f, frames); // duration of the frames, array of Frames     
        }
        frames.clear(); // clear the array so that we can use this for the next set of animations
        // Jump
        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
            marioJump = new Animation<TextureRegion>(0.1f, frames);
            
        }
        frames.clear();

        // For idle
        marioStand = new TextureRegion(getTexture(), 0, 11, 16, 16); // region, x, y, width, height | STarts from the top left corner then goes down | find region = name of the region
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM); // how large do we render the sprite on the screen
        setRegion(marioStand); // Associate marioStand with this region
        
        defineMario();
    }

    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody; //dynamic so he can move
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape(); //create circles
        shape.setRadius(6 / MarioBros.PPM);
        // Set category
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        // Set Mask | What can he collide with?
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.BRICK_BIT | MarioBros.COIN_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
        
        // Create Mario's head
        EdgeShape head = new EdgeShape(); //line between two different points
        head.set(new Vector2(-2 / MarioBros.PPM,  6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM,  6 / MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true; // if a fixture is a sensor, it won't collide with anything anymore. Just for boolean properties
       
        b2body.createFixture(fdef).setUserData("head"); // give an attribute to mario's head
        
        
        
    }

    // Update position
    public void setPosition(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        // set position to the b2d body. The b2d body is in the center of the fixture. we want the bottom left corner so since we start in the center, we get x then move it by the width of the sprite /2 since we are in the center
        // Do the same for y.
    }

    public void update(float dt) {
        setPosition(dt);
        setRegion(getFrame(dt)); // show the appropriate sprite needed to be displayed
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion tempRegion;
        switch (currentState) {
            case JUMPING:
                tempRegion = marioJump.getKeyFrame(stateTimer); //state timer decides which frame gets rendered from the animation. If its past the invididual frame time when we sent to the constructor, it moves to the next frame. If it loops, it goes back to the first frame.
                break;
            case RUNNING:
                tempRegion = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                tempRegion = marioStand;
                break;
        }
        
        
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !tempRegion.isFlipX()) {
            //if you are running in a positive x direction, you are going right, else if your x is negative, you go left. Remember to set the boolean 
            // isFLipX or is FlipY returns true if the texture is flipped over
            tempRegion.flip(true, false); //true for flip, false for don't | x, y textures
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && tempRegion.isFlipX()) {
            tempRegion.flip(true, false); //true for flip, false for don't | x, y textures
            runningRight = true;
        }

        // Does our currentState equal our previous state? We add dt to the statetimer
        // Else it should be 0. If our current state doesn't equal the pervious state, then we haven't moved on yet so reset the timer.
        if (currentState == previousState) {
            stateTimer += dt;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return tempRegion;
    }

    public State getState() {
        // Jumping up or falling down from a jump
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } // falling down
        else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } // not standing still
        else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

}
