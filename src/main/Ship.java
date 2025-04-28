package main;

public class Ship {

    private int health = 100;
    private String Status;

    public String getStatus() {
        if (health > 75) {
            return "Perfect Condition";
        } else if (health > 50) {
            return "Great Condition";
        } else if (health > 25) {
            return "Damaged";
        } else if (health > 0) {
            return "Severe Damage";
        } else {
            return "Sank";
        }
    }

    public int getHealth() {
        return health;
    }

    public void removeHealth(int amount) {
        health -= amount;
    }

    public void addHealth(int lumber) {
        int amountHeal = lumber * 4;
        health += amountHeal;
        if (health > 100) {
            health = 100;
        }
    }
}
