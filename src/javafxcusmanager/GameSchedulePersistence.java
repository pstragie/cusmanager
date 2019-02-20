/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

/**
 *
 * @author pieter
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameSchedulePersistence {

    public static ObservableList<Schedule> scheduleData = FXCollections.observableArrayList();

    public GameSchedulePersistence() throws JAXBException, IOException {
        File dbFile = getScheduleFilePath();
        if (dbFile == null) {
            setScheduleFilePath(new File(System.getProperty("user.home") + "/" + "user-account.xml"));
            dbFile = getScheduleFilePath();
        }

        if (!dbFile.exists()) {
            System.out.println("DatabaseFile does not exist. Creating test data...");
            createTestData();
            saveScheduleDataToFile(dbFile);
        } else {
            System.out.println("Database exists. Loading data...");
            loadScheduleDataFromFile(dbFile);
        }

        System.out.println("Persisted Data: ");
        System.out.println(
                Files.lines(dbFile.toPath())
                        .collect(Collectors.joining("\n"))
        );
        System.out.println("Database File: " + dbFile);
    }

    public static ObservableList<Schedule> getScheduleData() {
        return scheduleData;
    }
    
    private void createTestData() {
        ArrayList<Game> emptyGamesArray = new ArrayList<>();
        ArrayList<Game> gamesArray = new ArrayList<>();
        ArrayList<String> emptyUmpArray = new ArrayList<>();
        ArrayList<String> umpArray = new ArrayList<>();
        umpArray.add("Hermelien");
        gamesArray.add(new Game("", "Wielsbeke", "Frogs", "Pieter Stragier", umpArray));
        scheduleData.add(new Schedule("Gold", "1", gamesArray));
        scheduleData.add(new Schedule("Gold", "2", gamesArray));

        
    }
    
    /**
     * Returns the schedule file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public File getScheduleFilePath() {
        System.out.println("getScheduleFilePath");
        Preferences prefs = Preferences.userNodeForPackage(JavaFxCusManager.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setScheduleFilePath(File file) {
        System.out.println("setScheduleFilePath");
        Preferences prefs = Preferences.userNodeForPackage(JavaFxCusManager.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
        } else {
            prefs.remove("filePath");
        }
    }

    public void loadScheduleDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ScheduleListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ScheduleListWrapper wrapper = (ScheduleListWrapper) um.unmarshal(file);

            scheduleData.clear();
            scheduleData.addAll(wrapper.getSchedule());

            // Save the file path to the registry.
            setScheduleFilePath(file);
        } catch (Exception e) { // Catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void saveScheduleDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ScheduleListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ScheduleListWrapper wrapper = new ScheduleListWrapper();
            wrapper.setSchedule(scheduleData);
            
            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
            
            // Save the file path to the registry.
            setScheduleFilePath(file);
        } catch (Exception e) { // catches any exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}