package com.kagire.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kagire.snake.entity.Direction;
import com.kagire.snake.entity.Snake;

import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class SnakeGameScreen implements Screen {

    private static final String GAME_LAYER = "game-layer";
    private static final String SNAKE_LAYER = "snake-layer";
    private static final String FOOD_LAYER = "food-layer";

    private final Game gameRunner;

    private TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;

    private final Snake snake;
    private int foodX;
    private int foodY;
    boolean isMoveAvailable = true;
    Direction moveDirection = null;

    private static final Timer timer;

    static {
        timer = new Timer();
    }

    public SnakeGameScreen(Game gameRunner) {
        this.gameRunner = gameRunner;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 10,  10);

        this.initializeTileMap();
        this.snake = new Snake();
        this.snake.addHeadCell(0, 0);
        this.renderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);
        this.renderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.7f, 0.7f, 1.0f, 1);
        camera.update();
        renderer.render();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.moveDirection = Direction.LEFT;
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.moveDirection = Direction.RIGHT;
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.moveDirection = Direction.UP;
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.moveDirection = Direction.DOWN;
        }

        if (isMoveAvailable) {
            if (Objects.nonNull(this.moveDirection)) snake.moveSnake(this.moveDirection);
            else snake.moveSnake();
            this.moveDirection = null;
            if (snake.isHeadIn(foodX, foodY)) snake.enlargeSnake();
            this.moveCD();
            this.redrawSnake();
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
        this.isMoveAvailable = false;
    }

    private void redrawSnake() {
        var layer = (TiledMapTileLayer) this.map.getLayers().get(SNAKE_LAYER);
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
               Optional.ofNullable(layer.getCell(x, y)).ifPresent(cell -> cell.setTile(null));
            }
        }
        Pixmap orangePixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        orangePixmap.setColor(199, 180, 60, 1);
        orangePixmap.fillRectangle(0, 0 , 16, 16);
        var newHeadCell =  new TiledMapTileLayer.Cell();
        newHeadCell.setTile((new StaticTiledMapTile(new TextureRegion(new Texture(orangePixmap)))));
        layer.setCell(snake.headCell.x, snake.headCell.y, newHeadCell);
        this.snake.cells.forEach(snakeCell -> {
            var newCell =  new TiledMapTileLayer.Cell();
            newCell.setTile((new StaticTiledMapTile(new TextureRegion(new Texture(orangePixmap)))));
            layer.setCell(snakeCell.x, snakeCell.y, newCell);
        });
    }

    private void initializeTileMap() {
        map = new TiledMap();

        TiledMapTileLayer gameLayer = new TiledMapTileLayer(10, 10, 16, 16);
        gameLayer.setName(GAME_LAYER);
        Pixmap greenPixmap = new Pixmap(16, 16, Pixmap.Format.RGB888);
        greenPixmap.setColor(6, 186, 6, 1);
        greenPixmap.fillRectangle(0, 0 , 16, 16);
        Pixmap limePixmap = new Pixmap(16, 16, Pixmap.Format.RGB565);
        limePixmap.setColor(72, 232, 72, 1);
        limePixmap.fillRectangle(0, 0 , 16, 16);
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if (x % 2 == y % 2) cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(greenPixmap))));
                else cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(limePixmap))));
                gameLayer.setCell(x, y, cell);
            }
        }

        TiledMapTileLayer snakeLayer = new TiledMapTileLayer(10, 10, 16, 16);
        snakeLayer.setName(SNAKE_LAYER);
        Pixmap orangePixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        orangePixmap.setColor(199, 180, 60, 1);
        orangePixmap.fillRectangle(0, 0 , 16, 16);
        TiledMapTileLayer.Cell snakeCell = new TiledMapTileLayer.Cell();
        snakeCell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(orangePixmap))));
        snakeLayer.setCell(0, 0, snakeCell);

        TiledMapTileLayer foodLayer = new TiledMapTileLayer(10, 10, 16, 16);
        foodLayer.setName(FOOD_LAYER);
        Pixmap redPixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        redPixmap.setColor(1, 0f, 0, 1);
        redPixmap.fillRectangle(0, 0 , 16, 16);
        TiledMapTileLayer.Cell foodCell = new TiledMapTileLayer.Cell();
        foodCell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(redPixmap))));
        foodX = ThreadLocalRandom.current().nextInt(1, 10);
        foodY = ThreadLocalRandom.current().nextInt(1, 10);
        foodLayer.setCell(foodX, foodY, foodCell);

        this.map.getLayers().add(gameLayer);
        this.map.getLayers().add(snakeLayer);
        this.map.getLayers().add(foodLayer);
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
