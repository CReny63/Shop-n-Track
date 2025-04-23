import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MainFrame: The main window that switches among pages using CardLayout.
 * Pages include HomePage, SearchResultsPage, DisplayItemInfoPage, LoginPage, and ProfilePage.
 * It also holds static user account information.
 */
public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public static final String PAGE_HOME = "HomePage";
    public static final String PAGE_RESULTS = "SearchResultsPage";
    public static final String PAGE_ITEMINFO = "ItemInfoPage";
    public static final String PAGE_LOGIN = "LoginPage";
    public static final String PAGE_PROFILE = "ProfilePage";

    // User account management.
    public static User currentUser = null;
    public static List<User> accounts = new ArrayList<>();

    // Load items once.
    public static List<Item> allItems = loadItemsFromCSV();

    public MainFrame() {
        super("Shop N Track");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create and add app bar.
        //JPanel appBar = createAppBar();
        //add(appBar, BorderLayout.NORTH);

        // Create content panel with CardLayout.
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new HomePage(this), PAGE_HOME);
        add(contentPanel, BorderLayout.CENTER);

        // Show HomePage.
        cardLayout.show(contentPanel, PAGE_HOME);
        setVisible(true);
    }

    /* Creates the title*/
    private JPanel createAppBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Shop N Track", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    // Navigation methods.
    public void showHomePage() {
        HomePage homePage = new HomePage(this);
        contentPanel.add(homePage, PAGE_HOME);
        cardLayout.show(contentPanel, PAGE_HOME);
    }

    public void showSearchResults(String query) {
        SearchResultsPage resultsPage = new SearchResultsPage(this, query);
        contentPanel.add(resultsPage, PAGE_RESULTS);
        cardLayout.show(contentPanel, PAGE_RESULTS);
    }

    public void showItemInfo(Item item) {
        DisplayItemInfoPage itemInfoPage = new DisplayItemInfoPage(this, item);
        contentPanel.add(itemInfoPage, PAGE_ITEMINFO);
        cardLayout.show(contentPanel, PAGE_ITEMINFO);
    }

    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(this);
        contentPanel.add(loginPage, PAGE_LOGIN);
        cardLayout.show(contentPanel, PAGE_LOGIN);
    }

    public void showProfile(User user) {
        ProfilePage profilePage = new ProfilePage(this, user);
        contentPanel.add(profilePage, PAGE_PROFILE);
        cardLayout.show(contentPanel, PAGE_PROFILE);
    }

    // Load items from CSV.
    private static List<Item> loadItemsFromCSV() {
        List<Item> list = new ArrayList<>();
        InputStream in = MainFrame.class.getResourceAsStream("/items.csv");
        if (in == null) {
            try {
                in = new FileInputStream("items.csv");
            } catch (FileNotFoundException e) { //throw error if no file found
                System.err.println("CSV file not found.");
                return list;
            }
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            br.readLine(); // Skip header.
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String itemName = parts[0].trim();
                    String storeName = parts[1].trim();
                    double currentPrice = Double.parseDouble(parts[2].trim());
                    String imageFile = parts[3].trim();
                    List<Double> historyList = new ArrayList<>();
                    if (parts.length > 4) {
                        for (int i = 4; i < parts.length; i++) {
                            try {
                                historyList.add(Double.parseDouble(parts[i].trim()));
                            } catch (Exception ex) {}
                        }
                    }
                    double[] previousPrices = new double[historyList.size()];
                    for (int i = 0; i < historyList.size(); i++) {
                        previousPrices[i] = historyList.get(i);
                    }
                    list.add(new Item(itemName, storeName, currentPrice, imageFile, previousPrices));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch(Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
