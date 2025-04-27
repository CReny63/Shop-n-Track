package client1.src; // client1.src.HomePage.java

import common.src.Item;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * client1.src.HomePage: The first page that users see.
 * It features a centered logo (loaded from client resources), title, search bar,
 * a View Cart button at top-right, a 4-at-a-time horizontally scrolling carousel of
 * 6 popular items, and shows the account button only if no user is logged in.
 * All item panels have been resized for more compact display.
 */
class HomePage extends JPanel {
    private MainFrame frame;
    private JTextField searchField;
    private JPanel carouselPanel;
    private List<Item> popularItems;
    private int carouselIndex = 0;
    private static final int VISIBLE_COUNT = 4;
    private static final int TOTAL_COUNT = 6;
    private static final int ROTATE_DELAY_MS = 3000;

    public HomePage(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        // Row 0: View Cart button at top-right
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton viewCartBtn = new JButton("View Cart");
        viewCartBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        viewCartBtn.addActionListener(e -> {
            // create a dummy SearchResultsPage just to call its displayCart()
            //future update make display Cart its own class to get rid of dummy page
            new SearchResultsPage(frame, "").displayCart();
        });
        add(viewCartBtn, gbc);

        // Row 1: Logo centered
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        URL logoUrl = getClass().getResource("/client1/resources/SystemPics/Logo.png");
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image img = icon.getImage();
            int maxW = 350, maxH = 150;
            float scale = Math.min((float)maxW/img.getWidth(null), (float)maxH/img.getHeight(null));
            Image scaled = img.getScaledInstance(
                    Math.round(img.getWidth(null)*scale),
                    Math.round(img.getHeight(null)*scale),
                    Image.SCALE_SMOOTH
            );
            add(new JLabel(new ImageIcon(scaled)), gbc);
        }

        // Row 2: Title
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        //gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Shop N Track", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Row 3: Search bar
        gbc.gridy = 3;
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        searchField = new JTextField(15);
        searchField.setText("Search for an Item");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(searchPanel, gbc);
        gbc.gridwidth = 1;

        // Row 4: Store buttons
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel storePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        String[] stores = {"Apple","Samsung","Sony","LG"};
        for (String s : stores) {
            JButton btn = new JButton("Shop " + s);
            btn.addActionListener(e -> frame.showSearchResults(s));
            storePanel.add(btn);
        }
        add(storePanel, gbc);
        gbc.gridwidth = 1;


        // Row 5: Carousel panel
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        carouselPanel = new JPanel();
        carouselPanel.setLayout(new BoxLayout(carouselPanel, BoxLayout.X_AXIS));
        add(carouselPanel, gbc);
        gbc.gridwidth = 1;

        // Prepare exactly six popular items
        popularItems = new ArrayList<>(MainFrame.allItems.subList(0, Math.min(TOTAL_COUNT, MainFrame.allItems.size())));
        while (popularItems.size() < TOTAL_COUNT) {
            popularItems.addAll(new ArrayList<>(popularItems));
        }

        // Initial populate and start rotation timer
        updateCarousel();
        new Timer(ROTATE_DELAY_MS, e -> {
            carouselIndex = (carouselIndex + 1) % popularItems.size();
            updateCarousel();
        }).start();

        // Row 6: Account button
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton accountButton = new JButton();
        accountButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        if (MainFrame.currentUser == null) {
            accountButton.setText("Login / Create Account");
            accountButton.addActionListener(e -> frame.showLoginPage());
        } else {
            accountButton.setText("View Profile");
            accountButton.addActionListener(e -> frame.showProfile(MainFrame.currentUser));
        }
        add(accountButton, gbc);

        // Search button action
        searchButton.addActionListener(e -> {
            String q = searchField.getText().trim();
            if (!q.isEmpty()) frame.showSearchResults(q);
        });
    }

    /**
     * Populates carouselPanel with four items starting at carouselIndex.
     */
    private void updateCarousel() {
        carouselPanel.removeAll();
        for (int i = 0; i < VISIBLE_COUNT; i++) {
            Item item = popularItems.get((carouselIndex + i) % popularItems.size());
            carouselPanel.add(createItemPanel(item));
        }
        carouselPanel.revalidate();
        carouselPanel.repaint();
    }

    /**
     * Creates the item panel styled like SearchResultsPage.
     * Reduced sizes for compact display: 120x200 panel, 80x80 image, smaller fonts.
     */
    private JPanel createItemPanel(Item item) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        itemPanel.setPreferredSize(new Dimension(120, 100));
        itemPanel.setBackground(new Color(245, 245, 245));

        // Load image via server endpoint
        try {
            URL url = new URL("http://localhost:12345/ItemPics/" + item.imageFile);
            Image img = new ImageIcon(url).getImage();
            Image scaled = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaled));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemPanel.add(imgLabel);
        } catch (Exception ignored) {}

        // Name label
        JLabel nameLabel = new JLabel(item.itemName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(Box.createVerticalStrut(3));
        itemPanel.add(nameLabel);

        // Store label
        JLabel storeLabel = new JLabel(item.storeName, SwingConstants.CENTER);
        storeLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        storeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(storeLabel);

        // Price label
        JLabel priceLabel = new JLabel(String.format("$%.2f", item.currentPrice), SwingConstants.CENTER);
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.add(priceLabel);

        // Buttons
        JButton addToCart = new JButton("Add to Cart");
        addToCart.setMaximumSize(new Dimension(100, 20));
        addToCart.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCart.addActionListener(e -> {
            SearchResultsPage.sharedCart.add(item);
            JOptionPane.showMessageDialog(this, item.itemName + " added to cart!", "Cart Update", JOptionPane.INFORMATION_MESSAGE);
        });
        itemPanel.add(Box.createVerticalStrut(1));
        itemPanel.add(addToCart);

        JButton viewDetails = new JButton("View Details");
        viewDetails.setMaximumSize(new Dimension(100, 20));
        viewDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewDetails.addActionListener(e -> frame.showItemInfo(item));
        itemPanel.add(Box.createVerticalStrut(1));
        itemPanel.add(viewDetails);
        itemPanel.add(Box.createVerticalStrut(20)); //adds space under details botton, prevents cutoff

        return itemPanel;
    }
}
