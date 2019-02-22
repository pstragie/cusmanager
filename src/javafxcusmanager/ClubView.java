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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
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
                                    System.out.println("Data was added to clubs");
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
            teamArray.add(new Team("Wolfkes", "BB Rookies"));
            clubs.add(new Club("Wolverines", "0058", "Pieter Stragier", "pstragier@gmail.com", "0486208014", "Coolstraat", "5", "9600", "Ronse", teamArray));
            ArrayList<Team> frogsteamArray = new ArrayList<>();
            frogsteamArray.add(new Team("Frogs Seniors", "4BB"));
            frogsteamArray.add(new Team("Slowpitch", "SP Red"));
            clubs.add(new Club("Frogs", "0012", "Jonas Hoebeke", "jonas.hoebeke@hotmail.com", "04xxxxxxxx", "Scheldekant", "4", "9700", "Oudenaarde", frogsteamArray));
            
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedClub = new String();
        selectedClub = "Wolverines";
        clubList = new ListView();
        
        ListView clubListView = new ListView();
        ListView teamListView = new ListView();
        teamListView.setPadding(new Insets(10, 10, 10, 10));
        VBox teamVBox = new VBox();
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox clubVBox = new VBox();
        
        Label clublabel = new Label("Clubs");
        clublabel.setPadding(new Insets(0, 0, 0, 5));
        clublabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubVBox.getChildren().add(clublabel);
        
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 5));
        TextField filterField = new TextField();
        filterField.setPromptText("Club zoeken");
        filterField.textProperty().addListener((obs, oldText, newText) -> {
            System.out.println("Text changed from "+oldText+" to "+newText);

            if (newText == null || newText.isEmpty()) {
                
                // Reset the tabpane to show all clubs
                clubListView.getItems().clear();
                clubs.forEach(c -> {
                    clubListView.getItems().add(c.getClubNaam());
                });
            } else {
                clubListView.getItems().clear();
                clubs.filtered(c -> c.getClubNaam().startsWith(newText)).forEach(c -> {
                    clubListView.getItems().add(c.getClubNaam());
                });
                //clubListView.getItems().clear();
                //clubListView.getItems().addAll(clubList);
            }
        });
        Button resetButton = new Button("Reset");
        resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> {
            
            filterField.setText("");
        });
        filterHBox.getStyleClass().add("bordered-titled-border");
        filterHBox.setHgrow(filterField, Priority.ALWAYS);
        filterHBox.getChildren().add(filterField);
        filterHBox.getChildren().add(resetButton);
        
        clubVBox.getChildren().add(filterHBox);
        
        selectedClub = clubs.get(0).getClubNaam();
        
        clubs.forEach(c -> {
            clubListView.getItems().add(c.getClubNaam());
        });
        clubListView.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                    }
                };
                
                cell.setOnMouseClicked(event -> {
                   if (! cell.isEmpty()) {
                        selectedClub = cell.getItem().toString();
                        System.out.println("Club selected: " + selectedClub);
                        teamListView.getItems().clear();
                        FilteredList<Club> filteredArray = clubs.filtered(c -> c.getClubNaam().equals(selectedClub));
                        filteredArray.forEach(c -> c.getClubTeams().forEach(team -> {
                        teamListView.getItems().add(team);
                        }));
                   }
               });
               return cell;
        });
                
        
        clubVBox.getChildren().add(clubListView);
        
        

        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        // Vertical Box with Label, ListView 
        
        Label teamlabel = new Label("Teams");
        teamlabel.setPadding(new Insets(0, 0, 0, 5));
        teamlabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        teamVBox.getChildren().add(teamlabel);        
        
        
        
        // Filter teams for club and discipline
        // Get club -> get teams -> filter for discipline
        FilteredList<Club> filteredArray = clubs.filtered(c -> c.getClubNaam().equals(selectedClub));
        filteredArray.forEach(c -> c.getClubTeams().forEach(team -> {
            teamListView.getItems().add(team);
        }));     
        /*
        teamListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        teamListView.refresh();
                        return  new ClubView.XCell();
                    }
                });
        */
        teamVBox.getChildren().add(teamListView);
        
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
        borderPane.setMargin(teamVBox, new Insets(0, 10, 0, 10));
        borderPane.setLeft(clubVBox);
        borderPane.setCenter(teamVBox);
        borderPane.setBottom(buttonHBox);
        
        return borderPane;
    }
    
    class XCell extends ListCell<String> {
            
            HBox hbox = new HBox();
            Label label = new Label("(empty)");
            Pane pane = new Pane();
            Button vbutton = new Button("Wis");
            Button wbutton = new Button("Wijzig");
            Button upbutton = new Button("^");
            String lastItem;
            
            public XCell() {
                super();
                
                hbox.getChildren().addAll(label, pane, vbutton, wbutton, upbutton);
                hbox.setHgrow(pane, Priority.ALWAYS);
                vbutton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mainPanel.observableTabList.remove(lastItem);
                        System.out.println("tablijst: " + mainPanel.observableTabList);
                        
                        // Write to file
                        ArrayList<String> tmplijst = new ArrayList<>();
                        mainPanel.observableTabList.forEach(t -> tmplijst.add(t));
                        documentHandler.storeAfdelingen(tmplijst);
                        //updateItem(lastItem, true);
                        
                    }
                });
                wbutton.setOnAction((ActionEvent event) -> {
                    // Open frame to change label
                    //Afdelingen.ChangeAfdelingName changeafd1 = new Afdelingen.ChangeAfdelingName();
                    //changeafd1.changeAfdeling(label.getText());
                });
                            
                upbutton.setOnAction((ActionEvent event) -> { 
                   
                       // Move tab up in the order
                       
                            // Get upbutton row index
                            int index = mainPanel.observableTabList.indexOf(lastItem);
                            if(index >= 1) {
                                System.out.println("index: " + index);
                                String oud = mainPanel.observableTabList.get(index-1).toString();
                                System.out.println("oud: " + oud);
                                String nieuw = mainPanel.observableTabList.get(index);
                                int nieuwindex = mainPanel.observableTabList.indexOf(nieuw);
                                int oudindex = mainPanel.observableTabList.indexOf(oud);
                                System.out.println("index oud = " + oudindex + ", index nieuw = " + nieuwindex);
                                System.out.println("nieuw: " + nieuw);
                                mainPanel.observableTabList.remove(oud);
                                System.out.println("1 verwijderd: " + mainPanel.observableTabList);
                                mainPanel.observableTabList.add(index, oud);
                                System.out.println("New order: " + mainPanel.observableTabList);

                                // Write new order to file
                                ArrayList<String> tmplijst = new ArrayList<>();
                                mainPanel.observableTabList.forEach(t -> tmplijst.add(t));
                                documentHandler.storeAfdelingen(tmplijst);
                            }
                   
                }); 
            }
            

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                setText(null);  // No text in label of super class
                
                if (empty) {
                    lastItem = null;
                    setGraphic(null);
                } else {
                    lastItem = item;
                    label.setText(item!=null ? item : "<null>");
                    
                    setGraphic(hbox);
                }
                
             
            }
            
            
        }
    
}
