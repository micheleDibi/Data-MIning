package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * gestisce piu' client per volta
 */
public class MultiServer {

    /**
     * porta del server per la connessione, impostata come 8080 di default
     */
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
        System.out.println("Server started");

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
        try {
            new MultiServer(8080);
        } catch (IOException e) {
            System.out.println("[MultiServer_main_IOException] : " + e.getMessage());
        }

    }
}
