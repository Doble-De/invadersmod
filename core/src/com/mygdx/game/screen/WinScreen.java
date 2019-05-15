package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controls;
import com.mygdx.game.SpaceInvaders;

public class WinScreen extends SpaceInvadersScreen {

    TextureRegion win = assets.win;
    TextureRegion playagain = assets.playagain;
    TextureRegion iexit = assets.exit;
    private float stateTime = 0;
    TextureRegion nave = assets.naveshoot.getKeyFrame(stateTime, true);
    boolean salir = false;
    public SpriteBatch batch;
    public Viewport viewport;
    public int SCENE_WIDTH = 384;
    public int SCENE_HEIGHT = 256;
    public OrthographicCamera camera;



    public WinScreen(SpaceInvaders game) {
        super(game);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.set(SCENE_WIDTH/2, SCENE_HEIGHT/2, 0);
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();
    }

    @Override
    public void render(float delta){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(win,SCENE_WIDTH /2 -105 ,160 );

        batch.draw(playagain, SCENE_WIDTH /2 -45, 80, 90, 50);
        batch.draw(iexit, SCENE_WIDTH /2 -30, 50, 60, 20);
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
            batch.draw(nave, SCENE_WIDTH /2 -75, 100);
        } else if (salir){
            batch.draw(nave, SCENE_WIDTH /2 -60, 55);
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
        viewport.update(width, height);
    }
}
