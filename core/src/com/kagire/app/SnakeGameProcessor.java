package com.kagire.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.kagire.SnakeRunner;
import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.LayerName;
import com.kagire.entity.enumeration.TileType;
import com.kagire.entity.game.GameSettings;
import com.kagire.entity.snake.Snake;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SnakeGameProcessor {

    private final GraphicsTray graphicsTray;

    public SnakeGameProcessor() {
        this.graphicsTray = new GraphicsTray();
    }

    public List<Coordinates2D> getUnoccupiedCoordinates(Snake snake, TiledMapTileLayer layer) {
        return getUnoccupiedCoordinates(snake, layer, new ArrayList<>());
    }

    public List<Coordinates2D> getUnoccupiedCoordinates(Snake snake, TiledMapTileLayer layer, List<Coordinates2D> coordinates) {
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

    public GameSettings getSettings(Game game) {
        return ((SnakeRunner) game).settings;
    }

    // graphics
    public TiledMapTile initializeTile(LayerName layer, TileType tileType) {
        return new StaticTiledMapTile(new TextureRegion(getTexture(layer, tileType)));
    }

    public TiledMapTile initializeTile(LayerName layer, TileType tileType, int regionX, int regionY, int width, int height) {
        return new StaticTiledMapTile(new TextureRegion(getTexture(layer, tileType), regionX, regionY, width, height));
    }

    private Texture getTexture(LayerName layer, TileType tileType) {
        return switch (layer) {
            case FOOD_LAYER:
                this.graphicsTray.reloadFoodTexture(tileType);
                yield this.graphicsTray.getFoodTexture();
            case SNAKE_LAYER:
                this.graphicsTray.reloadSnakeTexture(tileType);
                yield this.graphicsTray.getSnakeTexture();
            case GAME_LAYER:
                this.graphicsTray.reloadGrassTexture(tileType);
                yield this.graphicsTray.getGrassTexture();
        };
    }
}
