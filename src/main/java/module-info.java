module org.example.logical_calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.logical_calculator to javafx.fxml;
    exports org.example.logical_calculator;
}