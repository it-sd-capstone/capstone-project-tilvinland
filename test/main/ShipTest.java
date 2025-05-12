package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    @Test
    public void testInitialHealthAndStatus() {
        Ship ship = new Ship();
        System.out.println("Initial Health: " + ship.getHealth());
        System.out.println("Initial Status: " + ship.getStatus());
        assertEquals(100, ship.getHealth());
        assertEquals("Perfect Condition", ship.getStatus());
    }

    @Test
    public void testHealthRemoval() {
        Ship ship = new Ship();

        ship.removeHealth(30);
        System.out.println("After -30 HP: " + ship.getHealth() + " | Status: " + ship.getStatus());
        assertEquals(70, ship.getHealth());
        assertEquals("Great Condition", ship.getStatus());

        ship.removeHealth(30);
        System.out.println("After -60 HP total: " + ship.getHealth() + " | Status: " + ship.getStatus());
        assertEquals(40, ship.getHealth());
        assertEquals("Damaged", ship.getStatus());

        ship.removeHealth(30);
        System.out.println("After -90 HP total: " + ship.getHealth() + " | Status: " + ship.getStatus());
        assertEquals(10, ship.getHealth());
        assertEquals("Severe Damage", ship.getStatus());

        ship.removeHealth(20);
        System.out.println("After -110 HP total: " + ship.getHealth() + " | Status: " + ship.getStatus());
        assertEquals(-10, ship.getHealth());
        assertEquals("Sank", ship.getStatus());
    }

    @Test
    public void testHealthAdditionAndCap() {
        Ship ship = new Ship();
        ship.removeHealth(80);
        System.out.println("After -80 HP: " + ship.getHealth());

        ship.addHealth(10); // 10 * 5 = +50
        System.out.println("After +50 HP (from 10 Lumber): " + ship.getHealth());
        assertEquals(70, ship.getHealth());

        ship.addHealth(10);
        System.out.println("After +50 HP (again): " + ship.getHealth());
        assertEquals(100, ship.getHealth());
    }

    @Test
    public void testSetHealthBounds() {
        Ship ship = new Ship();

        ship.setHealth(150);
        System.out.println("After setting to 150: " + ship.getHealth());
        assertEquals(100, ship.getHealth());

        ship.setHealth(-25);
        System.out.println("After setting to -25: " + ship.getHealth());
        assertEquals(0, ship.getHealth());

        ship.setHealth(65);
        System.out.println("After setting to 65: " + ship.getHealth());
        assertEquals(65, ship.getHealth());
    }
}
