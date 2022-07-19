package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ObjectInputStream getObjectInputStream(){
        return in;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return out;
    }

    public Client(String address, int port) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
}
