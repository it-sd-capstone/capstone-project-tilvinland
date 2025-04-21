package main;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static final String DATABASE_NAME = "vinland";
    public static final String DATABASE_PATH = DATABASE_NAME + ".db";
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
