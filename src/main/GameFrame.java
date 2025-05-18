package main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//TODO add party panel
//TODO rework welcome shop to be a general use shop panel.

public class GameFrame extends JFrame {

    // Vars
    Ship ship = new Ship();
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
    String LOCATION = "Location";
    String PARTY = "Party";
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
    ImageIcon bigLogo = new ImageIcon("./resources/logo.png");
    ImageIcon smallLogo = new ImageIcon("./resources/logo_small.png");
    ImageIcon tinyLogo = new ImageIcon("./resources/logo_tiny.png");
    ImageIcon shipIcon = new ImageIcon("./resources/shipIcon.jpg");

    ImageIcon partyOneLogo = new ImageIcon("./resources/PartyIcons/partyOne.png");
    ImageIcon partyTwoLogo = new ImageIcon("./resources/PartyIcons/partyTwo.png");
    ImageIcon partyThreeLogo = new ImageIcon("./resources/PartyIcons/partyThree.png");
    ImageIcon partyFourLogo = new ImageIcon("./resources/PartyIcons/partyFour.png");

    //Boarder Decos need to be smaller
    ImageIcon borderDecoTL = new ImageIcon("./resources/CornerTopLeft.png");
    ImageIcon borderDecoTR = new ImageIcon("./resources/CornerTopRight.png");

