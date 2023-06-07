package com.kagire.entity.food;

public class Apple extends Food {

    private final static int NUTRITION_VALUE = 1;

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    int nutritionValue() {
        return NUTRITION_VALUE;
    }
}
