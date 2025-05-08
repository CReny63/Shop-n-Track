package client1.src;

import common.src.User;
import common.src.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * client1.src.LoginPage: Validates credentials by POSTing to /api/login,
 * receives the full CSV line on success, parses it, and populates the User.
 */
public class LoginPage extends JPanel {
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
        JButton loginButton  = new JButton("Sign In");
        JButton createButton = new JButton("Create Account");
        JButton backButton   = new JButton("Back");
        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        createButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));

        buttonPanel.add(loginButton);
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both username and password.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Delegate to UserDAO.fetchUser(...) instead of manual HTTP + parsing
                User current = UserDAO.fetchUser(user, pass);
                if (current != null) {
                    MainFrame.currentUser = current;
                    frame.showSearchResults("");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid username or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error contacting server: " + ex.getMessage(),
                        "Network Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        createButton.addActionListener(e -> frame.showCreateAccountPage());
        backButton.addActionListener(e -> frame.showHomePage());
    }
}
