package com.kagire.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kagire.entity.Coordinates2D;
import com.kagire.entity.enumeration.Direction;
import com.kagire.entity.enumeration.LayerName;
import com.kagire.entity.enumeration.TileType;
import com.kagire.entity.food.Apple;
import com.kagire.entity.food.Food;
import com.kagire.entity.snake.Snake;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static com.kagire.entity.enumeration.LayerName.*;
import static com.kagire.entity.enumeration.TileType.*;
import static com.kagire.game.SnakeGameProcessor.getUnoccupiedCoordinates;

public class SnakeGameScreen implements Screen {

    private final Game gameRunner;

    private TiledMap map;
    private final GraphicsTray graphicsTray;
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
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 10,  10);
        this.viewport = new FitViewport(10, 10, camera);

        this.graphicsTray = new GraphicsTray();
        this.initializeTileMap();
        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        this.renderer.setView(camera);

        this.snake = new Snake(0, 0);
        this.unoccupiedCoordinates = getUnoccupiedCoordinates(snake, getLayer(SNAKE_LAYER));
        var foodCoordinates = unoccupiedCoordinates.get(ThreadLocalRandom.current().nextInt(0, unoccupiedCoordinates.size()));
        this.food = new Apple(foodCoordinates.x(), foodCoordinates.y());
        this.addTile(APPLE, FOOD_LAYER, food.x, food.y);
        this.addTile(SNAKE, SNAKE_LAYER, 0, 0);
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
            if (snake.isHeadIn(food.x, food.y)){
                snake.enlargeSnake();
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
            }, 300);
        }
        isMoveAvailable = false;
    }

    private void moveSnake() {
        Optional.ofNullable(moveDirection).ifPresentOrElse(snake::moveSnake, snake::moveSnake);

        snake.executeOnEachCell((cell) -> removeTile(SNAKE_LAYER, cell.getPreviousX(), cell.getPreviousY()), true);
        snake.executeOnEachCell((cell) -> addTile(SNAKE, SNAKE_LAYER, cell.getX(), cell.getY()), false);
    }

    private void moveFood() {
        removeTile(FOOD_LAYER, food.x, food.y);
        unoccupiedCoordinates = getUnoccupiedCoordinates(snake, getLayer(SNAKE_LAYER));
        if (!unoccupiedCoordinates.isEmpty()) {
            var foodCoordinates = unoccupiedCoordinates.get(ThreadLocalRandom.current().nextInt(0, unoccupiedCoordinates.size()));
            removeTile(FOOD_LAYER, food.x, food.y);
            food = new Apple(foodCoordinates.x(), foodCoordinates.y());
            addTile(APPLE, FOOD_LAYER, food.x, food.y);
        } else {
            // TODO: endgame
        }
        addTile(SNAKE, SNAKE_LAYER, snake.getHeadCell().getX(), snake.getHeadCell().getY());
    }

    private void initializeTileMap() {
        map = new TiledMap();

        TiledMapTileLayer gameLayer = this.initializeLayer(GAME_LAYER, 10, 10);
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(this.initializeTile(GRASS));
                gameLayer.setCell(x, y, cell);
            }
        }
        this.initializeLayer(SNAKE_LAYER, 10, 10);
        this.initializeLayer(FOOD_LAYER, 10, 10);
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

    private TiledMapTile initializeTile(TileType tileType) {
        return new StaticTiledMapTile(new TextureRegion(this.graphicsTray.getTileTexture(tileType)));
    }

    private void addTile(TileType tileType, LayerName layerName, int x, int y) {
        var layer = getLayer(layerName);
        TiledMapTileLayer.Cell snakeCell = new TiledMapTileLayer.Cell();
        snakeCell.setTile(initializeTile(tileType));
        if (x <= layer.getWidth() && y <= layer.getHeight()) {
            layer.setCell(x, y, snakeCell);
        }
    }

    private void removeTile(LayerName layerName, int x, int y) {
        Optional.ofNullable(getLayer(layerName).getCell(x, y))
                .ifPresent(cell -> cell.setTile(null));
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
