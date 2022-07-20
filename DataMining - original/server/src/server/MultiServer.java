package server;

import utility.Keyboard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * gestisce piu' client per volta
 */
public class MultiServer {

    /**
     * porta di connessione al server impostata come 8080 di default
     */
    private final static int defaultPort = 8080;
    private final int port;

    /**
     * costruttore per impostare la porta
     * 
     * @param port porta del server
     * @throws IOException eccezione generata in caso di errori in fase di
     *                     operazioni di I/O
     */
    public MultiServer(int port) throws IOException {
        this.port = port;
        System.out.println("Starting server...");

        run();
    }

    private void run() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);

        try (serverSocket) {
            System.out.println("Started: " + serverSocket);
            while (true) {
                System.out.println("In attesa di connessione da parte di un client...");
                Socket socket = serverSocket.accept();
                InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("Connessione avvenuta con successo: " + socketAddress);

                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    System.out.println("[MultiServer_run_IOexception] : " + e.getMessage());
                }
            }

        }

    }

    /**
     * metodo main
     * 
     * @param args argomenti passati al lancio del programma
     */
    public static void main(String[] args) {

        int suppPort = defaultPort;

        if(args.length == 1) {
            if(args[0].matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) {
                suppPort = Integer.parseInt(args[0]);
            }
        }

        if(suppPort == defaultPort) {
            do {
                System.out.print("Inserire la porta per abilitare la connessione al server: ");
                suppPort = Keyboard.readInt();

                if(suppPort < 1024 || suppPort > 65535) {
                    System.out.println("Porta non valida");
                }

            } while (suppPort < 1024 || suppPort > 65535);
        }

        try {
            System.out.println("Porta in ascolto: " + suppPort);
            new MultiServer(suppPort);
        } catch (IOException e) {
            System.out.println("[MultiServer_main_IOException] : " + e.getMessage());
        }

    }
}
