package com.example.datamining;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginController {

    @FXML
    private TextField txtIndirizzo;
    @FXML
    private TextField txtPorta;

    public void switchToMain(ActionEvent event, Client c) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setClient(c);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onActionBtnConnetti(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.ERROR);

        if (txtIndirizzo.getText()
                .matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
                || txtIndirizzo.getText().matches("^[a-zA-Z]*$")) {
            if (txtPorta.getText()
                    .matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {

                try {
                    InetAddress.getByName(txtIndirizzo.getText());

                    try {
                        switchToMain(event, new Client(txtIndirizzo.getText(), Integer.parseInt(txtPorta.getText())));
                    } catch (IOException e) {
                        a.setContentText("[IOException] : " + e.getMessage());
                        a.show();
                    } catch (ClassNotFoundException e) {
                        a.setContentText("[ClassNotFoundException] : " + e.getMessage());
                        a.show();
                    }

                } catch (UnknownHostException e) {
                    a.setContentText("[UnknownHostException] : " + e.getMessage());
                    a.show();
                }

            } else {
                if (txtPorta.getText().equals("")) {
                    a.setContentText("Inserire la porta del server a cui connettersi");
                    a.showAndWait();
                } else {
                    a.setContentText("Porta non valida");
                    a.showAndWait();
                }
            }
        } else {
            if (txtIndirizzo.getText().equals("")) {
                a.setContentText("Inserire l'indirizzo del server a cui connettersi");
                a.showAndWait();
            } else {
                a.setContentText("Indirizzo non valido");
                a.showAndWait();
            }
        }
    }
}
