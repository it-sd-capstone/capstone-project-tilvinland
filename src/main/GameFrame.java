package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    //TODO Implement a grid layout to allow more flexibility for component positions in a panels
    //TODO Add a text field to allow input of a seed before pressing playButton on mainMenu>menuOptions
    //TODO Restructure event listeners to use overrides for more control over button events

    // Deck Panel
    JPanel deck = new JPanel();
    String MAIN = "Main Menu";
    String PLAY = "Play Menu";

    // Add in resources
    ImageIcon tinyLogo = new ImageIcon("./lib/resources/logo_tiny.png");

    public GameFrame() {

        // set CardLayout to the deck panel layout manager
        CardLayout cardLayout = new CardLayout();
        deck.setLayout(cardLayout);

        /* ------------ Main Menu Panel ------------ */
        JPanel mainMenu = new JPanel();
        JPanel menuOptions = new JPanel();
        mainMenu.setLayout(new BorderLayout());
        mainMenu.setBackground(Color.lightGray);

        JLabel mainText = new JLabel();
        mainText.setText("Til Vinland!");
        mainText.setFont(new Font("Monospaced", Font.PLAIN, 40));
        mainText.setVerticalAlignment(JLabel.CENTER);
        mainText.setHorizontalAlignment(JLabel.CENTER);

        JButton playButton = new JButton();
        playButton.setBounds(100,100,100,100);
        playButton.setText("PLAY");
        playButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        playButton.setForeground(Color.green);
        playButton.setBackground(Color.lightGray);
        playButton.setBorder(BorderFactory.createEtchedBorder());
        playButton.setPreferredSize(new Dimension(200,100));

        JButton loadButton = new JButton();
        loadButton.setBounds(100,100,250,100);
        loadButton.setText("LOAD");
        loadButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        loadButton.setForeground(Color.blue);
        loadButton.setBackground(Color.lightGray);
        loadButton.setBorder(BorderFactory.createEtchedBorder());
        loadButton.setPreferredSize(new Dimension(200,100));

        menuOptions.setBackground(Color.darkGray);
        menuOptions.setPreferredSize(new Dimension(200,200));
        menuOptions.add(playButton);
        menuOptions.add(loadButton);


        mainMenu.add(menuOptions,BorderLayout.SOUTH);
        mainMenu.add(mainText,BorderLayout.CENTER);

        /* ------------ Party Status Panel ------------ */
        JPanel status = new JPanel();

        /* ------------ TEMPORARY PLAY SCREEN ------------ */
        JPanel testPlay = new JPanel();

        /* ------------ Frame Parameters ------------ */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setMinimumSize(new Dimension(720, 576));
        this.setTitle("Til Vinland");
        this.setIconImage(tinyLogo.getImage());

        // Add cards to deck panel
        deck.add(mainMenu,MAIN);
        deck.add(testPlay,PLAY);

        // Event handling
        playButton.addActionListener(e -> cardLayout.show(deck, PLAY));
        loadButton.addActionListener(e -> System.out.println("Error: No save game found"));

        // Add deck panel to the frame
        this.add(deck);

        /* !!! KEEP LAST !!! */
        this.setVisible(true);

    }
}