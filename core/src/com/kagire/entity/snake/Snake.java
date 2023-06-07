package com.kagire.entity.snake;

import com.kagire.entity.enumeration.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Snake {

    private final SnakeCell headCell;
    private final List<SnakeCell> cells;

    public Snake(int x, int y) {
        this.cells = new ArrayList<>();
        this.headCell = new SnakeCell(x, y , Direction.UP);
    }

    public void moveSnake() {
        moveSnake(headCell.getDirection());
    }

    public void moveSnake(Direction moveTo) {
        switch (moveTo) {
            case UP -> headCell.updateY(headCell.getY() + 1);
            case DOWN -> headCell.updateY(headCell.getY() - 1);
            case LEFT -> headCell.updateX(headCell.getX() - 1);
            case RIGHT -> headCell.updateX(headCell.getX() + 1);
        }
        headCell.setDirection(moveTo);
        for (SnakeCell cell : cells) {
            if (cell.getX() != cell.getNext().getPreviousX()) cell.updateX(cell.getNext().getPreviousX());
            if (cell.getY() != cell.getNext().getPreviousY()) cell.updateY(cell.getNext().getPreviousY());
        }
    }

    public boolean isHeadIn(int x, int y) {
        return headCell.getX() == x && headCell.getY() == y;
    }

    public void enlargeSnake() {
        var previousCell = cells.isEmpty() ? headCell : cells.get(cells.size() - 1);
        addCell(previousCell.getPreviousX(), previousCell.getPreviousY());
    }

    public void executeOnEachCell(Consumer<SnakeCell> consumer, boolean startFromHead) {
        var cell = headCell;
        if (startFromHead) {
            while (cell != null) {
                consumer.accept(cell);
                cell = cell.getPrevious();
            }
        } else {
            while (cell.getPrevious() != null) {
                cell = cell.getPrevious();
            }
            while (cell != null) {
                consumer.accept(cell);
                cell = cell.getNext();
            }
        }
    }

    private void addCell(int x, int y) {
        SnakeCell cell = new SnakeCell(x, y , null);
        if (cells.isEmpty()) {
            cell.setNext(headCell);
            headCell.setPrevious(cell);
        } else {
            cell.setNext(cells.get(cells.size() - 1));
            cells.get(cells.size() - 1).setPrevious(cell);
        }
        cells.add(cell);
    }
}
