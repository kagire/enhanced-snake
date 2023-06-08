package com.kagire;

import com.badlogic.gdx.Game;
import com.kagire.entity.settings.GameSettings;
import com.kagire.game.SnakeGameScreen;

public class SnakeRunner extends Game {

    public GameSettings settings;

    @Override
    public void create() {
        settings = GameSettings.defaultSettings();
        this.setScreen(new SnakeGameScreen(this));
    }
}
