package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

//<editor-fold desc="Test">
// </editor-fold>

//TODO add party panel
//TODO rework welcome shop to be a general use shop panel.

public class GameFrame extends JFrame {

    // Vars
    Ship ship = Main.getShip();
    Ship debugShip = new Ship();
    private static String currentPanel = "Main Menu";
    private static String previousPanel = "Main Menu";

    //<editor-fold desc="Panel Vars">
    // Deck Panels
    JPanel deck = new JPanel();
    String MAIN = "Main Menu";
    String WELCOME = "Welcome Menu";
    String SHOP = "Shop";
    String STATS = "Ship Status";
    String DBSTATS = "Debug Status";
    String EVENT = "Event";

    String START = "Start";
    String SCOTLAND = "Scotland";
    String ICELAND = "Iceland";
    String GREENLAND = "Greenland";
    String VINLAND = "Vinland End";
    String LOCATION = "Location";
    String PARTY = "Party";
    String COMBAT = "Combat";
    String FCOMBAT = "Finish Combat";
    String CALM = "CALM";
    String ROUGH = "ROUGH";
    String STORM = "STORM";
    String VILLAGE = "VILLAGE";
    String FOREST = "FOREST";
    String CONFRIM = "Resource Confirmed";
    String WIPE = "Party Wipe";
    String SANK = "Ship Sank";

    CardLayout cardLayout = new CardLayout();
    // </editor-fold>

    //Custom Colors
    private final Color emerald = new Color(105,220,158);
    private final Color gunmetal = new Color(37,48,49);
    private final Color darkslate = new Color(49,86,89 );
    private final Color cerulean = new Color(41,120,160);
    private final Color columbiablue = new Color(198,224,255);
    private final Color alertorange = new Color(227,159,78);
    private final Color buttonbrown = new Color(66,39,15);
    private final Color transparent = new Color(0,0,0,0);

    //<editor-fold desc="Resources">
    // Resources
    ImageIcon bigLogo = new ImageIcon("./resources/logo.png");
    ImageIcon smallLogo = new ImageIcon("./resources/logo_small.png");
    ImageIcon tinyLogo = new ImageIcon("./resources/logo_tiny.png");
    ImageIcon shipIcon = new ImageIcon("./resources/shipIcon.jpg");

    ImageIcon partyOneLogo = new ImageIcon("./resources/PartyIcons/partyOne.png");
    ImageIcon partyTwoLogo = new ImageIcon("./resources/PartyIcons/partyTwo.png");
    ImageIcon partyThreeLogo = new ImageIcon("./resources/PartyIcons/partyThree.png");
    ImageIcon partyFourLogo = new ImageIcon("./resources/PartyIcons/partyFour.png");
    ImageIcon partyInactiveLogo = new ImageIcon("./resources/PartyIcons/partyInactive.png");

    ImageIcon borderDecoTL = new ImageIcon("./resources/CornerTopLeft.png");
    ImageIcon borderDecoTR = new ImageIcon("./resources/CornerTopRight.png");

    ImageIcon lumberIcon = new ImageIcon("./resources/lumberPile.png");

    ImageIcon shipConditionPerfect = new ImageIcon("./resources/ShipDamageImages/Perfect.png");
    ImageIcon shipConditionGreat = new ImageIcon("./resources/ShipDamageImages/Great.png");
    ImageIcon shipConditionDamaged = new ImageIcon("./resources/ShipDamageImages/Damaged.png");
    ImageIcon shipConditionSevere = new ImageIcon("./resources/ShipDamageImages/Severe.png");
    ImageIcon shipConditionSank = new ImageIcon("./resources/ShipDamageImages/Sank.png");
    // </editor-fold>

    //Globals
    private String rescourceLabel = "";
    private JLabel shipStatusImage = new JLabel();
    public static int eventID = 0;

    ObjectMapper mapper = new ObjectMapper();
    TestDat testDat = new TestDat();
    File saveFile = new File("./resources/Data/testDat.json");

    private JButton eventOptionOne = new JButton();
    private JButton eventOptionTwo = new JButton();
    private JButton eventOptionThree = new JButton();
    private JButton eventOptionFour = new JButton();
    private JLabel eventTitle = new JLabel();
    private String eventTitleString;
    private JLabel eventImage = new JLabel();
    //Add image icons later
    private JTextArea eventDescription = new JTextArea(5,40);

    public GameFrame() {
        // Reference Main and Database
        //Main main = new Main();
        Connection db = Main.createConnection();

        final String[] labelText = {""};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // set CardLayout to the deck panel layout manager
        deck.setLayout(cardLayout);

        //<editor-fold desc="Main Menu Panel">
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
// </editor-fold>

        //<editor-fold desc="DEBUG PANELS">
        /* ############ Debug/Testing Panels ############ */
        //TODO ### REMOVE - TESTING ONLY ###
        // NOT FOR PRODUCTION - REMOVE AFTER TESTING
        JPanel dbStats = new JPanel();
        dbStats.setLayout(new GridBagLayout());
        JLabel dbHealthText = new JLabel();
        dbHealthText.setText("Ship Health: " + debugShip.getHealth());

        JLabel dbStatText = new JLabel();
        dbStatText.setText("Ship Status: " + debugShip.getStatus());

        JLabel dbStatsImage = new JLabel();
        dbStatsImage.setIcon(shipConditionPerfect);

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
        gbc.gridx = 1;
        gbc.gridy = 1;
        dbStats.add(dbStatsImage, gbc);
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
                if (debugShip.getStatus() == "Perfect Condition"){
                    dbStatsImage.setIcon(shipConditionPerfect);
                } else if (debugShip.getStatus() == "Great Condition") {
                    dbStatsImage.setIcon(shipConditionGreat);
                } else if (debugShip.getStatus() == "Damaged") {
                    dbStatsImage.setIcon(shipConditionDamaged);
                } else if (debugShip.getStatus() == "Severe Damage") {
                    dbStatsImage.setIcon(shipConditionSevere);
                } else if (debugShip.getStatus() == "Sank") {
                    dbStatsImage.setIcon(shipConditionSank);
                }

                //TODO json I/O save/load test
                //testDat.setDebugHealth(debugShip.getHealth());
                try {
                    TestDat modifyShipHealth = mapper.readValue(saveFile, TestDat.class);

                    System.out.println("JSON file read successfully.");
                    System.out.println("Original Seed in save file: " + modifyShipHealth.getDebugHealth());
                    System.out.println("Original DB Ship Health: " + modifyShipHealth.getDebugHealth());

                    modifyShipHealth.setDebugHealth(debugShip.getHealth());
                    mapper.writeValue(saveFile, modifyShipHealth);
                    System.out.println("JSON file updated successfully.");

                    TestDat readShipHealth = mapper.readValue(saveFile, TestDat.class);
                    System.out.println("DB Ship Health: " + readShipHealth.getDebugHealth());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }

            }
        });

        hpDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                debugShip.removeHealth(20);
                dbHealthText.setText("Ship Health: " + debugShip.getHealth());
                dbStatText.setText("Ship Status: " + debugShip.getStatus());

                if (debugShip.getStatus() == "Perfect Condition"){
                    dbStatsImage.setIcon(shipConditionPerfect);
                } else if (debugShip.getStatus() == "Great Condition") {
                    dbStatsImage.setIcon(shipConditionGreat);
                } else if (debugShip.getStatus() == "Damaged") {
                    dbStatsImage.setIcon(shipConditionDamaged);
                } else if (debugShip.getStatus() == "Severe Damage") {
                    dbStatsImage.setIcon(shipConditionSevere);
                } else if (debugShip.getStatus() == "Sank") {
                    dbStatsImage.setIcon(shipConditionSank);
                }

                //TODO json I/O save/load test
                try {
                    TestDat modifyShipHealth = mapper.readValue(saveFile, TestDat.class);

                    System.out.println("JSON file read successfully.");
                    System.out.println("Original Seed in save file: " + modifyShipHealth.getDebugHealth());
                    System.out.println("Original DB Ship Health: " + modifyShipHealth.getDebugHealth());

                    modifyShipHealth.setDebugHealth(debugShip.getHealth());
                    System.out.println("New Seed:" + modifyShipHealth.getSeedInput());

                    mapper.writeValue(saveFile, modifyShipHealth);
                    System.out.println("JSON file updated successfully.");

                    TestDat readShipHealth = mapper.readValue(saveFile, TestDat.class);
                    System.out.println("DB Ship Health: " + readShipHealth.getDebugHealth());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }
            }
        });
