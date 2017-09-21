/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jamesb.Sprites.Brick;
import com.jamesb.Sprites.Coin;
import com.jamesb.game.MarioBros;

/**
 *
 * @author james 
 */
public class B2WorldCreator {
    
    //private TiledMap map;
 
    public B2WorldCreator(World world, TiledMap map){
        // For now, we'll make the bodies here. Later add it to classes
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        // Ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // Dynamic = affected by gravity
            // Static = can't move. Can be moved by programming.
            // Kinematic bodies = not affected by gravity but can be affected by velocity. E.G moving platforms
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM); //get position of the rectangle // divided by 2 because rectangle boxes start in the center for coordinates
            
            body = world.createBody(bdef); // remember you have to call the parent class when you create body
            
            shape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM , (rect.getHeight() / 2) / MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
            
        }
        
        // Pipe
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // Dynamic = affected by gravity
            // Static = can't move. Can be moved by programming.
            // Kinematic bodies = not affected by gravity but can be affected by velocity. E.G moving platforms
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM); //get position of the rectangle // divided by 2 because rectangle boxes start in the center for coordinates
            
            body = world.createBody(bdef); // remember you have to call the parent class when you create body
            
            shape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM , (rect.getHeight() / 2) / MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        
        // Brick
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // Dynamic = affected by gravity
            // Static = can't move. Can be moved by programming.
            // Kinematic bodies = not affected by gravity but can be affected by velocity. E.G moving platforms
           
            new Brick(world, map, rect);
            
        }
        
        // Coin

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
           
            new Coin(world, map, rect);
            
        }
    }
 
    
}
