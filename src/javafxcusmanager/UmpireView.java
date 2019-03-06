/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author pieter
 */
public class UmpireView {
    private MainPanel mainPanel;
    //private NewUmpire newUmpireWindow;
    private ObservableList<Afdeling> afdelingen;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private ObservableList<Umpire> umpires;
    private NewUmpire newUmpire;
    private String selectedUmpire;
    private Umpire umpireselection;
    private Database database;
    //private ListView clubList;
    private ListView umpirelistview = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    private GridPane detailPane;
    
    public UmpireView(ObservableList umpires, ObservableList clubs, ObservableList teams, ObservableList afdelingen) {
        this.umpires = umpires;
        this.clubs = clubs;
        this.teams = teams;
        this.afdelingen = afdelingen;   
        database = new Database();
    }
    
    public Pane umpirePane() {
        // New Window
        
        BorderPane borderPane = new BorderPane();
        
        selectedUmpire = new String();
        if (umpires.size() > 0) {
            selectedUmpire = umpires.get(0).getUmpireNaam();
        } else {
            selectedUmpire = "";
        }
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox umpireVBox = new VBox(5);
        ListView umpirelistView = new ListView();
        umpirelistView = getUmpireListView(umpireVBox);
        umpireVBox.getChildren().add(umpirelistView);
        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        VBox teamVBox = new VBox(5);
        HBox newteamHBox = new HBox(5);
        
        
        
        //borderPane.setMargin(detailPane, new Insets(0, 10, 0, 10));
        borderPane.setLeft(umpireVBox);
        borderPane.setBottom(getButtonHBox());
        newUmpire = new NewUmpire();
        borderPane.setCenter(newUmpire.NewUmpire(afdelingen));
        
        
        
        return borderPane;
    }
    
    
    
    public HBox getButtonHBox() {
        // HBox with buttons
        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(5, 10, 5, 10));
        Button addUmpireButton = new Button("Umpire toevoegen");
        addUmpireButton.setOnAction(event -> { 
            // Show Pane in borderPane Right
            /*
            System.out.println("Umpire toevoegen");
            Stage stage = new Stage();
            Scene scene = new Scene(newClubPaneel(), 390, 400);
            //stage.setX(1000);
            //stage.setY(800);
            stage.setTitle("Club toevoegen");
            stage.setScene(scene);
            stage.show();
            */
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
        buttonHBox.getChildren().add(addUmpireButton);
        buttonHBox.getChildren().add(closeButton);
        
        return buttonHBox;
    }
    
    public ListView getUmpireListView(VBox umpirevbox) {
        Label umpirelabel = new Label("Umpires");
        umpirelabel.setPadding(new Insets(0, 0, 0, 5));
        umpirelabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        umpirevbox.getChildren().add(umpirelabel);
        umpirevbox.setPadding(new Insets(0, 0, 0, 5));
        //clubvbox.setBorder(new Border);
        
        ObservableList<Umpire> data = FXCollections.observableArrayList(umpires);
        FilteredList<Umpire> filteredData = new FilteredList<>(data, s -> true);
        
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 0));
        TextField filterInput = new TextField();
        filterInput.setPromptText("Umpire zoeken");
        filterInput.textProperty().addListener(obs -> {
            String filter = filterInput.getText();
            
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
                
            } else {
                filteredData.setPredicate(s -> s.getUmpireNaam().contains(filter));
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
        
        umpirevbox.getChildren().add(filterHBox);
        umpirelistview.setItems(umpires);
        umpirelistview.setPrefHeight(1200);
        //clubs.sorted().forEach(c -> {
        //    clubListView.getItems().add(c);
        //});
        umpirelistview.setCellFactory(lv -> {
                ListCell<Umpire> cell = new ListCell<Umpire>() {
                    @Override
                    public void updateItem(Umpire item, boolean empty) {
                        super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getUmpireNaam());
                    }
                    }
                };
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem verbergen = new MenuItem("Umpire actief/niet actief");
                    cm.getItems().add(verbergen);
                    verbergen.setOnAction(wiscel -> {
                        System.out.println("Verberg teams");
                        int newIndex = umpires.indexOf(umpires.get(cell.getIndex()));
                        if (umpires.get(newIndex).getActief()) {
                            umpires.get(newIndex).setActief(Boolean.FALSE);
                        } else {
                            umpires.get(newIndex).setActief(Boolean.TRUE);
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
                
                
                cell.setOnMouseClicked(event -> {
                   if (! cell.isEmpty()) {
                        
                        selectedUmpire = cell.getItem().getUmpireNaam();
                        umpireselection = cell.getItem();
                        
                        // Refresh pane on the left side
                   }
                });
               return cell;
        });
        return umpirelistview;
    }
    
    
    
    
}
