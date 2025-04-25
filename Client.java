// Client.java
import java.io.*;
import java.net.*;

/**
 * Client: Manages a TCP connection to the server for sending and receiving messages.
 */
public class Client {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /** Send a message to the server. */
    public void sendMessage(String msg) throws IOException {
        out.println(msg);
    }

    /** Read a line response from the server. */
    public String readMessage() throws IOException {
        return in.readLine();
    }

    /** Close the TCP connection. */
    public void close() throws IOException {
        socket.close();
    }
}
