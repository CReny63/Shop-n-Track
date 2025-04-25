import javax.swing.*;
import java.awt.*;

/**
 * DisplayItemInfoPage: Shows detailed information for a selected item,
 * including a price history graph and seller details.
 */
class DisplayItemInfoPage extends JPanel {
    public DisplayItemInfoPage(MainFrame frame, Item item) {
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Item Details: " + item.itemName, SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        double[] priceHistory;
        if (item.previousPrices != null && item.previousPrices.length > 0) {
            priceHistory = new double[item.previousPrices.length + 1];
            for (int i = 0; i < item.previousPrices.length; i++)
                priceHistory[i] = item.previousPrices[i];
            priceHistory[priceHistory.length - 1] = item.currentPrice;
        } else {
            priceHistory = new double[0];
        }
        PriceHistoryPanel graphPanel = new PriceHistoryPanel(priceHistory, item.currentPrice);
        graphPanel.setPreferredSize(new Dimension(300, 150));
        add(graphPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel sellerLabel = new JLabel("Seller: " + item.storeName);
        sellerLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(sellerLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        JLabel shippingLabel = new JLabel("Shipping: Free Shipping");
        shippingLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(shippingLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        JLabel rewardLabel = new JLabel("Reward Points Eligibility: Eligible");
        rewardLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(rewardLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        JLabel reputationLabel = new JLabel("Seller Reputation: Excellent");
        reputationLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(reputationLabel);
        infoPanel.add(Box.createVerticalStrut(10));

        JLabel disputeLabel = new JLabel("Dispute Resolution: Fair and Timely");
        disputeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(disputeLabel);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.addActionListener(e -> frame.showSearchResults(""));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(backButton);
        add(infoPanel, BorderLayout.SOUTH);
    }
}

