package com.kagire.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.kagire.SnakeRunner;
import com.kagire.entity.Coordinates2D;
import com.kagire.entity.settings.GameSettings;
import com.kagire.entity.snake.Snake;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SnakeGameProcessor {

    public static List<Coordinates2D> getUnoccupiedCoordinates(Snake snake, TiledMapTileLayer layer) {
        return getUnoccupiedCoordinates(snake, layer, new ArrayList<>());
    }

    public static List<Coordinates2D> getUnoccupiedCoordinates(Snake snake, TiledMapTileLayer layer, List<Coordinates2D> coordinates) {
        coordinates.clear();
        for (var w = 0; w < layer.getWidth(); w++) {
            for (var h = 0; h < layer.getWidth(); h++) {
                coordinates.add(new Coordinates2D(w, h));
            }
        }
        return coordinates
                .stream()
                .filter(c -> !snake.getHeadCell().compareXY(c) && snake.getCells().stream().noneMatch(sc -> sc.compareXY(c)))
                .collect(Collectors.toList());
    }

    public static GameSettings getSettings(Game game) {
        return ((SnakeRunner) game).settings;
    }
}
