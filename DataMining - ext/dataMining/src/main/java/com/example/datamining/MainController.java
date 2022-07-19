package com.example.datamining;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {
    private Client client;
    @FXML
    private TextField txtAreaFT;
    @FXML
    private TextField txtAreaFB;
    @FXML
    private TextField txtAreaDB;

    @FXML
    private ScatterChart scatterChartFT;
    @FXML
    private ScatterChart scatterChartFB;
    @FXML
    private ScatterChart scatterChartDB;

    private final Alert error = new Alert(Alert.AlertType.ERROR);
    private final Alert conf = new Alert(Alert.AlertType.INFORMATION);

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void initialize() {

    }

    public void onActionBtnFT() throws IOException, ClassNotFoundException {

        String risposta = null;
        String messaggio = null;

        try {
            if (!txtAreaFT.getText().equals("")) {
                client.getObjectOutputStream().writeObject(1);

                client.getObjectOutputStream().writeObject(txtAreaFT.getText());
                messaggio = (String) client.getObjectInputStream().readObject();

                risposta = (String) client.getObjectInputStream().readObject();
            }
        } catch (IOException e) {
            error.setContentText("[IOException]" + e.getMessage());
            error.showAndWait();
        } catch (ClassNotFoundException e) {
            error.setContentText("[ClassNotFoundException]" + e.getMessage());
            error.showAndWait();
        }

        if (!risposta.contains("@ERROR")) {
            conf.setContentText(messaggio);
            conf.showAndWait();

            client.getObjectOutputStream().writeObject(4);

            boolean flag = true; // reading example
            do {
                risposta = (String) (client.getObjectInputStream().readObject());
                value = null;

                if (!risposta.contains("@ENDEXAMPLE")) {
                    // sto leggendo l'esempio
                    String msg = (String) (client.getObjectInputStream().readObject());

                    if (risposta.equals("@READSTRING")) { // leggo una stringa
                        do {
                            showPopup(msg);
                        } while (value == null);

                        client.getObjectOutputStream().writeObject(value);
                    } else if (risposta.equals("@READDOUBLE")) { // leggo un numero
                        double x = Double.NaN;
                        do {
                            do {
                                showPopup(msg);
                            } while (value == null);

                            if (value.trim().matches("[0-9]+[\\.]?[0-9]*")) {
                                x = Double.parseDouble(value);
                            } else {
                                error.setContentText("Attributo continuo inserito non valido!");
                                error.showAndWait();
                            }

                        } while (Double.valueOf(x).equals(Double.NaN));
                        client.getObjectOutputStream().writeObject(x);
                    }

                } else
                    flag = false;
            } while (flag);

            // sto leggendo k
            risposta = (String) (client.getObjectInputStream().readObject());
            int k = 0;
            do {
                value = null;

                do {
                    showPopup(risposta);
                } while (value == null);

                if (value.trim().matches("[0-9]+")) {
                    k = Integer.parseInt(value);
                } else {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

                if (k < 1) {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

            } while (k < 1);
            client.getObjectOutputStream().writeObject(k);

            List<Double> predict = (List<Double>) client.getObjectInputStream().readObject();
            NumberAxis xAxis = new NumberAxis(0, predict.size(), 1);
            NumberAxis yAxis = new NumberAxis(0, max(predict) + 2, 1);
            scatterChartFT = new ScatterChart<>(xAxis, yAxis);

            xAxis.setLabel("Esempi");
            yAxis.setLabel("Target");
            scatterChartFT.setTitle("KNN");

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Esempi Dati");

            for (int i = 0; i < predict.size() - 1; i++) {
                series1.getData().add(new XYChart.Data(i, predict.get(i)));
            }

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Esempio calcolato: " + predict.get(predict.size() - 1));
            series2.getData().add(new XYChart.Data(predict.size(), predict.get(predict.size() - 1)));

            scatterChartFT.getData().addAll(series1, series2);

            Stage stage = new Stage();
            Scene scene = new Scene(scatterChartFT, 500, 320);
            stage.setScene(scene);
            stage.showAndWait();

        } else {
            error.setContentText(messaggio);
            error.showAndWait();
        }

    }

    public void onActionBtnFB() throws IOException, ClassNotFoundException {

        String risposta = null;
        String messaggio = null;

        try {
            if (!txtAreaFB.getText().equals("")) {
                client.getObjectOutputStream().writeObject(2);

                client.getObjectOutputStream().writeObject(txtAreaFB.getText());
                messaggio = (String) client.getObjectInputStream().readObject();

                risposta = (String) client.getObjectInputStream().readObject();
            }
        } catch (IOException e) {
            error.setContentText("[IOException]" + e.getMessage());
            error.showAndWait();
        } catch (ClassNotFoundException e) {
            error.setContentText("[ClassNotFoundException]" + e.getMessage());
            error.showAndWait();
        }

        if (!risposta.contains("@ERROR")) {
            conf.setContentText(messaggio);
            conf.showAndWait();

            client.getObjectOutputStream().writeObject(4);

            boolean flag = true; // reading example
            do {
                risposta = (String) (client.getObjectInputStream().readObject());
                value = null;

                if (!risposta.contains("@ENDEXAMPLE")) {
                    // sto leggendo l'esempio
                    String msg = (String) (client.getObjectInputStream().readObject());

                    if (risposta.equals("@READSTRING")) { // leggo una stringa
                        do {
                            showPopup(msg);
                        } while (value == null);

                        client.getObjectOutputStream().writeObject(value);
                    } else if (risposta.equals("@READDOUBLE")) { // leggo un numero
                        double x = Double.NaN;
                        do {
                            do {
                                showPopup(msg);
                            } while (value == null);

                            if (value.trim().matches("[0-9]+[\\.]?[0-9]*")) {
                                x = Double.parseDouble(value);
                            } else {
                                error.setContentText("Attributo continuo inserito non valido!");
                                error.showAndWait();
                            }

                        } while (Double.valueOf(x).equals(Double.NaN));
                        client.getObjectOutputStream().writeObject(x);
                    }

                } else
                    flag = false;
            } while (flag);

            // sto leggendo k
            risposta = (String) (client.getObjectInputStream().readObject());
            int k = 0;
            do {
                value = null;

                do {
                    showPopup(risposta);
                } while (value == null);

                if (value.trim().matches("[0-9]+")) {
                    k = Integer.parseInt(value);
                } else {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

                if (k < 1) {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

            } while (k < 1);
            client.getObjectOutputStream().writeObject(k);

            List<Double> predict = (List<Double>) client.getObjectInputStream().readObject();
            NumberAxis xAxis = new NumberAxis(0, predict.size(), 1);
            NumberAxis yAxis = new NumberAxis(0, max(predict) + 2, 1);
            scatterChartFB = new ScatterChart<>(xAxis, yAxis);

            xAxis.setLabel("Esempi");
            yAxis.setLabel("Target");
            scatterChartFB.setTitle("KNN");

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Esempi Dati");

            for (int i = 0; i < predict.size() - 1; i++) {
                series1.getData().add(new XYChart.Data(i, predict.get(i)));
            }

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Esempio calcolato: " + predict.get(predict.size() - 1));
            series2.getData().add(new XYChart.Data(predict.size(), predict.get(predict.size() - 1)));

            scatterChartFB.getData().addAll(series1, series2);

            Stage stage = new Stage();
            Scene scene = new Scene(scatterChartFB, 500, 320);
            stage.setScene(scene);
            stage.showAndWait();

        } else {
            error.setContentText(messaggio);
            error.showAndWait();
        }
    }

    public void onActionBtnDB() throws IOException, ClassNotFoundException {

        String risposta = null;
        String messaggio = null;

        try {
            if (!txtAreaDB.getText().equals("")) {
                client.getObjectOutputStream().writeObject(3);

                client.getObjectOutputStream().writeObject(txtAreaDB.getText());
                messaggio = (String) client.getObjectInputStream().readObject();

                risposta = (String) client.getObjectInputStream().readObject();
            }
        } catch (IOException e) {
            error.setContentText("[IOException]" + e.getMessage());
            error.showAndWait();
        } catch (ClassNotFoundException e) {
            error.setContentText("[ClassNotFoundException]" + e.getMessage());
            error.showAndWait();
        }

        if (!risposta.contains("@ERROR")) {
            conf.setContentText(messaggio);
            conf.showAndWait();

            client.getObjectOutputStream().writeObject(4);

            boolean flag = true; // reading example
            do {
                risposta = (String) (client.getObjectInputStream().readObject());
                value = null;

                if (!risposta.contains("@ENDEXAMPLE")) {
                    // sto leggendo l'esempio
                    String msg = (String) (client.getObjectInputStream().readObject());

                    if (risposta.equals("@READSTRING")) { // leggo una stringa
                        do {
                            showPopup(msg);
                        } while (value == null);

                        client.getObjectOutputStream().writeObject(value);
                    } else if (risposta.equals("@READDOUBLE")) { // leggo un numero
                        double x = Double.NaN;
                        do {
                            do {
                                showPopup(msg);
                            } while (value == null);

                            if (value.trim().matches("[0-9]+[\\.]?[0-9]*")) {
                                x = Double.parseDouble(value);
                            } else {
                                error.setContentText("Attributo continuo inserito non valido!");
                                error.showAndWait();
                            }

                        } while (Double.valueOf(x).equals(Double.NaN));
                        client.getObjectOutputStream().writeObject(x);
                    }

                } else
                    flag = false;
            } while (flag);

            // sto leggendo k
            risposta = (String) (client.getObjectInputStream().readObject());
            int k = 0;
            do {
                value = null;

                do {
                    showPopup(risposta);
                } while (value == null);

                if (value.trim().matches("[0-9]+")) {
                    k = Integer.parseInt(value);
                } else {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

                if (k < 1) {
                    error.setContentText("Valore di k non corretto!");
                    error.showAndWait();
                }

            } while (k < 1);
            client.getObjectOutputStream().writeObject(k);

            List<Double> predict = (List<Double>) client.getObjectInputStream().readObject();
            NumberAxis xAxis = new NumberAxis(0, predict.size(), 1);
            NumberAxis yAxis = new NumberAxis(0, max(predict) + 2, 1);
            scatterChartDB = new ScatterChart<>(xAxis, yAxis);

            xAxis.setLabel("Esempi");
            yAxis.setLabel("Target");
            scatterChartDB.setTitle("KNN");

            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Esempi Dati");

            for (int i = 0; i < predict.size() - 1; i++) {
                series1.getData().add(new XYChart.Data(i, predict.get(i)));
            }

            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Esempio calcolato: " + predict.get(predict.size() - 1));
            series2.getData().add(new XYChart.Data(predict.size(), predict.get(predict.size() - 1)));

            scatterChartDB.getData().addAll(series1, series2);

            Stage stage = new Stage();
            Scene scene = new Scene(scatterChartDB, 500, 320);
            stage.setScene(scene);
            stage.showAndWait();

        } else {
            error.setContentText(messaggio);
            error.showAndWait();
        }

    }

    private String value;

    void showPopup(String label) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();
        Scene scene = new Scene(root);
        Label lb = new Label(label);
        TextField tf = new TextField();
        Button submit = new Button("Continua");

        submit.setOnAction(e -> {
            if (!tf.getText().equals("")) {
                value = tf.getText();
                stage.close();
            }
        });

        root.getChildren().addAll(lb, tf, submit);
        stage.setScene(scene);
        stage.showAndWait();
    }

    Double max(List<Double> arrayList) {
        Double max = arrayList.get(1);

        for (Double supp : arrayList) {
            if (max < supp) {
                max = supp;
            }
        }

        return max;
    }
}