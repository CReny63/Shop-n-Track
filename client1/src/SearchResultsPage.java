package client1.src;

import common.src.Item;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * client1.src.SearchResultsPage: Displays items matching the search query.
 * At the top, it provides a search bar (with the search button to its right)
 * and the account button which is replaced with "View Profile" if the user is logged in.
 * Also includes a "View Cart" button at the bottom.
 */
class SearchResultsPage extends JPanel {
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

        // Account button: if user not logged in, show login; else view profile.
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

        // Listen for changes in the search field, allows for instant update when typing
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
            if (item.itemName.toLowerCase().contains(query)) {
                resultsPanel.add(createItemPanel(item));
            }
            if (item.storeName.toLowerCase().contains(query)) {
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

        // Load image via server endpoint
        try {
            URL url = new URL(SERVER_BASE + "/ItemPics/" + item.imageFile);
            Image itemImage = new ImageIcon(url).getImage();
            Image scaled = itemImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(imgLabel);
        } catch (Exception ignored) {}

        // Item name
        JLabel nameLabel = new JLabel(item.itemName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(nameLabel);

        // Store label
        JLabel storeLabel = new JLabel(item.storeName, SwingConstants.CENTER);
        storeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        storeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(storeLabel);

        // Price label
        JLabel priceLabel = new JLabel(String.format("$%.2f", item.currentPrice), SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(priceLabel);

        // Add to Cart button
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

        // View Details button
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        viewDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDetailsButton.addActionListener(e -> frame.showItemInfo(item));
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(viewDetailsButton);

        return itemPanel;
    }

    protected void displayCart() {
        JPanel cartPanel = new JPanel(new BorderLayout());
        StringBuilder cartDisplay = new StringBuilder();
        if (sharedCart.isEmpty())
            cartDisplay.append("Your cart is empty.");
        else {
            for (Item item : sharedCart) {
                cartDisplay.append(String.format("%s, Store: %s, Price: $%.2f%n",
                        item.itemName, item.storeName, item.currentPrice));
            }
        }
        JTextArea cartText = new JTextArea(cartDisplay.toString());
        cartText.setEditable(false);
        cartPanel.add(new JScrollPane(cartText), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
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
                JOptionPane.showMessageDialog(this, "Checkout successful! Reward points updated.",
                        "Checkout", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
