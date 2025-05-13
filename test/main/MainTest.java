package main;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;

class MainTest {
    @Test
    void createConnection() {
        assertDoesNotThrow(
                () -> {
                    Connection db = Main.createConnection();
                    assertNotNull(db);
                    assertFalse(db.isClosed());
                    db.close();
                    assertTrue(db.isClosed());
                }
        );
    }

    @Test
    void queryRaw() {
        assertDoesNotThrow(
                () -> {
                    try (Connection db = Main.createConnection()) {
                        ResultSet rows = Main.queryRaw(db, "SELECT 5 AS result");
                        assertNotNull(rows);
                        assertTrue(rows.next());
                        int result = rows.getInt("result");
                        assertEquals(5, result);
                        rows.close();
                    }
                }
        );
    }

    @Test
    void partyWipe() {
        ArrayList<Player> party = new ArrayList<Player>();
        party.add(new Player("0", 1, 1));
        party.add(new Player("1", 2, 1));
        party.get(1).setHealth(0);

        assertFalse(Main.partyWipe(party));

        party.get(0).setHealth(0);

        assertTrue(Main.partyWipe(party));
    }

    @Test
    void totalScore() {
        assertDoesNotThrow(
                () -> {
                    Connection db = Main.createConnection();

                    ArrayList<Player> party = new ArrayList<Player>();
                    party.add(new Player("0", 1, 1));
                    party.add(new Player("1", 2, 1));
                    party.add(new Player("2", 1, 1));
                    party.add(new Player("3", 2, 1));

                    ArrayList<Item> items = new ArrayList<Item>();
                    items.add(new Item("0", 1, "test item 0", 100));
                    items.add(new Item("1", 1, "test item 1", 100));
                    items.add(new Item("2", 1, "test item 2", 100));

                    Ship ship = new Ship();

                    int score = Main.totalScore(db, party, items, ship);

                    assertEquals(4200, score);
                }
        );

    }
}