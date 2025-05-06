package client1.src;

import common.src.User;
import common.src.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class CreateAccountPage extends JPanel {
    private JTextField firstNameField, lastNameField, emailField, usernameField;
    private JPasswordField passwordField, confirmPasswordField;

    public CreateAccountPage(MainFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(15);
        gbc.gridx = 1; add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(15);
        gbc.gridx = 1; add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Email:"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1; add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1; add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1; add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1; add(confirmPasswordField, gbc);

        JButton createAccountButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createAccountButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        createAccountButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName  = lastNameField.getText().trim();
            String email     = emailField.getText().trim();
            String username  = usernameField.getText().trim();
            String password  = new String(passwordField.getPassword()).trim();
            String confirm   = new String(confirmPasswordField.getPassword()).trim();
            List<String> purchaseHistory = List.of();

            if (firstName.isEmpty() || lastName.isEmpty() ||
                    email.isEmpty() || username.isEmpty() ||
                    password.isEmpty() || !password.equals(confirm)) {
                JOptionPane.showMessageDialog(this,
                        "All fields must be filled and passwords must match.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(username, password, email, 0, firstName, lastName, purchaseHistory);

            try {
                boolean success = UserDAO.sendUser(newUser);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Account created successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    MainFrame.currentUser = newUser;
                    frame.showHomePage();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Server error: account could not be created.",
                            "Server Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to send data: " + ex.getMessage(),
                        "Network Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> frame.showLoginPage());
    }
}
