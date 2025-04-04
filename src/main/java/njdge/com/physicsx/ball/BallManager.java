// BallManager.java
package njdge.com.physicsx.ball;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import lombok.Getter;
import njdge.com.physicsx.Boundary;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BallManager {
    private final List<Ball> balls;
    private final Boundary boundary;

    public BallManager(Pane pane, Boundary boundary) {
        this.boundary = boundary;
        balls = new ArrayList<>();
        balls.add(new Ball(300, 60, 15, 1, boundary));
        balls.add(new Ball(350, 50, 15, 3, boundary));
        balls.add(new Ball(400, 40, 15, 5, boundary));

        for (Ball ball : balls) {
            Circle circle = ball.getCircle();
            pane.getChildren().add(circle);
            circle.setOnMousePressed(this::onMousePressed);
            circle.setOnMouseDragged(this::onMouseDragged);
            circle.setOnMouseReleased(this::onMouseReleased);
        }
    }

    private void onMouseDragged(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        Ball ball = findBallByCircle(circle);
        if (ball != null) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - ball.getLastTime()) / 1e9;

            if (deltaTime < 1e-9) {
                deltaTime = 1e-9;
            }

            ball.setVelocityX((event.getX() - ball.getLastMouseX()) / deltaTime);
            ball.setVelocityY((event.getY() - ball.getLastMouseY()) / deltaTime);

            circle.setCenterX(event.getX());
            circle.setCenterY(event.getY());
            ball.setLastMouseX(event.getX());
            ball.setLastMouseY(event.getY());
            ball.setLastTime(currentTime);
        }
    }

    private void onMouseReleased(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        Ball ball = findBallByCircle(circle);
        if (ball != null) {
            ball.setDragging(false);
            double maxVelocity = 40;
            ball.setVelocityX(Math.max(Math.min(ball.getVelocityX(), maxVelocity), -maxVelocity));
            ball.setVelocityY(Math.max(Math.min(ball.getVelocityY(), maxVelocity), -maxVelocity));
        }
    }

    private void onMousePressed(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        Ball ball = findBallByCircle(circle);
        if (ball != null) {
            ball.setDragging(true);
            ball.setVelocityX(0);
            ball.setVelocityY(0);
            ball.setLastMouseX(event.getX());
            ball.setLastMouseY(event.getY());
            ball.setLastTime(System.nanoTime());
        }
    }

    private Ball findBallByCircle(Circle circle) {
        for (Ball ball : balls) {
            if (ball.getCircle().equals(circle)) {
                return ball;
            }
        }
        return null;
    }

    public void updateBalls() {
        for (Ball ball : balls) {
            ball.update();
            for (Ball other : balls) {
                if (ball != other && ball.isColliding(other)) {
                    ball.resolveCollision(other);
                }
            }
        }
    }
}