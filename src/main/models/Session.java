package main.models;

public class Session {
    private String seedInput;
    private int debugHealth;
    private String[] partyName;
    private int[] partyHealth;
    private int[] partyActive;

    public Session() {} // Required for Jackson

    public Session(String seedInput, int debugHealth, String[] partyName, int[] partyHealth,
                   int[] partyActive) {
        this.seedInput = seedInput;
        this.debugHealth = debugHealth;
        this.partyName = partyName;
        this.partyHealth = partyHealth;
        this.partyActive = partyActive;
    }

    // Getters and Setters
    public String getSeedInput() { return seedInput; }
    public void setSeedInput(String seedInput) { this.seedInput = seedInput; }

    public int getDebugHealth() { return debugHealth; }
    public void setDebugHealth(int debugHealth) { this.debugHealth = debugHealth; }

    public String[] getPartyName() { return partyName; }
    public void setPartyName(String[] partyName) { this.partyName = partyName; }

    public int[] getPartyHealth() { return partyHealth; }
    public void setPartyHealth(int[] partyHealth) { this.partyHealth = partyHealth; }

    public int[] getPartyActive() { return partyActive; }
    public void setPartyActive(int[] partyActive) { this.partyActive = partyActive; }

}

