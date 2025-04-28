package main;

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
