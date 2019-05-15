package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Assets;
import com.mygdx.game.Timer;
import com.mygdx.game.screen.GameScreen;

import java.util.logging.Handler;

public class World {
    Space space;
    Ship ship;
    AlienArmy alienArmy;
    Timer pauseTimer, finishTimer, endTimer;
    float pause = 3;
    float finish = 7;
    float end = 2;
    int randomx,  randomy;
    float stateTime;
    private int score = 0;
    private String textScore = "Score: ";
    int lifes = 3;
    boolean sound = true;
    BitmapFont puntuacion, vidas;
    enum StateGame{
        ACABADO, ENJUEGO, PAUSADO
    }

    StateGame stateGame;

    TextureRegion fuego1, fuego2;

    int WORLD_WIDTH, WORLD_HEIGHT;

    public World(int WORLD_WIDTH, int WORLD_HEIGHT){
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        space = new Space();
        ship = new Ship(WORLD_WIDTH/2);
        alienArmy = new AlienArmy(WORLD_WIDTH, WORLD_HEIGHT);
        puntuacion = new BitmapFont();
        vidas = new BitmapFont();
        stateGame = StateGame.ENJUEGO;
        pauseTimer = new Timer(pause);
        finishTimer = new Timer(finish);
        endTimer = new Timer(end);
    }

    public String render(float delta, SpriteBatch batch, Assets assets){

        fuego1 = assets.fuego1.getKeyFrame(stateTime, true);
        fuego2 = assets.fuego2.getKeyFrame(stateTime, true);
        if (lifes >0 ) {
            update(delta, assets);
            batch.begin();
            space.render(batch);
            ship.render(batch);
            batch.draw(assets.naveidle.getKeyFrame(stateTime, true), WORLD_WIDTH - 80, WORLD_HEIGHT - 15);
            if (stateGame == StateGame.ENJUEGO) alienArmy.render(batch);
            puntuacion.setColor(0.8f, 0.2f, 0.2f, 1.0f);
            puntuacion.draw(batch, textScore + score, WORLD_WIDTH - 380, WORLD_HEIGHT);
            vidas.draw(batch, "X " + String.valueOf(lifes),WORLD_WIDTH - 40, WORLD_HEIGHT - 5 );
            if (stateGame == StateGame.ACABADO){
                if (sound){
                    assets.winSound.play();
                }
                sound = false;
                puntuacion.draw(batch, "YOU WIN \n" + score, WORLD_WIDTH / 2 - 40, WORLD_HEIGHT / 2);

                batch.draw(fuego1, WORLD_WIDTH / 3, WORLD_HEIGHT / 3);
                batch.draw(fuego1, randomx, randomy);
                batch.draw(fuego1, randomx, randomy);
                batch.draw(fuego1, randomx, randomy);
                batch.draw(fuego2, randomx, randomy);
                batch.draw(fuego2, randomx, randomy);
                batch.draw(fuego2, randomx, randomy);
                batch.draw(fuego2, randomx, randomy);
                finishTimer.update(delta);
                if (finishTimer.check()){
                    return "win";
                }

            }
        }
        else {
            update(delta,assets);
            batch.begin();
            space.render(batch);
            ship.render(batch);
            alienArmy.render(batch);
            if (ship.isRespawning()){
                puntuacion.draw(batch, "GAME OVER", WORLD_WIDTH / 2 -30, WORLD_HEIGHT /2);
            }
            endTimer.update(delta);
            if (endTimer.check()) {
                return "over";
            }
        }
        batch.end();
        return "";
    }

    void update(float delta, Assets assets){

            setFrame(assets);


        if (!ship.isRespawning()){
            space.update(delta, assets);
            ship.update(delta, assets, WORLD_WIDTH, WORLD_WIDTH);
            if (stateGame == StateGame.ENJUEGO) alienArmy.update(delta, assets);
            checkCollisions(assets);
            checkAliensNumber();
        } else{
            pauseTimer.update(delta);
            if (pauseTimer.check()){
                ship.revive();
            }
        }
    }


    private void checkAliensNumber() {
        if (alienArmy.aliens.size == 0){
            ship.state = Ship.State.WINNER;
            stateGame = StateGame.ACABADO;
        }else if (alienArmy.aliens.size <= 20 && alienArmy.aliens.size > 15){
            alienArmy.moveTimer.set(1f);
        }else if (alienArmy.aliens.size <= 15 && alienArmy.aliens.size > 5){
            alienArmy.moveTimer.set(0.7f);
        }else if (alienArmy.aliens.size <= 5){
            alienArmy.moveTimer.set(0.4f);
        }
    }
    private void checkCollisions(Assets assets) {
        checkNaveInWorld();
        checkShootsInWorld();
        checkShootsToAlien(assets);
        checkShootsToShip(assets);
        checkNaveInWorld();
    }

    private void checkShootsToShip(Assets assets) {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for(AlienShoot shoot: alienArmy.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());

            if (Intersector.overlaps(shootRectangle, shipRectangle)) {
                ship.damage();
                lifes--;
                assets.explosionSound.play();
                shoot.remove();

            }
        }
    }

    private void checkShootsToAlien(Assets assets) {
        for(Shoot shoot: ship.weapon.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());
            for(Alien alien: alienArmy.aliens){
                if(alien.isAlive()) {
                    Rectangle alienRectangle = new Rectangle(alien.position.x, alien.position.y, alien.frame.getRegionWidth(), alien.frame.getRegionHeight());

                    if (Intersector.overlaps(shootRectangle, alienRectangle)) {
                        alien.kill();
                        shoot.remove();
                        assets.aliendieSound.play();
                        score +=100;
                    }
                }
            }
        }
    }

    private void checkShootsInWorld() {
        for(Shoot shoot: ship.weapon.shoots){
            if(shoot.position.y > WORLD_HEIGHT){
                shoot.remove();
            }
        }

        for(AlienShoot shoot: alienArmy.shoots){
            if(shoot.position.y < 0){
                shoot.remove();
            }
        }
    }

    private void checkNaveInWorld() {
        if(ship.position.x > WORLD_WIDTH-32){
            ship.position.x = WORLD_WIDTH-32;
        } else if(ship.position.x < 0){
            ship.position.x = 0;
        }
    }

    private void setFrame(Assets assets){
        fuego1 = assets.fuego1.getKeyFrame(stateTime, true);
        fuego2 = assets.fuego2.getKeyFrame(stateTime, true);
    }

}