    public GameFrame() throws SQLException {
        // Reference Main and Database
        Main main = new Main();
        Connection db = main.createConnection();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // set CardLayout to the deck panel layout manager
        deck.setLayout(cardLayout);

        /* ------------ Main Menu Panel ------------ */
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BorderLayout());
        mainMenu.setBackground(darkslate);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10);

        JPanel menuContent = new JPanel();
        menuContent.setLayout(new GridBagLayout());
        menuContent.setBackground(gunmetal);

        JLabel leftCornerDeco = new JLabel(borderDecoTL);
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        menuContent.add(leftCornerDeco, gbcMain);

        JLabel rightCornerDeco = new JLabel(borderDecoTR);
        gbcMain.gridx = 2;
        gbcMain.gridy = 0;
        menuContent.add(rightCornerDeco, gbcMain);

        JLabel gameTitle = new JLabel("Til Vinland");
        gameTitle.setForeground(alertorange);
        gameTitle.setPreferredSize(new Dimension(400, 200));
        gameTitle.setText("Til Vinland!");
        gameTitle.setFont(new Font("Monospaced", Font.PLAIN, 50));
        gameTitle.setForeground(columbiablue);
        gameTitle.setVerticalAlignment(JLabel.CENTER);
        gameTitle.setHorizontalAlignment(JLabel.CENTER);
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        menuContent.add(gameTitle, gbcMain);

        JPanel menuOptions = new JPanel();
        menuOptions.setLayout(new GridBagLayout());
        menuOptions.setBackground(cerulean);
        menuOptions.setPreferredSize(new Dimension(200,200));

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

        // Row 0: JTextField spanning 2 columns
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 2; // Span across both button columns
        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        menuOptions.add(seedField, gbcMain);

        // Row 1, Column 0: Play
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1; // Reset width
        gbcMain.fill = GridBagConstraints.NONE;
        menuOptions.add(playButton, gbcMain);

        // Row 1, Column 1: Load
        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        menuOptions.add(loadButton, gbcMain);

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

        /* ------------ Ship/Party Status Panel ------------ */
        //TODO add a ship repair button
        //TODO add a heal party button??
        //TODO add some art!
        //TODO grab party names and stats
        GridBagConstraints gbcStatus = new GridBagConstraints();
        gbcStatus.insets = new Insets(10, 10, 10, 10);

        JPanel status = new JPanel();
        status.setLayout(new BorderLayout());

        JPanel statusContent = new JPanel();
        statusContent.setLayout(new GridBagLayout());
        statusContent.setBackground(gunmetal);

        JPanel statusControls = new JPanel();
        statusControls.setLayout(new GridBagLayout());
        statusControls.setBackground(cerulean);
        statusControls.setPreferredSize(new Dimension(200,200));

        JLabel shipHealthText = new JLabel();
        shipHealthText.setText("Ship Health: " + ship.getHealth());

        JLabel shipStatText = new JLabel();
        shipStatText.setText("Ship Status: " + ship.getStatus());

        gbcStatus.gridx = 0;
        gbcStatus.gridy = 0;
        statusControls.add(createBackButton());

        status.add(statusControls,BorderLayout.SOUTH);
        status.add(statusContent);

        /* ------------ Party Creation Panel ------------ */
        JPanel party = new JPanel();
        party.setLayout(new BorderLayout());

        GridBagConstraints gbcParty = new GridBagConstraints();
        gbcParty.insets = new Insets(10, 30, 10, 30);

        JPanel partyContent = new JPanel();
        partyContent.setLayout(new GridBagLayout());
        partyContent.setBackground(gunmetal);

        JPanel partyControls = new JPanel();
        partyControls.setLayout(new GridBagLayout());
        partyControls.setBackground(cerulean);
        partyControls.setPreferredSize(new Dimension(200,200));

        JTextField partyOneField = new JTextField();
        partyOneField.setPreferredSize(new Dimension(200, 24));
        JTextField partyTwoField = new JTextField();
        partyTwoField.setPreferredSize(new Dimension(200, 24));
        JTextField partyThreeField = new JTextField();
        partyThreeField.setPreferredSize(new Dimension(200, 24));
        JTextField partyFourField = new JTextField();
        partyFourField.setPreferredSize(new Dimension(200, 24));

        JLabel partyOneIcon = new JLabel(partyOneLogo);
        JLabel partyTwoIcon = new JLabel(partyTwoLogo);
        JLabel partyThreeIcon = new JLabel(partyThreeLogo);
        JLabel partyFourIcon = new JLabel(partyFourLogo);

        gbcParty.gridx = 0;
        gbcParty.gridy = 0;
        partyContent.add(partyOneIcon, gbcParty);
        gbcParty.gridx = 0;
        gbcParty.gridy = 1;
        partyContent.add(partyOneField, gbcParty);
        gbcParty.gridx = 1;
        gbcParty.gridy = 0;
        partyContent.add(partyTwoIcon, gbcParty);
        gbcParty.gridx = 1;
        gbcParty.gridy = 1;
        partyContent.add(partyTwoField, gbcParty);
        gbcParty.gridx = 0;
        gbcParty.gridy = 2;
        partyContent.add(partyThreeIcon, gbcParty);
        gbcParty.gridx = 0;
        gbcParty.gridy = 3;
        partyContent.add(partyThreeField, gbcParty);
        gbcParty.gridx = 1;
        gbcParty.gridy = 2;
        partyContent.add(partyFourIcon, gbcParty);
        gbcParty.gridx = 1;
        gbcParty.gridy = 3;
        partyContent.add(partyFourField, gbcParty);

        JButton partyContinueButton = new JButton();
        partyContinueButton.setText("Continue");
        partyContinueButton.setPreferredSize(new Dimension(200,50));
        partyContinueButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        partyContinueButton.setForeground(alertorange);
        partyContinueButton.setBackground(buttonbrown);
        partyContinueButton.setBorder(BorderFactory.createEtchedBorder());

        gbcParty.gridx = 0;
        gbcParty.gridy = 0;
        partyControls.add(partyContinueButton, gbcParty);

        party.add(partyControls, BorderLayout.SOUTH);
        party.add(partyContent);

        partyContinueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String partyNameOneInput = partyOneField.getText();
                String partyNameTwoInput = partyTwoField.getText();
                String partyNameThreeInput = partyThreeField.getText();
                String partyNameFourInput = partyFourField.getText();

                //TODO take out active int w/ stevens code change to Main.createParty()
                Main.createParty(partyNameOneInput, 0, 1);
                Main.createParty(partyNameTwoInput, 0, 1);
                Main.createParty(partyNameThreeInput, 0,1);
                Main.createParty(partyNameFourInput,0, 1);
            }
        });

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
        deck.add(party, PARTY);

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
                } else if (seedInputText.equals("DEBUGPARTY")) { //TODO Testing - remove after
                    switchToPanel(PARTY);
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

        // Load button logic
        loadButton.addActionListener(e -> {
            Main.loadSave();
            // auto switches to next screen after loading
            switchToPanel(WELCOME);
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
