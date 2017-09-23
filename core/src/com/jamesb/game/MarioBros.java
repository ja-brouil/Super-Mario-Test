package com.jamesb.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jamesb.game.Screens.PlayScreen;

public class MarioBros extends Game {
	public SpriteBatch batch; // holds all sprites and released as we need. Very memory intensive, only create one. Make this public so every scene can access this
        public static final int V_WIDTH = 400;
        public static final int V_HEIGHT = 208;
        public static final float PPM = 100;
        
        // Filter Values
        // Bits, read notes on maximum and other important info
        public static final short GROUND_BIT = 1;
        public static final short MARIO_BIT = 2;
        public static final short BRICK_BIT = 4;
        public static final short COIN_BIT = 8;
        public static final short DESTROYED_BIT = 16;
        public static final short ENEMY_BIT = 32;
        public static final short OBJECT_BIT = 64;
        public static final short ENEMY_HEAD_BIT = 128;
        
        // Asset Manager Create. Avoid using this as static!!
	public static AssetManager assetManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
                assetManager = new AssetManager();
                assetManager.load("Audio/Music/mario_music.ogg", Music.class);
                assetManager.load("Audio/Sounds/breakblock.wav", Sound.class);
                assetManager.load("Audio/Sounds/bump.wav", Sound.class);
                assetManager.load("Audio/Sounds/coin.wav", Sound.class);
                assetManager.load("Audio/Sounds/powerdown.wav", Sound.class);
                assetManager.load("Audio/Sounds/powerup_Spawn.wav", Sound.class);
                assetManager.load("Audio/Sounds/stomp.wav", Sound.class);
                assetManager.load("Audio/Sounds/small_jump.wav", Sound.class);
                assetManager.finishLoading(); // force to load everything before continuing
                
                // Playscreen must be put after to load music
                setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render(); // delegate the render method to the active screen
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
}
