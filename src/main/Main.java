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
    static PreparedStatement statementScoreBoard;
    static PreparedStatement statementCharStatus;
    static PreparedStatement statementParty;
    static PreparedStatement statementLocation;
    static PreparedStatement statementEvent;
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
            command.executeUpdate("CREATE TABLE IF NOT EXISTS statuses (statusId INTEGER PRIMARY KEY, statusName TEXT NOT NULL, healthEffect INTEGER NOT NULL)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS party (memberNum INTEGER PRIMARY KEY, FOREIGN KEY (statusId) REFERENCES statuses(statusId), health INTEGER NOT NULL, name TEXT NULL)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS locations (locationsId INTEGER PRIMARY KEY, locationName TEXT NOT NULL, locationDiff INTEGER NOT NULL, " +
                    "locationDesc TEXT NOT NULL, locationType INTEGER NOT NULL, shop INTEGER KEY)");
            command.executeUpdate("CREATE TABLE IF NOT EXISTS events (eventId INTEGER PRIMARY KEY, eventName TEXT NOT NULL, eventDesc TEXT NOT NULL,eventType TEXT NOT NULL)");
            command.executeUpdate("INSERT INTO ");

            // Set SQL statement
//            "INSERT INTO statuses (statusId, statusName, healthEffect) VALUES (1, 'Wounded', -10)" <- ex: To fill in table
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