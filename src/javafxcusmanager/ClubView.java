/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafxcusmanager.Club;

/**
 *
 * @author pieter
 */
public class ClubView {
    
    private MainPanel mainPanel;
    private DocumentHandling documentHandler;
    private ArrayList<String> afdelingenArray;
    private ObservableList<Club> clubs;
    private ArrayList<String> clubArray;
    private String selectedClub;
    private ListView clubList;
    
    public ClubView() {
        // Get the list of afdelingen
        // Afdelingen afkomstig van observableTabList
            System.out.println("Run Constructor ClubView");		
            mainPanel = new MainPanel();
            documentHandler = new DocumentHandling();
            afdelingenArray = new ArrayList<>(mainPanel.getObservableList());
            clubs = FXCollections.observableArrayList();
            clubs.addListener((ListChangeListener.Change<? extends Club> change) -> { 
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            System.out.println("Update detected");
                            // Write to file
                                
                        } else
                            if (change.wasPermutated()) {
                                System.out.println("Was permutated");
                            } else {
                                if (change.wasAdded()) {
                                    //System.out.println("Data was added to gameData");
                                    // Write to file
                                    //clubList.refresh();
                                    // Save to database
                                    
                                    //GameSchedule.write(gameData, /home/pieter/wedstrijdschema.txt);
                                }
                            }
                        
                }
            });
            
            // Add Test data
            ArrayList<Team> teamArray = new ArrayList<>();
            teamArray.add(new Team("Wolverines Seniors", "Gold"));
            clubs.add(new Club("Wolverines", "0058", "Pieter Stragier", "pstragier@gmail.com", "0486208014", "Coolstraat", "5", "9600", "Ronse", teamArray));
            
            
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        selectedClub = new String();
        selectedClub = "Wolverines";
        clubList = new ListView();
        clubs.forEach(c -> {
            clubList.getItems().add(c.getClubNaam());
        });
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox clubVBox = new VBox();
        Label clublabel = new Label("Clubs");
        clublabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubVBox.getChildren().add(clublabel);
        
        HBox filterHBox = new HBox();
        TextField filterField = new TextField();
        filterField.setPromptText("Club zoeken");
        filterField.textProperty().addListener((obs, oldText, newText) -> {
            System.out.println("Text changed from "+oldText+" to "+newText);

            if (newText == null || newText.isEmpty()) {
                
                // Reset the tabpane to show all clubs
                clubList.getItems().clear();
                clubs.forEach(c -> {
                    clubList.getItems().add(c.getClubNaam());
                });
            } else {
                clubList.getItems().clear();
                clubs.filtered(c -> c.getClubNaam().startsWith(newText)).forEach(c -> {
                    clubList.getItems().add(c.getClubNaam());
                });
                
            }
        });
        Button resetButton = new Button("Reset");
        resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> {
            clubList.getItems().clear();
            clubList.getItems().addAll(clubs);
            filterField.setText("");
        });
        filterHBox.getStyleClass().add("bordered-titled-border");
        filterHBox.setHgrow(filterField, Priority.ALWAYS);
        filterHBox.getChildren().add(filterField);
        filterHBox.getChildren().add(resetButton);
        
        clubVBox.getChildren().add(filterHBox);
        
        
        
        clubVBox.getChildren().add(clubList);
       
        

        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        // Vertical Box with Label, ListView 
        VBox teamVBox = new VBox();
        Label teamlabel = new Label("Teams");
        teamlabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        teamVBox.getChildren().add(teamlabel);
        
        TabPane clubTabPane = new TabPane();
        clubTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        clubTabPane.getTabs().add(new Tab("Baseball"));
        clubTabPane.getTabs().add(new Tab("Softball"));
        clubTabPane.getTabs().forEach(t -> {
            
            t.setContent(getTeamsPerClub(selectedClub));
        });
        teamVBox.getChildren().add(clubTabPane);
        
        
        // HBox with buttons
        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(5, 10, 5, 10));
        Button addClubButton = new Button("Club toevoegen");
        addClubButton.setOnAction(event -> { 
            // Show Pane in borderPane Right
        });
        Button addTeamButton = new Button("Team toevoegen");
        addTeamButton.setOnAction(event -> {
            // Make empty line in listview editable
        });
        Button closeButton = new Button("Sluiten");
        closeButton.setOnAction(event -> {
           // Close window (and update teams 
        });
        buttonHBox.getChildren().add(addClubButton);
        buttonHBox.getChildren().add(addTeamButton);
        buttonHBox.getChildren().add(closeButton);
        borderPane.setLeft(clubVBox);
        borderPane.setCenter(teamVBox);
        borderPane.setBottom(buttonHBox);
        
        return borderPane;
    }
    
    private ListView getClubsFromFile() {
        ListView clubListView = new ListView();
        clubs.forEach(c -> {
            clubListView.getItems().add(c.getClubNaam());
        });
        return clubListView;
    }
    
    private ListView getTeamsPerClub(String clubnaam) {
        ListView teamListView = new ListView();
        
        // Filter teams for club and discipline
        // Get club -> get teams -> filter for discipline
        FilteredList<Club> filteredArray = clubs.filtered(c -> c.getClubNaam().equals(clubnaam));
        filteredArray.forEach(c -> c.getClubTeams().forEach(team -> {
            teamListView.getItems().add(team);
        
        }));
        return teamListView;
        
    }
}
