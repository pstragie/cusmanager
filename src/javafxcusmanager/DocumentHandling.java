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
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Pieter Stragier
 * @version 1.0
 * @since 1.0
 */
public class DocumentHandling {
    
    private MainPanel mainPanel;
    private Database database;
    /** Documentverwerking
     * 
     */
    public DocumentHandling() {
       mainPanel = new MainPanel();

    }
    
    /** Retrieve afdelingen from file
     * 
     * @param filepath filepath selected with filechooser
     * @return ArrayList van Afdeling
     */
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
        } else {
            // Dialog Wrong file!
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waarschuwing");
            alert.setHeaderText("Verkeerd bestand gekozen.");
            alert.setContentText("Selecteer een bestand met afdelingen.");
            alert.showAndWait();
        }
        return list;
    }
    
    public ArrayList<Afdeling> rewindImportFromBackupFile(String filepath) {
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
        } else {
            // Dialog Wrong file!
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waarschuwing");
            alert.setHeaderText("Verkeerd bestand gekozen.");
            alert.setContentText("Selecteer een bestand met afdelingen.");
            alert.showAndWait();
        }
        return list;
    }
    /** Haal clubs uit bestand
     * 
     * @param filepath
     * @return 
     */
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
                                
                                list.add(new Club(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], parts[9], parts[10], array, Boolean.TRUE, parts[12], parts[13]));
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
            alert.setContentText("Selecteer een bestand met clubs.");
            alert.showAndWait();
        } 
        return list;
    }
    
    /** Sla afdelingen op in bestand
     * 
     * @param afdelingen ArrayList Afdeling
     * @param backup Boolean value backup (true) or export (false)
     */
    public void storeAfdelingen(ArrayList<Afdeling> afdelingen, Boolean backup) {
        // Write to file
        String bestandsnaam = null;
        if (backup) {
            bestandsnaam = "afdelingen_backup.txt";
        } else {
            bestandsnaam = "afdelingen"+LocalDate.now()+".txt";
        }
        try (FileWriter fileWriter = new FileWriter(bestandsnaam)) {
            fileWriter.write("Afdelingen");
            afdelingen.forEach((k) ->  {
                String fileContent = "\n" + k.getAfdelingsNaam() + ";" + k.getAfdelingsCategorie() + ";True";
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
    
    public void storeClubs(ArrayList<Club> clubs, Boolean backup) {
        // Write to file
        String bestandsnaam = null;
        if (backup) {
            bestandsnaam = "clubs_backup.txt";
        } else {
            bestandsnaam = "clubs"+LocalDate.now()+".txt";
        }
        try (FileWriter fileWriter = new FileWriter(bestandsnaam)) {
            fileWriter.write("Clubs");
            clubs.forEach((k) ->  {
                List<String> tmpT = new ArrayList<>();
                for (Team t : k.getClubTeams()) {
                    String tmpString = t.getTeamNaam() + ":" + t.getTeamAfdeling();
                    tmpT.add(tmpString);
                }
                String teams = String.join(",", tmpT);
                String fileContent = "\n" + k.getClubNaam() + ";" + k.getLiga() + ";" + k.getClubNummer() + ";" + k.getVoorzitter() + ";" + k.getClubStraat() + ";" + k.getClubStraatNummer() + ";" + k.getClubPostcode() + ";" + k.getClubStad() + ";" + k.getClubEmail() + ";" + k.getClubTelefoon() + ";" + k.getClubWebsite() + ";" + teams + ";" + k.getVisible();
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
    /** Haal umpires uit bestand
     * 
     * @param filepath
     * @return 
     */
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
                            String[] afdparts = parts[10].split(",");
                            ArrayList<Afdeling> arraylist = new ArrayList<>();
                            for(String s : afdparts) {
                                //String afdlijst = s.split(",");
                                String[] afdsplit = s.split(":");
                                arraylist.add(new Afdeling(afdsplit[0], afdsplit[1]));
  
                            }
                            database = new Database();
                            Club yclub = database.getClubFromDatabase(parts[9]);
                            list.add(new Umpire(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8], yclub, arraylist, Boolean.getBoolean(parts[13]), parts[11], parts[12]));
                });
                

            } catch(IOException e) {
                System.out.println("Error reading file: " + e);
            }
            //System.out.println("List: " + list);
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
    
    /** Sla wedstrijdschema op in bestand
     * 
     * @param wedstrijdschema 
     */
    public void storeGameSchedule(ArrayList<Game> wedstrijdschema, Boolean backup) {
        // Write to file
        String bestandsnaam = null;
        if (backup) {
            bestandsnaam = "games_backup.txt";
        } else {
            bestandsnaam = "games"+LocalDate.now()+".txt";
        }
        try (FileWriter fileWriter = new FileWriter(bestandsnaam)) {
            fileWriter.write("Games");
            wedstrijdschema.forEach((k) ->  {
                String fileContent = "\n" + k.getWeekString() + ";"+k.getAfdelingString() + ";" + k.getGameDatum() + ";" + k.getGameUur() + ";" + k.getHomeTeamName() + ";" + k.getVisitingTeamName() + ";" + k.getPlateUmpireName() + ";" + k.getBase1UmpireName() + ";" + k.getBase2UmpireName() + ";" + k.getBase3UmpireName() + ";" + k.getGameNumber() + ";" + k.getGameindex() + ";" + k.getSeizoen();
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
    
    public ArrayList<Game> getGamesFromFile(String filepath) {
        System.out.println("Reading game file...");
        ArrayList<Game> list = new ArrayList<>();
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
        if (header.contains("Games")) {
        // Read from file
		try(Stream<String> stream = Files.lines(Paths.get(filepath))) {
                    
                    stream.filter(f -> !f.equals("Games"))
                            .forEach(line -> {
                                String[] parts = line.split(";");
                                ArrayList<String> array = new ArrayList<>();
                                //array.add(new Afdeling(parts[0], parts[1]));
                                String w = parts[0];
                                String afd = parts[1];
                                String gd = parts[2];
                                String gt = parts[3];
                                String ht = parts[4];
                                String vt = parts[5];
                                String pu = parts[6];
                                String b1 = parts[7];
                                String b2 = parts[8];
                                String b3 = parts[9];
                                String gn = parts[10];
                                String gi = parts[11];
                                String se = parts[12];
                                LocalDate datum = mainPanel.stringToLocalDate(gd);
                        list.add(new Game(gi, afd, w, datum, gt, ht, vt, pu, b1, b2, b3, gn, se));
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
            alert.setContentText("Selecteer een bestand met afdelingen.");
            alert.showAndWait();
        }
        return list;
    }
}
