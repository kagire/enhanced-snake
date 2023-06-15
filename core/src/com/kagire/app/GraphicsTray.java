package com.kagire.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.kagire.entity.enumeration.TileType;

public class GraphicsTray {

    private TileType currentFood;
    private Texture foodImage;
    private TileType currentSnake;
    private Texture snakeImage;
    private TileType currentGrass;
    private Texture grassImage;

    public GraphicsTray() {
        this.foodImage = new Texture(Gdx.files.internal("apple.png"));
        this.snakeImage = new Texture(Gdx.files.internal("snake.png"));
        this.grassImage = new Texture(Gdx.files.internal("grass.png"));
    }

    public void reloadFoodTexture(TileType reloadTo) {
        if (currentFood != reloadTo) {
            this.foodImage = new Texture(Gdx.files.internal(reloadTo.textureName));
            this.currentFood = reloadTo;
        }
    }

    public void reloadSnakeTexture(TileType reloadTo) {
        if (currentSnake != reloadTo) {
            this.snakeImage = new Texture(Gdx.files.internal(reloadTo.textureName));
            this.currentSnake = reloadTo;
        }
    }

    public void reloadGrassTexture(TileType reloadTo) {
        if (currentGrass != reloadTo) {
            this.grassImage = new Texture(Gdx.files.internal(reloadTo.textureName));
            this.currentGrass = reloadTo;
        }
    }

    public Texture getFoodTexture() {
        return this.foodImage;
    }

    public Texture getSnakeTexture() {
        return this.snakeImage;
    }

    public Texture getGrassTexture() {
        return this.grassImage;
    }
}
