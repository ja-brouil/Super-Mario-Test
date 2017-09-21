/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jamesb.game.MarioBros;

/**
 *
 * @author james
 */
public abstract class InteractiveTileObject {
    
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    
    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.map = map;
        this.bounds = bounds;
        
        
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.PPM); //get position of the bounds rectangle // divided by 2 because bounds rectangle boxes start in the center for coordinates

        body = world.createBody(bdef); // remember you have to call the parent class when you create body

        shape.setAsBox((bounds.getWidth() / 2) / MarioBros.PPM, (bounds.getHeight() / 2) / MarioBros.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        // When we create the fixture that surrounds the object, we need to capture it into a variable so we can call it later
        
    }
    
    public abstract void onHeadHit();
    
    // Change filters here
    // If you created the fixture already you can change the filter here
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    
    // Return the cell number from tiled map
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / 16), (int) (body.getPosition().y * MarioBros.PPM / 16)); 
        // Scale back up since we scaled it down, then get the x and cast to int as this method only can take integers. Dont forget to divide by the tilesize which in this case was 16 pixels
    }
}
