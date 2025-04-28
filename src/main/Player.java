package main;

public class Player {

    private String name;
    private int id;
    private int health;
    private int status;
    private int active;

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
}
