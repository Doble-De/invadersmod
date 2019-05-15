package com.mygdx.game.screen;

import com.mygdx.game.SpaceInvaders;

public class LoadingScreen extends SpaceInvadersScreen {

    public LoadingScreen(SpaceInvaders si) {
        super(si);
    }

    @Override
    public void render(float delta) {
        if(!assets.update()){
            return;
        }

        setScreen(new MenuScreen(game));
    }
}
