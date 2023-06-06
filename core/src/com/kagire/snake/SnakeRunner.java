package com.kagire.snake;

import com.badlogic.gdx.Game;

public class SnakeRunner extends Game {

    @Override
    public void create() {
        this.setScreen(new SnakeGameScreen(this));
    }
}
