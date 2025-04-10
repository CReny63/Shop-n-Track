import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class resultsPage extends JPanel {

    private String pageName = "Results Page";
    private GridBagConstraints c = new GridBagConstraints();
    private webPage parent;

    public resultsPage(webPage parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 255, 255));

        //error case no results
        //display result

        //sortByPrice - Low to High, high to low
        //searchBy Store
        viewCart();
        backButton();
    }

    public void viewCart() {
        JButton viewCart = new JButton("Cart");
        c.anchor = GridBagConstraints.FIRST_LINE_END; //set to top right
        c.gridx = 2;
        c.gridy = 0;
        add(viewCart, c);

        viewCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                parent.addToHist(resultsPage.this);
                parent.addPageName(pageName);
                parent.cartPage();
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
                parent.addToHist(resultsPage.this);
                parent.addPageName(pageName);
            }
        });
    }

}
