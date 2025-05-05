package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testInventoryAddRemove() {
        Player player = new Player("Test", 1, 1);

        // First round of adds
        player.addToInventory(new Item("Wood", 1, "Lumber for repairing ships", 100));
        player.addToInventory(new Item("Loot", 2, "The Treasure", 100));
        player.addToInventory(new Item("Food", 3, "Various Foodstuffs", 100));

        // Print first round
        System.out.println("Inventory after first add round:");
        for (Item item : player.getInventory()) {
            System.out.println(item.getName() + ": " + item.getAmount() + " (" + item.getDescription() + ")");
        }

        // Second round of adds (test adding duplicates)
        player.addToInventory(new Item("Wood", 1, "Lumber for repairing ships", 100));
        player.addToInventory(new Item("Loot", 2, "The Treasure", 100));
        player.addToInventory(new Item("Food", 3, "Various Foodstuffs", 100));

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
        player.removeFromInventory(3, 50);   // Remove 50 Food
        player.removeFromInventory(2, 200);  // Remove all Loot
        player.removeFromInventory(1, 250);  // Over-remove Wood

        // Validate results
        Item food = player.getInventory().stream().filter(i -> i.getId() == 3).findFirst().orElse(null);
        assertNotNull(food, "Food should still exist");
        assertEquals(150, food.getAmount(), "Food should be 150 after removal");

        boolean lootExists = player.getInventory().stream().anyMatch(i -> i.getId() == 2);
        assertFalse(lootExists, "Loot should be removed");

        boolean woodExists = player.getInventory().stream().anyMatch(i -> i.getId() == 1);
        assertFalse(woodExists, "Wood should be removed");

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
        String emptyOutput = player.checkInventory();
        System.out.println("After initialization:\n" + emptyOutput);
        assertEquals("Inventory is empty.\n", emptyOutput); // FIXED
        assertEquals(emptyOutput, player.getInventoryDisplay());

        // Add two items
        player.addToInventory(new Item("Wood", 1, "Lumber for repairing ships", 100));
        player.addToInventory(new Item("Food", 3, "Various Foodstuffs", 50));

        // Refresh inventory string
        String inventoryOutput = player.checkInventory();
        System.out.println("\nAfter adding items:\n" + inventoryOutput);

        // Check if contents are correct
        assertTrue(inventoryOutput.contains("Wood: 100 (Lumber for repairing ships)"));
        assertTrue(inventoryOutput.contains("Food: 50 (Various Foodstuffs)"));
        assertEquals(inventoryOutput, player.getInventoryDisplay());

        // Remove items
        player.removeFromInventory(1, 100);  // remove Wood
        player.removeFromInventory(3, 50);   // remove Food

        // Check if empty again
        String finalOutput = player.checkInventory();
        System.out.println("\nAfter removing all items:\n" + finalOutput);
        assertEquals("Inventory is empty.\n", finalOutput); // FIXED
        assertEquals(finalOutput, player.getInventoryDisplay());
    }
}
