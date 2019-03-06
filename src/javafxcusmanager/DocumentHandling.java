/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;

/**
 *
 * @author pieter
 */
public class DocumentHandling {
    
    private MainPanel mainPanel;
    
    public DocumentHandling() {
       mainPanel = new MainPanel();

    }
    
    public ArrayList<Afdeling> getAfdelingenFromFile(String filepath) {
        System.out.println("Reading file...");
        ArrayList<Afdeling> list = new ArrayList<>();
        String header = null;
        // Check if correct file
        try {
        BufferedReader brTest = new BufferedReader(new FileReader(filepath));
        header = brTest .readLine();
        System.out.println("Firstline is : " + header);
        } catch(IOException e) {
            System.err.println("Error reading umpire file");
        }
        // Read from file
        if (header.contains("Afdelingen")) {
        // Read from file
		try(Stream<String> stream = Files.lines(Paths.get(filepath))) {
                    
                    stream.filter(f -> !f.equals("Afdelingen"))
                            .forEach(line -> {
                                String[] parts = line.split(";");
                                ArrayList<String> array = new ArrayList<>();
                                //array.add(new Afdeling(parts[0], parts[1]));
                                System.out.println("parts 0: " + parts[0] + " 1: " + parts[1]);
                        list.add(new Afdeling(parts[0], parts[1]));
                    });
                    
                
			
			
		} catch(IOException e) {
                    System.out.println("Error reading file: " + e);
		}
		System.out.println("List: " + list);
        }
        return list;
    }
    
    public ArrayList<Club> getClubsFromFile(String filepath) {
        System.out.println("Reading file...");
        ArrayList<Club> list = new ArrayList<>();
        String header = null;
         // Check if correct file
        try {
        BufferedReader brTest = new BufferedReader(new FileReader(filepath));
        header = brTest .readLine();
        System.out.println("Firstline is : " + header);
        } catch(IOException e) {
            System.err.println("Error reading umpire file");
        }
        // Read from file
        if (header.contains("Clubs")) {
        // Read from file
		try(Stream<String> stream = Files.lines(Paths.get(filepath))) {
                    
                    stream.filter(f -> !f.equals("Clubs"))
                            .forEach(line -> {
                                String[] parts = line.split(";");
                                ArrayList<Team> array = new ArrayList<>();
                                System.out.println("parts: " + parts.length);
                                System.out.println("parts: 0:" + parts[0] + " 1: " + parts[1] + " 2: " + parts[2] + " 3: " + parts[3] + " 4: " + parts[4] + " 5: " + parts[5] + " 6: " + parts[6] + " 7: " + parts[7] + " 8: " + parts[8] + " 9: " + parts[9] + " 10: " + parts[10]);
                                // Extract teams from part 11
                                String[] teamparts = parts[11].split(",");
                                for(String s : teamparts) {
                                    String team = s.split(":")[0];
                                    Afdeling afd = new Afdeling(s.split(":")[1], "");
                                    array.add(new Team(team, afd));
                                }
                                
                                list.add(new Club(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9], parts[10], array, Boolean.TRUE));
                    });

		} catch(IOException e) {
                    System.out.println("Error reading file: " + e);
		}
		System.out.println("List: " + list);
        }   
        return list;
    }
    
    public void storeAfdelingen(ArrayList<String> afdelingen) {
        // Write to file
        try (FileWriter fileWriter = new FileWriter("afdelingen.txt")) {
            fileWriter.write("Afdelingen");
            afdelingen.forEach((k) ->  {
                String fileContent = "\n" + k;
                try {
                    fileWriter.write(fileContent);
                } catch(IOException e) {
                    System.out.println(e);
                }
            });
        } catch (IOException e) {
            System.out.println("Error writing afdelingen: " + e);
        }
        
    }
    
    
    
    public ArrayList<Umpire> getUmpiresFromFile(String filepath) {
        System.out.println("Reading file...");
        String header = null;
        ArrayList<Umpire> list = new ArrayList<>();
        
        // Check if correct file
        try {
        BufferedReader brTest = new BufferedReader(new FileReader(filepath));
        header = brTest .readLine();
        System.out.println("Firstline is : " + header);
        } catch(IOException e) {
            System.err.println("Error reading umpire file");
        }
        // Read from file
        if (header.contains("Umpires")) {

            try(Stream<String> stream = Files.lines(Paths.get(filepath))) {
                
                stream.filter(f -> !f.equals("Umpires") && !f.equals("") && !f.equals("\n"))
                        .forEach(line -> {
                            String[] parts = line.split(";");
                            ArrayList<Afdeling> array = new ArrayList<>();
                            System.out.println("parts: " + parts.length);
                            System.out.println("parts: 0:" + parts[0] + " 1: " + parts[1] + " 2: " + parts[2] + " 3: " + parts[3] + " 4: " + parts[4] + " 5: " + parts[5] + " 6: " + parts[6] + " 7: " + parts[7] + " 8: " + parts[8] + " 9: " + parts[9]);
                            // Extract teams from part 11
                            String[] afdparts = parts[9].split(",");
                            ArrayList<Afdeling> arraylist = new ArrayList<>();
                            for(String s : afdparts) {
                                //String afdlijst = s.split(",");
                                String[] afdsplit = s.split(":");
                                arraylist.add(new Afdeling(afdsplit[0], afdsplit[1]));
  
                            }

                            list.add(new Umpire(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], arraylist, Boolean.getBoolean(parts[10])));
                });
                

            } catch(IOException e) {
                System.out.println("Error reading file: " + e);
            }
            System.out.println("List: " + list);
        } else {
            // Dialog Wrong file!
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waarschuwing");
            alert.setHeaderText("Verkeerd bestand gekozen.");
            alert.setContentText("Selecteer een bestand met umpires.");
            alert.showAndWait();
        }
        return list;
    }
    
    public void storeGameSchedule(ArrayList<String> wedstrijdschema) {
        // Write to file
        try (FileWriter fileWriter = new FileWriter("wedstrijdschema.txt")) {
            fileWriter.write("Wedstrijdschema");
            wedstrijdschema.forEach((k) ->  {
                String fileContent = "\n" + k;
                try {
                    fileWriter.write(fileContent);
                } catch(IOException e) {
                    System.out.println(e);
                }
            });
        } catch (IOException e) {
            System.out.println("Error writing wedstrijdschema: " + e);
        }
        
    }
}
