package main;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;
import java.sql.*;

public class Main {
    // Database and SQL variables
    public static final String DATABASE_NAME = "vinland";
    public static final String DATABASE_PATH = DATABASE_NAME + ".db";
    static PreparedStatement statementScoreBoard;
    static PreparedStatement statementCharStatus;
    static PreparedStatement statementParty;
    static PreparedStatement statementLocation;
    static PreparedStatement statementEvent;
    private static final int TIMEOUT_STATEMENT_S = 5;


    private static Random rng;
    private static int eventTotal;
    private static int mainEventTotal;
    private static int currentEvent;
    private static Ship ship;
    private static int score;
    private static Enemy enemy;
    private static ArrayList<Player> party = new ArrayList<Player>();
    private static ArrayList<Item> items = new ArrayList<Item>();

    private static class InputFilter extends DocumentFilter {
        private static final int MAX_LENGTH = 10;

        @Override
        public void insertString(FilterBypass fb, int offset, String stringToAdd, AttributeSet attr)
                throws BadLocationException {

            String text = fb.getDocument().getText(0, fb.getDocument().getLength());

            if (fb.getDocument() != null) {
                if ((fb.getDocument().getLength() + stringToAdd.length()) <= MAX_LENGTH && text.matches("^[a-zA-Z0-9]*$")) {
                    super.insertString(fb, offset, stringToAdd, attr);
                }

            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int lengthToDelete, String stringToAdd, AttributeSet attr)
                throws BadLocationException {
            if (fb.getDocument() != null) {
                if ((fb.getDocument().getLength() - lengthToDelete + stringToAdd.length()) <= MAX_LENGTH)
                    super.replace(fb, offset, lengthToDelete, stringToAdd, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public static void main(String[] args) {
        // Test if application boots properly
        System.out.println("Wattup");

        //Runs the main game window from the GameFrame.java class
        GameFrame mainFrame = new GameFrame();
    }

    public static Connection createConnection() {
        Connection result = null;
        try {
            result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            Statement command = result.createStatement();
            command.setQueryTimeout(TIMEOUT_STATEMENT_S);

            // Create the tables if needed.
//            command.executeUpdate("CREATE TABLE IF NOT EXISTS player (id INTEGER PRIMARY KEY, name TEXT NOT NULL, score INTEGER NOT NULL)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS statuses (statusId INTEGER PRIMARY KEY, statusName TEXT NOT NULL, healthEffect INTEGER NOT NULL)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS party (memberNum INTEGER PRIMARY KEY, statusId INTEGER FOREIGN KEY, health INTEGER NOT NULL, name TEXT NOT NULL)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS locations (locationsId INTEGER PRIMARY KEY, locationName TEXT NOT NULL, locationDiff INTEGER NOT NULL, " +
                    "locationDesc TEXT NOT NULL, locationType INTEGER NOT NULL, shop INTEGER KEY)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS events (eventId INTEGER PRIMARY KEY, eventName TEXT NOT NULL, eventDesc TEXT NOT NULL,eventType TEXT NOT NULL)");

            // Set SQL statement
//            statementScoreBoard = result.prepareStatement("UPDATE players SET score = ? WHERE id = ?");
            statementParty = result.prepareStatement("UPDATE party SET health = ? WHERE memberNum = ?");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet queryRaw(Connection db, String sql) {
        ResultSet result = null;
        try {
            Statement statement = db.createStatement();
            result = statement.executeQuery(sql);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static void setSeed(String input) {
        long seed = (long) input.hashCode();
        rng = new Random();
        rng.setSeed(seed);

    }

    public static void decideEvent(int event, int mainEvent) {
        if (event % 4 == 0) {
            if (mainEvent == 0) {
                //TODO Change to Start Event panel
                currentEvent = 1;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 1) {
                //TODO Change to British Isle Event panel
                currentEvent = 2;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 2) {
                //TODO Change to Iceland Event panel
                currentEvent = 3;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 3) {
                //TODO Change to Greenland Event panel
                currentEvent = 4;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 4) {
                //TODO Change to End event panel
                currentEvent = 5;
                mainEventTotal++;
                eventTotal++;
            }
        } else {
            currentEvent = Math.max(6, 5 + rng.nextInt(6));
            //TODO Change to selected random event
            eventTotal++;
        }
    }

    public static void createParty() {
        party.clear();
        String playerName ="";//TODO read player1 name
        if (playerName.equals("")) {
            party.add(new Player(playerName, 1, 0));
        } else {
            party.add(new Player(playerName, 1, 1));
        }
        playerName ="";//TODO read player2 name
        if (playerName.equals("")) {
            party.add(new Player(playerName, 1, 0));
        } else {
            party.add(new Player(playerName, 1, 1));
        }
        playerName ="";//TODO read player3 name
        if (playerName.equals("")) {
            party.add(new Player(playerName, 1, 0));
        } else {
            party.add(new Player(playerName, 1, 1));
        }
        playerName ="";//TODO read player4 name
        if (playerName.equals("")) {
            party.add(new Player(playerName, 1, 0));
        } else {
            party.add(new Player(playerName, 1, 1));
        }

    }

    public static void runEvent(int event) { // Noncombat events
        if (event == 1) {
            // Shop events 1 - 4
            //TODO open Shop window
        } else if (event == 2) {
            // Buy lumber
            //TODO add lumber
            //TODO take gold
        } else if (event == 3) {
            // buy Rations
            //TODO add Rations
            //TODO take gold
        } else if (event == 4) {
            // Return to event
            //TODO swap back to current event
        } else if (event == 5) {
            // Run next event
            //TODO take rations from inventory
            decideEvent(eventTotal, mainEventTotal);
        } else if (event == 6) {
            // fishing
            int itemTOAdd = rng.nextInt(10);
            //TODO add Rations
            //TODO move to panel showing added food // maybe
        } else if (event == 7) {
            // hunker down
            ship.removeHealth(10);
            if (ship.getHealth() <= 0) {
                score = totalScore();
                //TODO move to Sank ship end screen
            }
        } else if (event == 8) {
            // push through rough waters
            ship.removeHealth(20);
            if (ship.getHealth() <= 0) {
                score = totalScore();
                //TODO move to Sank ship end screen
            }
        } else if (event == 9) {
            // push through heavy storm
            ship.removeHealth(30);
            if (ship.getHealth() <= 0) {
                score = totalScore(createConnection());
                //TODO move to Sank ship end screen
            }
        } else if (event == 10) {
            // repair ship
            //TODO change to ship repair menu
        } else if (event == 11) {
            ship.addHealth(1);
            //TODO refresh window
        } else if (event == 12) {
            // hunt
            int itemTOAdd = rng.nextInt(10);
            //TODO add Rations
            //TODO move to panel showing added food // maybe
        } else if (event == 13) {
            // CHopping wood
            int itemTOAdd = rng.nextInt(10);
            //TODO add lumber
            //TODO move to panel showing added lumber // maybe
        } else if (event == 14) {
            // END
            score = totalScore(createConnection());
            //TODO move to win screen
        } else if (event == 15) {
            // Start combat
            int enemyId = 0;
            if (mainEventTotal == 1 && eventTotal == 4) {
                enemyId = 1;
            } else {
                enemyId = 1 + rng.nextInt(10);
            }
            startCombat(enemyId, createConnection());
        }
    }

    public static void runCombat(int choice) {
        int computerChoice;
        int winner = 0;
        int partyMemberAtt = 0;
        do {
            computerChoice = rng.nextInt(3);
        } while (computerChoice > 0 && computerChoice <= 3 );

        if (computerChoice == 1 && choice == 2) {
            winner = 1;
        } else if (computerChoice == 1 && choice == 3) {
            winner = 0;
        } else if (computerChoice == 2 && choice == 3) {
            winner = 1;
        } else if (computerChoice == 2 && choice == 1) {
            winner = 0;
        } else if (computerChoice == 3 && choice == 1) {
            winner = 1;
        } else if (computerChoice == 3 && choice == 2) {
            winner = 0;
        } else {
            winner = 2;
        }

        if (winner == 1) {
            enemy.removeHealth(10);
            if (enemy.isDefeated()) {
                //TODO move to finish panel
            } else {
                //TODO refresh panel
            }
        } else if (winner == 0) {

            do {
                partyMemberAtt = rng.nextInt(4) - 1;
            } while (party.get(partyMemberAtt).getActive() != 1);

            party.get(partyMemberAtt).healthLoss(10);
            if (party.get(partyMemberAtt).getHealth() < 0) {
                party.get(partyMemberAtt).setHealth(0);
            }
            if (partyWipe()) {
                score = totalScore(createConnection());
                //TODO move to party wipe panel
            } else {
                //TODo refresh Screen
            }
        } else {
            // TODO refresh Screen
        }

    }

    public static boolean partyWipe() {
        boolean wipe = false;
        for (int i = 0; i < party.size(); i++) {
            if (party.get(i).getHealth() > 0) {
                return false;
            } else {
                wipe = true;
            }
        }
        return wipe;
    }

    public static int totalScore(Connection db) {
        int totalhealth = 0;
        int totalItems = 0;

        for (int i = 0; i < party.size(); i++) {
            totalhealth += party.get(i).getHealth();
        }

        for (int i = 0; i < items.size(); i++) {
            totalItems += items.get(i).getAmount();
        }

        return (totalhealth * 5) + (totalItems * 4) + (ship.getHealth() * 10);
    }

    public static void startCombat(int enemyID, Connection db) {
        //TODO update enemy to match at database id enemyID
        //TODO change to combat panel
    }




}
