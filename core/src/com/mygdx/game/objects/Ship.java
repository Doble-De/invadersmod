package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {



    enum State {
        IDLE, LEFT, RIGHT, SHOOT, DYING, RESPAWN,WINNER
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;
    int initialPosition;
    TextureRegion frame;

    Weapon weapon;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        this.initialPosition = initialPosition;
        state = State.IDLE;
        stateTime = 0;

        weapon = new Weapon();
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            case DYING:
                frame = assets.navedie.getKeyFrame(stateTime);
                break;
            case RESPAWN:
                frame = assets.navedie.getKeyFrame(11);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets, int WORLD_WIDTH, int WORLD_HEIGHT) {
        stateTime += delta;

        if (state == State.DYING){
            if (assets.navedie.isAnimationFinished(stateTime)){
                state = State.RESPAWN;
            }
        } else if (state == State.WINNER){
            animationWin(WORLD_WIDTH, WORLD_HEIGHT);
        } else if(Controls.isLeftPressed()){
            moveLeft();
        } else if(Controls.isRightPressed()){
            moveRight();
        } else {
            idle();
        }

        if(Controls.isShootPressed() && state != State.DYING && state != State.RESPAWN) {
            shoot();
            assets.shootSound.play();
        }

        setFrame(assets);

        weapon.update(delta, assets);
    }

    void idle(){
        state = State.IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = State.LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = State.RIGHT;
    }

    public void revive() {
        position.x = initialPosition;
        position.y = 10;
        idle();
    }

    void shoot(){
        state = State.SHOOT;
        weapon.shoot(position.x +16);
    }

    public void damage() {
        position.y -= frame.getRegionHeight();
        stateTime = 0;
        state = State.DYING;
    }

    public boolean isRespawning() {
        return state == State.RESPAWN;
    }

    private void animationWin(int WORLD_WIDTH, int WORLD_HEIGHT) {
        int limit =frame.getRegionWidth()/2;
        if (this.position.x < WORLD_WIDTH/2- limit || this.position.x > WORLD_WIDTH/2-limit){
            if (this.position.x < WORLD_WIDTH/2- limit){
                this.position.x += speed/7;
            }else {
                this.position.x -= speed/7;
            }
        }
        if (position.y <= 50) {
            this.position.y += speed / 6;
        }
    }
}
