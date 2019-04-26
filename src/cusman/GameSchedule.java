/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusman;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
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
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private MainPanel mainpanel;
    private Database database;
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
    private String colorCellNone = "transparent";
    private String colorCellError = "red";
    private LocalDate desiredDate;
    private String seizoen;
    private String startOfYear;
    private String defaultGamehour;
    private Preferences pref;
    private ApiLocationDistance apidistance;
    //private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

    
    // Constructor
    public GameSchedule(ObservableList<Game> gameData, ObservableList<Team> teams, ObservableList<Afdeling> afdelingen, ObservableList<Club> clubs, ObservableList<Umpire> umpires, String seizoen) {
        mainpanel = new MainPanel();
        
        this.teams = teams;
        this.afdelingen = afdelingen;
        this.clubs = clubs;
        this.umpires = umpires;
        this.seizoen = seizoen;
        this.gameData = gameData;
        // Load schedule data from the specified file. The current scheduel data will be replaced
        gameData = FXCollections.observableArrayList();
        pref = Preferences.userNodeForPackage(AppSettings.class);
        startOfYear = pref.get("StartOfYear", "12");
        defaultGamehour = pref.get("DefaultGameTime", "14:00");
        database = new Database();
    }

    /** Get desiredDate
     * 
     * @param calendarWeek
     * @param seizoen
     * @param dag (integer), Sat = 6
     * @return Date of the Friday of this week
     */
    public LocalDate getDate(int calendarWeek, String seizoen, int dag) {
        desiredDate = LocalDate.ofYearDay(Integer.parseInt(seizoen.trim()), 1)
                        .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, calendarWeek)
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.of(dag)));
        
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
    
    /** Get Current Week
     * 
     * @return Integer
     */
    private int getCurrentWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }
    
    /** Create Calendar for afdeling
     * 
     * @param afdeling
     * @return 
     */
    public VBox createCalendar(String afdeling) {
        
        VBox yearCalendar = new VBox();
        VBox vbox = new VBox();
        Double max_height = 170.0;
        yearCalendar.setPadding(new Insets(5, 0, 5, 0));
        for(int week=Integer.parseInt(startOfYear); week<=52; week++) {
            vbox.getChildren().add(createWeekCalendar(afdeling, week));
        }
        
        ScrollPane sideBarScroller = new ScrollPane(vbox);
        sideBarScroller.setFitToWidth(true);
        Double corr = Double.parseDouble(startOfYear);
        Double teller = getCurrentWeek() - corr - 1;
        Double noemer = 52.0 - corr;
        Double scrollHeight = teller / noemer;
        sideBarScroller.setVvalue(scrollHeight);
        yearCalendar.getChildren().add(sideBarScroller);
        return yearCalendar;
    }
    
    public VBox createWeekCalendar(String afdeling, int week) {
        // Get data for this seizoen, week and afdeling!!!
        //System.out.println("filtered games for " + afdeling + ", " + Integer.toString(week) + ", " + seizoen + ".");
        FilteredList<Game> seizoenFilter = gameData.filtered(s -> s.getSeizoen().equals(seizoen));
        FilteredList<Game> firstFilter = seizoenFilter.filtered(a -> a.getAfdelingString().equals(afdeling));
        FilteredList<Game> secondFilter = firstFilter.filtered(w -> w.getWeekString().equals(Integer.toString(week)));
        
        
        // Setup the tableview
        VBox calendarbox = new VBox(2);
        final Label label = new Label("Week " + week);
        label.setPadding(new Insets(1, 0, 1, 5));
        final Button addLineButton = new Button("+");
        addLineButton.setFont(new Font("Arial", 15));
        addLineButton.setOnAction(pressed -> {
            
        });
        // TODO: flexible week count according to user selection (1 January or start of competition
        label.setFont(new Font("Arial", 15));
        
        TableView table = new TableView();
        
        table.setEditable(true);
        table.setDisable(false);

        
        table.setOnDragEntered(value -> {
            // Verify if table of this week is empty
            String w = String.format("%02d", week);
            String g = String.format("%02d", 1);
            String gi = afdeling+seizoen+w+g+getRandomString();
            try {
                if (!database.checkIfWeekHasGame(week, afdeling)) {
                    System.out.println("Dragover detected, creating first empty game: " + gi);
                    gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));
                } 
            } catch (SQLException ex) {
                Logger.getLogger(GameSchedule.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        
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
                    
                    
                });Deurne Spartans
                row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                return row;
            }
        });
        */
        
        // TableColumn with gamenumber
        TableColumn<Game, String> gamenumberCol = new TableColumn<>("Game #");
        gamenumberCol.setMinWidth(5);
        gamenumberCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        gamenumberCol.setEditable(true);
        gamenumberCol.setCellValueFactory(new PropertyValueFactory<>("gameindex"));
        // gamenumberCol.setCellFactory(col -> new GamenumberCell());
        gamenumberCol.setOnEditCommit(event -> event.getRowValue().setGameNumber(event.getNewValue()));
        gamenumberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gamenumberCol.setOnEditCommit((CellEditEvent<Game, String> t) -> {
            ((Game) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setGameNumber(t.getNewValue());
            database.updateSingleItemInGameInDatabase("gamenumber", t.getTableView().getItems().get(t.getTablePosition().getRow()).getGameindex(), t.getNewValue());
        });
            
        // Invisible column gameindexCol
        TableColumn<Game, String> gameindexCol = new TableColumn<>("Hidden index");
        gameindexCol.setCellValueFactory(new PropertyValueFactory<>("gameindex"));
        


        // TableColumn with datePicker
        
        TableColumn<Game, LocalDate> gamedatumCol = new TableColumn<>("Datum");
        gamedatumCol.setMinWidth(5);
        gamedatumCol.setCellValueFactory(new PropertyValueFactory<>("gamedatum"));
        gamedatumCol.setCellFactory(col -> new GamedateCell());
        gamedatumCol.setEditable(true);
        gamedatumCol.setOnEditCommit(event -> { 
            event.getRowValue().setGameDatum(event.getNewValue());
            int rowIndex = event.getTablePosition().getRow();
            int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
            gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), event.getNewValue(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));                
        });
        
        TableColumn<Game, String> gameTimeCol = new TableColumn<>("Uur");
        gameTimeCol.setMinWidth(5);
        gameTimeCol.resizableProperty();
        gameTimeCol.prefWidthProperty().bind(table.widthProperty().divide(18));
        gameTimeCol.setCellValueFactory(new PropertyValueFactory<>("gameuur"));
        //gameTimeCol.setCellFactory(col -> new GameuurCell());
        gameTimeCol.setEditable(true);
        gameTimeCol.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,2}([\\:]\\d{0,2})?")) {
                gameTimeCol.setText(oldValue);
                
            } else {
                Integer index = table.getSelectionModel().getSelectedIndex();
                //database.updateSingleItemInGameInDatabase("gametime", newValue, gameData.get(index).getGameindex());
            }
        });
        //gameTimeCol.setOnEditCommit(event -> event.getRowValue().setGameUur(event.getNewValue()));
        gameTimeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gameTimeCol.setOnEditCommit((CellEditEvent<Game, String> t) -> {
            ((Game) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setGameUur(t.getNewValue());
            System.out.println("gameTimeCol edited! " + t.getNewValue());
            //database.updateSingleItemInGameInDatabase("gametime", t.getTableView().getItems().get(t.getTablePosition().getRow()).getGameindex(), t.getNewValue());
            database.updateSingleItemInGameInDatabase("gametime", t.getNewValue(), t.getTableView().getItems().get(t.getTablePosition().getRow()).getGameindex());
        });
        
        
        TableColumn homeTeamCol = new TableColumn("Home team");
        TableColumn visitingTeamCol = new TableColumn("Visiting team");

        homeTeamCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        homeTeamCol.setCellValueFactory(
            new PropertyValueFactory<>("hometeam"));
     
        homeTeamCol.setCellFactory(e -> {
            
            TableCell<ObservableList<Game>, Team> cell = new TableCell<ObservableList<Game>, Team> () {
                @Override
                public void updateItem(Team item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getTeamNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) homeTeamCol.getTableView().getItems().get(currentIndex);
                        Team vteam = thisGame.getVisitingTeam();
                        if (!empty) {
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                            if (vteam != null) {
                                if (vteam.getTeamNaam() != null && vteam.getTeamNaam().equals(item.getTeamNaam())) {
                                    setStyle("-fx-background-color: '" + colorCellError + "'");
                                }
                            } else {
                                setStyle("-fx-background-color: '" + colorCellFilled + "'");
                            }
                        } else {
                            setStyle("-fx-background-color: '" + colorCellNone + "'");
                        }
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
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), null, gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), null));

                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(),  null, null, null, null, null, null, null, seizoen, null));
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
                }
            });
            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped((DragEvent event) -> {
                int rowIndex = cell.getIndex();
                System.out.println("Cell row index: " + rowIndex);
                
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                System.out.println("Cell column index: " + columnIndex);

                Dragboard db = event.getDragboard();
                boolean success = false;
                
                if (event.getDragboard().hasString()) {
                    
                    Team team = database.getTeamFromDatabase(db.getString(), afdeling); // Team naam!
                    FilteredList filt = teams.filtered(t -> t.getTeamNaam().equals(team));
                    System.out.println("filter: " + filt);
                    String w = String.format("%02d", week);
                    String g = String.format("%02d", rowIndex+1);
                    String gi = afdeling+seizoen+w+g+getRandomString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            String ga = String.format("%02d", rowIndex-sprong+i+1);
                            String gix = afdeling+seizoen+w+ga+getRandomString();
                            gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                        }
                        gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, team, null, null, null, null, null, "", seizoen, database.getClubFromTeam(team.getTeamNaam())));                      
                    } else {
                        System.out.println("Data inserted in week: " + Integer.toString(week));
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        System.out.println("Home team = " + gameData.get(newIndex).getHomeTeam());
                        
                        if (gameData.get(newIndex).getHomeTeam() != null) {
                            String hcn = database.getClubFromTeam(gameData.get(newIndex).getHomeTeam().getTeamNaam()).getClubNummer();
                            if (hcn != null) {
                                System.out.println("Home club = " + hcn);
                            }
                        }
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), team, gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), database.getClubFromTeam(team.getTeamNaam())));

                    }
                    table.setItems(secondFilter);
                    System.out.println("After, Games in week " + Integer.toString(week) + ":\n" + gameData.toString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });  
            return cell;
        });
        
        TableColumn homeClubCol = new TableColumn("@Field");
        homeClubCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        homeClubCol.setCellValueFactory(
            new PropertyValueFactory<>("homeclub"));
     
        
        homeClubCol.setCellFactory(e -> {
            
            TableCell<ObservableList<Game>, Club> cell = new TableCell<ObservableList<Game>, Club> () {
                @Override
                public void updateItem(Club item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getClubNaam());
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
                    MenuItem wisCel = new MenuItem("Wis @field");
                    cm.getItems().add(wisCel);
                    wisCel.setOnAction(wiscel -> {
                        System.out.println("Wis @field");
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), null));

                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), null, null, null, null, null, null, null, null, null, seizoen, null));
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
                    if (cell.getItem() == null) {
                        cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                    } else {
                        cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
                }
            });
            // HERE IS HOW TO GET THE ROW AND CELL INDEX
            cell.setOnDragDetected(eh -> {
                // Get the row index of this cell
                int rowIndex = cell.getIndex();
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
            });

            cell.setOnDragDropped((DragEvent event) -> {
                int rowIndex = cell.getIndex();
                System.out.println("Cell row index: " + rowIndex);
                
                // Get the column index of this cell.
                int columnIndex = cell.getTableView().getColumns().indexOf(cell.getTableColumn());
                System.out.println("Cell column index: " + columnIndex);

                Dragboard db = event.getDragboard();
                boolean success = false;
                
                if (event.getDragboard().hasString()) {
                    
                    String text = db.getString(); // Team naam!
                    FilteredList filt = teams.filtered(t -> t.getTeamNaam().equals(text));
                    System.out.println("filter: " + filt);
                    String w = String.format("%02d", week);
                    String g = String.format("%02d", rowIndex+1);
                    String gi = afdeling+seizoen+w+g+getRandomString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            String ga = String.format("%02d", rowIndex-sprong+i+1);
                            String gix = afdeling+seizoen+w+ga+getRandomString();
                            gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                        }
                        gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, database.getClubFromTeam(text)));
                        
                    } else {
                        System.out.println("Data inserted in week: " + Integer.toString(week));
                        // get index of filteredGame in gameData
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        System.out.println("Home team = " + gameData.get(newIndex).getHomeTeam());
                        System.out.println("Home club = " + database.getClubFromTeam(text));
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), database.getClubFromTeam(text)));

                    }
                    table.setItems(secondFilter);
                    System.out.println("After, Games in week " + Integer.toString(week) + ":\n" + gameData.toString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });  
            return cell;
        });

        visitingTeamCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        visitingTeamCol.setCellValueFactory(
            new PropertyValueFactory<>("visitingteam")
            );
        visitingTeamCol.setCellFactory(e -> {
            TableCell<ObservableList<Game>, Team> cell = new TableCell<ObservableList<Game>, Team> () {
                @Override
                public void updateItem(Team item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getTeamNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) visitingTeamCol.getTableView().getItems().get(currentIndex);
                        Team hteam = thisGame.getHomeTeam();
                        if (!empty) {
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                            if (hteam != null) {
                                if (hteam.getTeamNaam() != null && hteam.getTeamNaam().equals(item.getTeamNaam())) {
                                    setStyle("-fx-background-color: '" + colorCellError + "'");
                                }
                            } else {
                                setStyle("-fx-background-color: '" + colorCellFilled + "'");
                            }
                        } else {
                            setStyle("-fx-background-color: '" + colorCellNone + "'");
                        }
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
                    gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), null, gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                });
                MenuItem wisRij = new MenuItem("Wis wedstrijd");
                cm.getItems().add(wisRij);
                wisRij.setOnAction(wisrij -> {
                    int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                    gameData.remove(newIndex);
                    //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), null, null, null, null, null, null, null, null, null, seizoen, null));
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
                if (cell.getItem() == null) {
                    cell.setStyle("-fx-background-color: '" + colorCellOK + "'");
                } else {
                    cell.setStyle("-fx-background-color: '" + colorCellWarning + "'");
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
                cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
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

                Team team = database.getTeamFromDatabase(db.getString(), afdeling);
                String w = String.format("%02d", week);
                String g = String.format("%02d", rowIndex+1);
                String gi = afdeling+seizoen+w+g+getRandomString();
                if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                    int sprong = rowIndex - secondFilter.size();
                    for(int i=0; i<sprong; i++) {
                        String ga = String.format("%02d", rowIndex-sprong+i+1);
                        String gix = afdeling+seizoen+w+ga+getRandomString();
                        gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                    }
                   gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, team, null, null, null, null, "", seizoen, null));
                } else {
                    // get index of filteredGame in gameData
                    int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                    gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), team, gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
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
        TableColumn base1UmpireCol = new TableColumn("Base 1");
        TableColumn base2UmpireCol = new TableColumn("Base 2");
        TableColumn base3UmpireCol = new TableColumn("Base 3");
        plateUmpireCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        
        plateUmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("plateUmpire")
        );
        
        plateUmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<Game>, Umpire> cell = new TableCell<ObservableList<Game>, Umpire> () {
                @Override
                public void updateItem(Umpire item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty ) {
                        setText(null);
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        //System.out.println("ump = " + item.getUmpireNaam());
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) plateUmpireCol.getTableView().getItems().get(currentIndex);
                        Umpire base1Ump = thisGame.getBase1Umpire();
                        Umpire base2Ump = thisGame.getBase2Umpire();
                        Umpire base3Ump = thisGame.getBase3Umpire();
                        
                        String color = colorCellNone;
                        if (!empty) {
                            color = colorCellFilled;
                            if (base1Ump != null && base1Ump.getUmpireLicentie().equals(item.getUmpireLicentie())) {
                                color = colorCellError;
                            } else if (base2Ump != null && base2Ump.getUmpireLicentie().equals(item.getUmpireLicentie())) {
                                color = colorCellError;
                            } else if (base3Ump != null && base3Ump.getUmpireLicentie().equals(item.getUmpireLicentie())) {
                                color = colorCellError;
                            } else {
                                color = colorCellFilled;
                            }
                        } else {
                            color = colorCellNone;
                            //setStyle("-fx-background-color: '" + colorCellError + "'");
                        }
                            
                        setStyle("-fx-background-color: '" + color + "'");
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
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), null, gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, null, seizoen, null));
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
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
                    System.out.println("String on db = " + db.getString());
                    Umpire umpire = database.getUmpireFromDatabase(db.getString());
                    String w = String.format("%02d", week);
                    String g = String.format("%02d", rowIndex+1);
                    String gi = afdeling+seizoen+w+g+getRandomString();
                    int newIndex1 = gameData.indexOf(secondFilter.get(rowIndex));
                    if (!database.umpireHasGameOnSameDay(umpire, gameData.get(newIndex1).getGameDatum())) {
                        System.out.println("Umpire does not have game on this day!");
                        if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                            int sprong = rowIndex - secondFilter.size();
                            for(int i=0; i<sprong; i++) {
                                String ga = String.format("%02d", rowIndex-sprong+i+1);
                                String gix = afdeling+seizoen+w+ga+getRandomString();
                                gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                            }
                            gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, umpire, null, null, null, "", seizoen, null));
                        } else {
                            // get index of filteredGame in gameData
                            int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                            gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), umpire, gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                        }
                    } else {
                        System.out.println("Umpire already has a game on this day!");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Mogelijks dubbele boeking");
                        if (gameData.get(newIndex1).getHomeClub() != null) {
                            alert.setHeaderText("Umpire heeft al een wedstrijd op " + gameData.get(newIndex1).getGameDatum() + "\nOp het veld van: " + gameData.get(newIndex1).getHomeClub().getClubNaam() + "\nAfdeling: " + gameData.get(newIndex1).getAfdelingString());
                        } else {
                            alert.setHeaderText("");
                        }
                        alert.setContentText("Umpire toch plaatsen?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                            if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                                int sprong = rowIndex - secondFilter.size();
                                for(int i=0; i<sprong; i++) {
                                    String ga = String.format("%02d", rowIndex-sprong+i+1);
                                    String gix = afdeling+seizoen+w+ga+getRandomString();
                                    gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                                }
                                gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, umpire, null, null, null, "", seizoen, null));
                            } else {
                                // get index of filteredGame in gameData
                                int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                                gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), umpire, gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                            }
                        } else {
                            System.out.println("Double booking canceled.");
                        }
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
        
        
        base1UmpireCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        base1UmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("base1Umpire")
        );
        base1UmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<Game>, Umpire> cell = new TableCell<ObservableList<Game>, Umpire> () {
                //@Override
                public void updateItem(Umpire item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) base1UmpireCol.getTableView().getItems().get(currentIndex);
                        Umpire plateUmp = thisGame.getPlateUmpire();
                        Umpire base2Ump = thisGame.getBase2Umpire();
                        Umpire base3Ump = thisGame.getBase3Umpire();
                        if (!empty && ((plateUmp != null && plateUmp.equals(item)) || (base2Ump != null && base2Ump.equals(item)) || (base3Ump != null && base3Ump.equals(item)))) {
                            setStyle("-fx-background-color: '" + colorCellError + "'");
                        } else {
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        }
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
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), null, gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, null, seizoen, null));
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
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

                    Umpire umpire = database.getUmpireFromDatabase(db.getString());
                    String w = String.format("%02d", week);
                    String g = String.format("%02d", rowIndex+1);
                    String gi = afdeling+seizoen+w+g+getRandomString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            String ga = String.format("%02d", rowIndex-sprong+i+1);
                            String gix = afdeling+seizoen+w+ga+getRandomString();
                            gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                        }
                        gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, umpire, null, null, "", seizoen, null));
                    } else {
                        
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), umpire, gameData.get(newIndex).getBase2Umpire(), gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));

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
        
        base2UmpireCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        base2UmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("base2Umpire")
        );
        base2UmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<Game>, Umpire> cell = new TableCell<ObservableList<Game>, Umpire> () {
                //@Override
                public void updateItem(Umpire item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) base1UmpireCol.getTableView().getItems().get(currentIndex);
                        Umpire plateUmp = thisGame.getPlateUmpire();
                        Umpire base1Ump = thisGame.getBase1Umpire();
                        Umpire base3Ump = thisGame.getBase3Umpire();
                        if (!empty && ((base1Ump != null && base1Ump.equals(item)) || (plateUmp != null && plateUmp.equals(item)) || (base3Ump != null && base3Ump.equals(item)))) {
                            setStyle("-fx-background-color: '" + colorCellError + "'");
                        } else {
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        }
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
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), null, gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, null, seizoen, null));
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
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

                    Umpire umpire = database.getUmpireFromDatabase(db.getString());
                    String w = String.format("%02d", week);
                    String g = String.format("%02d", rowIndex+1);
                    String gi = afdeling+seizoen+w+g+getRandomString();
                    if(rowIndex < 0 || rowIndex >= secondFilter.size()) {
                        
                        int sprong = rowIndex - secondFilter.size();
                        for(int i=0; i<sprong; i++) {
                            String ga = String.format("%02d", rowIndex-sprong+i+1);
                            String gix = afdeling+seizoen+w+ga+getRandomString();
                            gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                        }
                        gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, umpire, null, "", seizoen, null));
                    } else {
                        
                        int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), umpire, gameData.get(newIndex).getBase3Umpire(), gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));

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
        
        base3UmpireCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        base3UmpireCol.setCellValueFactory(
            new PropertyValueFactory<>("base3Umpire")
        );
        base3UmpireCol.setCellFactory(e -> {
            TableCell<ObservableList<Game>, Umpire> cell = new TableCell<ObservableList<Game>, Umpire> () {
                //@Override
                public void updateItem(Umpire item, boolean empty) {
                    // Make sure you call super.updateItem, or you might get really weird bugs.
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("-fx-background-color: '" + colorCellNone + "'");
                        setGraphic(null);
                    } else {
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                        int currentIndex = indexProperty().getValue();
                        Game thisGame = (Game) base1UmpireCol.getTableView().getItems().get(currentIndex);
                        Umpire plateUmp = thisGame.getPlateUmpire();
                        Umpire base1Ump = thisGame.getBase1Umpire();
                        Umpire base2Ump = thisGame.getBase2Umpire();
                        if (!empty && ((base1Ump != null && base1Ump.equals(item)) || (base2Ump != null && base2Ump.equals(item)) || (plateUmp != null && plateUmp.equals(item)))) {
                            setStyle("-fx-background-color: '" + colorCellError + "'");
                        } else {
                            setStyle("-fx-background-color: '" + colorCellFilled + "'");
                        }
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
                        gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), null, "", gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));
                    });
                    MenuItem wisRij = new MenuItem("Wis wedstrijd");
                    cm.getItems().add(wisRij);
                    wisRij.setOnAction(wisrij -> {
                        int newIndex = gameData.indexOf(secondFilter.get(cell.getIndex()));
                        gameData.remove(newIndex);
                        //gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, null, seizoen, null));
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
                    cell.setStyle("-fx-background-color: '" + colorCellNone + "'");
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

                        Umpire umpire = database.getUmpireFromDatabase(db.getString());
                        String w = String.format("%02d", week);
                        String g = String.format("%02d", rowIndex+1);
                        String gi = afdeling+seizoen+w+g+getRandomString();   
                        if(rowIndex < 0 || rowIndex >= secondFilter.size()) {

                            int sprong = rowIndex - secondFilter.size();
                            for(int i=0; i<sprong; i++) {
                                String ga = String.format("%02d", rowIndex-sprong+i+1);
                                String gix = afdeling+seizoen+w+ga+getRandomString();
                                gameData.add(new Game(gix, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, null, "", seizoen, null));                            
                            }
                            gameData.add(new Game(gi, afdeling, Integer.toString(week), getDate(week, seizoen, 6), defaultGamehour, null, null, null, null, null, umpire, "", seizoen, null));
                        } else {

                            int newIndex = gameData.indexOf(secondFilter.get(rowIndex));
                            gameData.set(newIndex, new Game(gameData.get(newIndex).getGameindex(), afdeling, Integer.toString(week), gameData.get(newIndex).getGameDatum(), gameData.get(newIndex).getGameUur(), gameData.get(newIndex).getHomeTeam(), gameData.get(newIndex).getVisitingTeam(), gameData.get(newIndex).getPlateUmpire(), gameData.get(newIndex).getBase1Umpire(), gameData.get(newIndex).getBase2Umpire(), umpire, gameData.get(newIndex).getGameNumber(), gameData.get(newIndex).getSeizoen(), gameData.get(newIndex).getHomeClub()));

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
        
        TableColumn<String, String> totalDistCol = new TableColumn("km");
        totalDistCol.prefWidthProperty().bind(table.widthProperty().divide(9));
        
        totalDistCol.setCellValueFactory((p) -> {
            String totalDistance = "";
            int i = table.getItems().indexOf(p.getValue());
            //String ind = gameindexCol.getCellData(i);
            String ind = gamenumberCol.getCellData(i);
            Club c = database.getClubFromGameIndex(ind);
            Umpire u = database.getUmpireFromGameSchedule(ind, "plateumpire");
            Double distp = Double.parseDouble(database.getDistFromUmpireClub(u.getUmpireLicentie(), c.getClubNummer()));
            Umpire b1 = database.getUmpireFromGameSchedule(ind, "base1umpire");
            Double distb1 = Double.parseDouble(database.getDistFromUmpireClub(b1.getUmpireLicentie(), c.getClubNummer()));
            Umpire b2 = database.getUmpireFromGameSchedule(ind, "base2umpire");
            Double distb2 = Double.parseDouble(database.getDistFromUmpireClub(b2.getUmpireLicentie(), c.getClubNummer()));
            Umpire b3 = database.getUmpireFromGameSchedule(ind, "base3umpire");
            Double distb3 = Double.parseDouble(database.getDistFromUmpireClub(b3.getUmpireLicentie(), c.getClubNummer()));
            totalDistance = Double.toString(round(distp*2 + distb1*2 + distb2*2 + distb3*2, 3));
            return new ReadOnlyStringWrapper(totalDistance);
        });
        
        
        
        table.setItems(secondFilter);
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(2.50)));
        //table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
        table.setMinHeight(170);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        //table.prefHeight(100 + 45);
        //table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(45));

        table.getColumns().addAll(gamenumberCol, gamedatumCol, gameTimeCol, homeTeamCol, visitingTeamCol, homeClubCol, plateUmpireCol, base1UmpireCol, base2UmpireCol, base3UmpireCol, totalDistCol);
        table.getSelectionModel().setCellSelectionEnabled(true);

        calendarbox.setPadding(new Insets(0, 0, 0, 0));
        calendarbox.getChildren().addAll(label, table);

        
    return calendarbox;

           
    }
    
    /** Check if umpire has already a game this date
     * 
     * @param umpirelicentie String umpire
     * @param datum String datum
     * @return Boolean value
     */
    private Boolean umpireDouble(String umpirelicentie, String datum) {
        Boolean bool = Boolean.FALSE;
        
        // Check if umpire is already present this week
        FilteredList<Game> secondFilter = gameData.filtered(g -> g.getGameDatum().equals(datum));
        for (Game gameToday : secondFilter) {
            ArrayList<String> umpiresForThisDate = new ArrayList<>();
            umpiresForThisDate.add(gameToday.getPlateUmpire().getUmpireLicentie());
            umpiresForThisDate.add(gameToday.getBase1Umpire().getUmpireLicentie());
            umpiresForThisDate.add(gameToday.getBase2Umpire().getUmpireLicentie());
            umpiresForThisDate.add(gameToday.getBase3Umpire().getUmpireLicentie());
            if (umpiresForThisDate.contains(umpirelicentie)) {
                bool = Boolean.TRUE;
            } else {
                bool = Boolean.FALSE;
            }
        }
        
        return bool;
    }

    private static double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

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

    private String getRandomString() {
        String s = "";
        Random rand = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz"; 
        String x = "";
        int n = rand.nextInt(1000);
        for (int i = 0; i < 3; i++) {
            x += alphabet.charAt(rand.nextInt(alphabet.length()));
        }
        s = x + n;
        return s;
    }
    
}
