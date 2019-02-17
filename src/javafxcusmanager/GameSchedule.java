/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author pieter
 */
public class GameSchedule {
    
    private Pane pane = new Pane();
    // Constructor
    public GameSchedule() {
    
    }
    
    public VBox createCalendar(String periode) {
        
        // Get empty Weekend game model
        TableView table = new TableView();
        final Label label = new Label("Game Schedule");
        label.setFont(new Font("Arial", 20));
        
        table.setEditable(true);
        
        TableColumn homeTeam = new TableColumn("Home team");
        TableColumn visitingTeam = new TableColumn("Visiting team");
        TableColumn umpire = new TableColumn("Umpire");
        
        table.getColumns().addAll(homeTeam, visitingTeam, umpire);
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        
        
        return vbox;
    }
    
}
