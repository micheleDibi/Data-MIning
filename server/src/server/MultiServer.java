package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

    private int port = 8080; //porta di default
    private Thread th;
    public MultiServer(int port) throws IOException {
        this.port = port;
        System.out.println("Server started");

        run();
    }

    private void run() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Started: " + serverSocket);

        try {
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connessione avvenuta con successo");

                try {
                    new ServeOneClient(socket);
                } catch (IOException e) {
                    System.out.println("[MultiServer_run_IOexception] : " + e.getMessage());
                }
            }

        } finally {
            System.out.println("[MultiServer_run] : sono nel finally");
            serverSocket.close();
        }

    }

    public static void main(String[] args) {

        try {
            new MultiServer(8080);
        } catch (IOException e) {
            System.out.println("[MultiServer_main_IOException] : " + e.getMessage());
        }


    }
}
