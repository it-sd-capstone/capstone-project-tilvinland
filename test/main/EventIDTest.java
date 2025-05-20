package main;

import java.sql.*;

public class EventIDTest {
    public static void main(String[] args) {
        try (Connection db = Main.createConnection()) {
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT eventId, eventName, eventDesc FROM events");

            int count = 0;
            System.out.println("=== EVENTS TABLE ===");
            while (rs.next()) {
                int id = rs.getInt("eventId");
                String name = rs.getString("eventName");
                String desc = rs.getString("eventDesc");

                System.out.println("ID: " + id + " | Name: " + name + " | Desc: " + desc);
                count++;
            }

            System.out.println("\nTotal events: " + count);
        } catch (SQLException e) {
            System.err.println("Error reading events:");
            e.printStackTrace();
        }
    }
}
