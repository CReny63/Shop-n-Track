// server/src/Server.java
package server.src;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * server.src.Server: Serves CSV data, images, handles login and account creation.
 */
public class Server {
    private static final int    PORT      = 12345;
    private static final String DB_DIR    = "database";
    private static final String ITEMS_CSV = "items.csv";
    private static final String USERS_CSV = "users.csv";
    private static final String IMAGES    = "ItemPics";

    public static void main(String[] args) throws IOException {
        HttpServer http = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Serve items.csv and users.csv
        http.createContext("/items.csv", ex -> handleCsvGet(ex, ITEMS_CSV));
        http.createContext("/users.csv", ex -> handleCsvGet(ex, USERS_CSV));

        // Serve images
        http.createContext("/ItemPics/", ex -> {
            String fn = ex.getRequestURI().getPath();
            fn = fn.substring(fn.lastIndexOf('/') + 1);
            handleStaticFile(ex, Paths.get(DB_DIR, IMAGES, fn));
        });

        // Login endpoint: validate credentials
        http.createContext("/api/login", Server::handleLogin);

        // Create Account endpoint: accept JSON payload
        http.createContext("/api/createAccount", Server::handleCreateAccount);

        // update user (after a purchase)
        http.createContext("/api/updateUser", Server::handleUpdateUser);

        http.setExecutor(null);
        http.start();
        System.out.println("Server listening on port " + PORT);
    }

    private static void handleCsvGet(HttpExchange ex, String fileName) throws IOException {
        Path path = Paths.get(DB_DIR, fileName);
        if (Files.exists(path)) {
            byte[] data = Files.readAllBytes(path);
            ex.getResponseHeaders().set("Content-Type", "text/csv");
            ex.sendResponseHeaders(200, data.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(data);
            }
        } else {
            ex.sendResponseHeaders(404, -1);
        }
        ex.close();
    }

    private static void handleLogin(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        Headers hdr = ex.getRequestHeaders();
        String ct = hdr.getFirst("Content-Type");
        if (ct == null || !ct.startsWith("application/x-www-form-urlencoded")) {
            ex.sendResponseHeaders(415, -1);
            ex.close();
            return;
        }

        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        // parse form data
        String[] pairs = body.split("&");
        String userIn = null, passIn = null;
        for (String pair : pairs) {
            String[] kv = pair.split("=",2);
            if (kv.length==2) {
                String key = java.net.URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                String val = java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name());
                if ("username".equals(key)) userIn = val;
                if ("password".equals(key)) passIn = val;
            }
        }
        // search CSV
        Path csv = Paths.get(DB_DIR, USERS_CSV);
        String matchedLine = null;
        try (BufferedReader br = Files.newBufferedReader(csv, StandardCharsets.UTF_8)) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine())!=null) {
                String t = line;
                if (t.startsWith("\"")) t = t.substring(1);
                int p = t.indexOf("\",\""); if (p<0) continue;
                String u = t.substring(0,p);
                String rest = t.substring(p+3);
                int q = rest.indexOf("\",\""); if (q<0) continue;
                String pw = rest.substring(0,q);
                if (u.equals(userIn) && pw.equals(passIn)) {
                    matchedLine = line;
                    break;
                }
            }
        }
        if (matchedLine!=null) {
            byte[] resp = matchedLine.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type","text/plain; charset=UTF-8");
            ex.sendResponseHeaders(200, resp.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(resp); }
        } else {
            ex.sendResponseHeaders(401, -1);
        }
        ex.close();
    }

    private static void handleCreateAccount(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        Headers hdr = ex.getRequestHeaders();
        String ct = hdr.getFirst("Content-Type");
        if (ct == null || !ct.toLowerCase().contains("application/json")) {
            ex.sendResponseHeaders(415, -1);
            ex.close();
            return;
        }
        // read JSON body
        String json = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        ex.getRequestBody().close();
        // convert JSON to CSV line
        String csvLine = common.src.UserDAO.jsonToCsv(json);
        // append to users.csv
        Path csv = Paths.get(DB_DIR, USERS_CSV);
        Files.write(csv, (csvLine+System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        ex.sendResponseHeaders(201, -1);
        ex.close();
    }

    /**
     * Expects a JSON body of the full User (same format as createAccount).
     * Reads database/users.csv, replaces the matching username line,
     * and writes the file back out.
     */
    private static void handleUpdateUser(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            ex.sendResponseHeaders(405, -1);
            ex.close();
            return;
        }
        String ct = ex.getRequestHeaders().getFirst("Content-Type");
        if (ct == null || !ct.contains("application/json")) {
            ex.sendResponseHeaders(415, -1);
            ex.close();
            return;
        }

        // 1) Read JSON → CSV line
        String json = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        ex.getRequestBody().close();
        String updatedCsv = common.src.UserDAO.jsonToCsv(json);

        // 2) Load all lines, replace the matched username row
        Path csvPath = Paths.get(DB_DIR, USERS_CSV);
        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        String matchName = updatedCsv.split(",", 2)[0].replaceAll("^\"|\"$", "");
        for (int i = 1; i < lines.size(); i++) {
            String existing = lines.get(i).split(",",2)[0].replaceAll("^\"|\"$", "");
            if (existing.equals(matchName)) {
                lines.set(i, updatedCsv);
                break;
            }
        }
        // 3) Overwrite the file
        Files.write(csvPath, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

        ex.sendResponseHeaders(200, -1);
        ex.close();
    }



    private static void handleStaticFile(HttpExchange ex, Path path) throws IOException {
        if (Files.exists(path) && !Files.isDirectory(path)) {
            String ct = Files.probeContentType(path);
            byte[] data = Files.readAllBytes(path);
            if (ct!=null) ex.getResponseHeaders().set("Content-Type", ct);
            ex.sendResponseHeaders(200, data.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(data); }
        } else {
            ex.sendResponseHeaders(404, -1);
        }
        ex.close();
    }
}