package com.kagire.entity;

public record Coordinates2D(int x, int y) {

    public static Coordinates2D of(int x, int y) {
        return new Coordinates2D(x, y);
    }
}
