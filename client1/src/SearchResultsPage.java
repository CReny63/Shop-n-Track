package client1.src;

import common.src.Item;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import common.src.UserDAO;

/**
 * SearchResultsPage: Displays items matching the search query.
 * Includes a search bar, account/profile button, and a "View Cart" button
 * whose dialog remains a fixed size even when content changes.
 */
public class SearchResultsPage extends JPanel {
    private MainFrame frame;
    private JTextField searchField;
    private JPanel resultsPanel;
    private String initialQuery;
    public static List<Item> sharedCart = new ArrayList<>();
    private static final String SERVER_BASE = "http://localhost:12345";

    public SearchResultsPage(MainFrame frame, String query) {
        this.frame = frame;
        this.initialQuery = query;
        setLayout(new BorderLayout());

        // Top panel: back, search, account
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener(e -> frame.showHomePage());
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        searchField = new JTextField(query, 20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        JButton accountButton = new JButton();
        accountButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        if (MainFrame.currentUser == null) {
            accountButton.setText("Login / Create Account");
            accountButton.addActionListener(e -> frame.showLoginPage());
        } else {
            accountButton.setText("View Profile");
            accountButton.addActionListener(e -> frame.showProfile(MainFrame.currentUser));
        }
        topPanel.add(accountButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Bottom panel: view cart
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        viewCartButton.addActionListener(e -> displayCart());
        bottomPanel.add(viewCartButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Center: results
        resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Search listeners
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });
        searchButton.addActionListener(e -> performSearch());
        performSearch();
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        resultsPanel.removeAll();
        for (Item item : MainFrame.allItems) {
            if (item.itemName.toLowerCase().contains(query) ||
                    item.storeName.toLowerCase().contains(query)) {
                resultsPanel.add(createItemPanel(item));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createItemPanel(Item item) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        itemPanel.setPreferredSize(new Dimension(150, 260));
        itemPanel.setBackground(new Color(245, 245, 245));

        try {
            URL url = new URL(SERVER_BASE + "/ItemPics/" + item.imageFile);
            Image itemImage = new ImageIcon(url).getImage();
            Image scaled = itemImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(imgLabel);
        } catch (Exception ignored) {}

        JLabel nameLabel = new JLabel(item.itemName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(nameLabel);

        JLabel storeLabel = new JLabel(item.storeName, SwingConstants.CENTER);
        storeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        storeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(storeLabel);

        JLabel priceLabel = new JLabel(String.format("$%.2f", item.currentPrice), SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(priceLabel);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> {
            sharedCart.add(item);
            JOptionPane.showMessageDialog(this,
                    item.itemName + " added to cart!",
                    "Cart Update", JOptionPane.INFORMATION_MESSAGE);
        });
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(addToCartButton);

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        viewDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDetailsButton.addActionListener(e -> frame.showItemInfo(item));
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(viewDetailsButton);

        return itemPanel;
    }

    public double getCartTotal() {
        double sum = 0;
        for (Item item : sharedCart) {
            sum += item.currentPrice;
        }
        return sum;
    }

    protected void displayCart() {
        // Build content
        double originalTotal = getCartTotal();
        StringBuilder cartTextBuilder = new StringBuilder();
        if (sharedCart.isEmpty()) {
            cartTextBuilder.append("Your cart is empty.\n");
        } else {
            for (Item item : sharedCart) {
                cartTextBuilder.append(
                        String.format("%s, Price: $%.2f%n",
                                item.itemName, item.currentPrice)
                );
            }
        }
        cartTextBuilder.append(String.format("%nOriginal Total: $%.2f", originalTotal));

        JTextArea cartText = new JTextArea(cartTextBuilder.toString());
        cartText.setEditable(false);
        JScrollPane cartScroll = new JScrollPane(cartText);
        cartScroll.setPreferredSize(new Dimension(480, 500));

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        cartPanel.setPreferredSize(new Dimension(200, 150));

        // Redeem button
        final int[] redeemedPoints = {0};
        final double[] discountDollars = {0};
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton redeemButton = new JButton("Redeem Points");
        redeemButton.addActionListener(e -> {
            if (MainFrame.currentUser == null) {
                JOptionPane.showMessageDialog(this,
                        "Please log in to redeem points.",
                        "No User", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int available = MainFrame.currentUser.getRewardPoints();
            String input = JOptionPane.showInputDialog(
                    this,
                    String.format(
                            "You have %d points.\nEnter points to redeem (multiples of 100):",
                            available
                    ),
                    "Redeem Points",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input == null) return;
            int pts;
            try {
                pts = Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid integer.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pts <= 0 || pts % 100 != 0) {
                JOptionPane.showMessageDialog(this,
                        "You must redeem a positive multiple of 100 points.",
                        "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pts > available) {
                JOptionPane.showMessageDialog(this,
                        "You don't have that many points.",
                        "Insufficient Points", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double dollars = pts / 100.0;
            if (dollars > originalTotal) {
                dollars = originalTotal;
                pts = (int)(originalTotal * 100);
            }
            redeemedPoints[0] = pts;
            discountDollars[0] = dollars;
            MainFrame.currentUser.changeRewardPoints(
                    MainFrame.currentUser.getRewardPoints() - pts
            );

            // Update text area
            StringBuilder updated = new StringBuilder();
            for (Item item : sharedCart) {
                updated.append(
                        String.format("%s, Price: $%.2f%n",
                                item.itemName, item.currentPrice)
                );
            }
            double newTotal = originalTotal - discountDollars[0];
            updated.append(String.format("%nOriginal Total: $%.2f", originalTotal));
            updated.append(
                    String.format("%nRedeemed: %d points → $%.2f",
                            redeemedPoints[0], discountDollars[0]
                    )
            );
            updated.append(String.format("%nNew Total: $%.2f", newTotal));
            updated.append(
                    String.format("%nRemaining Points: %d",
                            MainFrame.currentUser.getRewardPoints()
                    )
            );
            cartText.setText(updated.toString());
        });
        buttonPanel.add(redeemButton);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Show in a fixed-size dialog
        JOptionPane pane = new JOptionPane(
                cartPanel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                new String[]{"Checkout", "Close"},
                "Close"
        );
        JDialog dialog = pane.createDialog(this, "Shopping Cart");
        dialog.setResizable(false);
        dialog.setSize(200, 150);
        dialog.setMinimumSize(new Dimension(300, 275));
        dialog.setVisible(true);

        Object value = pane.getValue();
        if ("Checkout".equals(value)) {
            if (MainFrame.currentUser == null) {
                JOptionPane.showMessageDialog(this,
                        "Please log in to checkout.",
                        "Checkout Error", JOptionPane.ERROR_MESSAGE);
            } else if (sharedCart.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Your cart is empty.",
                        "Checkout", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Item purchased : sharedCart) {
                    MainFrame.currentUser.getPurchaseHistory().add(
                            String.format("%s at $%.2f",
                                    purchased.itemName, purchased.currentPrice
                            )
                    );
                    MainFrame.currentUser.changeRewardPoints(
                            MainFrame.currentUser.getRewardPoints() + 10
                    );
                    try {
                        boolean ok = UserDAO.sendUserUpdate(MainFrame.currentUser);
                        if (!ok) {
                            JOptionPane.showMessageDialog(this,
                                    "Failed to save your purchase.",
                                    "Save Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch(IOException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Network error: " + ex.getMessage(),
                                "Network Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                sharedCart.clear();
                JOptionPane.showMessageDialog(this,
                        String.format(
                                "Checkout successful!%nYou saved $%.2f by redeeming %d points.%nCurrent points balance: %d",
                                discountDollars[0], redeemedPoints[0], MainFrame.currentUser.getRewardPoints()
                        ),
                        "Checkout", JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
}
