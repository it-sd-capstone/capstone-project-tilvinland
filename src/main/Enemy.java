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

    public int getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

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
}
