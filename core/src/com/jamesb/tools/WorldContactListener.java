/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.jamesb.Sprites.Enemy;
import com.jamesb.Sprites.InteractiveTileObject;
import com.jamesb.game.MarioBros;

/**
 *
 * @author james
 */
public class WorldContactListener implements ContactListener {



    @Override // Called when two fixtures begin to collide
    // Two fixtures here. We need to figure out which is which
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        // Grab the gategory bits
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        // Check which fixture is mario's head | use instanceof to return the class
         if ((fixA.getUserData() == "head" || fixB.getUserData() == "head")) { // these are NOT STRINGS. they are objects so do NOT USE .equal() to compare them
            Fixture head;
            Fixture object;
            if (fixA.getUserData().equals("head")) {
                head = fixA;
                object = fixB;

                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).onHeadHit();  // if the object that you hit is an interactive tile object, call the head hit method
                }
            } else if (fixB.getUserData().equals("head")) {
                head = fixB;
                object = fixA;
                if (object.getUserData() instanceof InteractiveTileObject) {
                    ((InteractiveTileObject) object.getUserData()).onHeadHit();  // if the object that you hit is an interactive tile object, call the head hit method
                }
            }

        }
         
         // Goomba head collision detection
        switch (cDef){
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT: // if mario collides with the enemy's head bit
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT){
                    ((Enemy) fixA.getUserData()).hitOnHead();
                }
                else {
                    ((Enemy) fixB.getUserData()).hitOnHead(); // set the fixture to the enemy
                }
                break;
            case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT: // enemy colliding with the world
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false); // set the fixture to the enemy
                }
                break;
            case MarioBros.ENEMY_BIT | MarioBros.GROUND_BIT: // enemy colliding with the world
            if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT){
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
            }
            else {
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false); // set the fixture to the enemy
            }
            break;
            case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
                System.out.println("Mario died!");
                break;
            case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
        }
         
         
    }

    @Override // End contact when two fixtures disconnect from each other
    public void endContact(Contact contact) {

    }

    @Override // Change characteristics for the collision
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override // results due to the collision of the boxes
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
