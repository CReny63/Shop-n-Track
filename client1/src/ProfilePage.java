package client1.src;

import common.src.*;
import javax.swing.*;
import java.awt.*;

//points conversion in profile page, between colon and "points"
/**
 * client1.src.ProfilePage: Displays the user's account information in a professional layout.
 */


class ProfilePage extends JPanel {

    private String user;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int rewardPoints;
    private List purchaseHist;

    public ProfilePage(MainFrame frame, User user) {
        setLayout(new BorderLayout());
        JLabel header = new JLabel("User Profile", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel usernameLabel = new JLabel("Username: " + user.username);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(usernameLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        JLabel passwordLabel = new JLabel("Password: " + user.password);
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(passwordLabel);
        infoPanel.add(Box.createVerticalStrut(12));

        JLabel rewardLabel = new JLabel("Reward Points: " + user.rewardPoints);//points conversion in profile page, between colon and "points"
        rewardLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoPanel.add(rewardLabel);
        infoPanel.add(Box.createVerticalStrut(20));

        JLabel historyHeader = new JLabel("Purchase History:");
        historyHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        infoPanel.add(historyHeader);
        infoPanel.add(Box.createVerticalStrut(12));

        if (user.purchaseHistory.isEmpty()) {
            JLabel noPurchase = new JLabel("No purchases yet.");
            noPurchase.setFont(new Font("SansSerif", Font.PLAIN, 18));
            infoPanel.add(noPurchase);
        } else {
            for (String purchase : user.purchaseHistory) {
                JLabel purchaseLabel = new JLabel(purchase);
                purchaseLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
                infoPanel.add(purchaseLabel);
                infoPanel.add(Box.createVerticalStrut(8));
            }
        }

        add(infoPanel, BorderLayout.CENTER);

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
