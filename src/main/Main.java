package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.controllers.InputOutput;
import main.models.Session;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private static final int TIMEOUT_STATEMENT_S = 5;


    private static Random rng = new Random();
    //TODO dont need these
    private static int eventTotal;
    private static int mainEventTotal;

    private static int weight;
    private static int eventCounter = 1;

    private static int currentEvent;
    private static Ship ship = new Ship();
    private static int score;
    private static Enemy enemy;
    private static ArrayList<Player> party = new ArrayList<Player>();
    private static ArrayList<Item> items = new ArrayList<Item>();
    private static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private static int activePlayers;
    private static String seedUsed;
    public static Map<Integer, String> eventTitles = new HashMap<>();
    public static Map<Integer, String> eventDescriptions = new HashMap<>();

    //Runs the main game window from the GameFrame.java class
    public static GameFrame mainFrame;


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
        setShip(ship = new Ship());
        initializeLists();
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = "./resources/Data/session.json";
        File sessionFile = new File(filePath);
        try{
            if (!sessionFile.exists()) {
                sessionFile.createNewFile();
                Session defaultSession = new Session(
                        null,
                        100,
                        new String[] {null},
                        new int[] {0},
                        new int[] {0}
                );
                objectMapper.writeValue(sessionFile, defaultSession);
            } else {
                objectMapper.readValue(sessionFile, Session.class);
            }

        } catch (Exception e) {
            System.out.println("Session file corrupt or unreadable. Restoring...");
            restoreSession();
        }
        //Initiate the game frame
        mainFrame = new GameFrame();
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
            command.executeUpdate("CREATE TABLE IF NOT EXISTS enemy (enemyId INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(30) NOT NULL, health INT NOT NULL)");

            // Fills enemy table
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Knight', 150)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Village Man', 50)"));
            command.executeUpdate(("INSERT INTO enemy (name, health) VALUES ('Guard Captain', 100)"));
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

            // Load event titles and descriptions from database
            Statement fetchEvents = result.createStatement();
            ResultSet rs = fetchEvents.executeQuery("SELECT eventId, eventName, eventDesc FROM events");

            while (rs.next()) {
                int id = rs.getInt("eventId");
                eventTitles.put(id, rs.getString("eventName"));
                eventDescriptions.put(id, rs.getString("eventDesc"));
            }
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
        rng = null;
        if (input != null) {
            long seed = input.hashCode();
            //rng.setSeed(seed);
            rng = new Random(seed);
            seedUsed = input;
        } else {
            rng = new Random(System.currentTimeMillis());
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

        enemies.clear();
        try {
            Connection db = createConnection();  // or use an existing cached connection
            ResultSet rs = queryRaw(db, "SELECT * FROM enemy");
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("enemyId");
                int health = rs.getInt("health");
                enemies.add(new Enemy(name, id, health));
            }
            rs.close();
            db.close();
        } catch (Exception e) {
            System.err.println("Error loading enemies: " + e.getMessage());
            e.printStackTrace();
        }

        if (!enemies.isEmpty()) {
            enemy = enemies.get(0);
        } else {
            enemy = new Enemy("null", 0, 1);
        }

        mainEventTotal = 0;
        eventTotal = 0;
    }

    public static void createParty(String name1, String name2, String name3, String name4) {

        party.clear();
        if (!name1.isBlank()) {
            party.add(new Player(name1, 0, 1));
        } else {
            party.add(new Player("", 0, 0));
        }
        if (!name2.isBlank()) {
            party.add(new Player(name2, 1, 1));
        } else {
            party.add(new Player("", 1, 0));
        }
        if (!name3.isBlank()) {
            party.add(new Player(name3, 2, 1));
        } else {
            party.add(new Player("", 2, 0));
        }
        if (!name4.isBlank()) {
            party.add(new Player(name4, 3, 1));
        } else {
            party.add(new Player("", 3, 0));
        }




    }

    public static void runCombat(int choice) {
        int computerChoice;
        int winner = 0;
        int partyMemberAtt = 0;
        do {
            computerChoice = rng.nextInt(4);
        } while (computerChoice < 1 || computerChoice > 3 );

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
                addInventory(0, 20);
                addInventory(1, 40);
                addInventory(2, 10);
                mainFrame.switchToPanel(mainFrame.FCOMBAT);
            } else {
                mainFrame.switchToPanel(mainFrame.COMBAT);
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
                mainFrame.switchToPanel(mainFrame.WIPE);
            } else {
                mainFrame.switchToPanel(mainFrame.COMBAT);
            }
        } else {
            mainFrame.switchToPanel(mainFrame.COMBAT);
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
            statementScore.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return score;
    }

    public static void startCombat(int enemyID, Connection db) {

        enemy.setName(enemies.get(0).getName());
        enemy.setHealth(enemies.get(0).getHealth());
        mainFrame.switchToPanel(mainFrame.COMBAT);
    }

    // Save logic
    public static void saveGame(String saveName) {
        try (Connection db = createConnection()) {

            // Prepare SQL statements
            PreparedStatement saveStmt = db.prepareStatement(
                    "INSERT INTO saves (saveName, currenteventID, totalEvents, totalMainEvents, " +
                            "player1_status, player1_health, player2_status, player2_health, " +
                            "player3_status, player3_health, player4_status, player4_health, " +
                            "seed, gold, lumber, rations, shiphealth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            // Save statements
            saveStmt.setString(1, saveName);
            saveStmt.setInt(2, currentEvent);
            saveStmt.setInt(3, eventTotal);
            saveStmt.setInt(4, mainEventTotal);
            saveStmt.setInt(5, party.get(0).getStatus());    // player1_status
            saveStmt.setInt(6, party.get(0).getHealth());    // player1_health
            saveStmt.setInt(7, party.get(1).getStatus());    // player2_status
            saveStmt.setInt(8, party.get(1).getHealth());    // player2_health
            saveStmt.setInt(9, party.get(2).getStatus());    // player3_status
            saveStmt.setInt(10, party.get(2).getHealth());   // player3_health
            saveStmt.setInt(11, party.get(3).getStatus());   // player4_status
            saveStmt.setInt(12, party.get(3).getHealth());   // player4_health
            saveStmt.setString(13, seedUsed);

            // Checks inventory for items
            int gold = 0, lumber = 0, rations = 0;
            for (Item item : items) {
                switch (item.getId()) {
                    case 1 -> lumber = item.getAmount();
                    case 2 -> gold = item.getAmount();
                    case 3 -> rations = item.getAmount();
                }
            }

            // Moar save statements
            saveStmt.setInt(14, gold);
            saveStmt.setInt(15, lumber);
            saveStmt.setInt(16, rations);
            saveStmt.setInt(17, ship.getHealth());

            // Execute save
            saveStmt.executeUpdate();
            System.out.println("Game saved!");

        } catch (Exception e) {
            System.err.println("Save game failed!");
            e.printStackTrace();
        }
    }

    // Load game logic
    public static void loadSave() {
        try (Connection db = createConnection()) {
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM saves ORDER BY saveId DESC LIMIT 1");

            if (rs.next()) {
                // Adding event values
                currentEvent = rs.getInt("currenteventID");
                eventTotal = rs.getInt("totalEvents");
                mainEventTotal = rs.getInt("totalMainEvents");
                party.clear();

                // Adding player values
                Player p1 = new Player("Player 1", 1, 1);
                p1.setStatus(rs.getInt("player1_status"));
                p1.setHealth(rs.getInt("player1_health"));
                party.add(p1);

                Player p2 = new Player("Player 2", 2, 1);
                p2.setStatus(rs.getInt("player2_status"));
                p2.setHealth(rs.getInt("player2_health"));
                party.add(p2);

                Player p3 = new Player("Player 3", 3, 1);
                p3.setStatus(rs.getInt("player3_status"));
                p3.setHealth(rs.getInt("player3_health"));
                party.add(p3);

                Player p4 = new Player("Player 4", 4, 1);
                p4.setStatus(rs.getInt("player4_status"));
                p4.setHealth(rs.getInt("player4_health"));
                party.add(p4);

                // adding seed value
                seedUsed = rs.getString("seed");

                // adding inventory items
                items.clear();
                items.add(new Item("Lumber", 1, "Used to repair the ship", rs.getInt("lumber")));
                items.add(new Item("Gold", 2, "Valuables raided from settlements", rs.getInt("gold")));
                items.add(new Item("Rations", 3, "Food for your crew", rs.getInt("rations")));

                // adding ship values
                ship = new Ship();
                ship.setHealth(rs.getInt("shiphealth"));

                // output
                System.out.println("Game loaded!");
            } else {
                System.out.println("No save game found!");
            }

        } catch (Exception e) {
            System.err.println("Load game failed!");
            e.printStackTrace();
        }

    }

    public static void restoreSession() {
        String filePath = "./resources/Data/session.json";
        File sessionFile = new File(filePath);
        if (sessionFile.exists()) {
            sessionFile.delete();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                sessionFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Session defaultSession = new Session(
                        null,
                        100,
                        new String[] {null},
                        new int[] {0},
                        new int[] {0}
                );
                objectMapper.writeValue(sessionFile, defaultSession);
                System.out.println("Restore session successful!");
            } catch (Exception e) {
                System.out.println("Failed to restore session");
            }
        }
    }

    // Adds starter items to player's inventory using items table in database
    public static void addInventory(int id, int amount) {
        items.get(id).addItem(amount);
    }

    public static void removeItem(int id, int amount) {
        items.get(id).removeItem(amount);
    }

    public static int checkInventory(int id) {
        return items.get(id).getAmount();
    }

    public static void getActivePlayers() {
        activePlayers = 0;
        for (int i = 0; i < party.size(); i++) {
            if (party.get(i).getActive() == 1) {
                activePlayers++;
            }
        }
    }

    // Accessors for testing
    public static ArrayList<Player> getParty() {
        return party;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static String getSeedUsed() {
        return seedUsed;
    }

    public static void setShip(Ship newShip) {
        ship = newShip;
    }

    public static Ship getShip() {
        return ship;
    }

    public static Enemy getEnemy() {
        return enemy;
    }

}