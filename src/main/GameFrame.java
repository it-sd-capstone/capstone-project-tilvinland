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
    String SHOP = "Shop";
    String STATS = "Ship Status";
    String DBSTATS = "Debug Status";
    String EVENT = "Event";

    String START = "Start Area";
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

    ImageIcon borderDecoTL = new ImageIcon("./resources/CornerTopLeft.png");
    ImageIcon borderDecoTR = new ImageIcon("./resources/CornerTopRight.png");

    ImageIcon lumberIcon = new ImageIcon("./resources/lumberPile.png");

    private String rescourceLabel = "";

    public GameFrame() {
        // Reference Main and Database
        //Main main = new Main();
        Connection db = Main.createConnection();

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
        //TODO add a heal party button?? (-rations)
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

                Main.createParty(partyNameOneInput, 0);
                Main.createParty(partyNameTwoInput, 1);
                Main.createParty(partyNameThreeInput, 2);
                Main.createParty(partyNameFourInput,3);

                Main.runEvent(5);
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
                Main.runEvent(5);
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
                Main.runEvent(3);
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
                Main.runEvent(2);
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


        /* ------------ COMBAT EVENT SCREEN ------------ */
        JPanel combatEvent = new JPanel(new BorderLayout());

        JPanel combatControls = new JPanel(new GridBagLayout());
        combatControls.setBackground(cerulean);
        combatControls.setPreferredSize(new Dimension(200,200));

        JPanel combatContent = new JPanel(new GridBagLayout());
        eventContent.setBackground(gunmetal);
        String labelText = "";
        if (Main.getParty().get(0).getActive() == 1) {
            labelText = "<html>" + Main.getParty().get(0).getName() + "<br/>Health: " + Main.getParty().get(0).getHealth() + "</html>";
        }
        JLabel p1Status = new JLabel();
        p1Status.setText(labelText);
        p1Status.setFont(new Font("Monospaced", Font.BOLD, 30));

        if (Main.getParty().get(1).getActive() == 1) {
            labelText = "<html>" + Main.getParty().get(1).getName() + "<br/>Health: " + Main.getParty().get(1).getHealth() + "</html>";
        }
        JLabel p2Status = new JLabel();
        p2Status.setText(labelText);
        p2Status.setFont(new Font("Monospaced", Font.BOLD, 30));

        JLabel p3Status = new JLabel();
        if (Main.getParty().get(2).getActive() == 1) {
            labelText = "<html>" + Main.getParty().get(2).getName() + "<br/>Health: " + Main.getParty().get(2).getHealth() + "</html>";
            p3Status.setText(labelText);
        }
        p3Status.setFont(new Font("Monospaced", Font.BOLD, 30));

        JLabel p4Status = new JLabel();
        if (Main.getParty().get(3).getActive() == 1) {
            labelText = "<html>" + Main.getParty().get(3).getName() + "<br/>Health: " + Main.getParty().get(3).getHealth() + "</html>";
            p4Status.setText(labelText);
        }

        p4Status.setFont(new Font("Monospaced", Font.BOLD, 30));

        labelText = "<html>" + Main.getEnemy().getName() + "<br/>Health: " + Main.getEnemy().getHealth() + "</html>";

        JLabel enemyStatus = new JLabel();
        enemyStatus.setText(labelText);
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

        attackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.runEvent(5);
            }
        });

        endCombat.add(endCombatStatus,BorderLayout.CENTER);
        endCombat.add(endCombatControl,BorderLayout.SOUTH);

        /* ------------ PLACEHOLDER EVENT SCREEN ------------ */
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
        // Start Area Event ------------------------------
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

        JButton eventOptionSail = new JButton("Set Sail");
        eventOptionSail.setPreferredSize(new Dimension(200,50));

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
        eventLocationControls.add(eventOptionSail, gbc);

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

        // ScotLand Event ------------------------------
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

        // Iceland Event ------------------------------
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

        // Greenland Event ------------------------------
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

        // Vinland Event ------------------------------
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
        calmFish.addActionListener(e -> Main.runEvent(6)); // fishing
        calmRepair.addActionListener(e -> Main.runEvent(10)); // repair ship
        calmSailing.addActionListener(e -> Main.runEvent(5)); // continue sailing

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
        deck.add(calmPanel, CALM);
        /* -------------- End of Calm Seas Panel ------------ */

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
        roughWait.addActionListener(e -> Main.runEvent(7)); // hunker down
        roughFish.addActionListener(e -> Main.runEvent(6)); // fish
        roughSail.addActionListener(e -> Main.runEvent(8)); // continue sailing

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
        deck.add(roughPanel, ROUGH);
        /* -------------- End of Rough Seas Panel ------------ */

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
        stormWait.addActionListener(e -> Main.runEvent(7)); // hunker down
        stormSail.addActionListener(e -> Main.runEvent(9)); // storm event

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
        deck.add(stormPanel, STORM);
        /* -------------- End of Storm Panel ----------------- */

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
        JLabel villageTitle = new JLabel(Main.eventTitles.get(8));
        villageTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        villageTitle.setForeground(alertorange);

        // Village Text area
        JTextArea villageDesc = new JTextArea(Main.eventDescriptions.get(8));
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
        villageShop.addActionListener(e -> Main.runEvent(1)); // shop
        villageRepair.addActionListener(e -> Main.runEvent(10)); // repair?
        villageSail.addActionListener(e -> Main.runEvent(5)); // continue sailing

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
        deck.add(villagePanel, VILLAGE);
        /*--------------- End of Village Panel ------------------ */

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
        JLabel forestTitle = new JLabel(Main.eventTitles.get(9));
        forestTitle.setFont(new Font("Monospaced", Font.BOLD, 40));
        forestTitle.setForeground(alertorange);

        // Forest Test Area
        JTextArea forestDesc = new JTextArea(Main.eventDescriptions.get(9));
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
        forestWood.addActionListener(e -> Main.runEvent(13)); // Wood
        forestHunt.addActionListener(e -> Main.runEvent(12)); // Hunt
        forestSail.addActionListener(e -> Main.runEvent(5));  // Continue Sailing

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
        deck.add(forestPanel, FOREST);

        /* ------------ Ship Sank Panel ------------ */
        JPanel shipSankPanel = new JPanel(new BorderLayout());

        JPanel sankContent = new JPanel(new GridBagLayout());
        sankContent.setBackground(gunmetal);

        JPanel sankControls = new JPanel(new GridBagLayout());
        sankControls.setPreferredSize(new Dimension(200, 200));
        sankControls.setBackground(cerulean);

        GridBagConstraints gbcSank = new GridBagConstraints();

        //TODO add JLabel to display final score

        JLabel sankTitle = new JLabel("Your ship has sank");

        JTextArea sankDesc = new JTextArea("Your ship took much damage and rests with the Kraken.");

        JButton sankRestart = new JButton("Play Again?");
        JButton sankQuit = new JButton("Quit");

        // Brings player back to main menu panel
        sankRestart.addActionListener(e -> switchToPanel(MAIN));
        // Closes the program
        sankQuit.addActionListener(e -> this.dispose());

        // Build Sank Panel
        shipSankPanel.add(sankContent, BorderLayout.CENTER);
        shipSankPanel.add(sankControls, BorderLayout.SOUTH);

        /* ------------ Party Wipe Panel ------------ */
        JPanel partyWipePanel = new JPanel();

        JPanel wipeContent = new JPanel(new GridBagLayout());
        wipeContent.setBackground(gunmetal);

        JPanel wipeControls = new JPanel(new GridBagLayout());
        wipeControls.setPreferredSize(new Dimension(200, 200));
        wipeControls.setBackground(cerulean);

        GridBagConstraints gbcWipe = new GridBagConstraints();

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

        /* ------------ Resource Panel ------------ */

        JPanel resourcesPanel = new JPanel(new BorderLayout());

        JPanel resourcesContent = new JPanel(new GridBagLayout());
        resourcesContent.setBackground(gunmetal);

        JPanel resourcesControls = new JPanel(new GridBagLayout());
        resourcesControls.setPreferredSize(new Dimension(200, 200));
        resourcesControls.setBackground(cerulean);

        GridBagConstraints gbcResources = new GridBagConstraints();

        JLabel resourcesDesc = new JLabel(rescourceLabel);

        JButton resourcesButton = new JButton("Set Sail");

        gbcResources.gridx = 0;
        gbcResources.gridy = 0;
        resourcesContent.add(resourcesDesc, gbcResources);
        gbcResources.gridy = 0;
        gbcResources.gridx = 0;
        resourcesControls.add(resourcesButton, gbcResources);

        resourcesButton.addActionListener(e -> Main.runEvent(5));

        resourcesPanel.add(resourcesContent, BorderLayout.CENTER);
        resourcesPanel.add(resourcesControls, BorderLayout.SOUTH);



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
        deck.add(resourcesContent, CONFRIM);

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
                } else if (seedInputText.equals("DEBUGCOMBAT")) { //User leaves the field blank
                    //Temporary, should run the game - switch to the first game screen
                    switchToPanel(COMBAT);
                } else { //User leaves the field blank
                    //Temporary, should run the game - switch to the first game screen
                    Main.setSeed(seedInputText);
                    switchToPanel(PARTY);
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
            public void actionPerformed(ActionEvent e) {}
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

                Main.runEvent(5); // Random Event?

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
        back.setForeground(columbiablue);
        back.setBackground(darkslate);
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

    public void resourceChanges(String resourceName, int itemID) {
        rescourceLabel = "After gathering you now have " + Main.getItems().get(itemID).getAmount() + " " + resourceName;
    }

} // End of GameFrame.java
