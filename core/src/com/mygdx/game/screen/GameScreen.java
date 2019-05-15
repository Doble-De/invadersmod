package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SpaceInvaders;
import com.mygdx.game.objects.World;

public class GameScreen extends SpaceInvadersScreen {

    public SpriteBatch spriteBatch;

    public OrthographicCamera camera;
    public Viewport viewport;

    public int SCENE_WIDTH = 384;
    public int SCENE_HEIGHT = 256;

    World world;
    String tipo = "";

    public GameScreen(SpaceInvaders spaceInvaders) {
        super(spaceInvaders);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(SCENE_WIDTH/2, SCENE_HEIGHT/2, 0);
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.apply();

        spriteBatch = new SpriteBatch();

        world = new World(SCENE_WIDTH, SCENE_HEIGHT);
    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);

        tipo = world.render(delta, spriteBatch, assets);

        if (tipo.equals("over")){
            setScreen(new GameOverScreen(game));
            spriteBatch.end();
        } else if (tipo.equals("win")){
            setScreen(new WinScreen(game));
            spriteBatch.end();
        }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        viewport.update(width ,height);
    }
}
