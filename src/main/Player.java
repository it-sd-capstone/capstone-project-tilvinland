package main;
import java.util.ArrayList;
import java.util.List;


public class Player {

    private String name;
    private int id;
    private int health;
    private int status;
    private int active;
    private List<Item> inventory = new ArrayList<>();
    private String inventoryDisplay;



    public Player ( String name, int id, int active) {
        this.name = name;
        this.id = id;
        this.active = active;
        this.health = 100;
        this.status = 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    public void healthLoss(int change) {
        health -= change;
    }

    public void healthGain(int change) {
        health += change;
    }

    public List<Item> getInventory() {return inventory;}

    public String getInventoryDisplay() {return inventoryDisplay;}

    // Adds item to player inventory
    public void addToInventory(int itemId, int amount) {
        for (Item item : inventory) {
            if (item.getId() == itemId) {
                item.addItem(amount);
                return;
            }
        }

        // If item doesn't exist in inventory, add it
        Item template = Item.itemTemplates.get(itemId);
        if (template != null) {
            inventory.add(new Item(template.getName(), template.getId(), template.getDescription(), amount));
        } else {
            System.out.println("Invalid item ID: " + itemId);
        }
    }

    // Removes quantity of item from inventory
    public void removeFromInventory(int itemId, int amount) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getId() == itemId) {
                item.removeItem(amount);
                if (item.getAmount() <= 0) {
                    inventory.remove(i);
                }
                return;
            }
        }
    }

    // Stores a list of the player's inventory for potential display
    public List<String> checkInventory() {
        List<String> inventoryList = new ArrayList<>();

        if (inventory.isEmpty()) {
            inventoryList.add("Inventory is empty.");
            return inventoryList;
        }

        for (Item item : inventory) {
            inventoryList.add(item.getName() + ": " + item.getAmount() + " (" + item.getDescription() + ")");
        }

        return inventoryList;
    }

}
