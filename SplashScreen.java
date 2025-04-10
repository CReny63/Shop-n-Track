import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// This class represents the splash (login) screen with username/password widgets.
public class SplashScreen extends JPanel {
    private mainFrame frame;

    public SplashScreen(mainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label and TextField
        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        // Password Label and Password Field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(signInButton, gbc);

        // Create Account Button
        JButton createAccButton = new JButton("Create Account");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(createAccButton, gbc);

        // Action listener for the sign in button (for demo, we directly navigate to the dashboard).
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Here you might validate credentials before navigating.
                frame.showDashboard();
            }
        });

        // Action listener for the create account button.
        createAccButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Account creation not implemented.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
