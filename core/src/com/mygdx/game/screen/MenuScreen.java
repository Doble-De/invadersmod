package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;
import com.mygdx.game.SpaceInvaders;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

public class MenuScreen extends SpaceInvadersScreen {

    BitmapFont play, exit;
    TextureRegion spacelogo = assets.logo;
    private float stateTime = 0;
    TextureRegion nave = assets.naveright.getKeyFrame(stateTime, true);
    TextureRegion start = assets.start;
    TextureRegion iexit = assets.exit;
    public SpriteBatch batch;
    boolean salir = false;

    public MenuScreen(SpaceInvaders game) {
        super(game);
        play = new BitmapFont();
        exit = new BitmapFont();
        batch = new SpriteBatch();
    }


    @Override
    public void render(float delta) {

        batch.begin();
        batch.draw(spacelogo,140 ,300 );
        //batch.draw(nave, 120, 200);
        if(!assets.update()){
            return;
        }

        //play.draw(batch, "Pulsa la tecla Espacio para jugar.", 140, 200);
        //exit.draw(batch, "Pulsa la tecla E para salir.", 140, 150);
        batch.draw(start, 275, 200);
        batch.draw(iexit, 275, 130);
        if(Controls.isShootPressed()) {
            if (salir){
                assets.selectSound.play();
            }
            salir = false;
        } else if (Controls.isDownPressed()){
            if (!salir){
                assets.selectSound.play();
            }
            salir = true;
        }

        if (!salir){
            batch.draw(nave, 230, 210);
        } else if (salir){
            batch.draw(nave, 230, 140);
        }
        //setScreen(new GameScreen(game));

        if (Controls.isEnterPressed()){
            assets.startSound.play();
            if (!salir){
                setScreen(new GameScreen(game));
            }else if (salir){
                System.exit(0);
            }
        }
        batch.end();
    }




    @Override
    public void resize(int width, int height) {
        super.resize(width,height);
    }

}
