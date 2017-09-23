/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jamesb.Sprites.Enemy;
import com.jamesb.Sprites.Goomba;
import com.jamesb.Sprites.Mario;
import com.jamesb.game.MarioBros;
import com.jamesb.tools.B2WorldCreator;
import com.jamesb.tools.WorldContactListener;



/**
 *
 * @author james
 */
public class PlayScreen implements Screen{
    
    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort; // different types of window ports
    private HUD hud;
    private Mario player;
    
    // These are needed to load TMX Map files
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    
    // Box2D variables
    private World world;
    public Box2DDebugRenderer b2dr; //give small box for debug purposes
    private Vector2 gravity;
    private B2WorldCreator creator;
    
    // Sprites
    private TextureAtlas atlas;
    
    // Sounds
    private Music music;
    private Sound jumpSound;
    
    public PlayScreen(MarioBros game){ 
        this.game = game;
        gameCam = new OrthographicCamera(); // follow player around the world
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);   // maintain virtual aspect ratio     //MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam
        hud = new HUD(game.batch); //hud initialization
        
 
        // Initialize the map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM); // map + scale
        
        // The default game cam will center around 0,0. We need to change this to show all the 4 quadrants otherwise the camera would only show the first quandrant.
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0); // x, y, z if using 3D
        
        // Start world
        gravity = new Vector2(0, -9.81f);
        world = new World(gravity, true); // Gravity for x, y, are objects asleep by default? true/false
        b2dr = new Box2DDebugRenderer();
        
        // Mario Sprites
        atlas = new TextureAtlas(Gdx.files.internal("Sprite/Mario_and_Enemies.pack"));
        
        // Create Mario
        player = new Mario(this);
        
        
        
        // We want to create bodies at every map object layer
        // Layers start from the ground up 
        creator = new B2WorldCreator(this);
        
        // Contact listerner for collision
        world.setContactListener(new WorldContactListener());
        
        // Start music
        music = MarioBros.assetManager.get("Audio/Music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();
        jumpSound = MarioBros.assetManager.get("Audio/Sounds/small_jump.wav", Sound.class);
        
 
  
        
    }
    
    // Extra Getters
    public TiledMap getMap(){
        return map;
    }
    
    public World getWorld(){
        return world;
    }
    
    // Getter for atlas
    public TextureAtlas getAtlas(){
        return atlas;   
    }
    
    // input handling
    public void handleInput(float dt){
            
            // Keyboard input
            // Just pressed = right away | is pressed = can be held down
            // two Ways to get an objet to move. Force = gradual change | Impulse = immediate change
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                if (player.b2body.getLinearVelocity().y == 0){ // Check if he isn't already in the air to disallow double jumps
                   player.b2body.applyLinearImpulse(new Vector2(0, 4), player.b2body.getWorldCenter(), true); // Apply special gravity right away | Get world center = apply at the center, otherwise there is torque, it will change the angle. | 3rd parameter to awake the object 
                   jumpSound.play();
                }   
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            
            if  (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
                System.exit(0);
            }
    }
    
    // update game world
    public void update(float dt){
  
        // check for input
        handleInput(dt);
        // how many times do we need to update?
        world.step(1 / 60f , 6, 2); // First is update time | second is accuracy of collision, 
        
        // how many steps you want to check reccomended is 6 or 8 | accuracy of setting a body after a collision, reccomended is 2 or 3
        // Higher the number for second int the more intense the physics engine is
        
        // update player position
        player.update(dt);
        
        // update enmies
        for (Enemy enemy: creator.getGoombas()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM){ // when you get close to an enemy, active it. Calculate the pixels needed.
                enemy.b2body.setActive(true);
            }
        }
        
        // track mario cam
        gameCam.position.x = player.b2body.getPosition().x; // only want to track mario on X axis;
        
        // update cam when it moves
        gameCam.update();
        // Update map to render only what the game cam can see
        mapRenderer.setView(gameCam);
        
        // Update HUD
        hud.update(dt);
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        
        update(delta);
  
        // Clear screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // render map  
        mapRenderer.render();
        
        // render boxes
        b2dr.render(world, gameCam.combined);
        
        // draw sprites
        game.batch.setProjectionMatrix(gameCam.combined); // set the camera to render only what we want to see
        game.batch.begin(); //open batch and start drawing
        player.draw(game.batch);// draw the sprites needed
        for (Enemy enemy: creator.getGoombas()){
            enemy.draw(game.batch);
        }
        game.batch.end(); //close batch
        
        // Set Batch to what needs to be drawn
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined); //Start the camera
        hud.stage.draw(); //draw the HUD
        
        
    }

    @Override
    public void resize(int width, int height) {
        // Changing the size of the screen
        // Adjust the viewport here
        gamePort.update(width, height); // pass arguments of the size
        
    }
 
    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        // Free up ram during garbage collection
        // Remember the framework is in C++
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        
    }
    
   
    
}
