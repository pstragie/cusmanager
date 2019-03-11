/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import static java.util.Calendar.*;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;


/** Wedstrijdschema
 * 
 * @author Pieter Stragier <pstragier@gmail.be>
 * @version 1.0
 * @since 1.0
 */
public class GameSchedule {
    
    private static final int MAX_ITEMS = 3;
    private DocumentHandling documentHandler;
    public int startOfWeekCount = 0; // 0 = Runs with Year Calendar (January 1); 1 = Runs with competition start (date can be set);
    private final TableView<Game> table = new TableView<>();
    private ObservableList<Game> gameData;
    private ObservableList<Team> teams;
    private ObservableList<Afdeling> afdelingen;
    private ObservableList<Club> clubs;
    private ObservableList<Umpire> umpires;
    private ObservableList<LocalTime> uren;
    private ArrayList<Umpire> emptyArray = new ArrayList<>();
    private String colorCellFilled = "lightgreen";
    private String colorCellWarning = "orange";
    private String colorCellOK = "green";
    private LocalDate desiredDate;
    private String seizoen;
    //private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

    
    // Constructor
    public GameSchedule(ObservableList<Team> teams, ObservableList<Afdeling> afdelingen, ObservableList<Club> clubs, ObservableList<Umpire> umpires, String seizoen) {
        this.teams = teams;
        this.afdelingen = afdelingen;
        this.clubs = clubs;
        this.umpires = umpires;
        this.seizoen = seizoen;
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
        
        afdelingen.forEach(a -> {
            for (int calendarWeek=1; calendarWeek<2; calendarWeek++) {
                String x = Integer.toString(calendarWeek);
                desiredDate = getDate(calendarWeek, seizoen);
                String gamenumber = "a" + x;
                gameData.add(new Game(a.getAfdelingsNaam(), Integer.toString(calendarWeek), MonthDay.of(desiredDate.getMonth(), desiredDate.getDayOfMonth()), null, null, null, null, null, null, null, gamenumber));
            }
        });
        
        /*
        gameData.add(new Game("Gold BB", "1", MonthDay.of(APRIL, 14), LocalTime.of(2, 30), "Frogs", "Wolverines", "Isabelle Verelst", "Lilly Roos", null, null));
        gameData.add(new Game("Gold BB", "1", MonthDay.of(APRIL, 14), LocalTime.of(14, 00), "Wielsbeke", "Doornik", "Pieter Stragier", "Lilly Roos", null, null));
        gameData.add(new Game("Gold BB", "2", MonthDay.of(APRIL, 22), LocalTime.of(14, 00), "Ghent", "Zottegem", "Hermelien", null, null, null));
        gameData.add(new Game("1BB", "1", MonthDay.of(APRIL, 14), LocalTime.of(14, 00), "Eagles", "Pioneers", "Utah", null, null, null));
        */        
        
        
    }

    /** Get desiredDate
     * 
     * @param calendarWeek
     * @param seizoen
     * @return 
     */
    public LocalDate getDate(int calendarWeek, String seizoen) {
        LocalDate desiredDate = LocalDate.ofYearDay(Integer.parseInt(seizoen.trim()), 1)
                        .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, calendarWeek)
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
        
