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

    // GUI variables
    static JPanel deck;
    static JLabel labelReason;
    static JLabel labelUser;
    static JLabel labelState;
    static JButton buttonAcknowledge;
    private static final String CARD_MAIN = "Main";

    public static void main(String[] args) {
        // Test if application boots properly
        System.out.println("Wattup");

        //Test JFrame GUI initialization
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setPreferredSize(new Dimension(960, 540));
        frame.setMaximumSize(new Dimension(1920, 1080));

        deck = new JPanel(new CardLayout());
        Font fontMain = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

        // Main panel
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));
        panelMain.setMinimumSize(new Dimension(320, 240));
        panelMain.setPreferredSize(new Dimension(960, 540));
        panelMain.setMaximumSize(new Dimension(1920, 1080));
        panelMain.setBackground(Color.black);

        panelMain.add(Box.createVerticalGlue());
        JLabel labelDirective = new JLabel("Til Vinland", JLabel.LEADING);
        labelDirective.setFont(fontMain);
        labelDirective.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        labelDirective.setForeground(Color.cyan);
        panelMain.add(labelDirective);

        panelMain.add(Box.createVerticalGlue());

        deck.add(panelMain, CARD_MAIN);
        frame.getContentPane().add(deck, BorderLayout.CENTER);

        // Display the GUI
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
}
