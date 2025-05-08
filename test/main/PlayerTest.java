package main;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testInventoryAddRemove() {
        Player player = new Player("Test", 1, 1);

        // First round of adds
        player.addToInventory(1, 100); // Lumber
        player.addToInventory(2, 100); // Gold
        player.addToInventory(3, 100); // Rations

        // Print first round
        System.out.println("Inventory after first add round:");
        for (Item item : player.getInventory()) {
            System.out.println(item.getName() + ": " + item.getAmount() + " (" + item.getDescription() + ")");
        }

        // Second round of adds (test adding duplicates)
        player.addToInventory(1, 100);
        player.addToInventory(2, 100);
        player.addToInventory(3, 100);

        // Check that all are 200
        for (Item item : player.getInventory()) {
            assertEquals(200, item.getAmount(), "Unexpected amount for " + item.getName());
        }

        // Print second round
        System.out.println("\nInventory after second add round:");
        for (Item item : player.getInventory()) {
            System.out.println(item.getName() + ": " + item.getAmount() + " (" + item.getDescription() + ")");
        }

        // Third round: Test Removals
        player.removeFromInventory(3, 50);   // Remove 50 Rations
        player.removeFromInventory(2, 200);  // Remove all Gold
        player.removeFromInventory(1, 250);  // Over-remove Lumber

        // Validate results
        Item rations = player.getInventory().stream().filter(i -> i.getId() == 3).findFirst().orElse(null);
        assertNotNull(rations, "Rations should still exist");
        assertEquals(150, rations.getAmount(), "Rations should be 150 after removal");

        boolean goldExists = player.getInventory().stream().anyMatch(i -> i.getId() == 2);
        assertFalse(goldExists, "Gold should be removed");

        boolean lumberExists = player.getInventory().stream().anyMatch(i -> i.getId() == 1);
        assertFalse(lumberExists, "Lumber should be removed");

        // Print final inventory state after removals
        System.out.println("\nInventory after third round (removal):");
        for (Item item : player.getInventory()) {
            System.out.println(item.getName() + ": " + item.getAmount() + " (" + item.getDescription() + ")");
        }
    }

    @Test
    public void testCheckInventory() {
        Player player = new Player("Test", 1, 1);

        // Check empty inventory
        List<String> emptyOutput = player.checkInventory();
        System.out.println("After initialization:");
        emptyOutput.forEach(System.out::println);
        assertEquals(1, emptyOutput.size());
        assertEquals("Inventory is empty.", emptyOutput.get(0));

        // Add two items
        player.addToInventory(1, 100); // Lumber
        player.addToInventory(3, 50);  // Rations

        // Refresh inventory list
        List<String> inventoryOutput = player.checkInventory();
        System.out.println("\nAfter adding items:");
        inventoryOutput.forEach(System.out::println);

        // Check content
        assertTrue(inventoryOutput.stream().anyMatch(line -> line.contains("Lumber: 100")));
        assertTrue(inventoryOutput.stream().anyMatch(line -> line.contains("Rations: 50")));

        // Remove items
        player.removeFromInventory(1, 100);
        player.removeFromInventory(3, 50);

        // Check if empty again
        List<String> finalOutput = player.checkInventory();
        System.out.println("\nAfter removing all items:");
        finalOutput.forEach(System.out::println);
        assertEquals(1, finalOutput.size());
        assertEquals("Inventory is empty.", finalOutput.get(0));
    }
}
