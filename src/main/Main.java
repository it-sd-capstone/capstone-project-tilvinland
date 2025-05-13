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
    static PreparedStatement statementScore;
    static PreparedStatement statementNewMember;
    static PreparedStatement statementSaveOverwrite;
    static PreparedStatement statementSave;
    static PreparedStatement statementParty;
    static PreparedStatement statementFetchEvent;
    static PreparedStatement statementFetchEventName;

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
    private static int activePlayers;

    //Runs the main game window from the GameFrame.java class
    private static GameFrame mainFrame = new GameFrame();



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
        initializeLists();
    }

    public static Connection createConnection() {
        Connection result = null;
        try {
            result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            Statement command = result.createStatement();
            command.setQueryTimeout(TIMEOUT_STATEMENT_S);

            // Create the tables if needed.
            /* ------- Statuses Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS statuses (statusId INTEGER PRIMARY KEY AUTOINCREMENT, statusName TEXT NOT NULL, healthEffect INTEGER NOT NULL)");
            // Fills statuses table
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Healthy', 0)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Hungry', -1)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Injured', -5)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Wounded', -10)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Deceased', -100)");

            /* ------- Party Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS party (memberNum INTEGER PRIMARY KEY AUTOINCREMENT, health INTEGER NOT NULL, name TEXT NULL, statusId INTEGER NOT NULL, FOREIGN KEY (statusId) REFERENCES statuses(statusId))");

            /* ------- Locations Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS locations (locationsId INTEGER PRIMARY KEY AUTOINCREMENT, locationName TEXT NOT NULL, locationDiff INTEGER NOT NULL, " +
                    "locationDesc TEXT NOT NULL, locationType INTEGER NOT NULL, shop INTEGER KEY)");
            // Fills locations table
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Lindisfarne Monastery', 1, 'northeast England, wealthy Monastery, site of first famous Viking raid.', " +
                    "1)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Circinn Coast', 2, 'Pictish kingdom on east coast of Scotland, heavily defended Pict settlements were tough targets compared to the isolated monasteries.', " +
                    "2)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Orkney Islands', 3, 'Islands on northern tip of Scotland, were raided often by Norwegian Vikings after Lindisfarne.', 3)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Hebrides Islands', 4, 'Islands in western Scotland, often visited and raided by Vikings after Lindisfarne.', 4)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Iona Abbey', 5, 'Western Scotland, wealthy abbey, was raided at least 3 times after the Lindisfarne raid.', 5)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Shetland Islands', 6, 'Island chain north of Orkney Islands, not only raided early by Vikings, but settled early as well.', 6)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Faroe Islands', 7, 'Island chain north of Shetland, was only settled by a few Irish monks until the Vikings settled it in the early/mid 800''s.', 7)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Iceland', 8, 'Called Snowland by the first Viking who landed there, was also only settled by a few Irish monks who fled the island when they arrived, was not " +
                    "settled till mid/late 800''s.', 8)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Greenland', 9, 'Was not settled till the late 900''s, may have had Paleo-Eskimo tribes on the island but no record of Viking contact.', 9)");
            command.executeUpdate("INSERT INTO locations (locationName, locationDiff, locationDesc, locationType) VALUES ('Vinland', 10, 'We have made to New Land!', 10)");

            /* ------- Events Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS events (eventId INTEGER PRIMARY KEY AUTOINCREMENT, eventName TEXT NOT NULL, eventDesc TEXT NOT NULL, eventType TEXT NOT NULL)");

            // Fills events table
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Start', 'We are about to make a long voyage across the seas. We better stock up on any supplies we will need for the long trip.', 'Shop')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Vinland', 'Fresh lands where we are going to settle. Time to set up a base camp so we can get working shelter and food so we can make it through the coming winter.', 'End')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Scotland', 'A Scottish Village can be seen on the coast and might not be keen to see you near their village. We can stop here to repair our ship or raid them for supplies. If we do raid " +
                    "them for supplies it might be a challenge.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Iceland', 'An island village that welcomes you to their village. This might be a good time to restock on supplies or repair any damage that the ship has sustained.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Greenland', 'A rocky coast line where large animals can be seen from the shore. We could stock up on more food or repair our ship for the voyage ahead.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Calm Seas', 'With how calm the seas are today it seems like a good time to fish for some food or repair our ship.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Rough Seas', 'Huge waves have made travel difficult. If we are not careful we might damage our ship or worse injure ourselves.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Storm', 'A storm has rolled in. With the huge swells and rain, travel will be a challenge. We can push through and risk death or play it safe and sail another day.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Village', 'A small coastal village that might have supplies we could use. How we acquire these supplies is up to you.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Forest', 'A wooded coastline that would be perfect for finding more food or getting some lumber for ship repairs.', 'Land')");

            /* ------- Enemy Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS enemy (enemyId INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(30) NOT NULL, health INT NOT NULL)");

            // Fills enemy table
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Knight', 150)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Village Man', 50)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Gaurd Captin', 100)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Bear', 100)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Thief', 70)"));

            /* ------- Saves & Score Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS saves (saveId INTEGER PRIMARY KEY AUTOINCREMENT, saveName TEXT NOT NULL, locationsId INTEGER NOT NULL, eventsId INTEGER NOT NULL, memberNum INTEGER NOT NULL, " +
                    "FOREIGN KEY (locationsId) REFERENCES locations(locationsId), FOREIGN KEY (eventsId) REFERENCES events(eventsId), FOREIGN KEY (memberNum) REFERENCES party(memberNum))");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS score (scoreId INTEGER PRIMARY KEY AUTOINCREMENT, score INT NOT NULL, time TEXT NOT NULL)");

            // Set SQL statement
            statementScore = result.prepareStatement("INSERT INTO score (score, time) VALUES (?, date())");
            statementNewMember = result.prepareStatement("INSERT INTO party (statusId, health, name) VALUES (1, 100, ?)");
            statementSave = result.prepareStatement("INSERT INTO saves (saveName, locationsId, eventsId, memberNum) VALUES (?, ?, ?, ?)");
            statementSaveOverwrite = result.prepareStatement("UPDATE saves SET saveName = ?, locationsId = ?, eventsId = ?, memberNum = ? WHERE saveId = ?");
            statementParty = result.prepareStatement("UPDATE party SET health = ? WHERE memberNum = ?");
            statementFetchEventName = result.prepareStatement("SELECT eventName FROM events WHERE eventId = ?");
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
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                currentEvent = 1;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 1) {
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                currentEvent = 2;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 2) {
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                currentEvent = 3;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 3) {
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                currentEvent = 4;
                mainEventTotal++;
                eventTotal++;
            } else if (mainEvent == 4) {
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                currentEvent = 5;
                mainEventTotal++;
                eventTotal++;
            }
        } else {
            currentEvent = Math.max(6, 5 + rng.nextInt(6));
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            eventTotal++;
        }
    }

    public static void initializeLists() {
        party.clear();
        party.add(new Player("", 0, 0));
        party.add(new Player("", 1, 0));
        party.add(new Player("", 2, 0));
        party.add(new Player("", 3, 0));

        items.clear();
        items.add(new Item("Gold", 0, "Used to trade for materials", 100));
        items.add(new Item("Rations", 1, "Food", 100));
        items.add(new Item("Lumber", 2, "Used to fix ships", 100));
    }

    public static void createParty(String name, int id, int active) {
        if (name != null) {
            party.get(id).setName(name);
            party.get(id).setActive(active);
        }

    }

    public static void runEvent(int event) { // Noncombat events
        if (event == 1) {
            // Shop events 1 - 4
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.WELSHOP);
            //TODO
        } else if (event == 2) {
            // Buy lumber
            addInventory(2, 5);
            removeItem(0, 10);
        } else if (event == 3) {
            // buy Rations
            addInventory(1, 10);
            removeItem(0, 10);
        } else if (event == 4) {
            // Return to event
            //TODO
        } else if (event == 5) {
            // Run next event
            removeItem(1, activePlayers * 5);
            decideEvent(eventTotal, mainEventTotal);
        } else if (event == 6) {
            // fishing
            int itemTOAdd = rng.nextInt(10);
            addInventory(1, 10);
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        } else if (event == 7) {
            // hunker down
            ship.removeHealth(10);
            if (ship.getHealth() <= 0) {
                score = totalScore(createConnection(), party, items, ship);
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            }
        } else if (event == 8) {
            // push through rough waters
            ship.removeHealth(20);
            if (ship.getHealth() <= 0) {
                score = totalScore(createConnection(), party, items, ship);
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            }
        } else if (event == 9) {
            // push through heavy storm
            ship.removeHealth(30);
            if (ship.getHealth() <= 0) {
                score = totalScore(createConnection(), party, items, ship);
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            }
        } else if (event == 10) {
            // repair ship
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        } else if (event == 11) {
            ship.addHealth(1);
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        } else if (event == 12) {
            // hunt
            int itemTOAdd = rng.nextInt(10);
            addInventory(1, 10);
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        } else if (event == 13) {
            // CHopping wood
            int itemTOAdd = rng.nextInt(10);
            addInventory(2, 10);
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        } else if (event == 14) {
            // END
            score = totalScore(createConnection(), party, items, ship);
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
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
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
                //TODO ADD ITEMS TO PLAYERS INVENTORY
            } else {
                //TODO refresh panel
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            }
        } else if (winner == 0) {

            do {
                partyMemberAtt = rng.nextInt(4) - 1;
            } while (party.get(partyMemberAtt).getActive() != 1 && party.get(partyMemberAtt).getStatus() != 1);

            party.get(partyMemberAtt).healthLoss(10);
            if (party.get(partyMemberAtt).getHealth() <= 0) {
                party.get(partyMemberAtt).setHealth(0);
                party.get(partyMemberAtt).setStatus(2);
            }
            if (partyWipe(party)) {
                score = totalScore(createConnection(), party, items, ship);
                //TODO move to party wipe panel
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            } else {
                //TODo refresh Screen
                mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
            }
        } else {
            // TODO refresh Screen
            mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
        }

    }

    public static boolean partyWipe(ArrayList<Player> party) {
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

    public static int totalScore(Connection db, ArrayList<Player> party, ArrayList<Item> items, Ship ship) {
        int totalhealth = 0;
        int totalItems = 0;
        int score = 0;

        for (int i = 0; i < party.size(); i++) {
            totalhealth += party.get(i).getHealth();
        }

        for (int i = 0; i < items.size(); i++) {
            totalItems += items.get(i).getAmount();
        }
        score = (totalhealth * 5) + (totalItems * 4) + (ship.getHealth() * 10);
        try {
            statementScore.setInt(1, score);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return score;
    }

    public static void startCombat(int enemyID, Connection db) {

        try {
            PreparedStatement enemyStmt = db.prepareStatement("SELECT * FROM enemy WHERE enemyId = ?");
            enemyStmt.setInt(1, enemyID);

            ResultSet rs = enemyStmt.executeQuery();

            if (rs.next()) {
                enemy.setName(rs.getString("name"));
                enemy.setHealth(rs.getInt("health"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to start combat");
            e.printStackTrace();
        }
        //TODO change to combat panel
        mainFrame.cardLayout.show(mainFrame.deck, mainFrame.MAIN); //TODO
    }

    public static void saveGame() {
        //TODO
    }

    public static void loadGame() {
        //TODO
    }

    // Adds starter items to player's inventory using items table in database
    // IDK the ID/Name yet so I just used these as placeholders, will adjust later -JH
    public static void addInventory(int id, int amount) {
        items.get(id).addItem(amount);
    }


    public static void removeItem(int id, int amount) {
        items.get(id).removeItem(amount);
    }

    public static int checkInventory(int id) {
        return items.get(id).getAmount();
    }

}

