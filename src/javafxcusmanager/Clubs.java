/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Tab;

/**
 *
 * @author pieter
 */
public final class Clubs {
    /** KBBSF Clubs
	 * Te wijzigen lijst van clubs
	 */
	
	public static Map<String, String> clublijst;
	private DocumentHandling documentHandler;
        public ObservableMap observableClublist;
        
	// Constructor
	public Clubs() {
		clublijst = new HashMap<>();
		if(clublijst.isEmpty()) {
                    getList();
		}
		observableClublist = FXCollections.observableMap(clublijst);
                observableClublist.addListener((MapChangeListener<String, String>) change -> {
                      
                    
                        if (change.wasAdded()) {
                            System.out.println("Club added");
                            //internalStore.add(change.getValueAdded());
                        }
                        if (change.wasRemoved()) {
                            System.out.println("Club removed");
                            //internalStore.remove(change.getValueRemoved());
                        }
                    
                   
                });
	}
	
	// Getters
	public Map<String, String> getList() {
		// Read from file
		List<String> list = new ArrayList<>();
		String fileName = "clublijst.txt";
		try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.filter(line -> !line.startsWith("Clublijst"))
					.collect(Collectors.toList());
			
			
		} catch(IOException e) {
		}
		String club = "";
		String afd = "";
		for(String l: list) {
			String[] values = l.split(", ");
			club = values[0];
			afd = values[1];
			setList(club, afd);
		}
		return clublijst;
	}
	
	// Setters
	public void setList(String K, String V) {
		clublijst.put(K, V);
		try {
		writeListToFile(clublijst);
		} catch(IOException e) {
			System.out.println(e);
		}
				
	}
	
        public ArrayList<String> getClubArrayListFromFile() {
            /** Get tabs from the list and add content for that afdeling
             * 
             */
            System.out.println("Get Tabs from file\n________________");
            ArrayList<String> listOfItems = new ArrayList<>();
            listOfItems.addAll(createListOfItems("clublijst.txt"));
            ArrayList<String> clubs = new ArrayList<>();
            listOfItems.forEach(a -> {

                clubs.add(a);

            });        
            return clubs;
        }
        
        public ArrayList<String> createListOfItems(String filename) {
            //ArrayList<String> arraylist = new ArrayList<>();
            documentHandler = new DocumentHandling();
            ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getClubsFromFile();
            return arraylist;
        }
        
	public static void writeListToFile(Map<String,String> map) throws IOException {
            try (FileWriter fileWriter = new FileWriter("clublijst.txt")) {
                fileWriter.write("Clublijst\n");
                clublijst.forEach((k,val) ->  {
                    String fileContent = k + ", " + val + "\n";
                    try {
                        fileWriter.write(fileContent);
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                });
            }
	}
        
    
}

