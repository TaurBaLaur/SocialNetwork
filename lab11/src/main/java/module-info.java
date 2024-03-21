module map.lab11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens map.lab11 to javafx.fxml;
    exports map.lab11;
    exports map.lab11.controller;
    opens map.lab11.controller to javafx.fxml;
    opens map.lab11.domain to javafx.base;
}