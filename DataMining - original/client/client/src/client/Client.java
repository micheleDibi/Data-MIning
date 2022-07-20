package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;

/**
 * classe client per collegamento al server, inserimento del training set e ricevere il risultato calcolato
 */

public class Client {

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private final static String defaultAddress = "localhost";
    private final static String defaultPort = "8080";

    Client(String address, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(address, port);
        System.out.println(socket);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream()); // stream con richieste del client
        talking();
    }

    private void talking() throws IOException, ClassNotFoundException {

        int decision;
        String menu = "";

        do {
            menu = "";

            do {
                System.out.println("Caricare KNN da file [1]");
                System.out.println("Caricare KNN da file binario [2]");
                System.out.println("Caricare KNN da database [3]");
                decision = Keyboard.readInt();
            } while (decision < 0 || decision > 3);

            String risposta;
            do {
                out.writeObject(decision);

                if (decision == 1) {
                    System.out.println("Inserire il path di un file contenente un training set valido:");
                } else if (decision == 2) {
                    System.out.println("Inserire il path di un file binario");
                } else if (decision == 3) {
                    System.out.println("Inserire il nome della tabella presente nel database");
                }

                out.writeObject(Keyboard.readString()); // lettura nome file - tabella;
                System.out.println((String) in.readObject()); // stampa eventuale errore

                risposta = (String) in.readObject();
            } while (risposta.contains("@ERROR"));

            // predict
            String c;
            do {
                out.writeObject(4);

                boolean flag = true; // reading example
                do {
                    risposta = (String) (in.readObject());

                    if (!risposta.contains("@ENDEXAMPLE")) {
                        // sto leggendo l'esempio
                        String msg = (String) (in.readObject());

                        if (risposta.equals("@READSTRING")) { // leggo una stringa
                            System.out.println(msg);
                            out.writeObject(Keyboard.readString());
                        } else if (risposta.equals("@READDOUBLE")) { // leggo un numero
                            double x;
                            do {
                                System.out.println(msg);
                                x = Keyboard.readDouble();
                            } while (Double.valueOf(x).equals(Double.NaN));
                            out.writeObject(x);
                        }
                    } else
                        flag = false;
                } while (flag);

                // sto leggendo k
                risposta = (String) (in.readObject());
                int k;
                do {
                    System.out.print(risposta);
                    k = Keyboard.readInt();
                } while (k < 1);
                out.writeObject(k);
                // aspetto la predizione

                System.out.println("Prediction:" + in.readObject());
                System.out.println("Vuoi ripetere predizione? (Y/N)");
                c = Keyboard.readString();

            } while (c.equalsIgnoreCase("y"));

            while (!menu.equalsIgnoreCase("y") && !menu.equalsIgnoreCase("n")) {
                System.out.print("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN? (Y/N): ");
                menu = Keyboard.readString();
            }

        } while (menu.equalsIgnoreCase("y"));

    }

    /**
     * main del client per la definizione del server a cui collegarsi
     * @param args i primi due parametri che vengono inseriti sono indirizzo e porta del server a cui collegarsi
     */

    public static void main(String[] args) {

        String addressSupp = defaultAddress;
        String portSupp = defaultPort;
        String ans;

        if (args.length == 1) {

            if (args[0].matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
                    || args[0].matches("[a-z]*]")) {
                addressSupp = args[0];
            } else if (args[0]
                    .matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                portSupp = args[0];
            } else {
                System.out.println("Il parametro passato non è corretto");
            }

        } else if (args.length == 2) {

            if (args[0]
                    .matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
                    || args[0].matches("[a-z]*]")) {
                addressSupp = args[0];
            } else
                System.out.println("Indirizzo non valido");

            if (args[1]
                    .matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                portSupp = args[1];
            } else
                System.out.println("porta non valida");
        }

        boolean flag;
        do {
            flag = true;

            do {
                System.out.println("parametri di connessione impostati " + addressSupp + ":" + portSupp);
                System.out.println("Vuoi impostare una nuova connessione? [y/n]");
                ans = Keyboard.readString();
            } while (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n"));

            if (ans.equalsIgnoreCase("y")) {
                System.out.println("Inserire l'indirizzo del server: ");
                ans = Keyboard.readString();

                if (!ans.matches(
                        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
                        && !ans.matches("[a-z]*")) {
                    System.out.println("Indirizzo del server non valido");
                    flag = false;
                } else {
                    addressSupp = ans;
                }

                if (flag) {
                    System.out.println("Inserire la porta");
                    ans = Keyboard.readString();

                    if (!ans.matches(
                            "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                        System.out.println("Porta non valida");

                        addressSupp = defaultAddress;
                        flag = false;
                    } else {
                        portSupp = ans;
                    }
                }

            }

        } while (!flag);

        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(addressSupp);
        } catch (UnknownHostException e) {
            System.err.println("[UnknownHostException] : " + e.getMessage());
        }

        try {
            System.out.println("connessione in corso al seguente indirizzo: " + addr + "...");
            new Client(addressSupp, Integer.parseInt(portSupp));
        } catch (IOException e) {
            System.err.println("[client_main_IOException] : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("[client_main_ClassNotFoundException] : " + e.getMessage());
        }
    }
}
