package server;

import data.Data;
import data.TrainingDataException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import mining.KNN;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

class ServeOneClient extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    ServeOneClient(Socket skt) throws IOException {
        this.socket = skt;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start();
    }

    public void run() {

        try {

            do {
                KNN knn = null;
                int loadPick = (Integer) in.readObject();

                switch (loadPick) {

                    case 1 : {
                        Data trainingSet = null;
                        String file = "";

                        try {
                            file = (String) in.readObject();

                            trainingSet = new Data(file);
                            System.out.println(trainingSet);

                            out.writeObject("@OK");
                        } catch (TrainingDataException exc) {
                            System.out.println("[ServerOneClient_run_TrainingDataException] : " + exc.getMessage());
                            out.writeObject("@ERROR");
                        }

                        knn = new KNN(trainingSet);

                        try {
                            knn.salva(file + ".dmp");
                        } catch (IOException exc) {
                            System.out.println(exc.getMessage());
                        }
                    } break;

                    case 2: {
                        String file;

                        try {
                            file = (String) in.readObject();
                            knn = KNN.carica(file);
                            System.out.println(knn);

                            out.writeObject("@OK");
                        } catch (IOException | ClassNotFoundException exc) {
                            System.out.println("[ServerOneClient_run_IOException | ClassNotFoundException]" + exc.getMessage());
                            out.writeObject("@ERROR");
                        }

                    } break;

                    case 3: {
                        Data trainingSet = null;
                        String table = "";
                        DbAccess db = null;

                        try {
                            System.out.print("Connecting to DB...");
                            db = new DbAccess();
                            System.out.println("done!");

                            table = (String) in.readObject();

                            trainingSet = new Data(db, table);
                            System.out.println(trainingSet);

                            out.writeObject("@OK");
                        } catch (InsufficientColumnNumberException exc1) {
                            System.out.println("[ServerOneClient_run_InsufficientColumnNumberException]: " + exc1.getMessage());
                            out.writeObject("@ERROR");
                        } catch (DatabaseConnectionException exc2) {
                            System.out.println("[ServerOneClient_run_DatabaseConnectionException]: " + exc2.getMessage());
                            out.writeObject("@ERROR");
                        } catch (SQLException exc3) {
                            System.out.println("[ServerOneClient_run_SQLException]: " + exc3.getMessage());
                            out.writeObject("@ERROR");
                        } finally {
                            if (db != null) {
                                System.out.print("chiusura connessione database...");
                                db.closeConnection();
                                System.out.println("done");
                            }
                        }

                        knn = new KNN(trainingSet);
                        try {
                            knn.salva(table + "DB.dmp");
                        } catch (IOException exc) {
                            System.out.println("[ServerOneClient_run_IOException]: " + exc.getMessage());
                        }


                    } break;

                    case 4: {
                        out.writeObject(knn.predict(in, out));
                    } break;
                }

            } while (true);


        } catch (IOException e) {
            System.out.println("[ServerOneClient_run_IOException] : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[ServerOneClient_run_ClassNotFoundException] : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[ServerOneClient_run_IOException] Socket not closed : " + e.getMessage());
            }
        }
    }
}
