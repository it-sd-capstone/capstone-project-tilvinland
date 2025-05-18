package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {

    @Test
    public void testSaveAndLoad() {
        // Setup state for testing
        Main.setSeed("unit_test_seed_v2");
        // Main.createParty();

        // Set player conditions
        Main.getParty().get(0).setHealth(0);    // Dead
        Main.getParty().get(0).setStatus(0);    // Deceased
        Main.getParty().get(1).setHealth(80);   // Injured
        Main.getParty().get(2).setHealth(60);   // Wounded
        Main.getParty().get(3).setHealth(100);  // Healthy

        // Set ship health to 45
        Main.setShip(new Ship());
        Main.getShip().removeHealth(55); // start is 100

        // Add various item quantities
        Main.getItems().clear();
        Main.getItems().add(new Item("Lumber", 1, "Used to repair the ship", 5));
        Main.getItems().add(new Item("Gold", 2, "Valuables raided from settlements", 10));
        Main.getItems().add(new Item("Rations", 3, "Food for your crew", 7));

        // Save game state
        Main.saveGame("junit_state_test");

        // Wipe everything
        Main.getParty().clear();
        Main.getItems().clear();

        // Load saved state
        Main.loadSave();

        // Assertions
        assertEquals("unit_test_seed_v2", Main.getSeedUsed(), "Seed should match after load.");
        assertEquals(0, Main.getParty().get(0).getHealth(), "Player 1 should be dead.");
        assertEquals(45, Main.getShip().getHealth(), "Ship health should be 45.");
        assertEquals(3, Main.getItems().size(), "There should be 3 item types.");

        // Print loaded state to console
        System.out.println("=== Loaded Game State ===");
        System.out.println("Seed: " + Main.getSeedUsed());
        System.out.println("Ship Health: " + Main.getShip().getHealth());

        for (int i = 0; i < Main.getParty().size(); i++) {
            System.out.println("Player " + (i + 1) + " - Health: " + Main.getParty().get(i).getHealth() +
                    ", Status: " + Main.getParty().get(i).getStatus());
        }

        for (Item item : Main.getItems()) {
            System.out.println("Item: " + item.getName() + " - Amount: " + item.getAmount());
        }
    }
}