        return desiredDate;
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
            for(int week=1; week<52; week++) {
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
        
        FilteredList<Game> firstFilter = gameData.filtered(a -> a.getAfdelingString().equals(afdeling));
        FilteredList<Game> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
        // Setup the tableview
        VBox calendarbox = new VBox(2);
        final Label label = new Label("Week " + week);
        label.setPadding(new Insets(1, 0, 1, 5));
        // TODO: flexible week count according to user selection (1 January or start of competition
        label.setFont(new Font("Arial", 15));
        
        TableView table = new TableView();
        table.setEditable(true);
        table.setDisable(false);

       /*
        table.setRowFactory(new Callback<TableView<Game>, TableRow<Game>>() {
            @Override
            public TableRow<Game> call(TableView<Game> table) {
                final TableRow<Game> row = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                MenuItem wisRij = new MenuItem("Wis volledige wedstrijd");
                cm.getItems().add(wisRij);
                wisRij.setOnAction(wisrij -> {
                    System.out.println("Wis rij");
                    int newIndex = gameData.indexOf(secondFilter.get(row.getIndex()));
                    gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));

                });
                MenuItem wisCel = new MenuItem("Wis dit veld");
                cm.getItems().add(wisCel);
                wisCel.setOnAction(wiscel -> {
                    System.out.println("Wis cel");
                    int rowIndex = gameData.indexOf(secondFilter.get(row.getIndex()));
                    
                    
                });
                row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                return row;
            }
        });
        */
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
            
            TableCell<ObservableList<Team>, String> cell = new TableCell<ObservableList<Team>, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        setGraphic(null);
                    }
                }
                };
            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis home team");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis hometeam");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), null, gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));

                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                });    
            
            
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("ClubModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
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
                    FilteredList filt = teams.filtered(t -> t.getTeamNaam().equals(text));
                    System.out.println("filter: " + filt);
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.of(getDate(week, seizoen).getMonth(), getDate(week, seizoen).getDayOfMonth()), LocalTime.of(14, 00), null, null, null, null, null, null));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.of(getDate(week, seizoen).getMonth(), getDate(week, seizoen).getDayOfMonth()), LocalTime.of(14, 00), text, null, null, null, null, null));                            

                    } else {
                        System.out.println("Data inserted in week: " + Integer.toString(week));
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), text, gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));

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
                            setStyle("");
                            setGraphic(null);
                        } else {
                            setText(item);
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                            setGraphic(null);
                        }
                    }
                    };
            
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis visiting team");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis visitingteam");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), null, gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("ClubModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
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
                            gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.of(getDate(week, seizoen).getMonth(), getDate(week, seizoen).getDayOfMonth()), null, null, null, null, null, null, null));                            
                        }
                       gameData.add(new Game(afdeling, Integer.toString(week), MonthDay.of(getDate(week, seizoen).getMonth(), getDate(week, seizoen).getDayOfMonth()), null, null, text, null, null, null, null));
                    } else {
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), text, gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));
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
        plateUmpireCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        
        plateUmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("plateUmpireName")
        );
        
        plateUmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        setGraphic(null);
                    }
                }
                };
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis plate umpire");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis plate umpire");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), null, gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("UmpireModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
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
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, text, null, null, null));
                    } else {
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), text, gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));
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
        
        TableColumn base1UmpiresCol = new TableColumn("Base 1");
        base1UmpiresCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        base1UmpiresCol.setCellValueFactory(
            new PropertyValueFactory<>("base1UmpireName")
        );
        base1UmpiresCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                //@Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        setGraphic(null);
                    }
                }
            };

            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis base 1 umpire");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis base 1 umpire");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), null, gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
            
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("UmpireModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
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
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, text, null, null));
                    } else {
                        
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), text, gameData.get(newIndex).getBase2UmpireName(), gameData.get(newIndex).getBase3UmpireName()));

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
        
        TableColumn base2UmpiresCol = new TableColumn("Base 2");
        base2UmpiresCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        base2UmpiresCol.setCellValueFactory(
            new PropertyValueFactory<>("base2UmpireName")
        );
        base2UmpiresCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                //@Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        setGraphic(null);
                    }
                }
            };

            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis base 2 umpire");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis base 2 umpire");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), null, gameData.get(newIndex).getBase3UmpireName()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
            
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("UmpireModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
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
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));                            
                        }
                        gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, text, null));
                    } else {
                        
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), text, gameData.get(newIndex).getBase3UmpireName()));

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
        
        TableColumn base3UmpiresCol = new TableColumn("Base 3");
        base3UmpiresCol.prefWidthProperty().bind(table.widthProperty().divide(5));
        base3UmpiresCol.setCellValueFactory(
            new PropertyValueFactory<>("base3UmpireName")
        );
        base3UmpiresCol.setCellFactory(e -> {
            TableCell<ObservableList<String>, String> cell = new TableCell<ObservableList<String>, String> () {
                //@Override
                public void updateItem(String item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        setGraphic(null);
                    }
                }
            };

            cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    MenuItem wisCel = new MenuItem("Wis base 3 umpire");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis base 3 umpire");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase3UmpireName(), null));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
            
            cell.setOnDragOver((DragEvent event) -> {
                // data is dragged over the target 
                Dragboard db = event.getDragboard();
                Class gSource = event.getGestureSource().getClass().getEnclosingClass();
                if (event.getDragboard().hasString() && gSource.toString().contains("UmpireModel")) {
                    if (cell.getItem() != null) {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            cell.setOnDragExited((DragEvent event) -> {
                // drag has exited
                if (cell.getItem() != null) {
                    cell.setStyle("-fx-background-color: '" + colorCellFilled + "'");
                } else {
                    cell.setStyle("");
                }
            });
            
            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    System.out.println("drag detected");
                    dragDetected(event);
                }
                
                private void dragDetected(MouseEvent event) {
                    String sourceText = cell.getText();
                    if (sourceText == null || sourceText.trim().equals("")) {
                        event.consume();
                        return;
                    }
                    // Get the row index of this cell
                    int rowIndex = cell.getIndex();
                    // Get the column index of this cell.
                    int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                    
                    
                }
            });

            
            cell.setOnDragDropped(new EventHandler<DragEvent>() {
                
                @Override
                public void handle(DragEvent event) {
                    System.out.println("drag dropped");
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
                                gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, null));                            
                            }
                            gameData.add(new Game(afdeling, Integer.toString(week), null, null, null, null, null, null, null, text));
                        } else {

                            int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                            gameData.set(newIndex, new Game(afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeamName(), gameData.get(newIndex).getVisitingTeamName(), gameData.get(newIndex).getPlateUmpireName(), gameData.get(newIndex).getBase1UmpireName(), gameData.get(newIndex).getBase2UmpireName(), text));

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

        table.getColumns().addAll(gamedatumCol, gameTimeCol, homeTeamCol, visitingTeamCol, plateUmpireCol, base1UmpiresCol, base2UmpiresCol, base3UmpiresCol);
        table.getSelectionModel().setCellSelectionEnabled(true);

        calendarbox.setPadding(new Insets(0, 0, 0, 0));
        calendarbox.getChildren().addAll(label, table);

        
    return calendarbox;

           
    }
    
    /** Check if umpire has already a game this date
     * 
     * @param umpire String umpire
     * @param datum String datum
     * @return Boolean value
     */
    private Boolean umpireDouble(String umpire, String datum) {
        Boolean bool = Boolean.FALSE;
        
        // Check if umpire is already present this week
        FilteredList<Game> filteredGames = gameData.filtered(g -> g.getGameDatum().equals(datum));
        for (Game gameToday : filteredGames) {
            ArrayList<String> umpiresForThisDate = new ArrayList<>();
            umpiresForThisDate.add(gameToday.getPlateUmpireName());
            umpiresForThisDate.add(gameToday.getBase1UmpireName());
            umpiresForThisDate.add(gameToday.getBase2UmpireName());
            umpiresForThisDate.add(gameToday.getBase3UmpireName());
            if (umpiresForThisDate.contains(umpire)) {
                bool = Boolean.TRUE;
            } else {
                bool = Boolean.FALSE;
            }
        }
        
        return bool;
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
