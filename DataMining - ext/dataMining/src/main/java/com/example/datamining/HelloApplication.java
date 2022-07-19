package com.example.datamining;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("KNN");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:../../../../resources/img/knnLogo.png"));
        stage.setMinWidth(320);
        stage.setMinHeight(400);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}