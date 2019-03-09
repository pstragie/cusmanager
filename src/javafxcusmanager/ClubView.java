/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import static java.lang.System.in;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafxcusmanager.Club;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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
    private Database database;
    //private ListView clubList;
    private ListView clubListView = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    
    public ClubView(ObservableList clubs, ObservableList teams, ObservableList afdelingen) {
        this.clubs = clubs;
        this.teams = teams;
        this.afdelingen = afdelingen;   
        database = new Database();
    }
    
    public Pane clubPane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedClub = new String();
        if (clubs.size() > 0) {
            selectedClub = clubs.get(0).getClubNaam();
        } else {
            selectedClub = "";
        }
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox clubVBox = new VBox(5);
        ListView clublistView = new ListView();
        clublistView = getClubListView(clubVBox);
        clubVBox.getChildren().add(clublistView);
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
        //Text layoutIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CLOSE);
        //layoutIcon.getStyleClass().addAll("button-icon", "layout-button-icon");
        //closeButton.setGraphic(layoutIcon);
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
        
        ObservableList<Club> data = FXCollections.observableArrayList(clubs);
        FilteredList<Club> filteredData = new FilteredList<>(data, s -> true);
        
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 0));
        TextField filterInput = new TextField();
        filterInput.setPromptText("Club zoeken");
        filterInput.textProperty().addListener(obs -> {
            String filter = filterInput.getText();
            
            System.out.println("Text changed: " + obs);

            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
                System.out.println("newText null or empty");
            } else {
                System.out.println("newText not null nor empty");
                filteredData.setPredicate(s -> s.getClubNaam().contains(filter));
                
            }
        });
        Button resetButton = new Button("Reset");
        resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> {
            
            filterInput.setText("");
        });
        filterHBox.getStyleClass().add("bordered-titled-border");
        filterHBox.setHgrow(filterInput, Priority.ALWAYS);
        filterHBox.getChildren().add(filterInput);
        filterHBox.getChildren().add(resetButton);
        
        clubvbox.getChildren().add(filterHBox);
        clubListView.setItems(filteredData);
        clubListView.setPrefHeight(1200);
        //clubs.sorted().forEach(c -> {
        //    clubListView.getItems().add(c);
        //});
        clubListView.setCellFactory(lv -> {
                ListCell<Club> cell = new ListCell<Club>() {
                    @Override
                    public void updateItem(Club item, boolean empty) {
                        super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getClubNaam());
                    }
                    }
                };
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem verbergen = new MenuItem("Verberg/toon teams");
                    cm.getItems().add(verbergen);
                    verbergen.setOnAction(wiscel -> {
                        System.out.println("Verberg teams");
                        int newIndex = clubs.indexOf(clubs.get(cell.getIndex()));
                        if (clubs.get(newIndex).getVisible()) {
                            clubs.get(newIndex).setVisible(Boolean.FALSE);
                        } else {
                            clubs.get(newIndex).setVisible(Boolean.TRUE);
                        }
                        
                    });
                    MenuItem wisRij = new MenuItem("Wis club");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = clubs.indexOf(clubs.get(cell.getIndex()));
                        clubs.remove(clubs.get(newIndex));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                
                cell.setOnKeyReleased(event -> {
                    if (! cell.isEmpty()) {
                        System.out.println("Key was released");
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
        clubListView.getSelectionModel().select(0);
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
                    
                    database.removeTeamFromDatabase(team);
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
            database.insertTeamsInDatabase(nieuwTeam.getTeamNaam(), nieuwTeam.getTeamAfdeling().toString(), clubs.get(clubIndex).getClubNaam(), nieuwTeam.getTeamAfdeling().getAfdelingsCategorie());
        });
        hbox.setPadding(new Insets(5, 0, 0, 0));
        return hbox;
    }
        
}
