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

/**
 *
 * @author pieter
 */
public final class Clubs {
    /** KBBSF Clubs
	 * Te wijzigen lijst van clubs
	 */
	
	public static Map<String, String> clublijst;
	
	// Constructor
	public Clubs() {
		clublijst = new HashMap<>();
		if(clublijst.isEmpty()) {
			getList();
		}
		
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
