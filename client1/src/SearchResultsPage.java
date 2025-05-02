package client1.src;

import common.src.Item;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// SearchResultsPage: Displays items matching the search query.
// At the top, it provides a search bar (with the search button to its right)
// and the account button which is replaced with "View Profile" if the user is logged in.
// Also includes a "View Cart" button with redeem-points functionality.
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

        // Top panel with back button, search field, and account button.
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

        // Bottom panel with "View Cart" button.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        viewCartButton.addActionListener(e -> displayCart());
        bottomPanel.add(viewCartButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Center panel for search results.
        resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Listen for changes in the search field
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
            JOptionPane.showMessageDialog(this, item.itemName + " added to cart!",
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

    /**
     * Calculate the total price of all items in the shared cart.
     */
    public double getCartTotal() {
        double sum = 0;
        for (Item item : sharedCart) {
            sum += item.currentPrice;
        }
        return sum;
    }

    protected void displayCart() {
        JPanel cartPanel = new JPanel(new BorderLayout());

        double originalTotal = getCartTotal();
        StringBuilder cartTextBuilder = new StringBuilder();

        if (sharedCart.isEmpty()) {
            cartTextBuilder.append("Your cart is empty.\n");
        } else {
            for (Item item : sharedCart) {
                cartTextBuilder.append(String.format("%s, Store: %s, Price: $%.2f%n",
                        item.itemName, item.storeName, item.currentPrice));
            }
        }
        cartTextBuilder.append(String.format("%nOriginal Total: $%.2f", originalTotal));

        JTextArea cartText = new JTextArea(cartTextBuilder.toString());
        cartText.setEditable(false);
        cartPanel.add(new JScrollPane(cartText), BorderLayout.CENTER);

        final int[] redeemedPoints = {0};
        final double[] discountDollars = {0};

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton redeemButton = new JButton("Redeem Points");
        redeemButton.addActionListener(e -> {
            if (MainFrame.currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in to redeem points.",
                        "No User", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int available = MainFrame.currentUser.rewardPoints;
            String input = JOptionPane.showInputDialog(this,
                    String.format("You have %d points.\nEnter points to redeem (multiples of 100):", available),
                    "Redeem Points", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            int pts;
            try {
                pts = Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid integer.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pts <= 0 || pts % 100 != 0) {
                JOptionPane.showMessageDialog(this, "You must redeem a positive multiple of 100 points.",
                        "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pts > available) {
                JOptionPane.showMessageDialog(this, "You don't have that many points.",
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
            MainFrame.currentUser.rewardPoints -= pts;

            StringBuilder updated = new StringBuilder();
            for (Item item : sharedCart) {
                updated.append(String.format("%s, Store: %s, Price: $%.2f%n",
                        item.itemName, item.storeName, item.currentPrice));
            }
            double newTotal = originalTotal - discountDollars[0];
            updated.append(String.format("%nOriginal Total: $%.2f", originalTotal));
            updated.append(String.format("%nRedeemed: %d points → $%.2f", redeemedPoints[0], discountDollars[0]));
            updated.append(String.format("%nNew Total: $%.2f", newTotal));
            updated.append(String.format("%nRemaining Points: %d", MainFrame.currentUser.rewardPoints));
            cartText.setText(updated.toString());
        });
        buttonPanel.add(redeemButton);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showOptionDialog(this, cartPanel, "Shopping Cart",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new String[]{"Checkout", "Close"}, "Close");

        if (result == 0) {
            if (MainFrame.currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please log in to checkout.",
                        "Checkout Error", JOptionPane.ERROR_MESSAGE);
            } else if (sharedCart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.",
                        "Checkout", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Item purchased : sharedCart) {
                    MainFrame.currentUser.purchaseHistory.add(
                            String.format("%s at $%.2f", purchased.itemName, purchased.currentPrice));
                    MainFrame.currentUser.rewardPoints += 10;
                }
                sharedCart.clear();
                JOptionPane.showMessageDialog(this,
                        String.format("Checkout successful!%nYou saved $%.2f by redeeming %d points.%nCurrent points balance: %d",
                                discountDollars[0], redeemedPoints[0], MainFrame.currentUser.rewardPoints),
                        "Checkout", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
