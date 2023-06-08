package com.kagire.entity.food;

import com.kagire.entity.Coordinates2D;

import java.util.List;

public class Apple extends Food {

    private final static int NUTRITION_VALUE = 1;

    public Apple(int x, int y) {
        super(x, y);
    }

    @Override
    public int nutritionValue() {
        return NUTRITION_VALUE;
    }

    @Override
    protected List<Coordinates2D> initOccupiedPlace() {
        return List.of(Coordinates2D.of(0, 0));
    }
}
