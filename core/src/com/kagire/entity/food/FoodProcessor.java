package com.kagire.entity.food;

import com.kagire.entity.Coordinates2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FoodProcessor {

    private static final Random random;
    private static final List<Class<? extends Food>> availableFoodPool;

    static {
        random = new Random();
        availableFoodPool = List.of(Apple.class, Eggplant.class);
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

    public static Food randomAvailableFood(List<Coordinates2D> availableCoordinates) {
        var exclusions = new ArrayList<Class<? extends Food>>();
        for (var i = 0; i < availableFoodPool.size(); i++) {
            var food = randomFood(exclusions);
            var coordinates = randomAvailableCoordinate(availableCoordinates, food);
            if (coordinates != null) return generateFood(coordinates, food);
            else exclusions.add(food);
        }
        return null;
    }

    public static Class<? extends Food> randomFood(List<Class<? extends Food>> exclusions) {
        var foodPool = foodPool(exclusions);
        return foodPool.get(random.nextInt(foodPool.size()));
    }

    private static List<Class<? extends Food>> foodPool(List<Class<? extends Food>> exclusions) {
        return availableFoodPool.stream()
                .filter(f -> exclusions.stream().noneMatch(ef -> ef == f))
                .collect(Collectors.toList());
    }

    private static Food generateFood(Coordinates2D coordinates, Class<? extends Food> foodReference) {
        try {
            return foodReference.getDeclaredConstructor(int.class, int.class).newInstance(coordinates.x(), coordinates.y());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
