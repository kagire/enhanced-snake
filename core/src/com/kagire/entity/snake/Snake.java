package com.kagire.entity.snake;

import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class Snake {

    private final SnakeCell headCell;
    private final List<SnakeCell> cells;
    private int nutrition = 0;

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
        if (nutrition > 0) enlargeSnake();
    }

    public void feedSnake(int feedAmount) {
        nutrition += feedAmount;
    }

    public void executeOnEachCell(Consumer<SnakeCell> consumer, boolean startFromHead) {
        if (startFromHead) {
            var cell = firstCell();
            while (cell != null) {
                consumer.accept(cell);
                cell = cell.getPrevious();
            }
        } else {
            var cell = lastCell();
            while (cell != null) {
                consumer.accept(cell);
                cell = cell.getNext();
            }
        }
    }

    public boolean isHeadIn(List<Coordinates2D> coordinates) {
        return coordinates.stream().anyMatch(c -> c.x() == headCell.getX() && c.y() == headCell.getY());
    }

    private SnakeCell firstCell() {
        return headCell;
    }

    private SnakeCell lastCell() {
        var cell = firstCell();
        while (cell.getPrevious() != null) {
            cell = cell.getPrevious();
        }
        return cell;
    }

    private void enlargeSnake() {
        var cell = lastCell();
        addCell(cell.getPreviousX(), cell.getPreviousY());
        nutrition--;
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
