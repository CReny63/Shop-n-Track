package client1.src;

import common.src.User;
import javax.swing.*;
import java.awt.*;

/**
 * client1.src.ProfilePage: Displays the user's account information
 * and includes a help icon explaining the point system.
 */
public class ProfilePage extends JPanel {
    public ProfilePage(MainFrame frame, User user) {
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("User Profile", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Username
        JLabel usernameLabel = new JLabel("Username: " + user.getUser());
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(usernameLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        // First Name
        JLabel firstNameLabel = new JLabel("First Name: " + user.getFirstName());
        firstNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(firstNameLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name: " + user.getLastName());
        lastNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(lastNameLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        // Email
        JLabel emailLabel = new JLabel("Email: " + user.getEmail());
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        // Password (optional display)
        JLabel passwordLabel = new JLabel("Password: " + user.getPassword());
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(passwordLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        // Reward Points with help icon
        JLabel rewardLabel = new JLabel("Reward Points: " + user.getRewardPoints());
        rewardLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        Icon helpIcon = UIManager.getIcon("OptionPane.questionIcon");
        JButton helpButton = new JButton(helpIcon);
        helpButton.setBorderPainted(false);
        helpButton.setContentAreaFilled(false);
        helpButton.setToolTipText("How does the point system work?");
        helpButton.addActionListener(e -> {
            String message = "<html>"
                    + "<h2>Point System Explained</h2>"
                    + "<ul>"
                    + "<li>Earn <b>10 points</b> for every purchase.</li>"
                    + "<li>100 points = $1 off your next purchase.</li>"
                    + "<li>Points expire after <b>12 months</b>.</li>"
                    + "</ul>"
                    + "</html>";
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Point System",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        JPanel rewardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rewardPanel.add(rewardLabel);
        rewardPanel.add(Box.createHorizontalStrut(5));
        rewardPanel.add(helpButton);
        infoPanel.add(rewardPanel);
        infoPanel.add(Box.createVerticalStrut(20));

        // Purchase History
        JLabel historyHeader = new JLabel("Purchase History:");
        historyHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        infoPanel.add(historyHeader);
        infoPanel.add(Box.createVerticalStrut(12));

        if (user.getPurchaseHistory().isEmpty()) {
            JLabel noPurchase = new JLabel("No purchases yet.");
            noPurchase.setFont(new Font("SansSerif", Font.PLAIN, 18));
            infoPanel.add(noPurchase);
        } else {
            for (String purchase : user.getPurchaseHistory()) {
                JLabel purchaseLabel = new JLabel(purchase);
                purchaseLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
                infoPanel.add(purchaseLabel);
                infoPanel.add(Box.createVerticalStrut(8));
            }
        }

        add(infoPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> frame.showHomePage());
        logoutButton.addActionListener(e -> {
            MainFrame.currentUser = null;
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            frame.showHomePage();
        });
    }
}
