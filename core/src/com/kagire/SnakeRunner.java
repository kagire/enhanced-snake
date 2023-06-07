package com.kagire;

import com.badlogic.gdx.Game;
import com.kagire.game.SnakeGameScreen;

public class SnakeRunner extends Game {

    @Override
    public void create() {
        this.setScreen(new SnakeGameScreen(this));
    }
}
