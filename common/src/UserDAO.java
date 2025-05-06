package common.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * common.src.UserDAO
 *
 * Converts between User objects and CSV lines, and can fetch user data from the server.
 */
public class UserDAO {

    public static boolean sendUserUpdate(User user) throws IOException {
        URL url = new URL("http://localhost:12345/api/updateUser");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        String json = toJson(user);  // same toJson you wrote for creation
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        conn.disconnect();
        return status >= 200 && status < 300;
    }


    /**
     * Produce one CSV line for the given user.
     * Format: username,password,email,points,firstName,lastName,history1,history2,...
     */
    public static String toCsvLine(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(csvEscape(user.getUser())).append(",")
                .append(csvEscape(user.getPassword())).append(",")
                .append(csvEscape(user.getEmail())).append(",")
                .append(user.getRewardPoints()).append(",")
                .append(csvEscape(user.getFirstName())).append(",")
                .append(csvEscape(user.getLastName()));

        for (String item : user.getPurchaseHistory()) {
            sb.append(",").append(csvEscape(item));
        }

        return sb.toString();
    }

    public static String jsonToCsv(String json) throws IOException {
        String username  = extractString(json, "username");
        String password  = extractString(json, "password");
        String email     = extractString(json, "email");
        int    points    = extractInt(json, "points");
        String firstName = extractString(json, "firstName");
        String lastName  = extractString(json, "lastName");
        List<String> history = extractArray(json, "history");

        // build a temporary User to leverage toCsvLine()
        User u = new User(username, password, email, points, firstName, lastName, history);
        u.getPurchaseHistory().addAll(history);

        return toCsvLine(u);
    }
    /**
     * Find "key":"value" and return value (unescaped).
     */
    private static String extractString(String json, String key) throws IOException {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) throw new IOException("Missing key: " + key);
        // move to the first quote after the colon
        int colon = json.indexOf(':', idx + pattern.length());
        int start = json.indexOf('"', colon + 1) + 1;
        int end   = json.indexOf('"', start);
        if (start < 1 || end < 0) throw new IOException("Bad JSON string for key: " + key);
        return json.substring(start, end)
                .replace("\\\"", "\"")   // unescape any \" inside
                .replace("\\\\", "\\");  // unescape backslashes
    }

    /**
     * Find "key":number and return it.
     */
    private static int extractInt(String json, String key) throws IOException {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) throw new IOException("Missing key: " + key);
        int colon = json.indexOf(':', idx + pattern.length());
        int start = colon + 1;
        // read until non-digit
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            throw new IOException("Bad JSON number for key: " + key, e);
        }
    }

    /**
     * Find "key":[…] and return the items (without quotes).
     */
    private static List<String> extractArray(String json, String key) throws IOException {
        List<String> list = new ArrayList<>();
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return list;  // no history key → empty
        int colon = json.indexOf(':', idx + pattern.length());
        int start = json.indexOf('[', colon + 1) + 1;
        int end   = json.indexOf(']', start);
        if (start < 1 || end < 0) throw new IOException("Bad JSON array for key: " + key);
        String inside = json.substring(start, end).trim();
        if (inside.isEmpty()) return list;
        // split on commas *outside* of quotes (simple case: entries are simple strings)
        String[] parts = inside.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("\"") && part.endsWith("\"")) {
                part = part.substring(1, part.length() - 1);
            }
            list.add(part.replace("\\\"", "\"").replace("\\\\", "\\"));
        }
        return list;
    }

    /**
     * Build a JSON representation of the User for sending to server.
     */
    private static String toJson(User u) {
        String historyJson = u.getPurchaseHistory().stream()
                .map(item -> "\"" + item.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        return "{"
                + "\"username\":\"" + u.getUser() + "\","
                + "\"password\":\"" + u.getPassword() + "\","
                + "\"email\":\""    + u.getEmail() + "\","
                + "\"points\":"      + u.getRewardPoints() + ","
                + "\"firstName\":\""+ u.getFirstName() + "\","
                + "\"lastName\":\"" + u.getLastName() + "\","
                + "\"history\":"     + historyJson
                + "}";
    }

    /**
     * Sends a new User to server to create an account.
     * Expects JSON at /api/createAccount and returns true on 2xx.
     */
    public static boolean sendUser(User user) throws IOException {
        URL url = new URL("http://localhost:12345/api/createAccount");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        String json = toJson(user);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        conn.disconnect();
        return status >= 200 && status < 300;
    }

    /**
     * Fetches a user from the server by posting credentials to /api/login.
     * @return a populated User if credentials valid, or null if invalid.
     */
    public static User fetchUser(String username, String password) throws IOException {
        // build form data
        String form = "username=" + URLEncoder.encode(username, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");

        URL url = new URL("http://localhost:12345/api/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"
        );
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(form.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status == 200) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
            );
            String line = br.readLine();
            br.close();
            conn.disconnect();
            return fromCsvLine(line);
        } else {
            conn.disconnect();
            return null;
        }
    }

    /**
     * Parses a CSV line into a User object.
     */
    public static User fromCsvLine(String line) {
        // split on commas not inside quotes
        String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        // extract and strip surrounding quotes
        String user      = stripQuotes(cols[0]);
        String pass      = stripQuotes(cols[1]);
        String email     = stripQuotes(cols[2]);
        int    points    = Integer.parseInt(stripQuotes(cols[3]));
        String firstName = stripQuotes(cols[4]);
        String lastName  = stripQuotes(cols[5]);

        // remaining columns are purchase history entries
        List<String> history = new ArrayList<>();
        for (int i = 6; i < cols.length; i++) {
            String item = stripQuotes(cols[i]);
            if (!item.isEmpty())
                history.add(item);
        }

        User u = new User(user, pass, email, points, firstName, lastName, history);
        return u;
    }

    /**
     * Wraps and escapes a single CSV field.
     */
    private static String csvEscape(String field) {
        if (field == null) return "\"\"";
        String escaped = field.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    /**
     * Removes surrounding quotes from a field.
     */
    private static String stripQuotes(String s) {
        if (s == null) return "";
        return s.replaceAll("^\"|\"$", "");
    }
}
