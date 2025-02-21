import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class deckOfCards extends JFrame {
    private static ArrayList<ImageIcon> deck = new ArrayList<>();                                       //array list used to create deck of cards
    private static JPanel panel = new JPanel(new GridLayout(4, 13, 5, 5));        //new grid with 4 rows, 13 columns, horizontal gap = 5, vertical gap = 5

    public deckOfCards() {              //constructor to set up the User Interface
        setTitle("Deck of Cards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel.setBackground(new Color(65, 141, 65));        //sets background color

        createDeck();                       //creates deck from PNG-cards file
        printCards();                       //prints initial layout

        add(panel, BorderLayout.CENTER);    //adds panel with cards to the frame, in the center
        pack();                             //resize window to fit every card
        setLocationRelativeTo(null);        //opens window in the center of the screen
        setVisible(true);                   //makes window visible

        JButton randomize = new JButton("Randomize");              //creates new button labeled "Randomize"
        add(randomize, BorderLayout.SOUTH);                             //adds button to panel, on bottom of window
        randomize.addActionListener(e -> shuffleCards());    //pressing randomize activates action event e which calls shufflecards()


    }
    public static void createDeck() {
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};                                                 //array with suits
        String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};            //array with ranks
        for (String suit : suits) {                                                                                 //iterates through suits
            for (String rank : ranks) {                                                                             //iterates through ranks
                ImageIcon cardIcon = new ImageIcon("src/PNG-cards/" + rank + "_of_" + suit + ".png");       //gets png image from PNG-cards file

                deck.add(cardIcon);         //adds card to deck
            }

        }
    }
    public static void printCards(){
        panel.removeAll();                      //clears panel
        for (ImageIcon cardIcon : deck) {       //iterates through list of Icons (images)

            Image image = cardIcon.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);       //scales image: width = 100pxl; height = 140pxl
            cardIcon = new ImageIcon(image);                                                                     //converts image back to image icon

            JLabel cardLabel = new JLabel(cardIcon);        //create a label with Image icon
            panel.add(cardLabel);                           //add label to panel
        }
        panel.revalidate();         //ensures layout accuracy
        panel.repaint();            //ensures panel reflects changes
    }

    public static void shuffleCards(){
        Collections.shuffle(deck);      //shuffles arraylist deck
        printCards();                   //displays cards
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(deckOfCards::new);       //executes deckOfCards on Event Dispatch Thread

    }
}

