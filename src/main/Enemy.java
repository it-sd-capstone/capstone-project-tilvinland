package main;

public class Enemy {

    private String name;
    private int type;
    private int health;
    private boolean isDefeated;

    public Enemy(String name, int type, int health) {
        this.name = name;
        this.type = type;
        this.health = health;
        isDefeated = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public int getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) { this.health = health;}

    public boolean isDefeated() {
        return isDefeated;
    }

    public void checkDefeated() {
        if (health <= 0) {
            isDefeated = true;
        } else {
            isDefeated = false;
        }

    }

    public void removeHealth(int amount) {
        health -= amount;
    }
}
