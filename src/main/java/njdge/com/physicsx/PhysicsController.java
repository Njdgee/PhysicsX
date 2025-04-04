// PhysicsController.java
package njdge.com.physicsx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import njdge.com.physicsx.ball.BallManager;

@Getter
@Setter
public class PhysicsController {
    @FXML
    private Pane pane;
    private Boundary boundary;
    private BallManager ballManager;

    @FXML
    public void initialize() {
        this.boundary = new Boundary();
        this.ballManager = new BallManager(pane, boundary);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> updatePhysics()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setScene(Scene scene) {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateBoundaries(scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateBoundaries(scene));
        updateBoundaries(scene);
    }

    private void updateBoundaries(Scene scene) {
        boundary.setGroundY(scene.getHeight());
        boundary.setLeftBoundary(0);
        boundary.setRightBoundary(scene.getWidth());
    }

    private void updatePhysics() {
        ballManager.updateBalls();
    }
}