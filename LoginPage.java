import javax.swing.*;
import java.awt.*;

/**
 * LoginPage: A standalone page for logging in or creating an account.
 * Contains a temporary "Bypass" button for testing.
 */
class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage(MainFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login / Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JButton createButton = new JButton("Create Account");
        createButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JButton bypassButton = new JButton("Temporary Bypass");
        bypassButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttonPanel.add(loginButton);
        buttonPanel.add(createButton);
        buttonPanel.add(bypassButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            boolean found = false;
            for (User u : MainFrame.accounts) {
                if (u.username.equals(username) && u.password.equals(password)) {
                    MainFrame.currentUser = u;
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.showSearchResults("");
            }
        });

        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            for (User u : MainFrame.accounts) {
                if (u.username.equals(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.",
                            "Creation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User newUser = new User(username, password);
            MainFrame.accounts.add(newUser);
            MainFrame.currentUser = newUser;
            JOptionPane.showMessageDialog(this, "Account created successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.showHomePage();
        });

        bypassButton.addActionListener(e -> {
            String testUsername = "testUser";
            String testPassword = "test";
            boolean found = false;
            for (User u : MainFrame.accounts) {
                if (u.username.equals(testUsername)) {
                    MainFrame.currentUser = u;
                    found = true;
                    break;
                }
            }
            if (!found) {
                User testUser = new User(testUsername, testPassword);
                MainFrame.accounts.add(testUser);
                MainFrame.currentUser = testUser;
            }
            JOptionPane.showMessageDialog(this, "Bypass activated. Logged in as " + testUsername + ".",
                    "Bypass Login", JOptionPane.INFORMATION_MESSAGE);
            frame.showHomePage();
        });

        backButton.addActionListener(e -> frame.showHomePage());
    }
}
