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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafxcusmanager.Club;

/**
 *
 * @author pieter
 */
public class ClubView {
    
    private MainPanel mainPanel;
    private NewClub newClubWindow;
    private ObservableList<Afdeling> afdelingen;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private String selectedClub;
    private Club clubselection;
    //private ListView clubList;
    private ListView clubListView = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    
    public ClubView(ObservableList clubs, ObservableList teams, ObservableList afdelingen) {
        this.clubs = clubs;
        this.teams = teams;
        this.afdelingen = afdelingen;   
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedClub = new String();
        selectedClub = clubs.get(0).getClubNaam();  
        
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox clubVBox = new VBox(5);
        clubVBox.getChildren().add(getClubListView(clubVBox));
        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        VBox teamVBox = new VBox(5);
        HBox newteamHBox = new HBox(5);
        
        
        teamVBox.getChildren().add(getTeamListView(teamVBox));
        teamVBox.getChildren().add(addTeamHBox(newteamHBox));
        borderPane.setMargin(teamVBox, new Insets(0, 10, 0, 10));
        borderPane.setLeft(clubVBox);
        borderPane.setBottom(getButtonHBox());
        borderPane.setCenter(teamVBox);
        
        
        
        return borderPane;
    }
    
    private Pane newClubPaneel() {
        
            newClubWindow = new NewClub(clubs, afdelingen);
            return newClubWindow.clubPanel();
    }
    
    public HBox getButtonHBox() {
        // HBox with buttons
        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(5, 10, 5, 10));
        Button addClubButton = new Button("Club toevoegen");
        addClubButton.setOnAction(event -> { 
            // Show Pane in borderPane Right
            
            System.out.println("Club toevoegen");
            Stage stage = new Stage();
            Scene scene = new Scene(newClubPaneel(), 390, 400);
            //stage.setX(1000);
            //stage.setY(800);
            stage.setTitle("Club toevoegen");
            stage.setScene(scene);
            stage.show();
        });                   
             
        
        Button closeButton = new Button("Sluiten");
        closeButton.setOnAction(event -> {
           // Close window (and update teams)
           Stage stage = (Stage) closeButton.getScene().getWindow();
           stage.close();
        });
        buttonHBox.getChildren().add(addClubButton);
        buttonHBox.getChildren().add(closeButton);
        
        return buttonHBox;
    }
    public ListView getClubListView(VBox clubvbox) {
        Label clublabel = new Label("Clubs");
        clublabel.setPadding(new Insets(0, 0, 0, 5));
        clublabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubvbox.getChildren().add(clublabel);
        clubvbox.setPadding(new Insets(0, 0, 0, 5));
        //clubvbox.setBorder(new Border);
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 0));
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
        clubListView.setItems(clubs);
        clubListView.setPrefHeight(1200);
        //clubs.sorted().forEach(c -> {
        //    clubListView.getItems().add(c);
        //});
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
        // Team Table
        Label teamlabel = new Label("Teams");
        teamlabel.setPadding(new Insets(0, 0, 0, 5));
        teamlabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        teamvbox.getChildren().add(teamlabel);        
        teamvbox.setPrefHeight(800);
        // Create column teamnaam (Data type of String)
        TableColumn<Team, String> teamCol = new TableColumn<>("Team");
        
        teamCol.setEditable(true);
        teamCol.setCellValueFactory(
            new PropertyValueFactory<>("teamNaam"));
        
        // Create column afdeling
        TableColumn<Team, Afdeling> afdCol = new TableColumn<>("Afdeling");
        //afdCol.setCellValueFactory(new PropertyValueFactory<>("teamAfdeling"));
        afdCol.setEditable(true);
        afdCol.setCellValueFactory(cellData -> cellData.getValue().teamAfdelingProperty());
        
        afdCol.setCellFactory(ComboBoxTableCell.forTableColumn(afdelingen));
        
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
        //teamCol.setPrefWidth(220);
        teamCol.prefWidthProperty().bind(teamTable.widthProperty().divide(2));
        //afdCol.setPrefWidth(120);
        afdCol.prefWidthProperty().bind(teamTable.widthProperty().divide(4));
        //actionCol.setPrefWidth(50);
        actionCol.prefWidthProperty().bind(teamTable.widthProperty().divide(4));
        // Display row data
        FilteredList fc = clubs.filtered(cl -> cl.getClubNaam().equals(selectedClub));
        //teams.addAll(clubs.get(0).getClubTeams());
        teamTable.setPrefHeight(1200);
        teamTable.setEditable(true);
        //teamTable.setColumnResizePolicy(teamTable.CONSTRAINED_RESIZE_POLICY);
        teamTable.setPlaceholder(new Label("Geen teams gevonden.\n Voeg een team toe of \nselecteer een club om de teams te zien."));
        teamTable.getColumns().addAll(teamCol, afdCol, actionCol);
        teamTable.setItems(teams);
        
        return teamTable;
    }    
    
    private HBox addTeamHBox(HBox hbox) {
        TextField newTeamTF = new TextField();
        newTeamTF.setPrefWidth(220);
        newTeamTF.setPromptText("Nieuw team");
        ComboBox afdCombo = new ComboBox(afdelingen);
        afdCombo.setPrefWidth(120);
        afdCombo.setPromptText("Afdeling");
        Button addButton = new Button("Toevoegen");
        hbox.getChildren().addAll(newTeamTF,afdCombo, addButton);
        addButton.setDisable(true);
        newTeamTF.textProperty().addListener((observable, oldValue, newValue) -> { 
                    addButton.setDisable(newValue.trim().isEmpty() || clubselection == null);
                });
        addButton.setOnAction(event -> {
            // Make empty line in listview editable
            int clubIndex = clubs.indexOf(clubselection); // clubselection = type of Club
            Afdeling afd = new Afdeling(afdCombo.getValue().toString(), "");
            teams.add(new Team(newTeamTF.getText(), afd));
            Team nieuwTeam = new Team(newTeamTF.getText(), afd);
            clubs.get(clubIndex).getClubTeams().add(nieuwTeam);
            
        });
        hbox.setPadding(new Insets(5, 0, 0, 0));
        return hbox;
    }
        
}
