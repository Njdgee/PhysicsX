package njdge.com.physicsx;

import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ball {
    private final Circle circle;
    @Setter
    private double velocityX;
    @Setter
    private double velocityY;
    @Getter
    private boolean isDragging;
    @Setter
    private double lastMouseX;
    @Setter
    private double lastMouseY;
    @Setter
    private long lastTime;
    private double radius;
    private final PhysicsController physicsController;


    public Ball(double centerX, double centerY, double radius, PhysicsController physicsController) {
        this.circle = new Circle(centerX, centerY, radius);
        this.velocityX = 0;
        this.velocityY = 0;
        this.isDragging = false;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.radius = radius;
        this.physicsController = physicsController;

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

        double kx = (this.velocityX - other.velocityX);
        double ky = (this.velocityY - other.velocityY);
        double p = 2.0 * (nx * kx + ny * ky) / (this.circle.getRadius() + other.circle.getRadius());

        this.velocityX = this.velocityX - p * other.circle.getRadius() * nx;
        this.velocityY = this.velocityY - p * other.circle.getRadius() * ny;
        other.velocityX = other.velocityX + p * this.circle.getRadius() * nx;
        other.velocityY = other.velocityY + p * this.circle.getRadius() * ny;
    }

    public void update() {
        if (!isDragging()) {
            setVelocityY(getVelocityY() + getAccelerationY());
            double newX = getCircle().getCenterX() + getVelocityX();
            double newY = getCircle().getCenterY() + getVelocityY();
            double radius = getCircle().getRadius();

            if (newY > physicsController.groundY - radius) {
                newY = physicsController.groundY - radius;
                setVelocityY(-getVelocityY() * 0.8); // Bounce with damping
            }

            if (newX < physicsController.leftBoundary + radius) {
                newX = physicsController.leftBoundary + radius;
                setVelocityX(-getVelocityX() * 0.8); // Bounce with damping
            } else if (newX > physicsController.rightBoundary - radius) {
                newX = physicsController.rightBoundary - radius;
                setVelocityX(-getVelocityX() * 0.8); // Bounce with damping
            }

            if (newY < radius) {
                newY = radius;
                setVelocityY(-getVelocityY() * 0.8); // Bounce with damping
            }

            getCircle().setCenterX(newX);
            getCircle().setCenterY(newY);
            for (int i = 0; i < physicsController.getBalls().size(); i++) {
                for (int j = i + 1; j < physicsController.getBalls().size(); j++) {
                    Ball ball1 = physicsController.getBalls().get(i);
                    Ball ball2 = physicsController.getBalls().get(j);
                    if (ball1.isColliding(ball2)) {
                        ball1.resolveCollision(ball2);
                    }
                }
            }
        }
    }
}