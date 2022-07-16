package server;

import data.Data;
import data.TrainingDataException;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.InsufficientColumnNumberException;
import database.NoValueException;
import mining.KNN;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

/**
 * gestisce la comunicazione con un client
 */
class ServerOneClient extends Thread {

    /**
     * socket per la comunicazione con il client
     */
    private final Socket socket;
    /**
     * attributo per ricevere messaggi dal client
     */
    private final ObjectInputStream in;
    /**
     * attributo per inviare messaggi al client
     */
    private final ObjectOutputStream out;

    /**
     * costruttore per l'inizializzazione della comunicazione
     * @param skt socket per la comunicazione con il client
     * @throws IOException eccezione generata in case di errore di connessione con il client
     */
    ServerOneClient(Socket skt) throws IOException {
        this.socket = skt;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start();
    }

    /**
     * metodo principale per la comuncazione con il client
     */
    public void run() {

        try {

            KNN knn = null;

            do {

                int loadPick = (Integer) in.readObject();

                switch (loadPick) {
                    case 1 -> {
                        Data trainingSet;
                        String file;

                        try {
                            file = (String) in.readObject();

                            trainingSet = new Data(file);
                            System.out.println(trainingSet);

                            knn = new KNN(trainingSet);

                            try {
                                knn.salva(file.split("\\.")[0] + ".dmp");
                            } catch (IOException exc) {
                                System.out.println(exc.getMessage());
                            }

                            out.writeObject("KNN caricato con successo. E' stato effettuato un salvataggio nel file " + file + ".dmp");
                            out.writeObject("@OK");
                        } catch (TrainingDataException exc) {
                            System.out.println("[ServerOneClient_run_TrainingDataException] : " + exc.getMessage());
                            out.writeObject("[ServerOneClient_run_TrainingDataException] : " + exc.getMessage());
                            out.writeObject("@ERROR");
                        } catch (FileNotFoundException e) {
                            System.out.println("[ServerOneClient_run_FileNotFoundException] : " + e.getMessage());
                            out.writeObject("[ServerOneClient_run_FileNotFoundException] : " + e.getMessage());
                            out.writeObject("@ERROR");
                        }
                    }
                    case 2 -> {
                        String file;

                        try {
                            file = (String) in.readObject();
                            knn = KNN.carica(file);
                            System.out.println(knn);

                            out.writeObject("KNN caricato con successo.");
                            out.writeObject("@OK");
                        } catch (IOException | ClassNotFoundException exc) {
                            System.out.println("[ServerOneClient_run_IOException | ClassNotFoundException]: " + exc.getMessage());
                            out.writeObject("[ServerOneClient_run_IOException | ClassNotFoundException]: " + exc.getMessage());
                            out.writeObject("@ERROR");
                        }

                    }
                    case 3 -> {
                        Data trainingSet;
                        String table;
                        DbAccess db = null;

                        try {
                            System.out.print("Connecting to DB...");
                            db = new DbAccess();
                            System.out.println("done!");

                            table = (String) in.readObject();

                            trainingSet = new Data(db, table);
                            System.out.println(trainingSet);

                            knn = new KNN(trainingSet);

                            try {
                                knn.salva(table + "_DB.dmp");
                            } catch (IOException exc) {
                                System.out.println("[ServerOneClient_run_IOException]: " + exc.getMessage());
                            }

                            out.writeObject("KNN caricato con successo. E' stato effettuato un salvataggio nel file " + table + ".dmp");
                            out.writeObject("@OK");
                        } catch (NoValueException exc4) {
                            System.out.println("[ServerOneClient_run_NoValueException: " + exc4.getMessage());
                            out.writeObject("[ServerOneClient_run_NoValueException]: " + exc4.getMessage());
                            out.writeObject("@ERROR");
                        } catch (InsufficientColumnNumberException exc1) {
                            System.out.println("[ServerOneClient_run_InsufficientColumnNumberException]: " + exc1.getMessage());
                            out.writeObject("[ServerOneClient_run_InsufficientColumnNumberException]: " + exc1.getMessage());
                            out.writeObject("@ERROR");
                        } catch (DatabaseConnectionException exc2) {
                            System.out.println("[ServerOneClient_run_DatabaseConnectionException]: " + exc2.getMessage());
                            out.writeObject("[ServerOneClient_run_DatabaseConnectionException]: " + exc2.getMessage());
                            out.writeObject("@ERROR");
                        } catch (SQLException exc3) {
                            System.out.println("[ServerOneClient_run_SQLException]: " + exc3.getMessage());
                            out.writeObject("[ServerOneClient_run_SQLException]: " + exc3.getMessage());
                            out.writeObject("@ERROR");
                        } finally {
                            if (db != null) {
                                System.out.print("chiusura connessione database...");
                                db.closeConnection();
                                System.out.println("done");
                            }
                        }
                    }
                    case 4 -> {
                        assert knn != null;
                        out.writeObject(knn.predict(out, in));
                    }
                }

            } while (true);

        } catch (IOException e) {
            InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            System.out.println("[ServerOneClient_run_IOException] : " + e.getMessage() + socketAddress);
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