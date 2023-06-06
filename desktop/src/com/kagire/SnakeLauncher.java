package com.kagire;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.kagire.snake.SnakeRunner;

public class SnakeLauncher {

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        //config.setMaximized(true);
        config.setTitle("snake");
        new Lwjgl3Application(new SnakeRunner(), config);
    }
}
