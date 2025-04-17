import javax.swing.*;
import java.awt.*;

/**
 * HomePage: The first page that users see.
 * It features a centered search bar (with the search button immediately adjacent)
 * and shows the account button only if no user is logged in (otherwise “View Profile” is shown).
 * The duplicate brand label is removed (the app bar already shows the brand).
 */
class HomePage extends JPanel {
    private MainFrame frame;
    private JTextField searchField;

    public HomePage(MainFrame frame) {
        this.frame = frame;
        // Use GridBagLayout to center components in the middle of the page.
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Set insets to control spacing between components.
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Row 0: Title label.
        JLabel titleLabel = new JLabel("Shop N Track", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Row 1: Search panel (search field and search button side by side).
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchButton);
        gbc.gridy = 1;
        add(searchPanel, gbc);

        // Row 2: Account button.
        // If no user is logged in, display "Login / Create Account"; if logged in, display "View Profile".
        gbc.gridy = 2;
        JButton loginButton = new JButton();
        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        if (MainFrame.currentUser == null) {
            loginButton.setText("Login / Create Account");
            loginButton.addActionListener(e -> frame.showLoginPage());
        } else {
            loginButton.setText("View Profile");
            loginButton.addActionListener(e -> frame.showProfile(MainFrame.currentUser));
        }
        add(loginButton, gbc);

        // Action for search button.
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                frame.showSearchResults(query);
            }
        });
    }
}
