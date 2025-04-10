import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class newAccPage extends JPanel {

    private String pageName = "New Account";
    private String fname = "First Name";                   //store first name
    private String lname = "Last Name";                   //store last name
    private String email = "Email";                   //store email
    private String password = "Password";                //store password
    private GridBagConstraints c = new GridBagConstraints();//used for layout

    private webPage parent;

    public newAccPage(webPage parent) {
        this.parent = parent; //store reference to main frame
        setBackground(new Color(255, 255, 255));   //set background color
        setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        createTextBox(fname);
        createTextBox(lname);
        createTextBox(email);
        createTextBox(password);
        enterButton();
        backButton();

        setVisible(true);

    }

    public void createTextBox(String in){
        JTextField textBox = new JTextField((String)in, 10);

        add(textBox, c);
        in = textBox.getText();
        add(textBox, c);
        c.gridy++;
    }

    public void enterButton() {
        JButton enterButton = new JButton("Enter");
        c.gridx = 2;
        c.gridy = 2;
        add(enterButton, c);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send acc info to server, create new acc

            }
        });
    }

    public void backButton() {
        JButton backButton = new JButton("Back");
        c.gridx = 3;
        c.gridy = 2;
        add(backButton, c);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.prevPage();
                parent.addToHist(newAccPage.this);
                parent.addPageName(pageName);
            }
        });
    }

    //return acc info
}
