import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInPage extends JPanel {
    private String pageName = "Log In Page";
    private String email = "email";
    private String password = "password";

    private GridBagConstraints c = new GridBagConstraints();
    private webPage parent;

    public LogInPage(webPage parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 255, 255));
        c.gridx = 0;
        c.gridy = 0;

        addTextBox(email);
        addTextBox(password);
        addVerticalSpace();
        backButton();
        enterButton();
    }

    public void addTextBox(String in){
        JTextField textBox = new JTextField(in, 10);

        add(textBox, c);
        in = textBox.getText();

        c.gridx++;
    }

    public void backButton(){
        JButton backButton = new JButton("Back");
        c.gridx = 0;
        c.gridy = 1;
        add(backButton, c);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.prevPage();
                parent.addToHist(LogInPage.this);
                parent.addPageName(pageName);
            }
        });
    }

    public void addVerticalSpace(){
        JPanel spacer = new JPanel();   //sets space between obects
        spacer.setOpaque(false);//set transparent
        spacer.setPreferredSize(new Dimension(0, 100));
        c.fill = GridBagConstraints.HORIZONTAL;
        add(spacer, c);
    }

    public void enterButton(){
        JButton enterButton = new JButton("Enter");
        c.gridx = 1;
        c.gridy = 1;
        add(enterButton, c);

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //verify acc
                //report error
                //cont to search page logged in
            }
        });
    }
}
