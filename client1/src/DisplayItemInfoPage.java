package client1.src;

import common.src.*;
import javax.swing.*;
import java.awt.*;

/**
 * client1.src.DisplayItemInfoPage: Shows detailed information for a selected item,
 * including a price history graph and seller details.
 */
class DisplayItemInfoPage extends JPanel {
    public DisplayItemInfoPage(MainFrame frame, Item item) {
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Item Details: " + item.itemName, SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Price history graph
        double[] priceHistory;
        if (item.previousPrices != null && item.previousPrices.length > 0) {
            priceHistory = new double[item.previousPrices.length + 1];
            System.arraycopy(item.previousPrices, 0, priceHistory, 0, item.previousPrices.length);
            priceHistory[priceHistory.length - 1] = item.currentPrice;
        } else {
            priceHistory = new double[0];
        }
        PriceHistoryPanel graphPanel = new PriceHistoryPanel(priceHistory, item.currentPrice);
        graphPanel.setPreferredSize(new Dimension(300, 150));
        add(graphPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Seller store
        JLabel sellerLabel = new JLabel("Seller: " + item.storeName);
        sellerLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(sellerLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        // Shipping info
        JLabel shippingLabel = new JLabel("Shipping: Free Shipping");
        shippingLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(shippingLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        // Reward points
        JLabel rewardLabel = new JLabel("Reward Points Eligibility: Eligible");
        rewardLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(rewardLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        // Seller reputation with partial color coding
        String repText = "Excellent"; // Or fetch from item if available
        String color;
        switch (repText.toLowerCase()) {
            case "excellent": color = "green";  break;
            case "ok":        color = "yellow"; break;
            case "poor":      color = "red";    break;
            default:           color = "black";  break;
        }
        JLabel reputationLabel = new JLabel(
                String.format("<html>Seller Reputation: <span style='color:%s;'>%s</span></html>", color, repText)
        );
        reputationLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(reputationLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.addActionListener(e -> frame.showSearchResults(""));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(backButton);

        add(infoPanel, BorderLayout.SOUTH);
    }
}
