package main;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

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
}