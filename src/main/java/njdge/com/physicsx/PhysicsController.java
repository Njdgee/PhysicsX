package njdge.com.physicsx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Circle ball;

    private double velocityX = 0;
    private double velocityY = 0;
    private final double accelerationY = 0.98; // Gravity
    private final double groundY = 580; // Ground level
    private final double leftBoundary = 20; // Left boundary
    private final double rightBoundary = 780; // Right boundary
    private boolean dragging = false;
    private double lastMouseX;
    private double lastMouseY;
    private long lastTime;

    @FXML
    public void initialize() {
        ball.setOnMousePressed(this::onMousePressed);
        ball.setOnMouseDragged(this::onMouseDragged);
        ball.setOnMouseReleased(this::onMouseReleased);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePhysics();
            }
        };
        timer.start();
    }

    private void onMousePressed(MouseEvent event) {
        dragging = true;
        velocityX = 0;
        velocityY = 0;
        lastMouseX = event.getX();
        lastMouseY = event.getY();
        lastTime = System.nanoTime();
    }

    private void onMouseDragged(MouseEvent event) {
        ball.setCenterX(event.getX());
        ball.setCenterY(event.getY());
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1e9;
        velocityX = (event.getX() - lastMouseX) / deltaTime;
        velocityY = (event.getY() - lastMouseY) / deltaTime;
        lastMouseX = event.getX();
        lastMouseY = event.getY();
        lastTime = currentTime;
    }

    private void onMouseReleased(MouseEvent event) {
        dragging = false;
        // Limit the velocity to prevent excessive speed
        double maxVelocity = 20;
        velocityX = Math.max(Math.min(velocityX, maxVelocity), -maxVelocity);
        velocityY = Math.max(Math.min(velocityY, maxVelocity), -maxVelocity);
    }

    private void updatePhysics() {
        if (!dragging) {
            velocityY += accelerationY;
            double newX = ball.getCenterX() + velocityX;
            double newY = ball.getCenterY() + velocityY;

            if (newY > groundY) {
                newY = groundY;
                velocityY = -velocityY * 0.8; // Bounce with damping
            }

            if (newX < leftBoundary) {
                newX = leftBoundary;
                velocityX = -velocityX * 0.8; // Bounce with damping
            } else if (newX > rightBoundary) {
                newX = rightBoundary;
                velocityX = -velocityX * 0.8; // Bounce with damping
            }

            ball.setCenterX(newX);
            ball.setCenterY(newY);
        }
    }
}