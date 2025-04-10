import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Card names for the pages
    public static final String PAGE_SPLASH = "SplashScreen";
    public static final String PAGE_DASHBOARD = "Dashboard";

    public mainFrame() {
        setTitle("Shop N Track");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the top app bar
        JPanel appBar = createAppBar();
        add(appBar, BorderLayout.NORTH);

        // Create the content area that will swap pages
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Create and add the splash (login) screen and dashboard to the content panel
        contentPanel.add(new SplashScreen(this), PAGE_SPLASH);
        contentPanel.add(new DashboardPage(this), PAGE_DASHBOARD);

        add(contentPanel, BorderLayout.CENTER);

        // Initially show the splash screen
        cardLayout.show(contentPanel, PAGE_SPLASH);

        setVisible(true);
    }

    // Creates the top app bar with a centered title.
    private JPanel createAppBar() {
        JPanel appBar = new JPanel(new BorderLayout());
        appBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Shop N Track", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        appBar.add(titleLabel, BorderLayout.CENTER);

        return appBar;
    }

    // Method to switch to the dashboard page.
    public void showDashboard() {
        cardLayout.show(contentPanel, PAGE_DASHBOARD);
    }

    // Entry point
    public static void main(String[] args) {
        // Set the Nimbus look and feel for a modern appearance.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
