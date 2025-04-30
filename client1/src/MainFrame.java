package client1.src;

import common.src.Item;
import common.src.User;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final Client client;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public static final String PAGE_HOME     = "HomePage";
    public static final String PAGE_RESULTS  = "SearchResultsPage";
    public static final String PAGE_ITEMINFO = "ItemInfoPage";
    public static final String PAGE_LOGIN    = "LoginPage";
    public static final String PAGE_PROFILE  = "ProfilePage";

    public static User currentUser = null;
    public static List<User> accounts = new ArrayList<>();
    public static List<Item> allItems = loadItemsFromCSV();

    public MainFrame() throws IOException {
        super("Shop N Track");
        // Connect to server (socket)
        this.client = new Client("localhost", 12345);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // CardLayout container
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new HomePage(this), PAGE_HOME);
        add(contentPanel, BorderLayout.CENTER);

        // Show initial page
        cardLayout.show(contentPanel, PAGE_HOME);
        setVisible(true);

        // Start listening for server-driven navigation
        startResponseListener();
    }

    private void startResponseListener() {
        Thread listener = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.readMessage()) != null) {
                    handleServerMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Socket-Listener");
        listener.setDaemon(true);
        listener.start();
    }

    private void handleServerMessage(String msg) {
        // Expected format: COMMAND:PAYLOAD
        String[] parts = msg.split(":", 2);
        String cmd = parts[0], payload = parts.length > 1 ? parts[1] : null;

        SwingUtilities.invokeLater(() -> {
            switch (cmd) {
                case "SHOW_PAGE":
                    if (payload != null) cardLayout.show(contentPanel, payload);
                    break;
                // Add other command handlers if needed
            }
        });
    }

    public Client getClient() {
        return client;
    }

    // Navigation helpers (called after server instructs SHOW_PAGE)
    public void showHomePage() {
        HomePage homePage = new HomePage(this);
        contentPanel.add(homePage, PAGE_HOME);
        cardLayout.show(contentPanel, PAGE_HOME);
    }

    public void showSearchResults(String query) {
        SearchResultsPage page = new SearchResultsPage(this, query);
        contentPanel.add(page, PAGE_RESULTS);
        cardLayout.show(contentPanel, PAGE_RESULTS);
    }

    public void showItemInfo(Item item) {
        DisplayItemInfoPage page = new DisplayItemInfoPage(this, item);
        contentPanel.add(page, PAGE_ITEMINFO);
        cardLayout.show(contentPanel, PAGE_ITEMINFO);
    }

    public void showLoginPage() {
        LoginPage page = new LoginPage(this);
        contentPanel.add(page, PAGE_LOGIN);
        cardLayout.show(contentPanel, PAGE_LOGIN);
    }

    public void showProfile(User user) {
        ProfilePage page = new ProfilePage(this, user);
        contentPanel.add(page, PAGE_PROFILE);
        cardLayout.show(contentPanel, PAGE_PROFILE);
    }

    private static List<Item> loadItemsFromCSV() {
        List<Item> list = new ArrayList<>();
        String serverBase = "http://localhost:12345";
        String csvPath     = "/items.csv";

        try {
            // FETCH CSV directly (public raw endpoint)
            URL csvUrl = new URL(serverBase + csvPath);
            HttpURLConnection csvConn = (HttpURLConnection) csvUrl.openConnection();
            csvConn.setRequestMethod("GET");
            if (csvConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("Failed to fetch CSV (HTTP " + csvConn.getResponseCode() + ")");
                return list;
            }

            // PARSE CSV
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(csvConn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String nm    = parts[0].trim();
                        String store = parts[1].trim();
                        String storeRep = parts[2].trim();
                        int points = Integer.parseInt(parts[3].trim());
                        double cur   = Double.parseDouble(parts[4].trim());
                        String img   = parts[5].trim();


                        // parse any extra historical prices
                        List<Double> history = new ArrayList<>();
                        for (int i = 6; i < parts.length; i++) {
                            try { history.add(Double.parseDouble(parts[i].trim())); }
                            catch (NumberFormatException ignore) {}
                        }
                        double[] prev = history.stream().mapToDouble(d -> d).toArray();

                        list.add(new Item(nm, store, storeRep, points, cur, img, prev));
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading CSV from server: " + e.getMessage());
        }

        return list;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> {
            try { new MainFrame(); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }
}
