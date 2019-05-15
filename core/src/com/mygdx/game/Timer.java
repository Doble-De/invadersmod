package com.mygdx.game;

public class Timer {
    float time;
    float max;

    public Timer(float max){
        this.max = max;
    }

    public void update(float delta){
        time += delta;
    }

    public boolean check(){
        if(time >= max){
            time = 0;
            return true;
        }
        return false;
    }
    public void reset(){
        time = 0;
    }

    public void set(float max){
        this.max = max;
    }
}