package com.kagire.snake.entity;

public class SnakeCell {

    public int x;
    public int y;
    int previousX;
    int previousY;
    Direction direction;
    SnakeCell next;
    SnakeCell previous;

    SnakeCell(int x, int y, Direction direction) {
        this.x = x;
        this.previousX = x;
        this.y = y;
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
}
