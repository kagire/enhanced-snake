package com.kagire.entity.food;

import com.kagire.entity.Coordinates2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodProcessor {

    private static Random random;

    static {
        random = new Random();
    }

    public static Coordinates2D randomAvailableCoordinate(List<Coordinates2D> availableCoordinates, Class<? extends Food> foodReference) {
        try {
            var occupiedPlace = foodReference
                    .getDeclaredConstructor(int.class, int.class)
                    .newInstance(0, 0).initOccupiedPlace();
            if (occupiedPlace.size() > availableCoordinates.size()) return null;
            var availableList = new ArrayList<Coordinates2D>();
            for (var availableCoordinate : availableCoordinates) {
                var valid = true;
                for (var checkedCoordinate : occupiedPlace) {
                    var possibleCoordinate = availableCoordinates.stream()
                            .filter(ac -> availableCoordinate.x() + checkedCoordinate.x() == ac.x() &&
                                    availableCoordinate.y() + checkedCoordinate.y() == ac.y())
                            .findFirst();
                    if (possibleCoordinate.isEmpty()) {
                        valid = false;
                        break;
                    }
                }
                if (valid) availableList.add(availableCoordinate);
            }
            return availableList.isEmpty() ? null : availableList.get(random.nextInt(availableList.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO: optimize & beautify
    public static Food randomFood(int x, int y) {
        return random.nextInt(2) == 1 ? new Apple(x, y) : new Eggplant(x, y);
    }
}
