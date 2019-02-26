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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Club club;
    private DocumentHandling documentHandler;
    private ArrayList<String> afdelingenArray;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private ObservableList<Team> list;
    private ArrayList<String> clubArray;
    private String selectedClub;
    //private ListView clubList;
    private ListView clubListView = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    
    public ClubView() {
        // Get the list of afdelingen
        // Afdelingen afkomstig van observableTabList
        list = FXCollections.observableArrayList();
        
            System.out.println("Run Constructor ClubView");		
            documentHandler = new DocumentHandling();
            //afdelingenArray = new ArrayList<>(mainPanel.getObservableList());
            clubs = FXCollections.observableArrayList();
            teams = FXCollections.observableArrayList();
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
            
            teams.addListener((ListChangeListener.Change<? extends Team> change) -> { 
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            System.out.println("Update detected");
                            // Write to file
                                
                        } else
                            if (change.wasPermutated()) {
                                System.out.println("Was permutated");
                            } else {
                                if (change.wasAdded()) {
                                    System.out.println("Data was added to teams");
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
            clubs.add(new Club("Wolverines", "0058", "Pieter Stragier", "pstragier@gmail.com", "0486208014", "Coolstraat", "5", "9600", "Ronse", new Team("Wolverines Seniors", "Gold")));
            ArrayList<Team> frogsteamArray = new ArrayList<>();
            frogsteamArray.add(new Team("Frogs Seniors", "4BB"));
            frogsteamArray.add(new Team("Slowpitch", "SP Red"));
            clubs.add(new Club("Frogs", "0012", "Jonas Hoebeke", "jonas.hoebeke@hotmail.com", "04xxxxxxxx", "Scheldekant", "4", "9700", "Oudenaarde", new Team("Frogs Seniors", "Gold")));
            
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedClub = new String();
        selectedClub = "Wolverines";        
            
        
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox clubVBox = new VBox();
        clubVBox.getChildren().add(getClubListView(clubVBox));
        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        VBox teamVBox = new VBox();
        
        
        
        teamVBox.getChildren().add(getTeamListView(teamVBox));
        
        borderPane.setMargin(teamVBox, new Insets(0, 10, 0, 10));
        borderPane.setLeft(clubVBox);
        borderPane.setCenter(teamVBox);
        borderPane.setBottom(getButtonHBox());
        
        
        return borderPane;
    }
    
    public HBox getButtonHBox() {
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
        
        return buttonHBox;
    }
    public ListView getClubListView(VBox clubvbox) {
        Label clublabel = new Label("Clubs");
        clublabel.setPadding(new Insets(0, 0, 0, 5));
        clublabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubvbox.getChildren().add(clublabel);
        
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
        
        clubvbox.getChildren().add(filterHBox);
        
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
                        FilteredList<Club> filteredArray = clubs.filtered(c -> c.getClubNaam().equals(selectedClub));
                        teams.clear();
                        teams.add(0, filteredArray.get(0).getClubTeams().get());
                        System.out.print("Teams: " + teams);
                        teamTable.refresh();
                   }
                });
               return cell;
        });
        
        return clubListView;
    }
    
    public TableView getTeamListView(VBox teamvbox) {
        //ListView teamlistview = new ListView();
        
        
        // Team Table
        Label teamlabel = new Label("Teams");
        teamlabel.setPadding(new Insets(0, 0, 0, 5));
        teamlabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        teamvbox.getChildren().add(teamlabel);        

        // Create column teamnaam (Data type of String)
        TableColumn<Team, String> teamCol = new TableColumn<>("Team");
        teamCol.setCellValueFactory(
            new PropertyValueFactory<>("teamNaam"));
        
        // Create column afdeling
        TableColumn<Team, String> afdCol = new TableColumn<>("Afdeling");
        afdCol.setCellValueFactory(
            new PropertyValueFactory<>("teamAfdeling"));
        
        // Set sort type for afdeling column
        //afdCol.setSortType(TableColumn.SortType.ASCENDING);
        
        // Display row data
        FilteredList fc = clubs.filtered(cl -> cl.getClubNaam().equals(selectedClub));
        int index = clubs.indexOf(fc.get(0));
        Team t = clubs.get(index).getClubTeams().get();
        teams.addAll(t);
        System.out.println("Teams list: " + t.getTeamNaam());
        
        teamTable.getColumns().addAll(teamCol, afdCol);
        System.out.println("Teams: " + teams);
        teamTable.setItems(teams);
        
        return teamTable;
    }    
}
