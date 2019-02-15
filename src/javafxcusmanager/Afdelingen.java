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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author pieter
 */
public class Afdelingen {
    /** Competitie-afdelingen
	 * Te wijzigen lijst van afdelingen
	 */
	
	private static ArrayList<String> afdlijst;
        private MainPanel mainPanel;
        private GridPane grid;
        public ListView listview;
        public static ArrayList<String> lijst;
        public ChangeAfdelingName changeafd;
        private DocumentHandling documentHandler;
        
        //public static UmpireModel.UmpireTabPane umptabpane;
        
        // Constructor
	public Afdelingen(TabPane tabpane) {
            // Afdelingen afkomstig van observableTabList
            System.out.println("Run Constructor");		
            mainPanel = new MainPanel();
            documentHandler = new DocumentHandling();
            mainPanel.observableTabList = mainPanel.getObservableList();
                
            mainPanel.observableTabList.addListener((ListChangeListener.Change<? extends String> change) -> { 
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            System.out.println("Update detected");
                            
                        } else
                            if (change.wasPermutated()) {
                                System.out.println("Was permutated");
                            }
                        else 
                            for (String remitem: change.getRemoved()) {
                                System.out.println("remitem");
                                //umptabpane = new UmpireModel.UmpireTabPane();
                                tabpane.getTabs().removeIf(tab -> tab.getText().equals(remitem));
                                
                                mainPanel.resetTabpaneSide(tabpane);
                                
                                // Write to file
                                ArrayList<String> tmplijst = new ArrayList<>();
                                mainPanel.observableTabList.forEach(t -> tmplijst.add(t));
                                documentHandler.storeAfdelingen(tmplijst);
                                    
                            }
                            for (String additem : change.getAddedSubList()) {
                                System.out.println("additem");
                               
                                tabpane.getTabs().removeAll(mainPanel.observableTabList);
                                if(!mainPanel.observableTabList.contains(additem)) {
                                    tabpane.getTabs().add(new Tab(additem));
                                }

                                mainPanel.resetTabpaneSide(tabpane);
                                // Write to file
                                ArrayList<String> tmplijst = new ArrayList<>();
                                mainPanel.observableTabList.forEach(t -> tmplijst.add(t));
                                documentHandler.storeAfdelingen(tmplijst);
                            }
                    }
                });

	}
	
	
        
        
        
        
        public Pane afdelingenPanel() {
            // GridPane layout
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER_LEFT);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            
            // Grid with 2 items: ListView and Button
            // Variable list of textfields
                                listview = new ListView<>(mainPanel.observableTabList);
                //listview.getItems().addAll(observableTabList);
                //listview.setPrefSize(150, 100);
                listview.setOrientation(Orientation.VERTICAL);
                listview.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        listview.refresh();
                        return  new XCell();
                    }
                });
                grid.add(listview, 0, 1, 2, 1);
            
            HBox hbbox = new HBox();
            // Button voor toevoegen
            TextField nieuwnaam = new TextField();
            nieuwnaam.setPromptText("Nieuwe klasse");
            
            Button addButton = new Button("Nieuw");
            addButton.setDisable(true);
            nieuwnaam.textProperty().addListener((observable, oldValue, newValue) -> { 
                    addButton.setDisable(newValue.trim().isEmpty());
                });
            
            addButton.setOnAction((ActionEvent event) -> {
                System.out.println("Toevoegen ---------");
                    mainPanel.observableTabList.add(nieuwnaam.getText());
                    nieuwnaam.setText("");
                });
            
            
            // Button for saving
            Button cancel = new Button("Sluiten");
            cancel.setOnAction((ActionEvent event) -> {
                nieuwnaam.setText("");
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            });
            hbbox.getChildren().add(nieuwnaam);
            hbbox.getChildren().add(addButton);
            hbbox.getChildren().add(cancel);
            hbbox.setPadding(new Insets(15, 12, 15, 12));
            hbbox.setSpacing(10);
            hbbox.setAlignment(Pos.CENTER);
            hbbox.setBorder(Border.EMPTY);
            grid.add(hbbox, 0, 2);
            
            return grid;
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
                        ArrayList<String> tmplijst = new ArrayList<>();
                        mainPanel.observableTabList.forEach(t -> tmplijst.add(t));
                        documentHandler.storeAfdelingen(tmplijst);
                        //updateItem(lastItem, true);
                        
                    }
                });
                wbutton.setOnAction((ActionEvent event) -> {
                    // Open frame to change label
                    ChangeAfdelingName changeafd1 = new ChangeAfdelingName();
                    changeafd1.changeAfdeling(label.getText());
                });
                
                upbutton.setDisable(false);
                upbutton.setOnAction((ActionEvent event) -> { 
                   
                       // Move tab up in the order
                       
                            // Get upbutton row index
                            int index = mainPanel.observableTabList.indexOf(lastItem);
                            if(index >= 1) {
                                System.out.println("index: " + index);
                                String oud = mainPanel.observableTabList.get(index-1).toString();
                                System.out.println("oud: " + oud);
                                String nieuw = mainPanel.observableTabList.get(index);
                                System.out.println("nieuw: " + nieuw);
                                mainPanel.observableTabList.remove(oud);
                                System.out.println("1 verwijderd: " + mainPanel.observableTabList);
                                mainPanel.observableTabList.add(index, oud);
                                System.out.println("New order: " + mainPanel.observableTabList);

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
        
        
        
        public class ChangeAfdelingName {
            /** Pup Window to change a klassenaam
             * 
             */
            private Label label;
            private TextField textfield;
            private Button save;
            private Button cancel;
            
            // Constructor
            public ChangeAfdelingName() {
                
            }
            
            public Dialog changeAfdeling(String oudeWaarde) {
                
                Dialog<String> dialoog = new Dialog();
                dialoog.setTitle("Klasse wijzigen");
                dialoog.setHeaderText("De klasse van de clubs zal automatisch mee veranderen.");
                
                dialoog.setContentText("Naam van klasse wijzigen: ");
                // Create the inputfield 
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));
                
                // Set the button types
                ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                dialoog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
                
                // Set klassenaam labels and fields
                TextField klassenaam = new TextField();
                klassenaam.setPromptText(oudeWaarde);
                grid.add(new Label("Nieuwe naam: "), 0, 0);
                grid.add(klassenaam, 1, 0);
                
                // Enable/Disable ok button depending on whether nieuwe klasse was entered
                Node okButton = dialoog.getDialogPane().lookupButton(okButtonType);
                okButton.setDisable(true);
                
                // Do some validation
                klassenaam.textProperty().addListener((observable, oldValue, newValue) -> { 
                    okButton.setDisable(newValue.trim().isEmpty());
                });
                
                dialoog.getDialogPane().setContent(grid);
                
                // Convert the result to a string when the login button is clicked
                dialoog.setResultConverter(dialogButton -> { 
                    if (dialogButton == okButtonType) {
                        return new String(klassenaam.getText());
                    }
                    return null;
                });
                
                Optional<String> result = dialoog.showAndWait();
                if (result.isPresent()) {
                    String nieuweWaarde = result.get();
                    System.out.println("Nieuwe klasse: " + result.get());
                    // Save change in observed afdelingen list
                    Tab tab = new Tab(oudeWaarde);
                    int x = mainPanel.observableTabList.indexOf(tab);
                    //mainPanel.observableTabList.remove(tab);
                    mainPanel.observableTabList.set(x, nieuweWaarde);
                    System.out.println("replaced: " + mainPanel.observableTabList);
                }
                                
                
                return dialoog;
            }
        }
    }

