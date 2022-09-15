module com.example.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jfr;
    requires javafx.swing;


    opens com.example.paint to javafx.fxml;
    exports com.example.paint;
}