package main;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
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
            /* ------- Statuses Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS statuses (statusId INTEGER PRIMARY KEY AUTOINCREMENT, statusName TEXT NOT NULL, healthEffect INTEGER NOT NULL)");
            // Fills statuses table
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Healthy', 0)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Hungry', -1)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Injured', -5)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Wounded', -10)");
            command.executeUpdate("INSERT INTO statuses (statusName, healthEffect) VALUES ('Deceased', -100)");

            /* ------- Party Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS party (memberNum INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY (statusId) REFERENCES statuses(statusId), health INTEGER NOT NULL, name TEXT NULL)");

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
            // Static event data 1-5
            command.executeUpdate("INSERT INTO events (eventId, eventName, eventDesc, eventType) VALUES (1, ?, ?, ?)");
            command.executeUpdate("INSERT INTO events (eventId, eventName, eventDesc, eventType) VALUES (2, ?, ?, ?)");
            command.executeUpdate("INSERT INTO events (eventId, eventName, eventDesc, eventType) VALUES (3, ?, ?, ?)");
            command.executeUpdate("INSERT INTO events (eventId, eventName, eventDesc, eventType) VALUES (4, ?, ?, ?)");
            command.executeUpdate("INSERT INTO events (eventId, eventName, eventDesc, eventType) VALUES (5, ?, ?, ?)");

            // Fills events table
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Start', 'We are about to make a long voyage across the seas. We better stock up on any supplies we will need for the long trip.', 'Shop')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Vinland', 'Fresh lands where we are going to settle. Time to set up a base camp so we can get working shelter and food so we can make it through the coming winter.', 'End')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Calm Seas', 'With how calm the seas are today it seems like a good time to fish for some food or repair our ship.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Rough Seas', 'Huge waves have made travel difficult. If we are not careful we might damage our ship or worse injure ourselves.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Storm', 'A storm has rolled in. With the huge swells and rain, travel will be a challenge. We can push through and risk death or play it safe and sail another day.', 'Sea')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Village', 'A small coastal village that might have supplies we could use. How we acquire these supplies is up to you.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Forest', 'A wooded coastline that would be perfect for finding more food or getting some lumber for ship repairs.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Scotland', 'A Scottish Village can be seen on the coast and might not be keen to see you near their village. We can stop here to repair our ship or raid them for supplies. If we do raid " +
                    "them for supplies it might be a challenge.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Iceland', 'An island village that welcomes you to their village. This might be a good time to restock on supplies or repair any damage that the ship has sustained.', 'Land')");
            command.executeUpdate("INSERT INTO events (eventName, eventDesc, eventType) VALUES ('Greenland', 'A rocky coast line where large animals can be seen from the shore. We could stock up on more food or repair our ship for the voyage ahead.', 'Land')");

            /* ------- Saves & Score Table ------- */
            command.executeUpdate("CREATE TABLE IF NOT EXISTS saves (saveId INTEGER PRIMARY KEY AUTOINCREMENT, saveName TEXT NOT NULL, FOREIGN KEY (locationsId) REFERENCES locations(locationsId), " +
                    "FOREIGN KEY (eventsId) REFERENCES events(eventsId), FOREIGN KEY (memberNum) REFERENCES party(memberNum))");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS score (scoreId INTEGER PRIMARY KEY AUTOINCREMENT, player TEXT NULL, score INTEGER NOT NULL, time TEXT NOT NULL)");

            // Set SQL statement
            statementScore = result.prepareStatement("INSERT INTO score (player, score, time) VALUES (?, ?, date())");
            statementNewMember = result.prepareStatement("INSERT INTO party (statusId, health, name) VALUES (1, 100, ?)");
            statementSave = result.prepareStatement("INSERT INTO saves (saveName, locationsId, eventsId, memberNum) VALUES (?, ?, ?, ?)");
            statementSaveOverwrite = result.prepareStatement("UPDATE saves SET saveName = ?, locationsId = ?, eventsId = ?, memberNum = ? WHERE saveId = ?");
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
}