// </editor-fold>

        //<editor-fold desc="Ship/Party Stats Panel">
        /* ------------ Ship/Party Status Panel ------------ */
        //TODO add a ship repair button
        //TODO add a heal party button?? (-rations)
        //TODO add some art!
        //TODO grab party names and stats
        GridBagConstraints gbcStatusPanel = new GridBagConstraints();
        gbcStatusPanel.insets = new Insets(10, 10, 10, 10);

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
        shipHealthText.setText("Ship Health: " + Main.getShip().getHealth());
        shipHealthText.setForeground(alertorange);

        JLabel shipStatText = new JLabel();
        shipStatText.setText("Ship Status: " + Main.getShip().getStatus());
        shipStatText.setForeground(alertorange);

        JLabel p1StatusPicture = new JLabel(partyOneLogo);
        JLabel p1StatusText = new JLabel();
        p1StatusText.setText(Main.getParty().get(0).getName());
        p1StatusText.setForeground(alertorange);

        JLabel p2StatusPicture = new JLabel(partyTwoLogo);
        JLabel p2StatusText = new JLabel();
        p2StatusText.setText(Main.getParty().get(0).getName());
        p2StatusText.setForeground(alertorange);

        JLabel p3StatusPicture = new JLabel(partyThreeLogo);
        JLabel p3StatusText = new JLabel();
        p3StatusText.setText(Main.getParty().get(0).getName());
        p3StatusText.setForeground(alertorange);

        JLabel p4StatusPicture = new JLabel(partyFourLogo);
        JLabel p4StatusText = new JLabel();
        p4StatusText.setText(Main.getParty().get(0).getName());
        p4StatusText.setForeground(alertorange);


        gbcStatusPanel.gridx = 0;
        gbcStatusPanel.gridy = 0;
        statusContent.add(shipStatusImage, gbcStatusPanel);
        gbcStatusPanel.gridx = 0;
        gbcStatusPanel.gridy = 1;
        statusContent.add(shipHealthText, gbcStatusPanel);
        gbcStatusPanel.gridx = 0;
        gbcStatusPanel.gridy = 2;
        statusContent.add(shipStatText, gbcStatusPanel);
        gbcStatusPanel.gridx = 1;
        gbcStatusPanel.gridy = 0;
        statusContent.add(p1StatusPicture, gbcStatusPanel);
        gbcStatusPanel.gridx = 1;
        gbcStatusPanel.gridy = 1;
        statusContent.add(p1StatusText, gbcStatusPanel);
        gbcStatusPanel.gridx = 2;
        gbcStatusPanel.gridy = 0;
        statusContent.add(p2StatusPicture, gbcStatusPanel);
        gbcStatusPanel.gridx = 2;
        gbcStatusPanel.gridy = 1;
        statusContent.add(p2StatusText, gbcStatusPanel);
        gbcStatusPanel.gridx = 1;
        gbcStatusPanel.gridy = 2;
        statusContent.add(p3StatusPicture, gbcStatusPanel);
        gbcStatusPanel.gridx = 1;
        gbcStatusPanel.gridy = 3;
        statusContent.add(p3StatusText, gbcStatusPanel);
        gbcStatusPanel.gridx = 2;
        gbcStatusPanel.gridy = 2;
        statusContent.add(p4StatusPicture, gbcStatusPanel);
        gbcStatusPanel.gridx = 2;
        gbcStatusPanel.gridy = 3;
        statusContent.add(p4StatusText, gbcStatusPanel);

        gbcStatusPanel.gridx = 0;
        gbcStatusPanel.gridy = 0;
        statusControls.add(createBackButton(), gbcStatusPanel);

        status.add(statusControls,BorderLayout.SOUTH);
        status.add(statusContent);
// </editor-fold>

        //<editor-fold desc="WELCOME PANEL">
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
                //TODO runEvent Refactor - event = 0
                EventEngine.runEvent(-1);
            }
        });

        JButton welcomeShopButton = new JButton("Shop");
        welcomeShopButton.setPreferredSize(new Dimension(200,50));
        welcomeShopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO runEvent Refactor - event = 1
                EventEngine.runEvent(1);
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
// </editor-fold>

        //<editor-fold desc="SHOP PANEL">>
        /* ------------ SHOP SCREEN ------------ */
        JPanel shopPanel = new JPanel(new BorderLayout());

        GridBagConstraints gbcShop = new GridBagConstraints();
        gbcShop.insets = new Insets(10, 10, 10, 10);

        JPanel shopContent = new JPanel(new GridBagLayout());
        shopContent.setBackground(gunmetal);

        JPanel shopControls = new JPanel(new GridBagLayout());
        shopControls.setBackground(cerulean);
        shopControls.setPreferredSize(new Dimension(200,200));

        JButton shopRationsButton = new JButton("Buy Rations");
        shopRationsButton.setPreferredSize(new Dimension(200,50));
        shopRationsButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        shopRationsButton.setForeground(alertorange);
        shopRationsButton.setBackground(buttonbrown);
        shopRationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.addInventory(1, 10);
                Main.removeItem(0, 10);
                JOptionPane.showMessageDialog(null, "Full parameter test from GameFrame", "Full Test", JOptionPane.INFORMATION_MESSAGE, lumberIcon);
            }
        });

        JButton shopWoodButton = new JButton("Buy Wood");
        shopWoodButton.setPreferredSize(new Dimension(200,50));
        shopWoodButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        shopWoodButton.setForeground(alertorange);
        shopWoodButton.setBackground(buttonbrown);
        shopWoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.addInventory(1, 10);
                Main.removeItem(0, 10);
            }
        });

        gbcShop.gridx = 0;
        gbcShop.gridy = 0;
        shopControls.add(shopRationsButton, gbcShop);
        gbcShop.gridx = 1;
        gbcShop.gridy = 0;
        shopControls.add(shopWoodButton, gbcShop);
        gbcShop.gridx = 1;
        gbcShop.gridy = 1;
        shopControls.add(createBackButton(), gbcShop);

        shopPanel.add(shopContent);
        shopPanel.add(shopControls, BorderLayout.SOUTH);
