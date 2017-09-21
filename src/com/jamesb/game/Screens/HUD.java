/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamesb.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jamesb.game.MarioBros;

/**
 *
 * @author james
 */
public class HUD implements Disposable{

    // Variable
    public Stage stage;
    private Viewport viewPort; // want the hud to stay the same, we will make a new camera for the HUD so the world can move on its on
    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    // Create widgets
    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;

    public HUD(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewPort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, sb); // Stage is an empty box. This is what allows us to create the HUD. We will create a table so we can set the HUD elements on the stage

        Table table = new Table();
        table.top(); // put this at the top of the stage
        table.setFillParent(true); // table is the size of the stage
        
        BitmapFont font2 = new BitmapFont();
        font2.getData().setScale(0.5f);

        // List of strings
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(font2, Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Add labels to the table
        // Top Row
        table.add(marioLabel).expandX().pad(10); // Expand extends the label to fill the whole row. If you multiple things that are expanded, they will each share equally the space on the screen. Pad = pad the top by X pixels
        table.add(worldLabel).expandX().pad(10);
        table.add(timeLabel).expandX().pad(10);

        // Lower Row
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table); // Add the table to the stage

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
    
}
