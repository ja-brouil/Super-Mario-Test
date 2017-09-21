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
import com.jamesb.Sprites.InteractiveTileObject;

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
