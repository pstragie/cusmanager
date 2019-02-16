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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;

/**
 *
 * @author pieter
 */
public class DocumentHandling {
    
    private MainPanel mainPanel;
    
    public DocumentHandling() {
       mainPanel = new MainPanel();

    }
    
    public List<String> getAfdelingenFromFile() {
        System.out.println("Reading file...");
        
        // Read from file
		List<String> list = new ArrayList<>();
		String fileName = "afdelingen.txt";
		try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream
					.filter(line -> !line.startsWith("Afdelingen"))
					.collect(Collectors.toList());
			
			
		} catch(IOException e) {
                    System.out.println("Error reading file: " + e);
		}
		System.out.println("List: " + list);
                
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
    
    
}
