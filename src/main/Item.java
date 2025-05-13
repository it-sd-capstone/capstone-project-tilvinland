package main;

import java.util.HashMap;
import java.util.Map;

public class Item {

    private String name;
    private int id;
    private String description;
    private int amount;

    public Item(String name, int id, String description, int amount) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    // Static item templates using HashMap
    public static final Map<Integer, Item> itemTemplates = new HashMap<>();

    static {
        itemTemplates.put(1, new Item("Lumber", 1, "Used to repair the ship", 0));
        itemTemplates.put(2, new Item("Gold", 2, "Valuables raided from settlements", 0));
        itemTemplates.put(3, new Item("Rations", 3, "Food for your crew", 0));
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public void addItem(int change) {
        amount += change;
    }

    public void removeItem(int change) {
        amount -= change;
    }
}
