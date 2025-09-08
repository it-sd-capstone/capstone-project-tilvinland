package main;

//Event engine will take the handling of events out of main.

import java.util.Random;

public class EventEngine {

    // Vars
    private static Random rng = new Random();
    private static int weight;
    private static int eventCounter = 1;

    public static void runEvent(int event) { // Noncombat

        // event = rng.nextInt(max - min + 1) + min;
        //int event = 0;

        weight = rng.nextInt(100);
        System.out.println("eventCounter = " + eventCounter);

        if (eventCounter == 4) {
            event = -1;
        } else if (eventCounter == 8) {
            event = -2;
        } else if (weight < 70) { //Common
            event = rng.nextInt(6);
        } else if (weight < 80) { //Uncommon
            event = rng.nextInt(8 - 6 + 1) + 6;
        } else if (weight < 95) { //Rare
            event = rng.nextInt(11 - 9 + 1) + 9;
        } else if (weight > 95) { //Special
            event = 12;
        }

        if (event == 0) { // RANDOM EVENTS START HERE
            System.out.println("Common Event 1");
            Main.mainFrame.setEventID(1);
            eventCounter++;
        } else if (event == 1) {
            System.out.println("Common Event 2");
            Main.mainFrame.setEventID(2);
            eventCounter++;
        } else if (event == 2) {
            System.out.println("Common Event 3");
            Main.mainFrame.setEventID(3);
            eventCounter++;
        } else if (event == 3) {
            System.out.println("Common Event 4");
            Main.mainFrame.setEventID(4);
            eventCounter++;
        } else if (event == 4) {
            System.out.println("Common Event 5");
            Main.mainFrame.setEventID(5);
            eventCounter++;
        } else if (event == 5) {
            System.out.println("Common Event 6");
            Main.mainFrame.setEventID(6);
            eventCounter++;
        } else if (event == 6) {
            System.out.println("Uncommon Event 1");
            Main.mainFrame.setEventID(11);
            eventCounter++;
        } else if (event == 7) {
            System.out.println("Uncommon Event 2");
            Main.mainFrame.setEventID(12);
            eventCounter++;
        } else if (event == 8) {
            System.out.println("Uncommon Event 3");
            Main.mainFrame.setEventID(13);
            eventCounter++;
        } else if (event == 9) {
            System.out.println("Rare Event 1");
            Main.mainFrame.setEventID(21);
            eventCounter++;
        } else if (event == 10) {
            System.out.println("Rare Event 2");
            Main.mainFrame.setEventID(22);
            eventCounter++;
        } else if (event == 11) {
            System.out.println("Rare Event 3");
            Main.mainFrame.setEventID(23);
            eventCounter++;
        } else if (event == 12) {
            System.out.println("Special Event 1");
            Main.mainFrame.setEventID(31);
            eventCounter++;
        } else if (event == -1) {
            System.out.println("Main Event 1");
            Main.mainFrame.setEventID(-1);
            eventCounter++;
        } else if (event == -2) {
            System.out.println("Main Event 2");
            Main.mainFrame.setEventID(-2);
            eventCounter++;
        }

        //TODO any actions such as "Hunker down" should not be handled in events and can be explicitly called on an event by
        // event basis, for instance in the gameFrame element's action listener, this allows more flexibility in the outcome
        // of specific events *AND* maybe add some randomness to the risk of the event like heavy storm could do 8 to 10 damage to the ship
        // also, there should be some risk reward like pushing through a storm vs hunkering down may damage your ship more but
        // may also take less rations to do.
    }
}

//TODO this is some random stuff I want to remember that I'm working on. To be continued...

//        } else if (event == -2) {
//            // Start combat
//            // TODO needs to be refactored so enemy ID is chosen for main events but an else statement for every other
//            //Possibly randomize certain enemy IDs for other random events etc. thisll give a lot more control over what enemy can be displayed when.
//
//            int enemyId = 0;
//            if (eventTotal != 4) {
//                enemyId = 1 + rng.nextInt(4);
//            }
//            startCombat(enemyId, createConnection());
//        }
