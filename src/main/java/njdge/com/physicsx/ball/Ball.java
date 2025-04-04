// Ball.java
package njdge.com.physicsx.ball;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import njdge.com.physicsx.Boundary;

@Getter
@Setter
public class Ball {
    private final Circle circle;
    private double velocityX;
    private double velocityY;
    private boolean isDragging;
    private double lastMouseX;
    private double lastMouseY;
    private long lastTime;
    private double mass;
    private double radius;
    private final Boundary boundary;

    public Ball(double centerX, double centerY, double radius, double mass, Boundary boundary) {
        this.circle = new Circle(centerX, centerY, radius);
        this.circle.setFill(Color.WHITE);
        this.mass = mass;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isDragging = false;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.radius = radius;
        this.boundary = boundary;
    }

    public double getAccelerationY() {
        // Gravity
        return 0.98;
    }

    public boolean isColliding(Ball other) {
        double dx = this.circle.getCenterX() - other.circle.getCenterX();
        double dy = this.circle.getCenterY() - other.circle.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.circle.getRadius() + other.circle.getRadius();
    }

    public void resolveCollision(Ball other) {
        double dx = this.circle.getCenterX() - other.circle.getCenterX();
        double dy = this.circle.getCenterY() - other.circle.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            return;
        }

        double overlap = 0.5 * (distance - this.circle.getRadius() - other.circle.getRadius());

        this.circle.setCenterX(this.circle.getCenterX() - overlap * (dx / distance));
        this.circle.setCenterY(this.circle.getCenterY() - overlap * (dy / distance));

        other.circle.setCenterX(other.circle.getCenterX() + overlap * (dx / distance));
        other.circle.setCenterY(other.circle.getCenterY() + overlap * (dy / distance));
        double nx = dx / distance;
        double ny = dy / distance;

        double kx = this.velocityX - other.velocityX;
        double ky = this.velocityY - other.velocityY;
        double p = 2.0 * (nx * kx + ny * ky) / (this.mass + other.mass);

        this.velocityX = this.velocityX - p * other.mass * nx;
        this.velocityY = this.velocityY - p * other.mass * ny;
        other.velocityX = other.velocityX + p * this.mass * nx;
        other.velocityY = other.velocityY + p * this.mass * ny;
    }

    public void update() {
        if (!isDragging()) {
            setVelocityY(getVelocityY() + getAccelerationY());
            double newX = getCircle().getCenterX() + getVelocityX();
            double newY = getCircle().getCenterY() + getVelocityY();
            double radius = getCircle().getRadius();

            if (newY > boundary.getGroundY() - radius) {
                newY = boundary.getGroundY() - radius;
                setVelocityY(-getVelocityY() * 0.8 / mass); // Bounce with damping and mass consideration
            }

            if (newX < boundary.getLeftBoundary() + radius) {
                newX = boundary.getLeftBoundary() + radius;
                setVelocityX(-getVelocityX() * 0.8 / mass); // Bounce with damping and mass consideration
            } else if (newX > boundary.getRightBoundary() - radius) {
                newX = boundary.getRightBoundary() - radius;
                setVelocityX(-getVelocityX() * 0.8 / mass); // Bounce with damping and mass consideration
            }
            if (newY < radius) {
                newY = radius;
                setVelocityY(-getVelocityY() * 0.8 / mass); // Bounce with damping and mass consideration
            }

            getCircle().setCenterX(newX);
            getCircle().setCenterY(newY);
        }
    }
}