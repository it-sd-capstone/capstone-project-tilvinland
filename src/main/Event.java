package main;

public class Event {

    private String name;
    private int type;
    private int effect1;
    private int effect2;

    public Event(String name, int type, int effect1, int effect2) {
        this.name = name;
        this.type = type;
        this.effect1 = effect1;
        this.effect2 = effect2;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getEffect1() {
        return effect1;
    }

    public int getEffect2() {
        return effect2;
    }
}
