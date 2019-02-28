/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
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
import javafx.util.converter.DefaultStringConverter;
import javafxcusmanager.Club;

/**
 *
 * @author pieter
 */
public class ClubView {
    
    private MainPanel mainPanel;
    private Club club;
    private DocumentHandling documentHandler;
    private ObservableList<String> afdelingenArray;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private ArrayList<String> clubArray;
    private String selectedClub;
    private Club clubselection;
    private Afdelingen afdelingen;
    //private ListView clubList;
    private ListView clubListView = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    
    public ClubView(ObservableList clubs, ObservableList teams) {
        this.clubs = clubs;
        this.teams = teams;
        
        // Get the list of afdelingen
        // Afdelingen afkomstig van observableTabList
        
        //afdelingenArray = FXCollections.observableArrayList(afdelingen.getAfdelingen());
        
            System.out.println("Run Constructor ClubView");		
            documentHandler = new DocumentHandling();
            //afdelingenArray = new ArrayList<>(mainPanel.getObservableList());
            
        
            
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedClub = new String();
        selectedClub = "";  
        
        
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
            int clubIndex = clubs.indexOf(clubselection); // clubselection = type of Club
            Team nieuwTeam = new Team("N", "N");
            clubs.get(clubIndex).getClubTeams().add(nieuwTeam);
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
        clubvbox.setPrefHeight(800);
        //clubvbox.setBorder(new Border);
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 5));
        TextField filterField = new TextField();
        filterField.setPromptText("Club zoeken");
        filterField.textProperty().addListener((obs, oldText, newText) -> {
            System.out.println("Text changed from "+oldText+" to "+newText);

            if (newText == null || newText.isEmpty()) {
                
                // Reset the tabpane to show all clubs
                clubListView.getItems().clear();
                
                clubs.sorted().forEach(c -> {
                    clubListView.getItems().add(c.getClubNaam());
                });
            } else {
                clubListView.getItems().clear();
                clubs.sorted().filtered(c -> c.getClubNaam().startsWith(newText)).forEach(c -> {
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
        
        selectedClub = "";
        clubs.sorted().forEach(c -> {
            clubListView.getItems().add(c);
        });
        clubListView.setCellFactory(lv -> {
                ListCell<Club> cell = new ListCell<Club>() {
                    @Override
                    public void updateItem(Club item, boolean empty) {
                        super.updateItem(item, empty);
                        System.out.println("update club: " + item);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getClubNaam());
                    }
                    }
                };
                
                cell.setOnMouseClicked(event -> {
                   if (! cell.isEmpty()) {
                        
                        selectedClub = cell.getItem().getClubNaam();
                        clubselection = cell.getItem();
                        //clubselection = cell.getItem();
                        //clubselection = clubListView.getSelectionModel().getSelectedItem();
                        teams.clear();
                        FilteredList fc = clubs.filtered(cl -> cl.getClubNaam().equals(selectedClub));
                        int index = clubs.indexOf(fc.get(0));
                        teams.addAll(clubs.get(index).getClubTeams());
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
        teamCol.setEditable(true);
        teamCol.setCellValueFactory(
            new PropertyValueFactory<>("teamNaam"));
        
        // Create column afdeling
        TableColumn<Team, String> afdCol = new TableColumn<>("Afdeling");
        afdCol.setCellValueFactory(
            new PropertyValueFactory<>("teamAfdeling"));
       
        
        TableColumn<Team, Team> actionCol = new TableColumn("Wis");
        actionCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        actionCol.setCellFactory(param -> new TableCell<Team, Team>() {
            private final Button deleteButton = new Button("Wis");
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (team == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    getTableView().getItems().remove(team);
                    int clubIndex = clubs.indexOf(clubselection); // clubselection = type of Club
                    
                    clubs.get(clubIndex).getClubTeams().remove(team);
                });
                
            }
        });
                        

        // Set sort type for afdeling column
        //afdCol.setSortType(TableColumn.SortType.ASCENDING);
        teamCol.setPrefWidth(200);
        afdCol.setPrefWidth(100);
        actionCol.setPrefWidth(50);
        // Display row data
        FilteredList fc = clubs.filtered(cl -> cl.getClubNaam().equals(selectedClub));
        //teams.addAll(clubs.get(0).getClubTeams());
        teamTable.setPlaceholder(new Label("Geen teams gevonden.\n Voeg een team toe of \nselecteer een club om de teams te zien."));
        teamTable.getColumns().addAll(teamCol, afdCol, actionCol);
        teamTable.setItems(teams);
        
        return teamTable;
    }    
}
