package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controls;
import com.mygdx.game.SpaceInvaders;

public class GameOverScreen extends SpaceInvadersScreen{

    TextureRegion over = assets.over;
    TextureRegion tryagain = assets.tryagain;
    TextureRegion iexit = assets.exit;
    private float stateTime = 0;
    TextureRegion nave = assets.navedie.getKeyFrame(stateTime, true);
    boolean salir = false;
    public SpriteBatch batch;
    public Viewport viewport;
    public int SCENE_WIDTH = 384;
    public int SCENE_HEIGHT = 256;
    public OrthographicCamera camera;



    public GameOverScreen(SpaceInvaders game) {
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

        batch.draw(over,SCENE_WIDTH /2 -55 ,160, 110, 50 );

        batch.draw(tryagain,SCENE_WIDTH /2 -40, 90, 80, 25);
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
            batch.draw(nave, SCENE_WIDTH /2 -70, 85);
        } else if (salir){
            batch.draw(nave, SCENE_WIDTH /2 -60, 45);
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
