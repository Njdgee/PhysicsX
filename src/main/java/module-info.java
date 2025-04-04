module njdge.com.physicsx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;

    opens njdge.com.physicsx to javafx.fxml;
    exports njdge.com.physicsx;
    exports njdge.com.physicsx.ball;
    opens njdge.com.physicsx.ball to javafx.fxml;

}