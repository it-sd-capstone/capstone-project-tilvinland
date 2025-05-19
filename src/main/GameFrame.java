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

    // Deck Panels
    JPanel deck = new JPanel();
    String MAIN = "Main Menu";
    String WELCOME = "Welcome Menu";
    String WELSHOP = "Welcome Shop";
    String STATS = "Ship Status";
    String DBSTATS = "Debug Status";
    String EVENT = "Event";
    String START = "Start Area";
    String SCOTLAND = "Scotland";
    String ICELAND = "Iceland";
    String GREENLAND = "Greenland";
    String VINLAND = "Vinland End";
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

        JLabel eventTitle = new JLabel();
        eventTitle.setText("Placeholder");
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
        eventDescription.setText("placeholder");
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


        /* ------------ LOCATION EVENT SCREEN ------------ */
        // Start Area Event
        JPanel startEvent = new JPanel(new BorderLayout());

        JPanel eventLocationControls = new JPanel(new GridBagLayout());
        eventLocationControls.setBackground(cerulean);

        JPanel startContent = new JPanel(new GridBagLayout());
        startContent.setBackground(gunmetal);

        JButton eventOptionShop = new JButton("Shop");
        eventOptionShop.setPreferredSize(new Dimension(200,50));

        JButton eventOptionInv = new JButton("Check Inventory");
        eventOptionInv.setPreferredSize(new Dimension(200,50));

        JButton eventOptionMembers = new JButton("Check Members");
        eventOptionMembers.setPreferredSize(new Dimension(200,50));

        JButton eventOptionTil = new JButton("Til Vinland");
        eventOptionTil.setPreferredSize(new Dimension(200,50));

        JLabel startEventTitle = new JLabel("Start Area"); // Change start area name?
        startEventTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        startEventTitle.setForeground(alertorange);

        JLabel startEventImage = new JLabel();
        //Add image icons later

        JTextArea startEventDescription = new JTextArea(10,44);
        startEventDescription.setEditable(false);
        startEventDescription.setHighlighter(null);
        startEventDescription.setFocusable(false);
        startEventDescription.setBackground(transparent);
        startEventDescription.setLineWrap(true);
        startEventDescription.setText("We are about to make a long voyage across the seas. " +
                "We better stock up on any supplies we will need for the long trip.");
        startEventDescription.setForeground(emerald);

        eventLocationControls.setPreferredSize(new Dimension(200,200));
        gbc.gridx = 1;
        gbc.gridy = 0;
        eventLocationControls.add(eventOptionShop, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        eventLocationControls.add(eventOptionInv, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        eventLocationControls.add(eventOptionMembers, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        eventLocationControls.add(eventOptionTil, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        startContent.add(startEventTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        startContent.add(startEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        startContent.add(startEventDescription, gbc);

        startEvent.add(startContent);
        startEvent.add(eventLocationControls,BorderLayout.SOUTH);

        // ScotLand Event
        JPanel scotEvent = new JPanel(new BorderLayout());

        JPanel scotContent = new JPanel(new GridBagLayout());
        scotContent.setBackground(gunmetal);

        JLabel scotTitle = new JLabel("Scotland");
        scotTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        scotTitle.setForeground(alertorange);

        JLabel scotEventImage = new JLabel();
        //Add image icons later

        JTextArea scotDescription = new JTextArea(10,44);
        scotDescription.setEditable(false);
        scotDescription.setHighlighter(null);
        scotDescription.setFocusable(false);
        scotDescription.setBackground(transparent);
        scotDescription.setLineWrap(true);
        scotDescription.setText("A Scottish Village can be seen on the coast and might " +
                "not be keen to see you near their village. We can stop here to repair our " +
                "ship or raid them for supplies. If we do raid them for supplies it might be a challenge.");
        scotDescription.setForeground(emerald);

        gbc.gridx = 0;
        gbc.gridy = 0;
        scotContent.add(scotTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        scotContent.add(scotEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        scotContent.add(scotDescription, gbc);

        scotEvent.add(scotContent);
        scotEvent.add(eventLocationControls,BorderLayout.SOUTH);

        // Iceland Event
        JPanel iceEvent = new JPanel(new BorderLayout());

        JPanel iceContent = new JPanel(new GridBagLayout());
        iceContent.setBackground(gunmetal);

        JLabel iceTitle = new JLabel("Iceland");
        iceTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        iceTitle.setForeground(alertorange);

        JLabel iceEventImage = new JLabel();
        //Add image icons later

        JTextArea iceDescription = new JTextArea(10,44);
        iceDescription.setEditable(false);
        iceDescription.setHighlighter(null);
        iceDescription.setFocusable(false);
        iceDescription.setBackground(transparent);
        iceDescription.setLineWrap(true);
        iceDescription.setText("An island village that welcomes you to their village. " +
                "This might be a good time to restock on supplies or repair any damage that the ship has sustained.");
        iceDescription.setForeground(emerald);

        gbc.gridx = 0;
        gbc.gridy = 0;
        iceContent.add(iceTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        iceContent.add(iceEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        iceContent.add(iceDescription, gbc);

        iceEvent.add(iceContent);
        iceEvent.add(eventLocationControls,BorderLayout.SOUTH);

        // Greenland Event
        JPanel greenEvent = new JPanel(new BorderLayout());

        JPanel greenContent = new JPanel(new GridBagLayout());
        greenContent.setBackground(gunmetal);

        JLabel greenTitle = new JLabel("Greenland");
        greenTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        greenTitle.setForeground(alertorange);

        JLabel greenEventImage = new JLabel();
        //Add image icons later

        JTextArea greenDescription = new JTextArea(10,44);
        greenDescription.setEditable(false);
        greenDescription.setHighlighter(null);
        greenDescription.setFocusable(false);
        greenDescription.setBackground(transparent);
        greenDescription.setLineWrap(true);
        greenDescription.setText("A rocky coast line where large animals can be seen from " +
                "the shore. We could stock up on more food or repair our ship for the voyage ahead.");
        greenDescription.setForeground(emerald);

        gbc.gridx = 0;
        gbc.gridy = 0;
        greenContent.add(greenTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        greenContent.add(greenEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        greenContent.add(greenDescription, gbc);

        greenEvent.add(greenContent);
        greenEvent.add(eventLocationControls,BorderLayout.SOUTH);

        // Vinland Event
        JPanel vinEvent = new JPanel(new BorderLayout());

        JPanel vinContent = new JPanel(new GridBagLayout());
        vinContent.setBackground(gunmetal);

        JLabel vinTitle = new JLabel("Vinland");
        vinTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        vinTitle.setForeground(alertorange);

        JLabel vinEventImage = new JLabel();
        //Add image icons later

        JTextArea vinDescription = new JTextArea(10,44);
        vinDescription.setEditable(false);
        vinDescription.setHighlighter(null);
        vinDescription.setFocusable(false);
        vinDescription.setBackground(transparent);
        vinDescription.setLineWrap(true);
        vinDescription.setText("Fresh lands where we are going to settle. Time to set " +
                "up a base camp so we can get working shelter and food so we can make it through the coming winter.");
        vinDescription.setForeground(emerald);

        gbc.gridx = 0;
        gbc.gridy = 0;
        vinContent.add(vinTitle, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        vinContent.add(vinEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        vinContent.add(vinDescription, gbc);

        vinEvent.add(vinContent);
        vinEvent.add(eventLocationControls,BorderLayout.SOUTH);

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

        // Location Based Events
        deck.add(startEvent, START);
        deck.add(scotEvent, SCOTLAND);
        deck.add(iceEvent, ICELAND);
        deck.add(greenEvent, GREENLAND);
        deck.add(vinEvent, VINLAND);

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
                } else if (seedInputText.equals("DEBUGSTART")) {
                    switchToPanel(VINLAND);
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

        // Location Event Options
        eventOptionShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                switchToPanel(SHOP);
            }
        });

        eventOptionInv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                switchToPanel(INV);
            }
        });

        eventOptionMembers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                switchToPanel(PARTY);
            }
        });

        eventOptionTil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel(EVENT); // Random Event?
            }
        });

        // Load button logic
        loadButton.addActionListener(e -> {
            Main.loadSave();
            // auto switches to next screen after loading
//            cardLayout.show(deck, PLAY);
        });

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
