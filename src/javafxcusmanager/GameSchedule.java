/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.File;
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
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ScrollPane;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author pieter
 */
public class GameSchedule {
    
    private static final int MAX_ITEMS = 3;
    private DocumentHandling documentHandler;
    
    public int startOfWeekCount = 0; // 0 = Runs with Year Calendar (January 1); 1 = Runs with competition start (date can be set);
    private final TableView<Game> table = new TableView<>();
    private Schedule schedule;
    
    private ObservableList<Schedule> scheduleData;
    private final ObservableList<Schedule> scheduleDBdata = FXCollections.observableArrayList();
    
    private GameSchedulePersistence gsp;
    private ArrayList<String> emptyArray = new ArrayList<>();
    private ArrayList<Game> gameArray = new ArrayList<>();

    // Constructor
    public GameSchedule() throws JAXBException, IOException {
        // Load schedule data from the specified file. The current scheduel data will be replaced
        
        
        
        scheduleData = FXCollections.observableArrayList();
        gsp = new GameSchedulePersistence();
        
        scheduleDBdata.addAll(gsp.scheduleData);
        System.out.println("database file path = " + gsp.getScheduleFilePath());
        scheduleData.addListener((ListChangeListener.Change<? extends Schedule> change) -> { 
                    while(change.next()) {
                        if(change.wasUpdated()) {
                            System.out.println("Update detected");
                            // Write to file
                                
                        } else
                            if (change.wasPermutated()) {
                                System.out.println("Was permutated");
                            } else {
                                if (change.wasAdded()) {
                                    //System.out.println("Data was added to scheduleData");
                                    // Write to file
                                    
                                    // Save to database
                                    
                                    //GameSchedule.write(scheduleData, /home/pieter/wedstrijdschema.txt);
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
        Game game1 = new Game("14/04/2019, 15:00", "Frogs", "Wolverines", "Isabelle Verelst", umpArray);
        Game game2 = new Game("14/04/2019, 15:00", "Wielsbeke", "Doornik", "Pieter Stragier", emptyArray);
        Game game3 = new Game("14/04/2019, 15:00", "Ghent", "Zottegem", "Hermelien", emptyArray);
        gamesArray.add(game1);
        gamesArray.add(game2);
        gamesArray.add(game3);
        ArrayList<Game> emptyGamesArray = new ArrayList<>();
        
        
        scheduleData.add(new Schedule("Gold", "1", gamesArray));
        
        // Add 51 empty weeks
        for(int i=2; i<53; i++) {
            scheduleData.add(new Schedule("Gold", Integer.toString(i), emptyGamesArray));
        }
        
    }

    
    public ObservableList<Schedule> scheduleDBdata() { return scheduleDBdata; }

        @XmlElementWrapper(name="schedules")
        @XmlElement(name = "schedule")
        public List<Schedule> getAccounts() {
            return new ArrayList<>(scheduleDBdata);
        }

        public void setAccounts(List<Schedule> schedules) {
            this.scheduleDBdata.setAll(schedules);
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
        
        // Get data for this week and afdeling!!!
        ObservableList<Schedule> weekdata = FXCollections.observableArrayList();
        FilteredList<Schedule> firstFilter = scheduleDBdata.filtered(a -> a.getAfdelingsNaam().equals(afdeling));
        FilteredList<Schedule> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
        weekdata.addAll(secondFilter);
        System.out.println("filteredList: " + secondFilter);
        // Setup the tableview
        VBox calendarbox = new VBox();
        final Label label = new Label("Week " + week);
        // TODO: flexible week count according to user selection (1 January or start of competition
        label.setFont(new Font("Arial", 16));
        
        TableView table = new TableView();
        table.setEditable(true);
        table.setDisable(false);

        TableColumn gameDateCol = new TableColumn("Datum");
        
        gameDateCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        gameDateCol.setCellValueFactory(
                new PropertyValueFactory<>("gameDateString")
        );
        gameDateCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
                };
            cell.setEditable(true);
            return cell;
        });
        TableColumn homeTeamCol = new TableColumn("Home team");
        homeTeamCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        homeTeamCol.setCellValueFactory(
            new PropertyValueFactory<Schedule, String>("homeTeamName"));
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
                            
                            gameArray.add(new Game(null, text, null, null, emptyArray));
                            scheduleDBdata.add(new Schedule(afdeling, Integer.toString(week), gameArray));
                            
                        }
                        
                    } else {
                        // Get current gameArray for specific afdeling, week (and rowIndex)
                        ArrayList<Game> selectedGameArray = secondFilter.get(rowIndex).getGameArray();
                        // Adjust selected gameArray with new data
                        selectedGameArray.set(rowIndex, new Game(selectedGameArray.get(rowIndex).getGameDateString(), text, selectedGameArray.get(rowIndex).getVisitingTeamName(), selectedGameArray.get(rowIndex).getPlateUmpireName(), selectedGameArray.get(rowIndex).getBaseUmpireNames()));
                        // Add adjusted gameArray to schedule
                        // get index of secondfilter data in scheduleDBdata
                        int i = scheduleDBdata.indexOf(secondFilter);
                        System.out.println("index of secondfilter: " + i);
                        scheduleDBdata.set(i, new Schedule(afdeling, Integer.toString(week), selectedGameArray));

                    }
                    table.setItems(scheduleDBdata);
                    
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
                    if(rowIndex < 0 || rowIndex >= scheduleDBdata.size()) {
                        gameArray.add(new Game(null, null, text, null, emptyArray));
                            scheduleDBdata.add(new Schedule(afdeling, Integer.toString(week), gameArray));
                    } else {
                        // Get current gameArray for specific afdeling, week (and rowIndex)
                        FilteredList<Schedule> firstFilter = scheduleDBdata.filtered(a -> a.getAfdelingsNaam().equals(afdeling));
                        FilteredList<Schedule> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
                        ArrayList<Game> selectedGameArray = secondFilter.get(rowIndex).getGameArray();
                        // Adjust selected gameArray with new data
                        selectedGameArray.set(rowIndex, new Game(selectedGameArray.get(rowIndex).getGameDateString(), selectedGameArray.get(rowIndex).getHomeTeamName(), text, selectedGameArray.get(rowIndex).getPlateUmpireName(), selectedGameArray.get(rowIndex).getBaseUmpireNames()));
                        // Add adjusted gameArray to schedule
                        scheduleDBdata.set(rowIndex, new Schedule(afdeling, Integer.toString(week), selectedGameArray));                    }
                    table.setItems(scheduleDBdata);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  

            return cell;
        });


        TableColumn umpireCol = new TableColumn("Umpire");
        //umpireCol.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2));
        TableColumn plateUmpireCol = new TableColumn("Plate");
        TableColumn baseUmpiresCol = new TableColumn("Base");
        plateUmpireCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        baseUmpiresCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        umpireCol.getColumns().addAll(plateUmpireCol, baseUmpiresCol);
        
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
                    if(rowIndex < 0 || rowIndex >= scheduleDBdata.size()) {
                        gameArray.add(new Game(null, null, null, text, emptyArray));
                        scheduleDBdata.add(new Schedule(afdeling, Integer.toString(week), gameArray));
                    } else {
                        // Get current gameArray for specific afdeling, week (and rowIndex)
                        FilteredList<Schedule> firstFilter = scheduleDBdata.filtered(a -> a.getAfdelingsNaam().equals(afdeling));
                        FilteredList<Schedule> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
                        ArrayList<Game> selectedGameArray = secondFilter.get(rowIndex).getGameArray();
                        // Adjust selected gameArray with new data
                        selectedGameArray.set(rowIndex, new Game(selectedGameArray.get(rowIndex).getGameDateString(), selectedGameArray.get(rowIndex).getHomeTeamName(), selectedGameArray.get(rowIndex).getVisitingTeamName(), text, selectedGameArray.get(rowIndex).getBaseUmpireNames()));
                        // Add adjusted gameArray to schedule
                        scheduleDBdata.set(rowIndex, new Schedule(afdeling, Integer.toString(week), selectedGameArray));
                    }
                    table.setItems(scheduleDBdata);
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
                    if(rowIndex < 0 || rowIndex >= scheduleDBdata.size()) {
                        ArrayList<String> umpArray = new ArrayList<>();
                        umpArray.add(text);
                        
                        gameArray.add(new Game(null, null, null, null, umpArray));
                        scheduleDBdata.add(new Schedule(afdeling, Integer.toString(week), gameArray));
                    } else {
                        // Get current gameArray for specific afdeling, week (and rowIndex)
                        FilteredList<Schedule> firstFilter = scheduleDBdata.filtered(a -> a.getAfdelingsNaam().equals(afdeling));
                        FilteredList<Schedule> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
                        ArrayList<Game> selectedGameArray = secondFilter.get(rowIndex).getGameArray();
                        // Get current base umpires
                        ArrayList<String> umpArray = new ArrayList<>();
                        umpArray.addAll(selectedGameArray.get(rowIndex).getBaseUmpireNames());
                        umpArray.add(text);
                        // Adjust selected gameArray with new data, 
                        selectedGameArray.set(rowIndex, new Game(selectedGameArray.get(rowIndex).getGameDateString(), selectedGameArray.get(rowIndex).getHomeTeamName(), selectedGameArray.get(rowIndex).getVisitingTeamName(), selectedGameArray.get(rowIndex).getPlateUmpireName(), umpArray));
                        // Add adjusted gameArray to schedule
                        scheduleDBdata.set(rowIndex, new Schedule(afdeling, Integer.toString(week), selectedGameArray));
                    }
                    table.setItems(scheduleDBdata);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
                } 
            });  
            return cell;
        });
            
        table.setItems(scheduleData);
        table.setFixedCellSize(25);
        table.setMaxHeight(150);
        table.setMinHeight(150);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //table.prefHeight(100 + 45);
        //table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(45));

        table.getColumns().addAll(gameDateCol, homeTeamCol, visitingTeamCol, umpireCol);
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
