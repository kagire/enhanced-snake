package com.kagire.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.Direction;
import com.kagire.entity.enumeration.LayerName;
import com.kagire.entity.enumeration.TileType;
import com.kagire.entity.food.Food;
import com.kagire.entity.food.FoodProcessor;
import com.kagire.entity.game.GameSettings;
import com.kagire.entity.snake.Snake;

import java.util.*;

import static com.kagire.entity.enumeration.LayerName.*;
import static com.kagire.entity.enumeration.TileType.GRASS;
import static com.kagire.entity.enumeration.TileType.SNAKE;

public class SnakeGameScreen implements Screen {

    private final Game gameRunner;
    private final SnakeGameProcessor gameProcessor;

    private TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final Snake snake;
    private Food food;
    private List<Coordinates2D> unoccupiedCoordinates;
    private boolean isMoveAvailable = true;
    private Direction moveDirection = null;

    private static final Timer timer;

    static {
        timer = new Timer();
    }

    public SnakeGameScreen(Game gameRunner) {
        this.gameRunner = gameRunner;
        this.gameProcessor = new SnakeGameProcessor();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, getGameWidth(), getGameHeight());
        this.viewport = new FitViewport(getGameWidth(), getGameHeight(), camera);

        this.initializeTileMap();
        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        this.renderer.setView(camera);

        this.snake = new Snake(0, 0);
        this.unoccupiedCoordinates = this.gameProcessor.getUnoccupiedCoordinates(snake, getLayer(SNAKE_LAYER));
        var possibleFood =  FoodProcessor.randomAvailableFood(unoccupiedCoordinates);
        if (possibleFood != null) {
            this.food = possibleFood;
            this.renderFood();
        }
        this.addTile(SNAKE, SNAKE_LAYER, snake.getHeadCell().getX(), snake.getHeadCell().getX());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        camera.update();
        renderer.render();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveDirection = Direction.LEFT;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveDirection = Direction.RIGHT;
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveDirection = Direction.UP;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveDirection = Direction.DOWN;
        }

        if (isMoveAvailable) {
            moveSnake();
            if (snake.isHeadIn(food.coordinates)){
                snake.feedSnake(food.nutritionValue());
                moveFood();
            }
            moveDirection = null;
            moveCD();
        }
    }

    private void moveCD() {
        if (isMoveAvailable) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isMoveAvailable = true;
                }
            }, 1000 / getSettings().gameRules.gameSpeed);
        }
        isMoveAvailable = false;
    }

    private void moveSnake() {
        Optional.ofNullable(moveDirection).ifPresentOrElse(snake::moveSnake, snake::moveSnake);

        snake.executeOnEachCell((cell) -> removeTile(SNAKE_LAYER, cell.getPreviousX(), cell.getPreviousY()), true);
        snake.executeOnEachCell((cell) -> addTile(SNAKE, SNAKE_LAYER, cell.getX(), cell.getY()), false);
    }

    private void moveFood() {
        food.coordinates.forEach(coordinate -> removeTile(FOOD_LAYER, coordinate.x(), coordinate.y()));
        unoccupiedCoordinates = gameProcessor.getUnoccupiedCoordinates(snake, getLayer(SNAKE_LAYER), unoccupiedCoordinates);
        var possibleFood = FoodProcessor.randomAvailableFood(unoccupiedCoordinates);
        if (!unoccupiedCoordinates.isEmpty() && possibleFood != null) {
            food = possibleFood;
            renderFood();
        } else {
            endgame();
        }
        addTile(SNAKE, SNAKE_LAYER, snake.getHeadCell().getX(), snake.getHeadCell().getY());
    }

    private void initializeTileMap() {
        map = new TiledMap();

        TiledMapTileLayer gameLayer = this.initializeLayer(GAME_LAYER, getGameWidth(), getGameHeight());
        for(int x = 0; x < gameLayer.getWidth(); x++) {
            for(int y = 0; y < gameLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(gameProcessor.initializeTile(GAME_LAYER, GRASS));
                gameLayer.setCell(x, y, cell);
            }
        }
        this.initializeLayer(SNAKE_LAYER, getGameWidth(), getGameHeight());
        this.initializeLayer(FOOD_LAYER, getGameWidth(), getGameHeight());
    }

    private TiledMapTileLayer getLayer(LayerName layerName) {
        return (TiledMapTileLayer) map.getLayers().get(layerName.getValue());
    }

    private TiledMapTileLayer initializeLayer(LayerName layerName, int width, int height) {
        TiledMapTileLayer layer = new TiledMapTileLayer(width, height, 16, 16);
        layer.setName(layerName.getValue());
        map.getLayers().add(layer);
        return layer;
    }

    private void addTile(TileType tileType, LayerName layerName, int x, int y) {
        var layer = getLayer(layerName);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(gameProcessor.initializeTile(layerName, tileType));
        if (x <= layer.getWidth() && y <= layer.getHeight()) {
            layer.setCell(x, y, cell);
        }
    }

    private void removeTile(LayerName layerName, int x, int y) {
        Optional.ofNullable(getLayer(layerName).getCell(x, y))
                .ifPresent(cell -> cell.setTile(null));
    }

    public void renderFood() {
        var layer = getLayer(FOOD_LAYER);
        var maxYOffset = food.occupiedPlace.stream().max(Comparator.comparing(Coordinates2D::y)).orElseThrow();
        for (var i = 0; i <  food.coordinates.size(); i++) {
            var coordinates = food.coordinates.get(i);
            var xOffset = i == 0 ? 0 : coordinates.x() - food.coordinates.get(i - 1).x();
            var yOffset = i == 0 ? 0 : coordinates.y() - food.coordinates.get(i - 1).y();
            TiledMapTileLayer.Cell foodCell = new TiledMapTileLayer.Cell();
            foodCell.setTile(gameProcessor.initializeTile(FOOD_LAYER, food.tileType(), xOffset * 16, (maxYOffset.y() - yOffset) * 16, 16, 16));
            if (coordinates.x() <= layer.getWidth() && coordinates.y() <= layer.getHeight()) {
                layer.setCell(coordinates.x(), coordinates.y(), foodCell);
            }
        }
    }

    private int getGameWidth() {
        return getSettings().mapSettings.mapWidth;
    }

    private int getGameHeight() {
        return getSettings().mapSettings.mapHeight;
    }

    private GameSettings getSettings() {
        return gameProcessor.getSettings(gameRunner);
    }

    //TODO: endgame
    private void endgame() {
        System.out.println("endgame");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