// </editor-fold

        //<editor-fold desc="RANDOM EVENT PANEL">
        /* ------------ RANDOM EVENT SCREEN ------------ */
        JPanel randEvent = new JPanel(new BorderLayout());

        JPanel eventControls = new JPanel(new GridBagLayout());
        eventControls.setBackground(cerulean);

        JPanel eventContent = new JPanel(new GridBagLayout());
        eventContent.setBackground(gunmetal);

        eventOptionOne.setPreferredSize(new Dimension(200,50));
        eventOptionTwo.setPreferredSize(new Dimension(200,50));
        eventOptionThree.setPreferredSize(new Dimension(200,50));
        eventOptionFour.setPreferredSize(new Dimension(200,50));
        eventTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        eventTitle.setForeground(alertorange);
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
// </editor-fold>

        //<editor-fold desc="COMBAT PANEL">
        /* ------------ COMBAT EVENT SCREEN ------------ */
        JPanel combatEvent = new JPanel(new BorderLayout());

        JPanel combatControls = new JPanel(new GridBagLayout());
        combatControls.setBackground(cerulean);
        combatControls.setPreferredSize(new Dimension(200,200));

        JPanel combatContent = new JPanel(new GridBagLayout());
        combatContent.setBackground(gunmetal);

        JLabel p1Status = new JLabel();

        JLabel p2Status = new JLabel();

        JLabel p3Status = new JLabel();

        JLabel p4Status = new JLabel();

        labelText[0] = "<html>" + Main.getEnemy().getName() + "<br/>Health: " + Main.getEnemy().getHealth() + "</html>";

        JLabel enemyStatus = new JLabel();
        enemyStatus.setText(labelText[0]);
        enemyStatus.setForeground(alertorange);
        enemyStatus.setFont(new Font("Monospaced", Font.BOLD, 30));

        JButton blockButton = new JButton("Block");
        blockButton.setPreferredSize(new Dimension(200,100));
        JButton parryButton = new JButton("Parry");
        parryButton.setPreferredSize(new Dimension(200,100));
        JButton attackButton = new JButton("Attack");
        attackButton.setPreferredSize(new Dimension(200,100));

        JLabel blankCombat = new JLabel();
        blankCombat.setPreferredSize(new Dimension(200,50));
        JLabel blankCombat1 = new JLabel();
        blankCombat1.setPreferredSize(new Dimension(50,50));
        JLabel blankCombat2 = new JLabel();
        blankCombat2.setPreferredSize(new Dimension(50,50));


        gbc.gridx = 0;
        gbc.gridy = 0;
        combatContent.add(p1Status, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        combatContent.add(p2Status, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        combatContent.add(p3Status, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        combatContent.add(p4Status, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        combatContent.add(blankCombat, gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        combatContent.add(enemyStatus, gbc);


        gbc.gridx = 0;
        gbc.gridy = 0;
        combatControls.add(blockButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        combatControls.add(blankCombat1, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        combatControls.add(parryButton, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        combatControls.add(blankCombat2, gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        combatControls.add(attackButton, gbc);

        blockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runCombat(1);
            }
        });

        parryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runCombat(2);
            }
        });

        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runCombat(3);
            }
        });

        combatEvent.add(combatControls,BorderLayout.SOUTH);
        combatEvent.add(combatContent,BorderLayout.CENTER);
