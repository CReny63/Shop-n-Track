package server.src;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * server.src.Server: Serves static CSV data and images for Track N Shop.
 * Endpoints:
 *   GET /items.csv    - product data CSV from database/items.csv
 *   GET /users.csv    - user data CSV from database/users.csv
 *   GET /ItemPics/{file} - image files from database/ItemPics/
 */
public class Server {
    private static final int PORT = 12345;
    private static final String DB_DIR = "database";
    private static final String ITEMS_CSV = "items.csv";
    private static final String USERS_CSV = "users.csv";
    private static final String IMAGES_PATH = "ItemPics";

    public static void main(String[] args) throws IOException {
        HttpServer http = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Serve items.csv
        http.createContext("/items.csv", exchange -> handleCsv(exchange, ITEMS_CSV));

        // Serve users.csv
        http.createContext("/users.csv", exchange -> handleCsv(exchange, USERS_CSV));

        // Serve images under /ItemPics
        http.createContext("/ItemPics/", exchange -> {
            String uriPath = exchange.getRequestURI().getPath();
            String filename = uriPath.substring(uriPath.lastIndexOf('/') + 1);
            Path imageFile = Paths.get(DB_DIR, IMAGES_PATH, filename);
            handleStaticFile(exchange, imageFile);
        });

        http.setExecutor(null);
        http.start();
        System.out.println("Server listening on port " + PORT);
    }

    private static void handleCsv(HttpExchange exchange, String csvName) throws IOException {
        Path csvFile = Paths.get(DB_DIR, csvName);
        if (Files.exists(csvFile)) {
            byte[] data = Files.readAllBytes(csvFile);
            exchange.getResponseHeaders().set("Content-Type", "text/csv");
            exchange.sendResponseHeaders(200, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    private static void handleStaticFile(HttpExchange exchange, Path filePath) throws IOException {
        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            String contentType = Files.probeContentType(filePath);
            byte[] data = Files.readAllBytes(filePath);
            if (contentType != null) {
                exchange.getResponseHeaders().set("Content-Type", contentType);
            }
            exchange.sendResponseHeaders(200, data.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }
}
