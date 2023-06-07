package com.kagire.entity.snake;

import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.Direction;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SnakeCell {

    private int x;
    private int y;
    private int previousX;
    private int previousY;
    @Setter
    private Direction direction;
    @Setter
    private SnakeCell next;
    @Setter
    private SnakeCell previous;

    SnakeCell(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.previousX = x;
        this.previousY = y;
        this.direction = direction;
    }

    void updateX(int x) {
        this.previousX = this.x;
        this.previousY = this.y;
        this.x = x;
    }

    void updateY(int y) {
        this.previousX = this.x;
        this.previousY = this.y;
        this.y = y;
    }

    public boolean compareXY(Coordinates2D coordinates) {
        return x == coordinates.x() && y == coordinates.y();
    }
}
