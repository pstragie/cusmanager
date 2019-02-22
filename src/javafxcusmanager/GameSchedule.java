/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.MonthDay;
import static java.util.Calendar.*;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author pieter
 */
public class GameSchedule {
    
    private static final int MAX_ITEMS = 3;
    private DocumentHandling documentHandler;
    public int startOfWeekCount = 0; // 0 = Runs with Year Calendar (January 1); 1 = Runs with competition start (date can be set);
    private final TableView<Game> table = new TableView<>();
    
    private ObservableList<Game> gameData;
    private ObservableList<LocalTime> uren;
    private ArrayList<String> emptyArray = new ArrayList<>();

    // Constructor
    public GameSchedule() {
        // Load schedule data from the specified file. The current scheduel data will be replaced
        
        
        
        gameData = FXCollections.observableArrayList();
        
        gameData.addListener((ListChangeListener.Change<? extends Game> change) -> { 
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
                                    
                                    // Save to database
                                    
                                    //GameSchedule.write(gameData, /home/pieter/wedstrijdschema.txt);
                                }
                            }
                        
                }
            });
        
        String week = "1";
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
        ArrayList<Game> gamesArray = new ArrayList<>();
        ArrayList<String> umpArray = new ArrayList<>();
        umpArray.add("Pieter");
        umpArray.add("Hermelien");
        ArrayList<String> emptyArray = new ArrayList<>();
        gameData.add(new Game("Gold", "1", MonthDay.of(APRIL, 14), LocalTime.of(2, 30), "Frogs", "Wolverines", "Isabelle Verelst", umpArray));
        gameData.add(new Game("Gold", "1", MonthDay.of(APRIL, 14), LocalTime.of(14, 00), "Wielsbeke", "Doornik", "Pieter Stragier", emptyArray));
        gameData.add(new Game("Gold", "2", MonthDay.of(APRIL, 22), LocalTime.of(14, 00), "Ghent", "Zottegem", "Hermelien", emptyArray));
        gameData.add(new Game("1BB", "1", MonthDay.of(APRIL, 14), LocalTime.of(14, 00), "Eagles", "Pioneers", "Utah", emptyArray));
                
        // Add 51 empty weeks
        for(int i=2; i<5; i++) {
            gameData.add(new Game("Gold", Integer.toString(i), MonthDay.of(APRIL, 14), LocalTime.of(14, 00), null, null, null, emptyArray));
        }
        
    }

    
    public ObservableList<Game> gameData() { return gameData; }

        public List<Game> getAccounts() {
            return new ArrayList<>(gameData);
        }

        public void setAccounts(List<Game> schedules) {
            this.gameData.setAll(schedules);
        }
    
    private Date stringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        //String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
        String dateInString = dateString;
        Date date = new Date();
        try {

            date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return date;
    }
    
    public VBox createCalendar(String afdeling) {
        
        VBox yearCalendar = new VBox();
        VBox vbox = new VBox();
        
        
        yearCalendar.setPadding(new Insets(5, 0, 5, 0));
        if(startOfWeekCount == 0) {
            for(int week=1; week<10; week++) {
                vbox.getChildren().add(createWeekCalendar(afdeling, week));
            }
        } else {
            for(int week=1; week<=30; week++) {
                vbox.getChildren().add(createWeekCalendar(afdeling, week));
            }
        }
        ScrollPane sideBarScroller = new ScrollPane(vbox);
        sideBarScroller.setFitToWidth(true);
        yearCalendar.getChildren().add(sideBarScroller);
        return yearCalendar;
    }
    
    public VBox createWeekCalendar(String afdeling, int week) {
        System.out.println("Before, Games in week " + Integer.toString(week) + ":\n" + gameData.toString());
        // Get data for this week and afdeling!!!
        FilteredList<Game> firstFilter = gameData.filtered(a -> a.getAfdelingString().equals(afdeling));
        FilteredList<Game> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
        // Setup the tableview
        VBox calendarbox = new VBox();
        final Label label = new Label("Week " + week);
        // TODO: flexible week count according to user selection (1 January or start of competition
        label.setFont(new Font("Arial", 16));
        
        TableView table = new TableView();
        table.setEditable(true);
        table.setDisable(false);

        // TableColumn with datePicker
        TableColumn<Game, MonthDay> gamedatumCol = new TableColumn<>("Datum");
        gamedatumCol.setMinWidth(5);
        gamedatumCol.setCellValueFactory(new PropertyValueFactory<>("gamedatum"));
        gamedatumCol.setCellFactory(col -> new GamedateCell());
        gamedatumCol.setEditable(true);
        gamedatumCol.setOnEditCommit(event -> event.getRowValue().setGameDatum(event.getNewValue()));
        
        TableColumn<Game, MonthDay> gameTimeCol = new TableColumn<>("Uur");
        gameTimeCol.setMinWidth(5);
        gameTimeCol.setCellValueFactory(new PropertyValueFactory<>("gameuur"));
        //gameTimeCol.setCellFactory(col -> new GameuurCell());
        gameTimeCol.setEditable(true);
        gameTimeCol.setOnEditCommit(event -> event.getRowValue().setGameDatum(event.getNewValue()));
        
        
        TableColumn homeTeamCol = new TableColumn("Home team");
        homeTeamCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        homeTeamCol.setCellValueFactory(
            new PropertyValueFactory<>("homeTeamName"));
        homeTeamCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
                };
            
            cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
                }
            });

            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                int rowIndex = cell.getIndex();
                System.out.println("Cell row index: " + rowIndex);

                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                System.out.println("Cell column index: " + columnIndex);

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getDragboard().hasString()) {            

                    String text = db.getString();

                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.now(), LocalTime.of(14, 00), null, null, null, emptyArray));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.now(), LocalTime.of(14, 00), text, null, null, emptyArray));                            

                    } else {
                        System.out.println("Data inserted in week: " + Integer.toString(week));
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), text, gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBaseUmpireNames()));

                    }
                    table.setItems(secondFilter);
                    System.out.println("After, Games in week " + Integer.toString(week) + ":\n" + gameData.toString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  
            return cell;
        });

            TableColumn visitingTeamCol = new TableColumn("Visiting team");
            visitingTeamCol.prefWidthProperty().bind(table.widthProperty().divide(5));
            visitingTeamCol.setCellValueFactory(
                new PropertyValueFactory<>("visitingTeamName")
                );
            visitingTeamCol.setCellFactory(e -> {
                TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Make sure you call super.updateItem, or you might get really weird bugs.
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                    };

            cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
                }
            });

            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getDragboard().hasString()) {            

                    String text = db.getString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, emptyArray));                            
                        }
                       gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, text, null, emptyArray));
                    } else {
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), text, gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBaseUmpireNames()));
                    }
                    table.setItems(secondFilter);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  

            return cell;
        });


        //umpireCol.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2));
        TableColumn plateUmpireCol = new TableColumn("Plate");
        TableColumn baseUmpiresCol = new TableColumn("Base");
        plateUmpireCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        baseUmpiresCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        
        plateUmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("plateUmpireName")
        );
        baseUmpiresCol.setCellValueFactory(
            new PropertyValueFactory<>("baseUmpireNames")
        );
        plateUmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
                };

            cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
                }
            });
            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                int rowIndex = cell.getIndex();
                System.out.println("Cell row index: " + rowIndex);

                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                System.out.println("Cell column index: " + columnIndex);

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getDragboard().hasString()) {            

                    String text = db.getString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, emptyArray));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, text, emptyArray));
                    } else {
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), text, gameData.get(newIndex).getBaseUmpireNames()));
                    }
                    table.setItems(secondFilter);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  
            return cell;
        });
              
        baseUmpiresCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                //@Override
                public void updateItem(ArrayList<String> item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item.toString(), empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
                };

            cell.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()){
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
                }
            });
            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (event.getDragboard().hasString()) {            

                    String text = db.getString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        ArrayList<String> umpArray = new ArrayList<>();
                        umpArray.add(text);
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, emptyArray));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, umpArray));
                    } else {
                        ArrayList<String> umpArray = secondFilter.get(rowIndex).getBaseUmpireNames();
                        umpArray.add(text);
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), umpArray));

                    }
                    table.setItems(secondFilter);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  
            return cell;
        });
            
        table.setItems(secondFilter);
        table.setFixedCellSize(25);
        table.setMaxHeight(150);
        table.setMinHeight(150);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //table.prefHeight(100 + 45);
        //table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(45));

        table.getColumns().addAll(gamedatumCol, gameTimeCol, homeTeamCol, visitingTeamCol, plateUmpireCol, baseUmpiresCol);
        table.getSelectionModel().setCellSelectionEnabled(true);

        calendarbox.setPadding(new Insets(0, 0, 0, 0));
        calendarbox.getChildren().addAll(label, table);

        
    return calendarbox;

           
    }
    
    

    
    
    

    class EditingCell extends TableCell<Game, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    
}
