package com.kagire.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LayerName {

    GAME_LAYER("game-layer"),
    SNAKE_LAYER("snake-layer"),
    FOOD_LAYER("food-layer");

    @Getter
    private String value;
}
