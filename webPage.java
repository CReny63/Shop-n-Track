import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class webPage extends JFrame {

    private Stack<JPanel> pageHistory = new Stack<>();
    private Stack<String> histTitle = new Stack<>();
    private String searchIn = "";

    // Constructor: Creates the frame and initializes modern settings.
    public webPage() {
        // Set Nimbus Look and Feel for a modern UI appearance
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

        // Enable anti-aliasing for smoother text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);  // Increased size for a more modern display
        setLocationRelativeTo(null);

        // Show the home page before making the frame visible
        showHomePage();
        setVisible(true);
    }

    public void setSearchIn(String in) {
        this.searchIn = in;
    }

    public String getSearchIn() {
        return searchIn;
    }

    // Switches panels by replacing the content pane and updating the title
    public void moveTo(JPanel panel, String pageName) {
        getContentPane().removeAll();
        setTitle(pageName);
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    // The following methods assume you have implemented these panel classes (HomePage, NewAccPage, etc.)
    public void showHomePage() {
        moveTo(new homePage(this), "Home Page");
    }

    public void newAccPage() {
        moveTo(new newAccPage(this), "New Account");
    }

    public void LogInPage() {
        moveTo(new LogInPage(this), "Log In");
    }

    public void cartPage() {
        moveTo(new cartPage(this), "Cart");
    }

    public void resultsPage() {
        moveTo(new resultsPage(this), "Results for: " + searchIn);
    }

    // History functionality (if you need to navigate back)
    public void addToHist(JPanel panel) {
        pageHistory.push(panel);
    }

    public void addPageName(String name) {
        histTitle.push(name);
    }

    public void prevPage() {
        if (!pageHistory.empty() && !histTitle.empty()) {
            getContentPane().removeAll();
            JPanel prev = pageHistory.pop();
            String name = histTitle.pop();
            setTitle(name);
            getContentPane().add(prev);
            revalidate();
            repaint();
        }
    }


}
