package com.kagire;

import com.badlogic.gdx.Game;
import com.kagire.app.SnakeGameScreen;
import com.kagire.entity.game.GameSettings;

public class SnakeRunner extends Game {

    public GameSettings settings;

    @Override
    public void create() {
        settings = GameSettings.defaultSettings();
        this.setScreen(new SnakeGameScreen(this));
    }
}
