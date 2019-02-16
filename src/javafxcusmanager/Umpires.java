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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 *
 * @author pieter
 */
public class Umpires {
    /** KBBSF Umpires
	 * Te wijzigen lijst van umpires
	 */
	
	public static Map<String, String> umpirelijst;
	private DocumentHandling documentHandler;
        public ObservableMap observableUmpirelist;
        
	// Constructor
	public Umpires() {
		umpirelijst = new HashMap<>();
		if(umpirelijst.isEmpty()) {
                    getList();
		}
		observableUmpirelist = FXCollections.observableMap(umpirelijst);
                observableUmpirelist.addListener((MapChangeListener<String, String>) change -> {
                      
                    
                        if (change.wasAdded()) {
                            System.out.println("Umpire added");
                            //internalStore.add(change.getValueAdded());
                        }
                        if (change.wasRemoved()) {
                            System.out.println("Umpire removed");
                            //internalStore.remove(change.getValueRemoved());
                        }
                    
                   
                });
	}
	
	// Getters
	public Map<String, String> getList() {
		// Read from file
		List<String> list = new ArrayList<>();
		String fileName = "umpirelijst.txt";
		try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.filter(line -> !line.startsWith("Umpirelijst"))
					.collect(Collectors.toList());
			
			
		} catch(IOException e) {
		}
		String umpire = "";
		String afd = "";
		for(String l: list) {
			String[] values = l.split(", ");
			umpire = values[0];
			afd = values[1];
			setList(umpire, afd);
		}
		return umpirelijst;
	}
	
	// Setters
	public void setList(String K, String V) {
		umpirelijst.put(K, V);
		try {
		writeListToFile(umpirelijst);
		} catch(IOException e) {
			System.out.println(e);
		}
				
	}
	
        public ArrayList<String> getUmpireArrayListFromFile() {
            /** Get tabs from the list and add content for that afdeling
             * 
             */
            System.out.println("Get Tabs from file\n________________");
            ArrayList<String> listOfItems = new ArrayList<>();
            listOfItems.addAll(createListOfItems("umpirelijst.txt"));
            ArrayList<String> umpires = new ArrayList<>();
            listOfItems.forEach(a -> {

                umpires.add(a);

            });        
            return umpires;
        }
        
        public ArrayList<String> createListOfItems(String filename) {
            //ArrayList<String> arraylist = new ArrayList<>();
            documentHandler = new DocumentHandling();
            ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getUmpiresFromFile();
            return arraylist;
        }
        
	public static void writeListToFile(Map<String,String> map) throws IOException {
            try (FileWriter fileWriter = new FileWriter("umpirelijst.txt")) {
                fileWriter.write("Umpirelijst\n");
                umpirelijst.forEach((k,val) ->  {
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
