/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
	
        private MainPanel mainPanel;
        private GridPane grid;
        public ListView listview;
        public static ArrayList<String> lijst;
        private DocumentHandling documentHandler;
        private ObservableList<Afdeling> afdelingenlijst;
        private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

        //public static UmpireModel.UmpireTabPane umptabpane;
        
        // Constructor
	public Afdelingen(TabPane tabpaneleft, TabPane tabpaneright, TabPane tabpanecenter, ObservableList afdelingenlijst) {
            // Afdelingen afkomstig van observableTabList
            this.afdelingenlijst = afdelingenlijst;
            System.out.println("Run Constructor");		
            mainPanel = new MainPanel();
            System.out.println("afdlijst: " + afdelingenlijst);
            
        }
	
	
        public ArrayList<String> getAfdelingsnamen() {
            //* Method to fill combobox
            ArrayList<String> afdLijst = new ArrayList<>();
            List<String> list = new ArrayList<>();
            afdelingenlijst.forEach(a -> list.add(a.getAfdelingsNaam()));
		
            afdLijst.addAll(list);
            return afdLijst;
        }
        
        
        
        public Pane afdelingenPanel() {
            /** New Frame to add/change/delete afdelingen
             * 
             */
            // GridPane layout
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER_LEFT);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            
            // Grid with 2 items: ListView and Button
            // Variable list of textfields
                TableView afdelingstabel = new TableView();
                afdelingstabel.setRowFactory(tv -> {
                    TableRow<Afdeling> row = new TableRow<>();

                    row.setOnDragDetected(event -> {
                        if (! row.isEmpty()) {
                            Integer index = row.getIndex();
                            Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                            db.setDragView(row.snapshot(null, null));
                            ClipboardContent cc = new ClipboardContent();
                            cc.put(SERIALIZED_MIME_TYPE, index);
                            db.setContent(cc);
                            event.consume();
                        }
                    });

                    row.setOnDragOver(event -> {
                        Dragboard db = event.getDragboard();
                        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                            if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                                event.consume();
                            }
                        }
                    });

                    row.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                            int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                            Object draggedAfdeling = afdelingstabel.getItems().remove(draggedIndex);

                            int dropIndex ; 

                            if (row.isEmpty()) {
                                dropIndex = afdelingstabel.getItems().size() ;
                            } else {
                                dropIndex = row.getIndex();
                            }

                            afdelingstabel.getItems().add(dropIndex, draggedAfdeling);

                            event.setDropCompleted(true);
                            afdelingstabel.getSelectionModel().select(dropIndex);
                            event.consume();
                        }
                    });

                    return row ;
                });
                afdelingstabel.setEditable(true);
                // Create column teamnaam (Data type of String)
                TableColumn<Afdeling, String> afdCol = new TableColumn<>("Afdeling");
                afdCol.setEditable(true);
                afdCol.setCellValueFactory(
                    new PropertyValueFactory<>("afdelingsnaam"));

                // Create column afdeling
                TableColumn<Afdeling, String> catCol = new TableColumn<>("Discipline");
                //catCol.setCellValueFactory(
                 //   new PropertyValueFactory<>("afdelingscategorie"));
                catCol.setCellValueFactory(cellData -> cellData.getValue().afdelingscategorieProperty());
                catCol.setCellFactory(ComboBoxTableCell.forTableColumn("Baseball", "Softball"));

                TableColumn<Afdeling, Afdeling> actionCol = new TableColumn("Wis");
                actionCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
                actionCol.setSortable(false);
                actionCol.setCellFactory(param -> new TableCell<Afdeling, Afdeling>() {
                    private final Button deleteButton = new Button("Wis");
                    
                    protected void updateItem(Afdeling afd, boolean empty) {
                        super.updateItem(afd, empty);
                        if (afd == null) {
                            setGraphic(null);
                            return;
                        }
                        setGraphic(deleteButton);
                        deleteButton.setOnAction(event -> {
                            getTableView().getItems().remove(afd);
                            
                            // Remove from afdelingenlijst
                            afdelingenlijst.remove(afd);
                        });

                    }
                });
                afdCol.setPrefWidth(200);
                catCol.setPrefWidth(200);
                actionCol.setPrefWidth(50);
                afdelingstabel.getColumns().addAll(afdCol, catCol, actionCol);
                
                afdelingstabel.setItems(afdelingenlijst);

                grid.add(afdelingstabel, 0, 1, 2, 1);
            
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
                    afdelingenlijst.add(new Afdeling(nieuwnaam.getText(), ""));
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
        
        /*
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
                    ChangeAfdelingName changeafd1 = new ChangeAfdelingName();
                    changeafd1.changeAfdeling(label.getText());
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
        */
        
        
        
    }

