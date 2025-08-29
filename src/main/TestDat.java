package main;

//Test to display seed input inside the console when the play button is pressed
//Test to save ship health in DEBUGSTATUS panel persistently - this is testing viability of the greater
// save/load functions using json.

public class TestDat {
    private String seedInput;
    private int debugHealth;

    public TestDat() {} // Required for Jackson

    public TestDat(String seedInput, int debugHealth) {
        this.seedInput = seedInput;
        this.debugHealth = debugHealth;
    }

    // Getters and Setters
    public String getSeedInput() { return seedInput; }
    public void setSeedInput(String seedInput) { this.seedInput = seedInput; }

    public int getDebugHealth() { return debugHealth; }
    public void setDebugHealth(int debugHealth) { this.debugHealth = debugHealth; }

}
