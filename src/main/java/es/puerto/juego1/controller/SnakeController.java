package es.puerto.juego1.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.*;

public class SnakeController extends ControladorAbstracto {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final List<Rectangle> snake = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private Rectangle food;
    private Timeline gameLoop;

    private void setupGameLoop(Pane root) {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            moveSnake();
            checkCollisions(root);
        }));
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();
    }

    private void moveSnake() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }

        Rectangle head = snake.get(0);
        switch (direction) {
            case UP -> head.setY(head.getY() - TILE_SIZE);
            case DOWN -> head.setY(head.getY() + TILE_SIZE);
            case LEFT -> head.setX(head.getX() - TILE_SIZE);
            case RIGHT -> head.setX(head.getX() + TILE_SIZE);
        }
    }

    private void checkCollisions(Pane root) {
        Rectangle head = snake.get(0);

        if (head.getBoundsInParent().intersects(food.getBoundsInParent())) {
            growSnake();
            spawnFood(root);
        }

        if (head.getX() < 0 || head.getX() >= WIDTH ||
                head.getY() < 0 || head.getY() >= HEIGHT) {
            gameOver();
        }
    }

    private void growSnake() {
        Rectangle newSegment = new Rectangle(-TILE_SIZE, -TILE_SIZE, TILE_SIZE, TILE_SIZE);
        newSegment.setFill(Color.valueOf("#34495e"));
        snake.add(newSegment);
    }

    private void spawnFood(Pane root) {
        if (food != null)
            root.getChildren().remove(food);

        Random rand = new Random();
        food = new Rectangle(
                rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE,
                rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE,
                TILE_SIZE, TILE_SIZE);
        food.setFill(Color.valueOf("#e74c3c"));
        root.getChildren().add(food);
    }

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP -> {
                if (direction != Direction.DOWN)
                    direction = Direction.UP;
            }
            case DOWN -> {
                if (direction != Direction.UP)
                    direction = Direction.DOWN;
            }
            case LEFT -> {
                if (direction != Direction.RIGHT)
                    direction = Direction.LEFT;
            }
            case RIGHT -> {
                if (direction != Direction.LEFT)
                    direction = Direction.RIGHT;
            }
        }
    }

    private void gameOver() {
        gameLoop.stop();
        System.out.println("Game Over - Puntuación: " + (snake.size() - 1));
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}
