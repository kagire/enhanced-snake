package com.kagire.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.kagire.entity.enumeration.TileType;

public class GraphicsTray {

    private final Texture appleImage;
    private final Texture snakeImage;
    private final Texture grassImage;

    public GraphicsTray() {
        this.appleImage = new Texture(Gdx.files.internal("apple.png"));
        this.snakeImage = new Texture(Gdx.files.internal("snake.png"));
        this.grassImage = new Texture(Gdx.files.internal("grass.png"));
    }

    public Texture getTileTexture(TileType tileType) {
        return switch (tileType) {
            case GRASS -> grassImage;
            case APPLE -> appleImage;
            case SNAKE -> snakeImage;
        };
    }
}
