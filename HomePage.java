// HomePage.java
import javax.swing.*;
import java.awt.*;

/**
 * HomePage: The first page that users see.
 * It features a centered logo (scaled to fit), title, search bar,
 * and shows the account button only if no user is logged in.
 */
class HomePage extends JPanel {
    private MainFrame frame;
    private JTextField searchField;

    public HomePage(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Row 0: Logo image scaled to fit screen
        ImageIcon originalIcon = new ImageIcon("ItemPics/Logo.png"); // Update path
        Image img = originalIcon.getImage();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = screenSize.width / 2;
        int maxHeight = screenSize.height / 5;
        int imgWidth = originalIcon.getIconWidth();
        int imgHeight = originalIcon.getIconHeight();
        float scale = Math.min((float) maxWidth / imgWidth, (float) maxHeight / imgHeight);
        int newWidth = Math.round(imgWidth * scale);
        int newHeight = Math.round(imgHeight * scale);
        Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(scaledImg);
        JLabel logoLabel = new JLabel(logoIcon);
        gbc.gridy = 0;
        add(logoLabel, gbc);

        // Row 1: Title label.
        JLabel titleLabel = new JLabel("Shop N Track", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridy = 1;
        add(titleLabel, gbc);

        // Row 2: Search panel (search field and search button side by side).
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchButton);
        gbc.gridy = 2;
        add(searchPanel, gbc);

        // ADD ROW: will contain popular items
        JPanel popularPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        // TODO: populate with popular items
        gbc.gridy = 3;
        add(popularPanel, gbc);

        // Row 4: Account button.
        gbc.gridy = 4;
        JButton accountButton = new JButton();
        accountButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        if (MainFrame.currentUser == null) {
            accountButton.setText("Login / Create Account");
            accountButton.addActionListener(e -> frame.showLoginPage());
        } else {
            accountButton.setText("View Profile");
            accountButton.addActionListener(e -> frame.showProfile(MainFrame.currentUser));
        }
        add(accountButton, gbc);

        // Action for search button.
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                frame.showSearchResults(query);
            }
        });
    }
}
