package main;

import javax.swing.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.SystemColor.text;

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

    private static Random rng;

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

    public static void setSeed(String input) {
        long seed = (long) input.hashCode();
        rng = new Random();
        rng.setSeed(seed);

    }


}
