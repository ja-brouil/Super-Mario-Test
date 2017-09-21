package com.jamesb.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jamesb.game.Screens.PlayScreen;

public class MarioBros extends Game {
	public SpriteBatch batch; // holds all sprites and released as we need. Very memory intensive, only create one. Make this public so every scene can access this
        public static final int V_WIDTH = 400;
        public static final int V_HEIGHT = 208;
        public static final float PPM = 100;
        
        // Filter Values
        // Bits, read notes on maximum and other important info
        public static final short DEFAULT_BIT = 1;
        public static final short MARIO_BIT = 2;
        public static final short BRICK_BIT = 4;
        public static final short COIN_BIT = 8;
        public static final short DESTROYED_BIT = 16;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render(); // delegate the render method to the active screen
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		
	}
}
