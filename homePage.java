import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class homePage extends JPanel {
    private String pageName = "Home Page";
    private GridBagConstraints c = new GridBagConstraints();
    private webPage parent;

    public homePage(webPage parent) {
        this.parent = parent;   //assign reference
        setLayout(new GridBagLayout());
        setBackground(new Color(72, 124, 112));

        c.insets = new Insets(10, 10, 10, 10);


        addTitle();         //adds title to page
        //createTextBox();    //sends request for results page
        createLogin();      //sends request for login page
        createAccount();    //sends request for new account page

    }

    private void addTitle() {
        JLabel brand = new JLabel("Shop n Track", SwingConstants.CENTER);
        brand.setFont(new Font("Arial", Font.BOLD, 24));
        c.gridx = 1;         // center column
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        add(brand, c);
    }

    //creates the search box, and button
    public void createTextBox() {
        JTextField textBox = new JTextField("Enter Text", 20);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        add(textBox, c);

        JButton searchButton = new JButton("Enter");
        c.gridx = 2;
        c.gridy = 2;
        add(searchButton, c);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call results page or error
                parent.addToHist(homePage.this);
                parent.addPageName(pageName);
                parent.setSearchIn(textBox.getText()); //stores text in "searchText"
                parent.resultsPage();
            }
        });
    }

    public void createLogin() {
        JPanel spacer = new JPanel();   //sets space between obects
        spacer.setOpaque(false);//set transparent
        spacer.setPreferredSize(new Dimension(0, 100));
        c.fill = GridBagConstraints.HORIZONTAL;
        add(spacer, c);

        JButton login = new JButton("Login");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 3;
        add(login, c);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                parent.addPageName(pageName);           //add page name to history page stack
                parent.addToHist(homePage.this);  //add panel to history panel stack
                parent.LogInPage();
            }
        });
    }

    public void createAccount() {
        JButton newAccount = new JButton("Create Account");
        c.gridx = 1;
        c.gridy = 4;
        add(newAccount, c);

        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //directs to create account page
                parent.addPageName(pageName);   //add page name to history page stack
                parent.addToHist(homePage.this);     //add panel to history panel stack
                parent.newAccPage();            //go to new acc page
            }
        });
    }

}


