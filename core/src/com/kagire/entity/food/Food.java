package com.kagire.entity.food;

import com.kagire.entity.Coordinates2D;

import java.util.ArrayList;
import java.util.List;

public abstract class Food {

    /**
     * matrix, where first cell is coordinates start (0, 0)
     * others are coordinates, related to first
     * X coordinate is left-to-right
     * Y coordinate is bottom-to-top
     */
    protected List<Coordinates2D> occupiedPlace;

    public List<Coordinates2D> coordinates;

    protected Food(int x, int y) {
        this.occupiedPlace = initOccupiedPlace();
        this.coordinates = new ArrayList<>();
        this.occupiedPlace.forEach(opc -> this.coordinates.add(new Coordinates2D(x + opc.x(), y + opc.y())));
    }

    public abstract int nutritionValue();

    protected abstract List<Coordinates2D> initOccupiedPlace();

    public int getX() {
        return coordinates.get(0).x();
    }

    public int getY() {
        return coordinates.get(0).y();
    }
}
