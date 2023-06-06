package com.kagire.snake.entity;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    public SnakeCell headCell;
    public final List<SnakeCell> cells;

    public Snake() {
        cells = new ArrayList<>();
    }

    public void addCell(int x, int y) {
        SnakeCell cell = new SnakeCell(x, y , null);
        if (cells.isEmpty()) {
            cell.next = headCell;
            headCell.previous = cell;
        } else {
            cell.next = cells.get(cells.size() - 1);
            cells.get(cells.size() - 1).previous = cell;
        }
        cells.add(cell);
    }

    public void addHeadCell(int x, int y) {
        headCell = new SnakeCell(x, y , Direction.UP);
    }

    public void moveSnake(Direction moveTo) {
        switch (moveTo) {
            case UP -> headCell.updateY(headCell.y + 1);
            case DOWN -> headCell.updateY(headCell.y - 1);
            case LEFT -> headCell.updateX(headCell.x - 1);
            case RIGHT -> headCell.updateX(headCell.x + 1);
        }
        headCell.direction = moveTo;
        for (SnakeCell cell : cells) {
            if (cell.x != cell.next.previousX) cell.updateX(cell.next.previousX);
            if (cell.y != cell.next.previousY) cell.updateY(cell.next.previousY);
        }
    }

    public void moveSnake() {
        moveSnake(headCell.direction);
    }

    public void enlargeSnake() {
        var previousCell = cells.isEmpty() ? headCell : cells.get(cells.size() - 1);
        addCell(previousCell.previousX, previousCell.previousY);
    }

    public boolean isHeadIn(int x, int y) {
        return headCell.x == x && headCell.y == y;
    }
}
