package com.kagire.entity.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TileType {

    // snake
    SNAKE("snake.png"),

    // grass
    GRASS("grass.png"),

    // food
    APPLE("apple.png"),
    EGGPLANT("eggplant.png", 1, 2);

    public String textureName;
    public int textureXSize;
    public int textureYSize;

    TileType(String textureName) {
        this.textureName = textureName;
        this.textureXSize = 1;
        this.textureYSize = 1;
    }
}
