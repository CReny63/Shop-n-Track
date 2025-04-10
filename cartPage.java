import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cartPage extends JPanel {
    private String pageName = "Cart";
    private webPage parent;

    private GridBagConstraints c = new GridBagConstraints();

    public cartPage(webPage parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 255, 255));

        backButton();

    }

    public void backButton(){
        JButton backButton = new JButton("Back");
        c.gridx = 0;
        c.gridy = 1;
        add(backButton, c);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.prevPage();
            }
        });
    }
}
