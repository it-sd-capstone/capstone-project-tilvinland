package main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//TODO add a control panel to each side of the main control panels using border east and west for party and ship stats and save and load/main menu
//TODO set up random event panel and play buttons to re-roll random events.
//TODO
//TODO
//TODO First event

public class GameFrame extends JFrame {

    // Vars
    Ship gameShip = new Ship();
    Ship debugShip = new Ship();
    private static String currentPanel = "Main Menu";
    private static String previousPanel = "Main Menu";

    CardLayout cardLayout = new CardLayout();

    // Deck Panels
    JPanel deck = new JPanel();
    String MAIN = "Main Menu";
    String WELCOME = "Welcome Menu";
    String WELSHOP = "Welcome Shop";
    String STATS = "Ship Status";
    String DBSTATS = "Debug Status";
    String EVENT = "Event";
    String LOCATION = "Location";

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

    public GameFrame() throws SQLException {
        // Reference Main and Database
        Main main = new Main();
        Connection db = main.createConnection();

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

        JButton hpUpButton = new JButton("+5 Lumber(25 HP)");
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

        status.add(createBackButton());

        /* ------------ WELCOME SCREEN ------------ */
        JPanel welcomePanel = new JPanel(new BorderLayout());

        JPanel welcomeContent = new JPanel(new GridBagLayout());
        welcomeContent.setBackground(gunmetal);

        JPanel welcomeControls = new JPanel(new GridBagLayout());
        welcomeControls.setBackground(cerulean);

        JButton setSailButton = new JButton("Set Sail");
        setSailButton.setPreferredSize(new Dimension(200,50));
        setSailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.decideEvent(4,0);
            }
        });

        JButton welcomeShopButton = new JButton("Shop");
        welcomeShopButton.setPreferredSize(new Dimension(200,50));
        welcomeShopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runEvent(1);
            }
        });

        JLabel welcomeTitle = new JLabel();
        welcomeTitle.setText("TIL VINLAND");
        welcomeTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        welcomeTitle.setForeground(alertorange);

        JLabel welcomeImage = new JLabel();
        //Add image icons later

        JTextArea welcomeDescription = new JTextArea(5,40);
        welcomeDescription.setEditable(false);
        welcomeDescription.setHighlighter(null);
        welcomeDescription.setFocusable(false);
        welcomeDescription.setBackground(transparent);
        welcomeDescription.setLineWrap(true);
        welcomeDescription.setText("Set sail for the New Land or head into the Shop to grab supplies?");
        welcomeDescription.setForeground(alertorange);
        welcomeDescription.setFont(new Font("Monospaced", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomeContent.add(welcomeTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        welcomeContent.add(welcomeDescription, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        welcomeContent.add(welcomeImage, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomeControls.add(setSailButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        welcomeControls.add(welcomeShopButton, gbc);

        welcomePanel.add(welcomeContent);
        welcomePanel.add(welcomeControls, BorderLayout.SOUTH);

        /* ------------ WELCOME SHOP SCREEN ------------ */
        JPanel welcomeShopPanel = new JPanel(new BorderLayout());

        JPanel wShopContent = new JPanel(new GridBagLayout());
        wShopContent.setBackground(gunmetal);

        JPanel wShopControls = new JPanel(new GridBagLayout());
        wShopControls.setBackground(cerulean);
        wShopControls.setPreferredSize(new Dimension(200,200));

        JButton wShopRationsButton = new JButton("Buy Rations");
        wShopRationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runEvent(1);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        wShopControls.add(wShopRationsButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        wShopControls.add(createBackButton());

        welcomeShopPanel.add(wShopContent);
        welcomeShopPanel.add(wShopControls, BorderLayout.SOUTH);

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

        // Random generator for event name & description (Maybe needed to have them ref same ID)
        Random rng = new Random();
        int randomEvent = rng.nextInt(5) + 1;

        JLabel eventTitle = new JLabel();
        eventTitle.setText(main.fetchEvent(db,"name", randomEvent));
        eventTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        eventTitle.setForeground(alertorange);

        JLabel eventImage = new JLabel();
        //Add image icons later

        JTextArea eventDescription = new JTextArea(5,40);
        eventDescription.setEditable(false);
        eventDescription.setHighlighter(null);
        eventDescription.setFocusable(false);
        eventDescription.setBackground(transparent);
        eventDescription.setLineWrap(true);

        eventDescription.setText(main.fetchEvent(db,"desc", randomEvent));

        eventDescription.setForeground(emerald);

        eventOptionOne.setText("Option One");
        eventOptionTwo.setText("Option Two");
        eventOptionThree.setText("Option Three");
        eventOptionFour.setText("Option Four");

        eventControls.setPreferredSize(new Dimension(200,200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        eventControls.add(createDefaultControls(), gbc);
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
        gbc.gridwidth = 5;
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
        deck.add(welcomePanel,WELCOME);
        deck.add(status, STATS);
        deck.add(randEvent, EVENT);
        deck.add(welcomeShopPanel, WELSHOP);

        // Debug Panels
        deck.add(dbStats, DBSTATS);

        /* ------------ Event handling ------------ */
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String seedInputText = seedField.getText();

                if (seedInputText.equals("DEBUGSTATUS")) { //TODO Testing - remove after
                    switchToPanel(DBSTATS);
                } else if (seedInputText.equals("DEBUGEVENT")) { //TODO Testing - remove after
                    switchToPanel(EVENT);
                } else if (seedInputText.equals("")) { //User leaves the field blank
                    //Temporary, should run the game - switch to the first game screen
                    switchToPanel(WELCOME);
                } else { // The field has anything else entered or is blank (run with seed)

                }
            }
        });

        // Event Option Buttons
        eventOptionOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        });

        eventOptionTwo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        });

        eventOptionThree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        });

        eventOptionFour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {}
        });

        // See Ship Stats Button
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

    private JButton createBackButton() {
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel(previousPanel);
            }
        });
        return back;
    }

    private JPanel createDefaultControls() {
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel defaultControls = new JPanel(new GridBagLayout());

        JButton shipStatButton = new JButton(shipIcon);
        shipStatButton.setBounds(100,100,100,100);
        shipStatButton.setForeground(emerald);
        shipStatButton.setBackground(cerulean);
        shipStatButton.setBorder(BorderFactory.createEtchedBorder());
        shipStatButton.setPreferredSize(new Dimension(80,80));

        gbc.gridx = 0;
        gbc.gridy = 0;
        defaultControls.add(shipStatButton, gbc);

        shipStatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel(STATS);
            }
        });

        return defaultControls;
    }

    public void switchToPanel(String panelName) {
        if (!panelName.equals(currentPanel)) {
            previousPanel = currentPanel;
            currentPanel = panelName;
        }

        cardLayout.show(deck, panelName);
    }

}
