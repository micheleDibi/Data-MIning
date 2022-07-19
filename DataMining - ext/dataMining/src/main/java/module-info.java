module com.example.datamining {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.datamining to javafx.fxml;
    exports com.example.datamining;
}