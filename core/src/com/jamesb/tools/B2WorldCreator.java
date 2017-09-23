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
import com.badlogic.gdx.utils.Array;
import com.jamesb.Sprites.Brick;
import com.jamesb.Sprites.Coin;
import com.jamesb.Sprites.Goomba;
import com.jamesb.game.MarioBros;
import com.jamesb.game.Screens.PlayScreen;

/**
 *
 * @author james 
 */
public class B2WorldCreator {
    private Array<Goomba> goombas;
    
    //private TiledMap map;
 
    public B2WorldCreator(PlayScreen screen){
        // For now, we'll make the bodies here. Later add it to classes
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
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
            fdef.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fdef);
        }
        
        // Brick
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            // Dynamic = affected by gravity
            // Static = can't move. Can be moved by programming.
            // Kinematic bodies = not affected by gravity but can be affected by velocity. E.G moving platforms
           
            new Brick(screen, rect);
            
        }
        
        // Coin

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
           
            new Coin(screen, rect);
            
        }
        
        // Create Goombas
        goombas = new Array<Goomba>();
        for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
  
}
