package com.kagire.entity.food;

import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.TileType;

import java.util.ArrayList;
import java.util.List;

public abstract class Food {

    /**
     * matrix, where first cell is coordinates start (0, 0)
     * others are coordinates, related to first
     * X coordinate is left-to-right
     * Y coordinate is bottom-to-top
     */
    public List<Coordinates2D> occupiedPlace;

    public List<Coordinates2D> coordinates;

    protected TileType tileType;

    protected Food(int x, int y) {
        this.tileType = initTileType();
        this.occupiedPlace = initOccupiedPlace();
        this.coordinates = new ArrayList<>();
        this.occupiedPlace.forEach(opc -> this.coordinates.add(new Coordinates2D(x + opc.x(), y + opc.y())));
    }

    public abstract int nutritionValue();

    protected abstract List<Coordinates2D> initOccupiedPlace();

    protected abstract TileType initTileType();

    public TileType tileType() {
        return tileType;
    }
}