// </editor-fold>

        //<editor-fold desc="END COMBAT PANEL">
        // -- END COMBAT EVENT SCREEN --

        JPanel endCombat = new JPanel(new BorderLayout());

        JPanel endCombatStatus = new JPanel(new GridBagLayout());
        endCombatStatus.setBackground(cerulean);
        combatControls.setPreferredSize(new Dimension(200,200));

        JPanel endCombatControl = new JPanel();
        endCombatControl.setLayout(new GridBagLayout());
        endCombatControl.setBackground(gunmetal);

        JLabel combatResolution = new JLabel();
        combatResolution.setText("<html>You defeated " + Main.getEnemy().getName() + "!<br/>" +
                " You gained 20 gold, 40 rations, and 10 lumber</html>");

        JButton endCombatButton = new JButton("Continue Adventure");
        endCombatButton.setPreferredSize(new Dimension(200,100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        endCombatStatus.add(combatResolution, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        endCombatControl.add(endCombatButton, gbc);

        // Why this here?

//        attackButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                //What this do?
//                Main.runEvent(5);
//            }
//        });

        endCombat.add(endCombatStatus,BorderLayout.CENTER);
        endCombat.add(endCombatControl,BorderLayout.SOUTH);
// </editor-fold>

        //<editor-fold desc="GENERIC LOCATION PANEL">

        /* ------------ LOCATION EVENT SCREEN ------------ */
        // Start Area Event ------------------------------
        JPanel startEvent = new JPanel(new BorderLayout());

        JPanel startControls = new JPanel(new GridBagLayout());
        startControls.setBackground(cerulean);

        JPanel startContent = new JPanel(new GridBagLayout());
        startContent.setBackground(gunmetal);

        JButton eventOptionShop = new JButton("Shop");
        eventOptionShop.setFont(new Font("Monospaced", Font.BOLD, 20));
        eventOptionShop.setPreferredSize(new Dimension(200,100));
        eventOptionShop.setBackground(buttonbrown);
        eventOptionShop.setForeground(alertorange);

        JButton eventOptionInv = new JButton("Check Inventory");
        eventOptionInv.setPreferredSize(new Dimension(200,100));

        JButton eventOptionMembers = new JButton("Check Members");
        eventOptionMembers.setPreferredSize(new Dimension(200,50));

        JButton eventOptionSail = new JButton("Set Sail");
        eventOptionSail.setFont(new Font("Monospaced", Font.BOLD, 20));
        eventOptionSail.setPreferredSize(new Dimension(200,100));
        eventOptionSail.setBackground(buttonbrown);
        eventOptionSail.setForeground(alertorange);

        JLabel startEventTitle = new JLabel("Start Area"); // Change start area name?
        startEventTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        startEventTitle.setForeground(alertorange);

        //JLabel startEventImage = new JLabel();
        //Add image icons later

        JTextArea startEventDescription = new JTextArea(10,44);
        startEventDescription.setEditable(false);
        startEventDescription.setHighlighter(null);
        startEventDescription.setFocusable(false);
        startEventDescription.setBackground(transparent);
        startEventDescription.setLineWrap(true);
        startEventDescription.setText("We are about to make a long voyage across the seas. \n" +
                "We better stock up on any supplies we will need for the long trip.");
        startEventDescription.setForeground(emerald);

        startControls.setPreferredSize(new Dimension(200,200));

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        startControls.add(createDefaultControls(),gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        startControls.add(eventOptionShop, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        startControls.add(eventOptionSail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        startContent.add(startEventTitle, gbc);
        //gbc.gridx = 0;
        //gbc.gridy = 1;
        //startContent.add(startEventImage, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        startContent.add(startEventDescription, gbc);

        startEvent.add(startContent, BorderLayout.CENTER);

        startEvent.add(startControls,BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="SCOTLAND">

        // ScotLand Event ------------------------------
        JPanel scotEvent = new JPanel(new BorderLayout());

        JPanel scotContent = new JPanel(new GridBagLayout());
        scotContent.setBackground(gunmetal);

        JPanel scotControls = new JPanel(new GridBagLayout());
        scotContent.setBackground(cerulean);

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

        JButton scotCombat = new JButton("Raid village");
        JButton scotLumber = new JButton("Gather lumber");
        scotCombat.setPreferredSize(new Dimension(200, 50));
        scotLumber.setPreferredSize(new Dimension(200, 50));
        scotCombat.setBackground(buttonbrown);
        scotLumber.setBackground(buttonbrown);
        scotCombat.setForeground(alertorange);
        scotLumber.setForeground(alertorange);
        scotCombat.addActionListener(e -> EventEngine.runEvent(-2)); // start combat
        scotLumber.addActionListener(e -> EventEngine.runEvent(13)); //Chop wood event

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        scotControls.add(scotCombat, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        scotControls.add(scotLumber, gbc);

        scotEvent.add(scotContent, BorderLayout.CENTER);
        scotEvent.add(scotControls,BorderLayout.SOUTH);

        // </editor-fold>

        //<editor-fold desc="ICELAND">

        // Iceland Event ------------------------------
        JPanel iceEvent = new JPanel(new BorderLayout());

        JPanel iceContent = new JPanel(new GridBagLayout());
        iceContent.setBackground(gunmetal);

        JPanel iceControls = new JPanel(new GridBagLayout());
        iceContent.setBackground(cerulean);

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

        JButton iceShop = new JButton("Shop");
        JButton iceFish = new JButton("Fish");
        iceShop.setPreferredSize(new Dimension(200, 50));
        iceFish.setPreferredSize(new Dimension(200, 50));
        iceShop.setBackground(buttonbrown);
        iceFish.setBackground(buttonbrown);
        iceShop.setForeground(alertorange);
        iceFish.setForeground(alertorange);
        iceShop.addActionListener(e -> EventEngine.runEvent(1)); // shop
        iceFish.addActionListener(e -> EventEngine.runEvent(6)); //fish

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        iceControls.add(iceShop, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        iceControls.add(iceFish, gbc);


        iceEvent.add(iceContent, BorderLayout.CENTER);
        iceEvent.add(iceControls,BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="GREENLAND">

        // Greenland Event ------------------------------
        JPanel greenEvent = new JPanel(new BorderLayout());

        JPanel greenContent = new JPanel(new GridBagLayout());
        greenContent.setBackground(gunmetal);

        JPanel greenControls = new JPanel(new GridBagLayout());
        greenControls.setBackground(cerulean);

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

        JButton greenHunt = new JButton("Hunt for food");
        JButton greenLumber = new JButton("Gather lumber");
        greenHunt.setPreferredSize(new Dimension(200, 50));
        greenLumber.setPreferredSize(new Dimension(200, 50));
        greenHunt.setBackground(buttonbrown);
        greenLumber.setBackground(buttonbrown);
        greenHunt.setForeground(alertorange);
        greenLumber.setForeground(alertorange);
        greenHunt.addActionListener(e -> EventEngine.runEvent(12)); // hunt
        greenLumber.addActionListener(e -> EventEngine.runEvent(13)); // chop wood

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        greenControls.add(greenHunt, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        greenControls.add(greenLumber, gbc);

        greenEvent.add(greenContent, BorderLayout.CENTER);
        greenEvent.add(greenControls,BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="VINLAND">

        // Vinland Event ------------------------------
        JPanel vinEvent = new JPanel(new BorderLayout());

        JPanel vinContent = new JPanel(new GridBagLayout());
        vinContent.setBackground(gunmetal);

        JPanel vinControls = new JPanel(new GridBagLayout());
        vinControls.setBackground(cerulean);

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

        JButton finish = new JButton("Back to main menu");
        finish.setPreferredSize(new Dimension(200, 50));
        finish.setBackground(buttonbrown);
        finish.setForeground(alertorange);
        finish.addActionListener(e -> switchToPanel(MAIN));

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        vinControls.add(finish, gbc);

        vinEvent.add(vinContent, BorderLayout.CENTER);
        vinEvent.add(vinControls,BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="CALM SEAS PANEL">

        /* -------------- Calm Seas Panel ------------ */
        // Create Calm Seas Panel
        JPanel calmPanel = new JPanel(new BorderLayout());
        calmPanel.setBackground(gunmetal);

        JPanel calmContent = new JPanel(new GridBagLayout());
        calmContent.setBackground(gunmetal);

        JPanel calmControls = new JPanel(new GridBagLayout());
        calmControls.setBackground(cerulean);
        calmControls.setPreferredSize(new Dimension(200, 200));

        // GBC for spacing between buttons
        GridBagConstraints controlGbc = new GridBagConstraints();
        controlGbc.insets = new Insets(10, 30, 10, 30);

        // Calm Seas Title
        JLabel calmTitle = new JLabel(Main.eventTitles.get(4));
        calmTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        calmTitle.setForeground(alertorange);

        // Calm Seas Text Area
        JTextArea calmDesc = new JTextArea(Main.eventDescriptions.get(4));
        calmDesc.setEditable(false);
        calmDesc.setHighlighter(null);
        calmDesc.setFocusable(false);
        calmDesc.setBackground(transparent);
        calmDesc.setLineWrap(true);
        calmDesc.setWrapStyleWord(true);
        calmDesc.setForeground(emerald);
        calmDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        calmDesc.setPreferredSize(new Dimension(600, 100));

        // Calm Seas Buttons
        JButton calmFish = new JButton("Fish");
        JButton calmRepair = new JButton("Repair Ship");
        JButton calmSailing = new JButton("Continue Sailing");
        calmFish.setPreferredSize(new Dimension(200, 50));
        calmRepair.setPreferredSize(new Dimension(200, 50));
        calmSailing.setPreferredSize(new Dimension(200, 50));
        calmFish.setBackground(buttonbrown);
        calmRepair.setBackground(buttonbrown);
        calmSailing.setBackground(buttonbrown);
        calmFish.setForeground(alertorange);
        calmRepair.setForeground(alertorange);
        calmSailing.setForeground(alertorange);
        calmFish.addActionListener(e -> EventEngine.runEvent(6)); // fishing
        // TODO repair can be removed?
        calmRepair.addActionListener(e -> EventEngine.runEvent(10)); // repair ship - currently call null event - pre-event refactor
        calmSailing.addActionListener(e -> EventEngine.runEvent(-1));

        // Add content to calmContent
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        calmContent.add(calmTitle, gbc);
        gbc.gridy = 1;
        calmContent.add(calmDesc, gbc);

        // Add controls to bottom calmControls
        controlGbc.gridx = 0;
        controlGbc.gridy = 0;
        calmControls.add(createDefaultControls(), controlGbc);
        controlGbc.gridx = 1;
        calmControls.add(calmFish, controlGbc);
        controlGbc.gridx = 2;
        calmControls.add(calmRepair, controlGbc);
        controlGbc.gridx = 3;
        calmControls.add(calmSailing, controlGbc);

        // Assemble calmPanel
        calmPanel.add(calmContent, BorderLayout.CENTER);
        calmPanel.add(calmControls, BorderLayout.SOUTH);

        // Add calmPanel to deck
        /* -------------- End of Calm Seas Panel ------------ */
        // </editor-fold>

        //<editor-fold desc="ROUGH SEAS PANEL">

        /* -------------- Rough Seas Panel ------------------ */
        // Create Rough Seas panel
        JPanel roughPanel = new JPanel(new BorderLayout());
        roughPanel.setBackground(gunmetal);

        JPanel roughContent = new JPanel(new GridBagLayout());
        roughContent.setBackground(gunmetal);

        JPanel roughControls = new JPanel(new GridBagLayout());
        roughControls.setPreferredSize(new Dimension(200, 200));
        roughControls.setBackground(cerulean);

        // GBC for spacing between buttons
        controlGbc.insets = new Insets(10, 30, 10, 30);

        // Rough Seas Title
        JLabel roughTitle = new JLabel(Main.eventTitles.get(5));
        roughTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        roughTitle.setForeground(alertorange);

        // Rough Seas Text Area
        JTextArea roughDesc = new JTextArea(Main.eventDescriptions.get(5));
        roughDesc.setEditable(false);
        roughDesc.setHighlighter(null);
        roughDesc.setFocusable(false);
        roughDesc.setBackground(transparent);
        roughDesc.setLineWrap(true);
        roughDesc.setWrapStyleWord(true);
        roughDesc.setForeground(emerald);
        roughDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        roughDesc.setPreferredSize(new Dimension(600, 100));

        // Rough Seas Buttons
        JButton roughWait = new JButton("Hunker Down");
        JButton roughFish = new JButton("Fish");
        JButton roughSail = new JButton("Continue Sailing");
        roughWait.setPreferredSize(new Dimension(200, 50));
        roughFish.setPreferredSize(new Dimension(200, 50));
        roughSail.setPreferredSize(new Dimension(200, 50));
        roughWait.setBackground(buttonbrown);
        roughFish.setBackground(buttonbrown);
        roughSail.setBackground(buttonbrown);
        roughWait.setForeground(alertorange);
        roughFish.setForeground(alertorange);
        roughSail.setForeground(alertorange);
        roughWait.addActionListener(e -> EventEngine.runEvent(7)); // hunker down
        roughFish.addActionListener(e -> EventEngine.runEvent(6)); // fish
        roughSail.addActionListener(e -> EventEngine.runEvent(8)); // continue sailing

        // Add content to roughContent
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        roughContent.add(roughTitle, gbc);
        gbc.gridy = 1;
        roughContent.add(roughDesc, gbc);

        // Add controls to roughControls
        controlGbc.gridx = 0;
        controlGbc.gridy = 0;
        roughControls.add(createDefaultControls(), controlGbc);
        controlGbc.gridx = 1;
        roughControls.add(roughWait, controlGbc);
        controlGbc.gridx = 2;
        roughControls.add(roughFish, controlGbc);
        controlGbc.gridx = 3;
        roughControls.add(roughSail, controlGbc);

        // Assemble roughPanel
        roughPanel.add(roughContent, BorderLayout.CENTER);
        roughPanel.add(roughControls, BorderLayout.SOUTH);

        // Add roughPanel to deck

        /* -------------- End of Rough Seas Panel ------------ */
        // </editor-fold>

        //<editor-fold desc="STORM PANEL">

        /* ----------------- Storm Panel --------------------- */
        // Create Storm Panel
        JPanel stormPanel = new JPanel(new BorderLayout());
        stormPanel.setBackground(gunmetal);

        JPanel stormContent = new JPanel(new GridBagLayout());
        stormContent.setBackground(gunmetal);

        JPanel stormControls = new JPanel(new GridBagLayout());
        stormControls.setPreferredSize(new Dimension(200, 200));
        stormControls.setBackground(cerulean);

        // GBC for spacing between buttons
        controlGbc.insets = new Insets(10, 30, 10, 30);

        // Storm Title
        JLabel stormTitle = new JLabel(Main.eventTitles.get(6));
        stormTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        stormTitle.setForeground(alertorange);

        // Storm Text Area
        JTextArea stormDesc = new JTextArea(Main.eventDescriptions.get(6));
        stormDesc.setEditable(false);
        stormDesc.setHighlighter(null);
        stormDesc.setBackground(transparent);
        stormDesc.setLineWrap(true);
        stormDesc.setWrapStyleWord(true);
        stormDesc.setForeground(emerald);
        stormDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        stormDesc.setPreferredSize(new Dimension(600, 100));

        // Storm buttons
        JButton stormWait = new JButton("Hunker Down");
        JButton stormSail = new JButton("Continue Sailing");
        stormWait.setPreferredSize(new Dimension(200, 50));
        stormSail.setPreferredSize(new Dimension(200, 50));
        stormWait.setBackground(buttonbrown);
        stormSail.setBackground(buttonbrown);
        stormWait.setForeground(alertorange);
        stormSail.setForeground(alertorange);
        stormWait.addActionListener(e -> EventEngine.runEvent(7)); // hunker down
        stormSail.addActionListener(e -> EventEngine.runEvent(9)); // storm event

        // Add content to stormContent
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        stormContent.add(stormTitle, gbc);
        gbc.gridy = 1;
        stormContent.add(stormDesc, gbc);

        // Add controls to stormControls
        controlGbc.gridx = 0;
        controlGbc.gridy = 0;
        stormControls.add(createDefaultControls(), controlGbc);
        controlGbc.gridx = 1;
        stormControls.add(stormWait, controlGbc);
        controlGbc.gridx = 2;
        stormControls.add(stormSail, controlGbc);

        // Assemble stormPanel
        stormPanel.add(stormContent, BorderLayout.CENTER);
        stormPanel.add(stormControls, BorderLayout.SOUTH);

        // Add stormPanel to deck

        /* -------------- End of Storm Panel ----------------- */
        // </editor-fold>

        //<editor-fold desc="VILLAGE PANEL">

        /*--------------- Village Panel ---------------------- */
        // Create village panel
        JPanel villagePanel = new JPanel(new BorderLayout());
        villagePanel.setBackground(gunmetal);

        JPanel villageContent = new JPanel(new GridBagLayout());
        villageContent.setBackground(gunmetal);

        JPanel villageControls = new JPanel(new GridBagLayout());
        villageControls.setPreferredSize(new Dimension(200, 200));
        villageControls.setBackground(cerulean);

        // GBC for spacing between buttons
        controlGbc.insets = new Insets(10, 30, 10, 30);

        // Village Title
        JLabel villageTitle = new JLabel(Main.eventTitles.get(7));
        villageTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        villageTitle.setForeground(alertorange);

        // Village Text area
        JTextArea villageDesc = new JTextArea(Main.eventDescriptions.get(7));
        villageDesc.setEditable(false);
        villageDesc.setHighlighter(null);
        villageDesc.setBackground(transparent);
        villageDesc.setLineWrap(true);
        villageDesc.setWrapStyleWord(true);
        villageDesc.setForeground(emerald);
        villageDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        villageDesc.setPreferredSize(new Dimension(600, 100));

        // Village Buttons
        JButton villageShop = new JButton("Visit Shop");
        JButton villageRepair = new JButton("Repair Ship");
        JButton villageSail = new JButton("Continue Sailing");
        villageShop.setPreferredSize(new Dimension(200, 50));
        villageRepair.setPreferredSize(new Dimension(200, 50));
        villageSail.setPreferredSize(new Dimension(200, 50));
        villageShop.setBackground(buttonbrown);
        villageRepair.setBackground(buttonbrown);
        villageSail.setBackground(buttonbrown);
        villageShop.setForeground(alertorange);
        villageRepair.setForeground(alertorange);
        villageSail.setForeground(alertorange);
        villageShop.addActionListener(e -> EventEngine.runEvent(1)); // shop
        villageRepair.addActionListener(e -> EventEngine.runEvent(10)); // repair?
        villageSail.addActionListener(e -> EventEngine.runEvent(-1)); // continue sailing

        // add content to villageContent
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        villageContent.add(villageTitle, gbc);
        gbc.gridy = 1;
        villageContent.add(villageDesc, gbc);

        // Add controls to stormControls
        controlGbc.gridx = 0;
        controlGbc.gridy = 1;
        villageControls.add(createDefaultControls(), controlGbc);
        controlGbc.gridx = 1;
        villageControls.add(villageShop, controlGbc);
        controlGbc.gridx = 2;
        villageControls.add(villageRepair, controlGbc);
        controlGbc.gridx = 3;
        villageControls.add(villageSail, controlGbc);

        // Assemble villagePanel
        villagePanel.add(villageContent, BorderLayout.CENTER);
        villagePanel.add(villageControls, BorderLayout.SOUTH);

        // Add villagePanel to deck

        /*--------------- End of Village Panel ------------------ */
        // </editor-fold>

        //<editor-fold desc="FOREST PANEL">

        /* ----------------- Forest Panel --------------------- */
        // Create Forest Panel
        JPanel forestPanel = new JPanel(new BorderLayout());
        forestPanel.setBackground(gunmetal);

        JPanel forestContent = new JPanel(new GridBagLayout());
        forestContent.setBackground(gunmetal);

        JPanel forestControls = new JPanel(new GridBagLayout());
        forestControls.setPreferredSize(new Dimension(200, 200));
        forestControls.setBackground(cerulean);

        // GBC for spacing between buttons
        controlGbc.insets = new Insets(10, 30, 10, 30);

        // Forest Title
        JLabel forestTitle = new JLabel(Main.eventTitles.get(8));
        forestTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        forestTitle.setForeground(alertorange);

        // Forest Test Area
        JTextArea forestDesc = new JTextArea(Main.eventDescriptions.get(8));
        forestDesc.setEditable(false);
        forestDesc.setHighlighter(null);
        forestDesc.setBackground(transparent);
        forestDesc.setLineWrap(true);
        forestDesc.setWrapStyleWord(true);
        forestDesc.setForeground(emerald);
        forestDesc.setFont(new Font("Monospaced", Font.BOLD, 20));
        forestDesc.setPreferredSize(new Dimension(600, 100));

        // Forest Buttons
        JButton forestWood = new JButton("Chop some Wood");
        JButton forestHunt = new JButton("Hunt wild game");
        JButton forestSail = new JButton("Continue Sailing");
        forestWood.setPreferredSize(new Dimension(200, 50));
        forestHunt.setPreferredSize(new Dimension(200, 50));
        forestSail.setPreferredSize(new Dimension(200, 50));
        forestWood.setBackground(buttonbrown);
        forestHunt.setBackground(buttonbrown);
        forestSail.setBackground(buttonbrown);
        forestWood.setForeground(alertorange);
        forestHunt.setForeground(alertorange);
        forestSail.setForeground(alertorange);
        forestWood.addActionListener(e -> EventEngine.runEvent(13)); // Wood
        forestHunt.addActionListener(e -> EventEngine.runEvent(12)); // Hunt
        forestSail.addActionListener(e -> EventEngine.runEvent(-1));  // Continue Sailing

        // Add content to forestContent
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        forestContent.add(forestTitle, gbc);
        gbc.gridy = 1;
        forestContent.add(forestDesc, gbc);

        // Add controls to forestControls
        gbc.gridx = 0;
        gbc.gridy = 0;
        forestControls.add(createDefaultControls(), gbc);
        gbc.gridx = 1;
        forestControls.add(forestWood, gbc);
        gbc.gridx = 2;
        forestControls.add(forestHunt, gbc);
        gbc.gridx = 3;
        forestControls.add(forestSail, gbc);

        // Assemble forestPanel
        forestPanel.add(forestContent, BorderLayout.CENTER);
        forestPanel.add(forestControls, BorderLayout.SOUTH);

        // Add forestPanel to deck
        // </editor-fold>

        //<editor-fold desc="SANK PANEL">

        /* ------------ Ship Sank Panel ------------ */
        JPanel shipSankPanel = new JPanel(new BorderLayout());

        JPanel sankContent = new JPanel(new GridBagLayout());
        sankContent.setBackground(gunmetal);

        JPanel sankControls = new JPanel(new GridBagLayout());
        sankControls.setPreferredSize(new Dimension(200, 200));
        sankControls.setBackground(cerulean);

        GridBagConstraints gbcSank = new GridBagConstraints();
        gbcSank.insets = new Insets(10, 10, 10, 10);

        //TODO add JLabel to display final score

        JLabel sankSpacer = new JLabel(" ");
        sankSpacer.setPreferredSize(new Dimension(50, 50));

        JLabel sankTitle = new JLabel("Your ship has sank");
        sankTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        sankTitle.setForeground(alertorange);

        JTextArea sankDesc = new JTextArea("Your ship took too much damage and rests with the Kraken.");
        sankDesc.setEditable(false);
        sankDesc.setBackground(transparent);
        sankDesc.setForeground(emerald);

        JButton sankRestart = new JButton("Play Again?");
        JButton sankQuit = new JButton("Quit");

        // Brings player back to main menu panel
        sankRestart.addActionListener(e -> switchToPanel(MAIN));
        // Closes the program
        sankQuit.addActionListener(e -> this.dispose());

        gbcSank.gridx = 0;
        gbcSank.gridy = 0;
        sankContent.add(sankTitle, gbcSank);
        gbcSank.gridy = 1;
        sankContent.add(sankSpacer, gbcSank);
        gbcSank.gridy = 2;
        sankContent.add(sankDesc, gbcSank);

        gbcSank.gridy = 0;
        sankControls.add(sankRestart, gbcSank);
        gbcSank.gridx = 1;
        sankControls.add(sankQuit, gbcSank);

        // Build Sank Panel
        shipSankPanel.add(sankContent, BorderLayout.CENTER);
        shipSankPanel.add(sankControls, BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="PARTY WIPE">

        /* ------------ Party Wipe Panel ------------ */
        JPanel partyWipePanel = new JPanel();

        JPanel wipeContent = new JPanel(new GridBagLayout());
        wipeContent.setBackground(gunmetal);

        JPanel wipeControls = new JPanel(new GridBagLayout());
        wipeControls.setPreferredSize(new Dimension(200, 200));
        wipeControls.setBackground(cerulean);

        GridBagConstraints gbcWipe = new GridBagConstraints();
        gbcWipe.insets = new Insets(10, 10, 10, 10);

        //TODO add JLabel to display final score

        JLabel wipeTitle = new JLabel("Party defeated");

        JTextArea wipeDesc = new JTextArea("Your party has been slain.\nMay their souls find peace in Valhalla");

        JButton wipeRestart = new JButton("Play Again?");
        JButton wipeQuit = new JButton("Quit");

        // Brings player back to main menu panel
        wipeRestart.addActionListener(e -> switchToPanel(MAIN));
        // Closes the program
        wipeQuit.addActionListener(e -> this.dispose());

        // Build Wipe Panel
        partyWipePanel.add(wipeContent, BorderLayout.CENTER);
        partyWipePanel.add(wipeControls, BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="RESOURCE PANEL">

        /* ------------ Resource Panel ------------ */

        JPanel resourcesPanel = new JPanel(new BorderLayout());

        JPanel resourcesContent = new JPanel(new GridBagLayout());
        resourcesContent.setBackground(gunmetal);

        JPanel resourcesControls = new JPanel(new GridBagLayout());
        resourcesControls.setPreferredSize(new Dimension(200, 200));
        resourcesControls.setBackground(cerulean);

        GridBagConstraints gbcResources = new GridBagConstraints();


        JLabel resourcesDesc = new JLabel();
        resourcesDesc.setText(rescourceLabel);
        resourcesDesc.setFont(new Font("Monospaced", Font.BOLD, 40));
        resourcesDesc.setForeground(alertorange);


        JButton resourcesButton = new JButton("Set Sail");

        gbcResources.gridx = 0;
        gbcResources.gridy = 0;
        resourcesContent.add(resourcesDesc, gbcResources);
        gbcResources.gridy = 0;
        gbcResources.gridx = 0;
        resourcesControls.add(resourcesButton, gbcResources);

        resourcesButton.addActionListener(e -> EventEngine.runEvent(-1));

        resourcesPanel.add(resourcesContent, BorderLayout.CENTER);
        resourcesPanel.add(resourcesControls, BorderLayout.SOUTH);
        // </editor-fold>

        //<editor-fold desc="PARTY CREATION PANEL">

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

        //TODO have a cath for when a player does not enter any names. Make a pop up that states at least one name must be filled in
        partyContinueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String partyNameOneInput = partyOneField.getText();
                String partyNameTwoInput = partyTwoField.getText();
                String partyNameThreeInput = partyThreeField.getText();
                String partyNameFourInput = partyFourField.getText();

                ImageIcon partyOneLogo = new ImageIcon("./resources/PartyIcons/partyOne.png");
                ImageIcon partyTwoLogo = new ImageIcon("./resources/PartyIcons/partyTwo.png");
                ImageIcon partyThreeLogo = new ImageIcon("./resources/PartyIcons/partyThree.png");
                ImageIcon partyFourLogo = new ImageIcon("./resources/PartyIcons/partyFour.png");

                Main.createParty(partyNameOneInput, partyNameTwoInput, partyNameThreeInput, partyNameFourInput);
                p1StatusText.setText(Main.getParty().get(0).getName());
                p1StatusText.setForeground(alertorange);

                p2StatusText.setText(Main.getParty().get(1).getName());
                p2StatusText.setForeground(alertorange);

                p3StatusText.setText(Main.getParty().get(2).getName());
                p3StatusText.setForeground(alertorange);

                p4StatusText.setText(Main.getParty().get(3).getName());
                p4StatusText.setForeground(alertorange);


                if (Main.getParty().get(0).getActive() == 1) {
                    labelText[0] = "<html>" + Main.getParty().get(0).getName() + "<br/>Health: " + Main.getParty().get(0).getHealth() + "</html>";
                    p1Status.setText(labelText[0]);
                    partyOneIcon.setIcon(partyOneLogo);

                } else if (Main.getParty().get(0).getActive() == 0) {
                    labelText[0] = " ";
                    p1Status.setText(labelText[0]);
                    p1StatusPicture.setIcon(partyInactiveLogo);
                }
                p1Status.setForeground(alertorange);
                p1Status.setFont(new Font("Monospaced", Font.BOLD, 30));

                if (Main.getParty().get(1).getActive() == 1) {
                    labelText[0] = "<html>" + Main.getParty().get(1).getName() + "<br/>Health: " + Main.getParty().get(1).getHealth() + "</html>";
                    p2Status.setText(labelText[0]);
                    partyTwoIcon.setIcon(partyTwoLogo);
                } else if (Main.getParty().get(1).getActive() == 0) {
                    labelText[0] = " ";
                    p2Status.setText(labelText[0]);
                    p2StatusPicture.setIcon(partyInactiveLogo);
                }

                p2Status.setForeground(alertorange);
                p2Status.setFont(new Font("Monospaced", Font.BOLD, 30));

                if (Main.getParty().get(2).getActive() == 1) {
                    labelText[0] = "<html>" + Main.getParty().get(2).getName() + "<br/>Health: " + Main.getParty().get(2).getHealth() + "</html>";
                    p3Status.setText(labelText[0]);
                    partyThreeIcon.setIcon(partyThreeLogo);
                } else if (Main.getParty().get(2).getActive() == 0) {
                    labelText[0] = " ";
                    p3Status.setText(labelText[0]);
                    p3StatusPicture.setIcon(partyInactiveLogo);
                }
                p3Status.setForeground(alertorange);
                p3Status.setFont(new Font("Monospaced", Font.BOLD, 30));

                if (Main.getParty().get(3).getActive() == 1) {
                    labelText[0] = "<html>" + Main.getParty().get(3).getName() + "<br/>Health: " + Main.getParty().get(3).getHealth() + "</html>";
                    p4Status.setText(labelText[0]);
                    partyFourIcon.setIcon(partyFourLogo);
                } else if (Main.getParty().get(3).getActive() == 0) {
                    labelText[0] = " ";
                    p4Status.setText(labelText[0]);
                    p4StatusPicture.setIcon(partyInactiveLogo);
                }
                p4Status.setForeground(alertorange);
                p4Status.setFont(new Font("Monospaced", Font.BOLD, 30));

                enemyStatus.repaint();

                EventEngine.runEvent(-1);

                //TODO json reading test - remove after testing
                TestDat readTestDat = null;
                try {
                    readTestDat = mapper.readValue(new File("./resources/Data/testDat.json"), TestDat.class);
                    System.out.println("Seed Input from json: " + readTestDat.getSeedInput());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //Main.runEvent(-1);
            }
        });
        // </editor-fold>

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
        deck.add(shopPanel, SHOP);
        deck.add(party, PARTY);
        deck.add(combatEvent, COMBAT);
        deck.add(endCombat, FCOMBAT);
        deck.add(resourcesPanel, CONFRIM);

        deck.add(stormPanel, STORM);
        deck.add(forestPanel, FOREST);
        deck.add(villagePanel, VILLAGE);
        deck.add(roughPanel, ROUGH);
        deck.add(calmPanel, CALM);

        // Location Based Events
        deck.add(startEvent, START);
        deck.add(scotEvent, SCOTLAND);
        deck.add(iceEvent, ICELAND);
        deck.add(greenEvent, GREENLAND);
        deck.add(vinEvent, VINLAND);

        //Defeat screens
        deck.add(shipSankPanel, SANK);
        deck.add(partyWipePanel, WIPE);

        // Debug Panels
        deck.add(dbStats, DBSTATS);

        /* ------------ Event handling ------------ */
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String seedInputText = seedField.getText();

                if (seedInputText.equals("DEBUGSTATUS")) { //TODO Testing - remove after

                    TestDat readTestDat = null;
                    try {
                        readTestDat = mapper.readValue(new File("./resources/Data/testDat.json"), TestDat.class);
                        System.out.println("Last recorded ship health from json: " + readTestDat.getDebugHealth());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    debugShip.setHealth(readTestDat.getDebugHealth());
                    dbHealthText.setText("Ship Health: " + debugShip.getHealth());
                    dbStatText.setText("Ship Status: " + debugShip.getStatus());

                    if (debugShip.getStatus() == "Perfect Condition"){
                        dbStatsImage.setIcon(shipConditionPerfect);
                    } else if (debugShip.getStatus() == "Great Condition") {
                        dbStatsImage.setIcon(shipConditionGreat);
                    } else if (debugShip.getStatus() == "Damaged") {
                        dbStatsImage.setIcon(shipConditionDamaged);
                    } else if (debugShip.getStatus() == "Severe Damage") {
                        dbStatsImage.setIcon(shipConditionSevere);
                    } else if (debugShip.getStatus() == "Sank") {
                        dbStatsImage.setIcon(shipConditionSank);
                    }

                    switchToPanel(DBSTATS);

                } else if (seedInputText.equals("DEBUGEVENT")) { //TODO Testing - remove after
                    switchToPanel(EVENT);
                } else if (seedInputText.equals("DEBUGSTART")) { //TODO Testing - remove after
                    switchToPanel(VINLAND);
                } else if (seedInputText.equals("DEBUGPARTY")) { //TODO Testing - remove after
                    switchToPanel(PARTY);
                } else if (seedInputText.equals("DEBUGSTATUS2")) { //TODO Testing - remove after
                    switchToPanel(STATS);
                } else if (seedInputText.equals("DEBUGCALM")) { //TODO Testing - remove after
                    switchToPanel(CALM);
                } else if (seedInputText.equals("DEBUGROUGH")) { //TODO Testing - remove after
                    switchToPanel(ROUGH);
                } else if (seedInputText.equals("DEBUGSTORM")) { //TODO Testing - remove after
                    switchToPanel(STORM);
                } else if (seedInputText.equals("DEBUGVILLAGE")) { //TODO Testing - remove after
                    switchToPanel(VILLAGE);
                } else if (seedInputText.equals("DEBUGFOREST")) { //TODO Testing - remove after
                    switchToPanel(FOREST);
                } else if (seedInputText.equals("DEBUGCOMBAT")) {
                    switchToPanel(COMBAT);
                } else if (seedInputText.equals("DEBUGWIPE")) {
                    switchToPanel(WIPE);
                } else if (seedInputText.equals("DEBUGTESTEVENTRAND")) {
                    EventEngine.runEvent(-1);
                } else if (seedInputText.equals("DEBUGSANK")) {
                    switchToPanel(CONFRIM);
                } else if (seedInputText.equals("DEBUGJSON")) {
                    System.out.println(testDat.getDebugHealth() + testDat.getSeedInput());
                } else { //User leaves the field blank
                    //Temporary, should run the game - switch to the first game screen
                    long randSeedLong = System.currentTimeMillis();
                    String randSeedString = Long.toString(randSeedLong);
                    Main.setSeed(randSeedString);
                    switchToPanel(PARTY);
                }// end debug if's

                //TODO testing json I/O delete after testing!

                try {

                    //Read JSON file
                    TestDat modifySeed = mapper.readValue(saveFile, TestDat.class);
                    //Modify SeedInput
                    modifySeed.setSeedInput(seedInputText);
                    //Write changes to saveFile
                    mapper.writeValue(saveFile, modifySeed);

                    //TODO Testing - remove for production
//                    TestDat readSeed = mapper.readValue(saveFile, TestDat.class);
//
//                    System.out.println(readSeed.getSeedInput());
//                    System.out.println(readSeed.getDebugHealth());

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        }); // End of playButton listener

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
            public void actionPerformed(ActionEvent e) {
                EventEngine.runEvent(-1);
            }
        });


        // Location Event Options
        eventOptionShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel(SHOP);
            }
        });

        eventOptionInv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                switchToPanel(INV);
            }
        });

        eventOptionMembers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToPanel(PARTY);
            }
        });

        eventOptionSail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                EventEngine.runEvent(10000000); // Random Event?

            }
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
        switchToPanel(MAIN);
    }

    private JButton createBackButton() {
        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(200, 50));
        back.setFont(new Font("Monospaced", Font.BOLD, 20));
        back.setForeground(buttonbrown);
        back.setBackground(alertorange);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //isGoingBack = true;
                switchToPanel(previousPanel);
                //isGoingBack = false;
            }
        });
        return back;
    }

    private JPanel createDefaultControls() {
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel defaultControls = new JPanel(new GridBagLayout());
        defaultControls.setPreferredSize(new Dimension(200, 200));
        defaultControls.setBackground(transparent);

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
                if (Main.getShip().getStatus() == "Perfect Condition"){
                    shipStatusImage.setIcon(shipConditionPerfect);
                } else if (Main.getShip().getStatus() == "Great Condition") {
                    shipStatusImage.setIcon(shipConditionGreat);
                } else if (Main.getShip().getStatus() == "Damaged") {
                    shipStatusImage.setIcon(shipConditionDamaged);
                } else if (Main.getShip().getStatus() == "Severe Damage") {
                    shipStatusImage.setIcon(shipConditionSevere);
                } else if (Main.getShip().getStatus() == "Sank") {
                    shipStatusImage.setIcon(shipConditionSank);
                }

                switchToPanel(STATS);
            }
        });

        return defaultControls;
    }

    public void switchToPanel(String panelName) {
        if (!panelName.equals(currentPanel)) {
            previousPanel = currentPanel;
            System.out.println("Switching to " + panelName + " from " + currentPanel + ", previous = " + previousPanel);
            currentPanel = panelName;
        }

        cardLayout.show(deck, panelName);

    }

    public void resourceChanges(String resourceName) {
        rescourceLabel = resourceName;
    }

    public void setEventID(int eventID) {
        GameFrame.eventID = eventID;
        //TODO I want this to be read from a json file so it knows to change events by reading event data from the json
        if (eventID == 1) {
            eventTitle.setText("Common event 1");
        } else if (eventID == 2) {
            eventTitle.setText("Common event 2");
        } else if (eventID == 3) {
            eventTitle.setText("Common event 3");
        } else if (eventID == 4) {
            eventTitle.setText("Common event 4");
        } else if (eventID == 5) {
            eventTitle.setText("Common event 5");
        } else if (eventID == 6) {
            eventTitle.setText("Common event 6");
        } else {
            eventTitle.setText("Placeholder");
        }
        this.repaint();
        this.revalidate();
    }

} // End of GameFrame.java