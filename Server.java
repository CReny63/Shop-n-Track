import java.io.*;
import java.net.*;
import java.util.concurrent.*;

    // 1. Listen on a port
    public class Server {
        private static final int PORT = 12345;
        private static final ExecutorService pool =
                Executors.newFixedThreadPool(10);  // up to 10 simultaneous clients

        public static void main(String[] args) throws IOException {
            try (ServerSocket listener = new ServerSocket(PORT)) {
                System.out.println("Server listening on " + PORT);
                while (true) {
                    Socket client = listener.accept();
                    pool.submit(new ClientHandler(client));
                }
            }
        }
    }

    // 2. Handle each client in its own thread
    class ClientHandler implements Runnable {
        private final Socket socket;
        ClientHandler(Socket socket) { this.socket = socket; }

        @Override
        public void run() {
            try (
                    BufferedReader in  = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter    out = new PrintWriter(
                            socket.getOutputStream(), true)
            ) {
                String msg;
                while ((msg = in.readLine()) != null) {
                    // TODO: parse & dispatch your own commands (e.g. JSON, “SEARCH:foo”, etc.)
                    System.out.println("Received: " + msg);
                    out.println("ACK: " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }

