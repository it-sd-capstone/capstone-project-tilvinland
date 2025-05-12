package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame {

    // Vars
    Ship gameShip = new Ship();
    Ship debugShip = new Ship();

    // Deck Panels
    JPanel deck = new JPanel();
    String MAIN = "Main Menu";
    String PLAY = "Play Menu";
    String STATS = "Ship Status";
    String DBSTATS = "Debug Status";
    String EVENT = "Event";
    String LOCATION = "Location";
    CardLayout cardLayout = new CardLayout();

    //Custom Colors
    private Color emerald = new Color(105,220,158);
    private Color gunmetal = new Color(37,48,49);
    private Color darkslate = new Color(49,86,89 );
    private Color cerulean = new Color(41,120,160);
    private Color columbiablue = new Color(198,224,255);
    private Color alertorange = new Color(227,159,78);
    private Color buttonbrown = new Color(66,39,15);
    private Color transparent = new Color(0,0,0,0);

    // Resources
    ImageIcon tinyLogo = new ImageIcon("./resources/logo_tiny.png");
    ImageIcon shipIcon = new ImageIcon("./resources/shipIcon.jpg");

    //Boarder Decos need to be smaller
    //ImageIcon borderDecoTL = new ImageIcon("./resources/CornerTopLeft.png");
    //ImageIcon borderDecoTR = new ImageIcon("./resources/CornerTopRight.png");


    public GameFrame() {

        GridBagConstraints gbc = new GridBagConstraints();

        // set CardLayout to the deck panel layout manager
        deck.setLayout(cardLayout);

        /* ------------ Main Menu Panel ------------ */
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BorderLayout());
        mainMenu.setBackground(darkslate);

        JPanel menuContent = new JPanel();
        menuContent.setLayout(new GridBagLayout());
        menuContent.setBackground(gunmetal);

//        JLabel leftCornerDeco = new JLabel(borderDecoTL);
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        menuContent.add(leftCornerDeco, gbc);
//
//        JLabel rightCornerDeco = new JLabel(borderDecoTR);
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        menuContent.add(rightCornerDeco, gbc);

        JPanel menuOptions = new JPanel();
        menuOptions.setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel mainText = new JLabel();
        mainText.setText("Til Vinland!");
        mainText.setFont(new Font("Monospaced", Font.PLAIN, 40));
        mainText.setForeground(columbiablue);
        mainText.setVerticalAlignment(JLabel.CENTER);
        mainText.setHorizontalAlignment(JLabel.CENTER);

        JButton playButton = new JButton();
        playButton.setBounds(100,100,100,100);
        playButton.setText("PLAY");
        playButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        playButton.setForeground(alertorange);
        playButton.setBackground(buttonbrown);
        playButton.setBorder(BorderFactory.createEtchedBorder());
        playButton.setPreferredSize(new Dimension(200,100));
        playButton.setMargin(new Insets(0, 0, 0, 0));
        playButton.setBorder(null);

        JButton loadButton = new JButton();
        loadButton.setBounds(100,100,250,100);
        loadButton.setText("LOAD");
        loadButton.setFont(new Font("Monospaced", Font.BOLD, 40));
        loadButton.setForeground(alertorange);
        loadButton.setBackground(buttonbrown);
        loadButton.setBorder(BorderFactory.createEtchedBorder());
        loadButton.setPreferredSize(new Dimension(200,100));
        loadButton.setToolTipText("Load most recent game");

        JTextField seedField = new JTextField();
        seedField.setFont(new Font("Monospaced", Font.PLAIN, 20));
        seedField.setPreferredSize(new Dimension(400, 40));
        seedField.setToolTipText("Enter seed for game generation or leave blank for a random seed.");

        menuOptions.setBackground(cerulean);
        menuOptions.setPreferredSize(new Dimension(200,200));

        // Row 0: JTextField spanning 2 columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both button columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menuOptions.add(seedField, gbc);

        // Row 1, Column 0: Play
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset width
        gbc.fill = GridBagConstraints.NONE;
        menuOptions.add(playButton, gbc);

        // Row 1, Column 1: Load
        gbc.gridx = 1;
        gbc.gridy = 1;
        menuOptions.add(loadButton, gbc);

        mainMenu.add(menuOptions,BorderLayout.SOUTH);
        mainMenu.add(menuContent,BorderLayout.CENTER);

        /* ############ Debug/Testing Panels ############ */
        //TODO ### REMOVE - TESTING ONLY ###
        // NOT FOR PRODUCTION - REMOVE AFTER TESTING
        JPanel dbStats = new JPanel();
        dbStats.setLayout(new GridBagLayout());
        JLabel dbHealthText = new JLabel();
        dbHealthText.setText("Ship Health: " + debugShip.getHealth());

        JLabel dbStatText = new JLabel();
        dbStatText.setText("Ship Status: " + debugShip.getStatus());

        JButton hpUpButton = new JButton("+5 Lumber(20 HP)");
        hpUpButton.setPreferredSize(new Dimension(200,50));
        JButton hpDownButton = new JButton("-20 HP");
        hpDownButton.setPreferredSize(new Dimension(200,50));

        gbc.gridx = 0;
        gbc.gridy = 0;
        dbStats.add(dbHealthText, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        dbStats.add(dbStatText, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        dbStats.add(hpUpButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        dbStats.add(hpDownButton, gbc);

        hpUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                debugShip.addHealth(5);
                dbHealthText.setText("Ship Health: " + debugShip.getHealth());
                dbStatText.setText("Ship Status: " + debugShip.getStatus());
            }
        });

        hpDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                debugShip.removeHealth(20);
                dbHealthText.setText("Ship Health: " + debugShip.getHealth());
                dbStatText.setText("Ship Status: " + debugShip.getStatus());
            }
        });


        /* ------------ Ship Status Panel ------------ */
        JPanel status = new JPanel();
        JLabel healthText = new JLabel();
        healthText.setText("Ship Health: " + gameShip.getHealth());

        JLabel statText = new JLabel();
        statText.setText("Ship Status: " + gameShip.getStatus());

        /* ------------ MAIN PLAY SCREEN ------------ */
        JPanel playPanel = new JPanel();
        playPanel.setLayout(new BorderLayout());

        JButton shipStatButton = new JButton(shipIcon);
        shipStatButton.setBounds(100,100,100,100);
        //shipStatButton.setText("Ship");
        //shipStatButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        shipStatButton.setForeground(emerald);
        shipStatButton.setBackground(cerulean);
        shipStatButton.setBorder(BorderFactory.createEtchedBorder());
        shipStatButton.setPreferredSize(new Dimension(80,80));

        JPanel playMenuContent = new JPanel();

        JPanel playMenuControls = new JPanel();
        playMenuControls.setBackground(cerulean);
        playMenuControls.setPreferredSize(new Dimension(200,200));

        playPanel.add(playMenuContent);
        playPanel.add(playMenuControls,BorderLayout.SOUTH);

        playMenuControls.add(shipStatButton);

        /* ------------ RANDOM EVENT SCREEN ------------ */
        JPanel randEvent = new JPanel(new BorderLayout());

        JPanel eventControls = new JPanel(new GridBagLayout());
        eventControls.setBackground(cerulean);

        JPanel eventContent = new JPanel(new GridBagLayout());
        eventContent.setBackground(gunmetal);

        JButton eventOptionOne = new JButton();
        eventOptionOne.setPreferredSize(new Dimension(200,50));

        JButton eventOptionTwo = new JButton();
        eventOptionTwo.setPreferredSize(new Dimension(200,50));

        JButton eventOptionThree = new JButton();
        eventOptionThree.setPreferredSize(new Dimension(200,50));

        JButton eventOptionFour = new JButton();
        eventOptionFour.setPreferredSize(new Dimension(200,50));

        JLabel eventTitle = new JLabel();
        eventTitle.setText("Placeholder");
        eventTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        eventTitle.setForeground(alertorange);

        JLabel eventImage = new JLabel();
        //Add image icons later

        JTextArea eventDescription = new JTextArea();
        eventDescription.setEditable(false);
        eventDescription.setHighlighter(null);
        eventDescription.setFocusable(false);
        eventDescription.setBackground(transparent);
        eventDescription.setLineWrap(true);
        eventDescription.setText("PLACEHOLDER DESCRIPTION");
        eventDescription.setForeground(emerald);

        eventOptionOne.setText("Option One");
        eventOptionTwo.setText("Option Two");
        eventOptionThree.setText("Option Three");
        eventOptionFour.setText("Option Four");

        eventControls.setPreferredSize(new Dimension(200,200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        eventControls.add(shipStatButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        eventControls.add(eventOptionOne, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        eventControls.add(eventOptionTwo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        eventControls.add(eventOptionThree, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        eventControls.add(eventOptionFour, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        eventContent.add(eventTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        eventContent.add(eventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        eventContent.add(eventDescription, gbc);

        randEvent.add(eventContent);
        randEvent.add(eventControls,BorderLayout.SOUTH);

        /* ------------ Frame Parameters ------------ */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 720);
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setMinimumSize(new Dimension(720, 576));
        this.setTitle("Til Vinland");
        this.setIconImage(tinyLogo.getImage());

        // Add cards to deck panel
        deck.add(mainMenu,MAIN);
        deck.add(playPanel,PLAY);
        deck.add(status, STATS);
        deck.add(randEvent, EVENT);

        // Debug Panels
        deck.add(dbStats, DBSTATS);

        /* ------------ Event handling ------------ */
        //playButton.addActionListener(e -> cardLayout.show(deck, PLAY));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String seedInputText = seedField.getText();

                if (seedInputText.equals("DEBUGSTATUS")) { //TODO Testing - remove after
                    cardLayout.show(deck, DBSTATS);
                } else if (seedInputText.equals("DEBUGEVENT")) { //TODO Testing - remove after
                    cardLayout.show(deck, EVENT);
                } else if (seedInputText.equals("")) { //User leaves the field blank
                    //Temporary, should run the game
                    cardLayout.show(deck, PLAY);
                } else { // The field has anything else entered or is blank (run with seed)

                }

            }
        });

        shipStatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(deck, STATS);
            }
        });

        //TODO Should load the last game in progress
        loadButton.addActionListener(e -> System.out.println("Error: No save game found"));

        // Add deck panel to the frame
        this.add(deck);

        /* !!! KEEP LAST !!! */
        this.setVisible(true);

    }
}