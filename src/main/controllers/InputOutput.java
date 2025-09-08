package main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.TestDat;
import main.models.Session;

import java.io.File;
import java.io.IOException;

public class InputOutput {

    ObjectMapper mapper = new ObjectMapper();
    Session session = new Session();
    File saveFile = new File("./resources/Data/session.json");

    //Read JSON
    public void read(String field, String value) throws IOException {
        //Convert the string to int if needed
        int intString = Integer.parseInt(value);

        try {
            session = mapper.readValue(saveFile, Session.class);
            System.out.println("Seed Input from json: " + session.getSeedInput());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Write to JSON
    public void write(String field, String value) throws IOException {

        int intString = Integer.parseInt(value);

        try {
            //Read JSON file
            session = mapper.readValue(saveFile, Session.class);

            if (field == "Seed") {
                //Modify SeedInput
                session.setSeedInput(value);
                //Write changes to saveFile
                mapper.writeValue(saveFile, session);
            } else if (field == "DebugHealth") {
                // Set the integer converted value of "value"
                session.setDebugHealth(intString);
                mapper.writeValue(saveFile, session);
            } else if (field == "DebugHealth") {
                // Set the integer converted value of "value"
                session.setDebugHealth(intString);
                mapper.writeValue(saveFile, session);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void restoreSession() {
        if (saveFile.exists()) {
            saveFile.delete();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Session defaultSession = new Session(
                        null,
                        100,
                        new String[] {null},
                        new int[] {0},
                        new int[] {0}
                );
                objectMapper.writeValue(saveFile, defaultSession);

            } catch (Exception e) {
                System.out.println("Failed to restore session");
            }
        }
    }
}
