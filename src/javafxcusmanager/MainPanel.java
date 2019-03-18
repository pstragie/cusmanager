/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

/**
 *
 * @author pieter
 */
public class MainPanel {
    
    private TextField topPaneTextField, bottomPaneTextField;
    private static MenuBar mainMenu;
    private Pane umpirepanel;
    public String jaartal = "2016";
    //public ObservableList<String> observableTabList;
    public ObservableList<Afdeling> afdelingen;
    public ObservableList<Club> clubs;
    public ObservableList<Team> teams;
    public ObservableList<Umpire> umpires;
    public ObservableList<Game> gameData;
    private DocumentHandling documentHandler;
    private Pane leftPane = new Pane();
    private Pane rightPane = new Pane();
    public TabPane leftTabPane = new TabPane();
    public TabPane rightTabPane = new TabPane();
    private Pane centerPane = new Pane();
    public TabPane centerTabPane = new TabPane();
    private Afdelingen changeAfdelingenpane;
    private AppSettings changeSettingspane;
    private ClubView clubview;
    private UmpireView umpireview;
    private UmpireModel umpiremodel;
    private ClubModel clubmodel;
    private GameSchedule gameSchedule;
    private Database database = new Database();
    public Button resetButton;
    private TextField clubfilterField;
    private Preferences pref;
    
    /** MainPanel
     * 
     * @return Paneel
     */
    public Pane MainPanel() {    
        pref = Preferences.userNodeForPackage(AppSettings.class);
        jaartal = pref.get("Seizoen", Integer.toString(LocalDate.now().getYear()));
        afdelingen = FXCollections.observableArrayList();
        // Get afdelingen from database
        afdelingen.addAll(database.getAllAfdelingenFromDatabase());
        afdelingen.addListener((ListChangeListener.Change<? extends Afdeling> change) -> { 
            while(change.next()) {
                if(change.wasUpdated()) {
                    System.out.println("Update detected");

                } else
                    if (change.wasPermutated()) {
                        System.out.println("Was permutated");
                    }
                        else 
                            for (Afdeling remitem: change.getRemoved()) {
                                System.out.println("remitem");

                                leftTabPane.getTabs().removeIf(tab -> tab.getText().equals(remitem.getAfdelingsNaam()));
                                rightTabPane.getTabs().removeIf(tab -> tab.getText().equals(remitem.getAfdelingsNaam()));
                                centerTabPane.getTabs().removeIf(tab -> tab.getText().equals(remitem.getAfdelingsNaam()));

                                // TO DO: Delete from Database
                                database.deleteAfdelingFromDatabase(remitem.getAfdelingsNaam());

                            }
                            for (Afdeling additem : change.getAddedSubList()) {
                                System.out.println("additem");

                                leftTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                rightTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                centerTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                leftTabPane.getTabs().addAll(getClubTabArrayList());
                                rightTabPane.getTabs().addAll(getUmpireTabArrayList());
                                centerTabPane.getTabs().addAll(getGameTabArrayList());
                                // TO DO: Store in database
                                database.insertNewAfdelingToDatabase(additem.getAfdelingsNaam(), additem.getAfdelingsCategorie(), Boolean.TRUE);
                            }
            }
        });
        
        
        
        // Club and teams
        clubs = FXCollections.observableArrayList();
        clubs.addAll(database.getAllClubsFromDatabase());
        clubs.addListener((ListChangeListener.Change<? extends Club> change) -> { 
            while(change.next()) {
                if(change.wasUpdated()) {
                            System.out.println("Update detected");
                            // Write to file

                } else
                    if (change.wasPermutated()) {
                                System.out.println("Was permutated");
                            } else {
                                if (change.wasAdded()) {
                            System.out.println("Data " + change + " was added to clubs");
                                    // Write to database

                        }
                         }

            }
        });
        umpires = FXCollections.observableArrayList();
        umpires.addAll(database.getAllUmpiresFromDatabase());
        umpires.addListener((ListChangeListener.Change<? extends Umpire> change) -> { 
            while(change.next()) {
                if(change.wasUpdated()) {
                    System.out.println("Umpires Update detected");
                    // Write to file?

                } else
                    if (change.wasPermutated()) {
                        System.out.println("Umpires Was permutated");
                    } 
                        else 
                            for (Umpire additem: change.getAddedSubList()) {
                            System.out.println("Data " + change + " was added to umpires");
                            // Write to database: Done when addButton is pressed
                            Comparator<Umpire> umpireComparator = Comparator.comparing(Umpire::getUmpireNaam);
                            umpiremodel.umpirelijstPerafdeling.sort(umpireComparator);
                        }
                        for (Umpire remitem: change.getRemoved()) {
                            System.out.println("Umpire was removed: " + remitem);
                            // Remove from database
                         }

            }
        });
        teams = FXCollections.observableArrayList();
        teams.addListener((ListChangeListener.Change<? extends Team> change) -> { 
                while(change.next()) {
                    if(change.wasUpdated()) {
                        System.out.println("Update teams detected");
                        // Write to file

                    } else
                        if (change.wasPermutated()) {
                            System.out.println("Was permutated team");
                        } else {
                            if (change.wasAdded()) {
                                System.out.println("Data was added to teams");
                                // Write to file
                                //clubList.refresh();
                                // Save to database

                                //GameSchedule.write(gameData, /home/pieter/wedstrijdschema.txt);
                            }
                        }

            }
        });
            
        // Games
        gameData = FXCollections.observableArrayList();
        gameData.addAll(database.getAllGamesFromDatabase());
        System.out.println("gamedata = " + gameData);
        gameData.addListener((ListChangeListener.Change<? extends Game> change) -> { 
            while(change.next()) {
                if(change.wasUpdated()) {
                    System.out.println("Umpires Update detected");
                    // Write to file?

                } else
                    if (change.wasPermutated()) {
                        System.out.println("Umpires Was permutated");
                    } 
                        else 
                            for (Game remitem: change.getRemoved()) {
                                System.out.println("Game was removed: " + remitem);
                                // Remove from database
                                database.deleteGameFromDatabase(remitem.getGameindex());
                             }
                
                            for (Game additem: change.getAddedSubList()) {
                            System.out.println("Data " + change + " was added to games");
                            try {
                                // Write to database
                                if (database.checkIfGameExists(additem.getGameindex())) {
                                    System.out.println("Game bestaat al!!! Update existing game: " + additem.getGameindex());
                                    database.updateGameToDatabase(Integer.parseInt(additem.getWeekString()), additem.getAfdelingString(), localDateToString(additem.getGameDatum()), localTimeToString(additem.getGameUur()), additem.getHomeTeamName(), additem.getVisitingTeamName(), additem.getPlateUmpireName(), additem.getBase1UmpireName(), additem.getBase2UmpireName(), additem.getBase3UmpireName(), additem.getGameNumber(), additem.getGameindex(), additem.getSeizoen());
                                } else {
                                    database.insertNewGameToDatabase(Integer.parseInt(additem.getWeekString()), additem.getAfdelingString(), localDateToString(additem.getGameDatum()), localTimeToString(additem.getGameUur()), additem.getHomeTeamName(), additem.getVisitingTeamName(), additem.getPlateUmpireName(), additem.getBase1UmpireName(), additem.getBase2UmpireName(), additem.getBase3UmpireName(), additem.getGameNumber(), additem.getGameindex(), additem.getSeizoen());
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        

            }
        });
        
        // Add Test data
        
        ArrayList<Afdeling> afdArray = new ArrayList<>();
        afdArray.add(new Afdeling("Gold", "Baseball"));
        afdArray.add(new Afdeling("1BB", "Baseball"));
        
        
        documentHandler = new DocumentHandling();
        
        BorderPane borderPane = new BorderPane();
        
        
        
        // Top and Bottom Pane
        topPaneTextField = new TextField( "Top Pane" );
        bottomPaneTextField = new TextField( "Copyright Pieter Stragier" );
        bottomPaneTextField.setBackground(Background.EMPTY);
        bottomPaneTextField.setDisable(true);
        
        // Menu
        mainMenu = new MenuBar();
        mainMenu = createMainMenu();        
        borderPane.setTop(mainMenu);
        
        // Left Pane -> Clubs
        leftPane = new Pane();
        leftPane = createLeftSidePane(leftTabPane);
        borderPane.setMargin(leftPane, new Insets(0, 0, 0, 10));
        borderPane.setLeft(leftPane);
        
        // Right Pane -> Umpires
        rightPane = new Pane();
        rightPane = createRightSidePane(rightTabPane);
        borderPane.setRight(rightPane);
        borderPane.setMargin(rightPane, new Insets(0, 10, 0, 0));
        borderPane.setBottom(bottomPaneTextField);
        
        // Center Pane -> Game schedule
        centerPane = new Pane();
        centerPane = createCenterPane(centerTabPane);
        borderPane.setMargin(centerPane, new Insets(0, 10, 0, 10));
        borderPane.setCenter(centerPane);
        return borderPane;
    }
    
    public ArrayList<String> getAfdelingsnamenlijst() { 
        ArrayList<String> afdelingStrings = new ArrayList<>();
        afdelingen.forEach(afd -> afdelingStrings.add(afd.getAfdelingsNaam()));
    
        //observableTabList.addAll(tabs);
        return afdelingStrings;
    }
    
    public MenuBar createMainMenu() {
        // Create a Menu
        MenuBar menubar = new MenuBar();

        // Menu Umpires
        Menu menuUmpires = new Menu("Umpires");
        menubar.getMenus().add(menuUmpires);
        MenuItem umpiresBeheren = new MenuItem("Umpires beheren...");
        umpiresBeheren.setOnAction(e -> {
              
            System.out.println("umpiresBeheren Clicked");
            Stage stage = new Stage();
            Scene scene = new Scene(UmpirePaneel(), 1000, 800);
            //stage.initStyle(StageStyle.UNIFIED);
            //stage.setAlwaysOnTop(true);
            stage.setTitle("Umpires beheren");
            stage.setScene(scene);
            stage.show();
            
        });
        
        menuUmpires.getItems().add(umpiresBeheren);
        MenuItem umpiresImporteren = new MenuItem("Umpires importeren");
        menuUmpires.getItems().add(umpiresImporteren);
        umpiresImporteren.setOnAction(e -> {
            Stage stage = new Stage();
            String absPath = getFilePath(stage, "Umpires");
            if (absPath != "") {
                // Wis de huidige afdelingen
                database.deleteAllUmpiresInDatabase();
                // Import afdelingen in de database
                ArrayList<Umpire> arrayUmpires = documentHandler.getUmpiresFromFile(absPath);
                for(Umpire ump : arrayUmpires) {
                    //System.out.println("Umpire afdeling: " + ump.getAfdelingsNaam());
                    // Write afdelingen Array as String
                    String s = "";
                    for (Afdeling a : ump.getUmpireAfdelingen()) {
                        s += a.getAfdelingsNaam() + ":" + a.getAfdelingsCategorie() + ",";
                    }
                    System.out.print("String s = " + s);
                    database.insertNewUmpireToDatabase(ump.getUmpireNaam(), ump.getUmpireVoornaam(), ump.getUmpireLicentie(), ump.getUmpireStraat(), ump.getUmpireHuisnummer(), ump.getUmpirePostcode(), ump.getUmpireStad(), ump.getUmpireTelefoon(), ump.getUmpireEmail(), ump.getUmpireClub().toString(), s, ump.getActief());
                }
                umpires.clear();
                umpires.addAll(database.getAllUmpiresFromDatabase());
            }
        });
        Button button = new Button("Exporteren (backup)");
        CustomMenuItem customMenuItem = new CustomMenuItem();
        customMenuItem.setContent(button);
        customMenuItem.setHideOnClick(true);
        menuUmpires.getItems().add(customMenuItem);
        // Menu Clubs
        Menu clubsAndTeams = new Menu("Clubs & Teams");
        menubar.getMenus().add(clubsAndTeams);        
        MenuItem clubsBeheren = new MenuItem("Clubs & Teams beheren...");
        clubsAndTeams.getItems().add(clubsBeheren);

        clubsBeheren.setOnAction(e -> {
            
            System.out.println("menuClubItem1 Clicked");
            Stage stage = new Stage();
            Scene scene = new Scene(ClubPaneel(), 800, 600);
            stage.setTitle("Clubs beheren");
            //stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
            
        });
        MenuItem clubsImporteren = new MenuItem("Clubs & Teams importeren...");
        clubsAndTeams.getItems().add(clubsImporteren);
        clubsImporteren.setOnAction(e -> {
            Stage stage = new Stage();
            String absPath = getFilePath(stage, "Clubs");
            if (absPath != "") {
                // Wis de huidige clubs
                database.deleteAllClubsInDatabase();
                database.deleteAllTeamsInDatabase();
                // Import clubs in de database
                ArrayList<Club> arrayClubs = documentHandler.getClubsFromFile(absPath);
                for(Club cl : arrayClubs) {
                    //System.out.println(cl);
                    database.insertNewClubToDatabase(cl.getClubNaam(), cl.getVoorzitter(), cl.getClubNummer(), cl.getClubStraat(), cl.getClubStraatNummer(), cl.getClubPostcode(), cl.getClubStad(), cl.getClubEmail(), cl.getClubTelefoon(), cl.getLiga(), cl.getClubWebsite(), cl.getVisible());
                    for (Team t: cl.getClubTeams()) {
                        database.insertTeamsInDatabase(t.getTeamNaam(), t.getTeamAfdeling().getAfdelingsNaam(), cl.getClubNaam(), t.getTeamAfdeling().getAfdelingsCategorie());
                    }
                }
                clubs.clear();
                clubs.addAll(database.getAllClubsFromDatabase());
            }
        });
        
        // Menu Afdelingen
        Menu menuAfdelingen = new Menu("Afdelingen");
        menubar.getMenus().add(menuAfdelingen);
        MenuItem afdelingenBeheren = new MenuItem("Afdelingen beheren...");
        menuAfdelingen.getItems().add(afdelingenBeheren);
        MenuItem afdelingenImporteren = new MenuItem("Afdelingen importeren...");
        menuAfdelingen.getItems().add(afdelingenImporteren);
        
        afdelingenBeheren.setOnAction(e -> { 
            Stage stage = new Stage();
            Scene scene = new Scene(newAfdelingPaneel(leftTabPane, rightTabPane, centerTabPane, afdelingen));
            stage.setTitle("Afdelingen wijzigen");
            //stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        });

        // Afdelingen importeren menu item action
        afdelingenImporteren.setOnAction(e -> {
            Stage stage = new Stage();
            String absPath = getFilePath(stage, "Afdelingen");
            if (absPath != "") {
                // Make backup to rewind import
                ArrayList<Afdeling> af = new ArrayList<>(afdelingen);
                documentHandler.storeAfdelingen(af, Boolean.TRUE);
                // Import afdelingen in de database
                ArrayList<Afdeling> arrayAfdelingen = documentHandler.getAfdelingenFromFile(absPath);
                System.out.println("Insert afdelingen to Database...");
                /*
                for(Afdeling afd : arrayAfdelingen) {
                    database.insertNewAfdelingToDatabase(afd.getAfdelingsNaam(), afd.getAfdelingsCategorie(), Boolean.TRUE);
                }
                */
                System.out.println("Afdelingen lijst wissen...");
                afdelingen.clear();
                System.out.println("Afdelingen toevoegen aan afdelingenlijst...");
                afdelingen.addAll(arrayAfdelingen);
            }
        });
        MenuItem rewindImportAfdelingen = new MenuItem("Import terugdraaien");
        menuAfdelingen.getItems().add(rewindImportAfdelingen);
        File tmpDir = new File("afdelingen_backup.txt");
        boolean exists = tmpDir.exists();
        rewindImportAfdelingen.setVisible(exists);
  
        rewindImportAfdelingen.setOnAction(rewind -> {
            // Import afdelingen in de database
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import van afdelingen terugdraaien.");
            try {
                BasicFileAttributes attr = Files.readAttributes(tmpDir.toPath(), BasicFileAttributes.class);
                final String headerText = "creationTime: " + attr.lastModifiedTime();
                alert.setHeaderText("Backup bestand laatst gewijzigd op: " + headerText);
            } catch (IOException e) {
                System.err.println("Error obtaining file creationTime: " + e);
                alert.setHeaderText("Datum van backup bestand ongekend.");
            }
            
            alert.setContentText("Doorgaan?");
            alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            ArrayList<Afdeling> arrayAfdelingen = documentHandler.getAfdelingenFromFile("afdelingen_backup.txt");
            afdelingen.clear();
            afdelingen.addAll(arrayAfdelingen);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
                
        });
        // Custom menu exporteren
        Button expAfdButton = new Button("Exporteren (backup)");
        CustomMenuItem customExportAfdeling = new CustomMenuItem();
        customExportAfdeling.setContent(expAfdButton);
        customExportAfdeling.setHideOnClick(true);
        menuAfdelingen.getItems().add(customExportAfdeling);
        expAfdButton.setOnAction(clicked -> {
            ArrayList<Afdeling> af = new ArrayList<>(afdelingen);
            documentHandler.storeAfdelingen(af, Boolean.FALSE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Afdelingen exporteren.");
            alert.setHeaderText("Exporteren klaar.");
            alert.setContentText("OK om verder te gaan.");
            alert.showAndWait();
        });
        
        // Games menu
        Menu menuGames = new Menu("Wedstrijden");
        menubar.getMenus().add(menuGames);
        MenuItem gamesImporteren = new MenuItem("Wedstrijden importeren...");
        menuGames.getItems().add(gamesImporteren);
        // Afdelingen importeren menu item action
        gamesImporteren.setOnAction(e -> {
            Stage stage = new Stage();
            String absPath = getFilePath(stage, "Games");
            if (absPath != "") {
                // Make backup to rewind import
                ArrayList<Game> gam = new ArrayList<>(gameData);
                documentHandler.storeGameSchedule(gam, Boolean.TRUE);
                // Import games in de database
                ArrayList<Game> arrayGames = documentHandler.getGamesFromFile(absPath);
                System.out.println("Insert games to Database...");
                /* ObservableList Games changed -> handler will store in the database
                for(Afdeling afd : arrayAfdelingen) {
                    database.insertNewAfdelingToDatabase(afd.getAfdelingsNaam(), afd.getAfdelingsCategorie(), Boolean.TRUE);
                }
                */
                System.out.println("Games lijst wissen...");
                gameData.clear();
                database.deleteAllGamesInDatabase();
                System.out.println("Game toevoegen aan gamelijst...");
                gameData.addAll(arrayGames);
                // Store in database
                arrayGames.forEach((g) -> {
                    System.out.println("datum en tijd: " + g.getGameDatum() + " en " + g.getGameUur());
                    String date = localDateToString(g.getGameDatum());
                    System.out.println("Date string = " + date);
                    String time = localTimeToString(g.getGameUur());
                    System.out.println("Time string = " + time);
                    database.insertNewGameToDatabase(Integer.parseInt(g.getWeekString()), g.getAfdelingString(), date, time, g.getHomeTeamName(), g.getVisitingTeamName(), g.getPlateUmpireName(), g.getBase1UmpireName(), g.getBase2UmpireName(), g.getBase3UmpireName(), g.getGameNumber(), g.getGameindex(), g.getSeizoen());
                });
            }
        });
        
        MenuItem rewindImportGames = new MenuItem("Import terugdraaien");
        menuGames.getItems().add(rewindImportGames);
        File gtmpDir = new File("games_backup.txt");
        boolean gexists = gtmpDir.exists();
        rewindImportGames.setVisible(gexists);
  
        rewindImportGames.setOnAction(rewind -> {
            // Import afdelingen in de database
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import van wedstrijden terugdraaien.");
            try {
                BasicFileAttributes attr = Files.readAttributes(tmpDir.toPath(), BasicFileAttributes.class);
                final String headerText = "creationTime: " + attr.lastModifiedTime();
                alert.setHeaderText("Backup bestand laatst gewijzigd op: " + headerText);
            } catch (IOException e) {
                System.err.println("Error obtaining file creationTime: " + e);
                alert.setHeaderText("Datum van backup bestand ongekend.");
            }
            
            alert.setContentText("Doorgaan?");
            alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            ArrayList<Game> arrayGames = documentHandler.getGamesFromFile("games_backup.txt");
            gameData.clear();
            gameData.addAll(arrayGames);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        });
        
        // Custom menu exporteren
        Button expGameButton = new Button("Exporteren (backup)");
        CustomMenuItem customExportGames = new CustomMenuItem();
        customExportGames.setContent(expGameButton);
        customExportGames.setHideOnClick(true);
        menuGames.getItems().add(customExportGames);
        expGameButton.setOnAction(clicked -> {
            ArrayList<Game> gam = new ArrayList<>(gameData);
            documentHandler.storeGameSchedule(gam, Boolean.FALSE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wedstrijdschema exporteren.");
            alert.setHeaderText("Exporteren klaar.");
            alert.setContentText("OK om verder te gaan.");
            alert.showAndWait();
        });
        // Menu Settings
        Menu menuSettings = new Menu("Settings");
        menubar.getMenus().add(menuSettings);
        MenuItem settingsWijzigen = new MenuItem("Aanpassen");
        menuSettings.getItems().add(settingsWijzigen);
        settingsWijzigen.setOnAction(setting -> {
            Stage stage = new Stage();
            Scene scene = new Scene(newSettingsPane());
            stage.setTitle("Settings");
            //stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        
        });
        return menubar;
    }
    
    /** Get FilePath
     * 
     * @param stage
     * @param naam
     * @return 
     */
    public String getFilePath(Stage stage, String naam) {
        
        
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Bestand selecteren: " + naam);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                String x = selectedFile.getAbsolutePath();
                return x;
            } else {
                String x = "";
                return x;
            }
      
        
        
    }
    
    public VBox createLeftSidePane(TabPane sideTabPane) {
        // Create a VBox with HBox for filter, and TabPane
        VBox vbox = new VBox();
            // Create HBox with textfield and button
            VBox verBox = createHorBoxFilterClubs(sideTabPane);
        
        vbox.getChildren().add(verBox);
        
            // Create TabPane
            sideTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            sideTabPane.getTabs().addAll(getClubTabArrayList());
            //tabpane.getStylesheets().add(getClass().getResource("css/TabPaneStyles.css").toExternalForm());
            Text placeHolder = new Text( " Geen afdelingen gevonden." );
                    placeHolder.setFont( Font.font( null, FontWeight.BOLD, 14 ) );
                    BooleanBinding bb = Bindings.isEmpty( sideTabPane.getTabs() );
                    placeHolder.visibleProperty().bind( bb );
                    placeHolder.managedProperty().bind( bb );
        vbox.getChildren().add(placeHolder); // Show placeholder when no tabs
        vbox.getChildren().add(sideTabPane); // Show tabs if present
        vbox.setPrefSize(300, 800);
        
        return vbox;
    }
    
    public VBox createRightSidePane(TabPane sideTabPane) {
        // Create a VBox with HBox for filter, and TabPane
        VBox vbox = new VBox();
            // Create HBox with textfield and button
            VBox horBox = createHorBoxFilterUmpires(sideTabPane);
            
        vbox.getChildren().add(horBox);
        
            // Create TabPane
            sideTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            
            sideTabPane.getTabs().addAll(getUmpireTabArrayList());
            //tabpane.getStylesheets().add(getClass().getResource("css/TabPaneStyles.css").toExternalForm());
            Text placeHolder = new Text( " Geen afdelingen gevonden." );
                    placeHolder.setFont( Font.font( null, FontWeight.BOLD, 14 ) );
                    BooleanBinding bb = Bindings.isEmpty( sideTabPane.getTabs() );
                    placeHolder.visibleProperty().bind( bb );
                    placeHolder.managedProperty().bind( bb );
        vbox.getChildren().add(placeHolder); // Show placeholder when no tabs
        vbox.getChildren().add(sideTabPane); // Show tabs if present
        vbox.setPrefSize(300, 800);
        
        return vbox;
    }
    
    public VBox createCenterPane(TabPane middleTabPane) {
        VBox vbox = new VBox();
            VBox horBox = createHorBoxFilterGames(middleTabPane);    
            vbox.getChildren().add(horBox);
        vbox.setBorder(new Border(new BorderStroke(Color.DARKSLATEBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3, 3, 3, 3))));
        
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        //Label gameScheduleLabel = new Label("Game Schedule");
        //gameScheduleLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        //gameScheduleLabel.setAlignment(Pos.CENTER);
        //hbox.getChildren().add(gameScheduleLabel);
        //vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox);
        middleTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        middleTabPane.getTabs().addAll(getGameTabArrayList());
            
        vbox.getChildren().add(middleTabPane);
        vbox.setPadding(new Insets(0, 5, 0, 5));
        return vbox;
    }
    
    /** Reset right side tabPane
     * 
     * @param sideTabPane 
     */
    public void resetRightTabpaneSide(TabPane sideTabPane) {
        // Reset the tabpane to show all tabs
        ObservableList<Afdeling> tablist = getTabsList();
        tablist.forEach(tab1 -> {
            sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1.getAfdelingsNaam()));
        });
        sideTabPane.getTabs().addAll(getUmpireTabArrayList());
    }
    
    public void resetLeftTabpaneSide(TabPane sideTabPane) {
        // Reset the tabpane to show all tabs
        ObservableList<Afdeling> tablist = getTabsList();
        tablist.forEach(tab1 -> {
            sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1.getAfdelingsNaam()));
        });
        sideTabPane.getTabs().addAll(getClubTabArrayList());
    }
    
    public VBox createHorBoxFilterUmpires(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        VBox vbox = new VBox();
        Label umpireLabel = new Label("Umpires");
        umpireLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        umpireLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(umpireLabel);
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                System.out.println("Text changed from "+oldText+" to "+newText);
                
                if (newText == null || newText.isEmpty()) {
                    //System.out.println("current observableTabList: " + afdelingen);
                    //System.out.println("Nothing to filter: " + afdelingen);
                    // Reset the tabpane to show all tabs
                    sideTabPane.getTabs().clear();
                    
                    sideTabPane.getTabs().addAll(getUmpireTabArrayList());
                } else {
                    //System.out.println("Filter active: " + newText);
                    //System.out.println("Filtered List = " + afdelingen.filtered(tab -> tab.getAfdelingsNaam().contains(newText)));
                    
                    sideTabPane.getTabs().clear();
                    sideTabPane.getTabs().addAll(getUmpireTabArrayList());
                    sideTabPane.getTabs().removeIf(tab -> !tab.getText().contains(newText));
                                        
                }
            });
            
            resetButton = new Button();
            resetButton.setText("Reset");
            resetButton.setOnAction(event -> {
                sideTabPane.getTabs().clear();
                sideTabPane.getTabs().addAll(getUmpireTabArrayList());
                filterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(filterField, Priority.ALWAYS);
            hbox.getChildren().add(filterField);
            hbox.getChildren().add(resetButton);
            vbox.getChildren().add(hbox);
        return vbox;
    }
    
    /** Reset side tabPanes
     * 
     */
    public void resetSideTabPanes() {
        leftTabPane.getTabs().clear();
        leftTabPane.getTabs().addAll(getUmpireTabArrayList());
        rightTabPane.getTabs().clear();
        rightTabPane.getTabs().addAll(getClubTabArrayList());
    }
    
    /** Creates a VBox with textfield and button for filtering tabpanes
     * 
     * @param sideTabPane
     * @return 
     */
    public VBox createHorBoxFilterClubs(TabPane sideTabPane) {
        VBox vbox = new VBox();
        Label clubLabel = new Label("Teams");
        clubLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(clubLabel);
        
        HBox hbox = new HBox();
            clubfilterField = new TextField();
            clubfilterField.setPromptText("Filter tabs");
            clubfilterField.textProperty().addListener((obs, oldText, newText) -> {
                
                if (newText == null || newText.isEmpty()) {
                    // Reset the tabpane to show all tabs
                    sideTabPane.getTabs().clear();
                    sideTabPane.getTabs().addAll(getClubTabArrayList());
                } else {
                                      
                    sideTabPane.getTabs().clear();
                    sideTabPane.getTabs().addAll(getClubTabArrayList());
                    sideTabPane.getTabs().removeIf(tab -> !tab.getText().contains(newText));
                    //sideTabPane.getTabs().filtered(tab -> tab.getText().contains(newText));
                    
                }
            });
            
            Button filterButton = new Button();
            filterButton.setText("Reset");
            filterButton.setOnAction(event -> {
                sideTabPane.getTabs().clear();
                sideTabPane.getTabs().addAll(getClubTabArrayList());
                clubfilterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(clubfilterField, Priority.ALWAYS);
            hbox.getChildren().add(clubfilterField);
            hbox.getChildren().add(filterButton);
            vbox.getChildren().add(hbox);
        return vbox;
    }
    
    /** Maak een VBox om tabbladen te filteren
     * 
     * @param centerTabPane
     * @return VBox
     */
    public VBox createHorBoxFilterGames(TabPane centerTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        VBox vbox = new VBox();
        String seizoensstring = pref.get("Seizoen", Integer.toString(LocalDate.now().getYear()));
        Label gameLabel = new Label("Wedstrijdschema " + seizoensstring);
        gameLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        gameLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gameLabel);
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                clubfilterField.setText(newText);
                if (newText == null || newText.isEmpty()) {
                    // Reset the tabpane to show all tabs
                    centerTabPane.getTabs().clear();
                    centerTabPane.getTabs().addAll(getGameTabArrayList());
                    
                } else {                    
                    centerTabPane.getTabs().clear();
                    centerTabPane.getTabs().addAll(getGameTabArrayList());
                    
                    centerTabPane.getTabs().removeIf(tab -> !tab.getText().contains(newText));
                                        
                }
            });
            
            resetButton = new Button();
            resetButton.setText("Reset");
            resetButton.setOnAction(event -> {
                centerTabPane.getTabs().clear();
                centerTabPane.getTabs().addAll(getGameTabArrayList());
            
                filterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(filterField, Priority.ALWAYS);
            hbox.getChildren().add(filterField);
            hbox.getChildren().add(resetButton);
            vbox.getChildren().add(hbox);
        return vbox;
    }
    
    public ObservableList<Afdeling> getTabsList() {
        return afdelingen;
    }
    
     public void setTabs(ObservableList<Afdeling> tabs) {
        this.afdelingen = tabs;
    }
    
     
    public ArrayList<Tab> getClubTabArrayList() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        clubmodel = new ClubModel(clubs, teams);
        System.out.println("Get Tabs from file and create club content\n________________");
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(getAfdelingsnamenlijst());
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(clubmodel.createClubContent(a)); // Set filtered content
            tabs.add(tab);
            
        });        
        return tabs;
    }
    
    public ArrayList<Tab> getUmpireTabArrayList() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        umpiremodel = new UmpireModel(umpires);
        System.out.println("Get Tabs from file and create umpire content\n________________");
        ArrayList<Afdeling> listOfItems = new ArrayList<>();
        listOfItems.addAll(database.getAllAfdelingenFromDatabase());
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a.getAfdelingsNaam());
            tab.setContent(umpiremodel.createUmpireContent(a)); // Set filtered content
            tabs.add(tab);
            
        });        
        return tabs;
    }
    
    public ArrayList<Tab> getGameTabArrayList() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        gameSchedule = new GameSchedule(gameData, teams, afdelingen, clubs, umpires, jaartal);
        System.out.println("Get Tabs from file and create club content\n________________");
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(getAfdelingsnamenlijst());
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(gameSchedule.createCalendar(a)); // Set filtered content
            tabs.add(tab);
            tab.setOnSelectionChanged(tabevent -> {
                // Automatically filter team afdelingen based on selected tab in gameData schedule.
                clubfilterField.setText(a);
            });
            
        });        
        return tabs;
    }
     
    
    /*
    public ArrayList<Afdeling> createListOfItems(String filename) {
        //ArrayList<String> arraylist = new ArrayList<>();
        documentHandler = new DocumentHandling();
        ArrayList<Afdeling> arraylist = documentHandler.getAfdelingenFromFile();
        return arraylist;
    }
    */
    public Pane newAfdelingPaneel(TabPane tabpaneleft, TabPane tabpaneright, TabPane tabpanecenter, ObservableList afdelingenlijst) {
        BorderPane border = new BorderPane();
        
            changeAfdelingenpane = new Afdelingen(afdelingenlijst);
            border.setCenter(changeAfdelingenpane.afdelingenPanel());
        
        return border;
    }
    
    private Pane newSettingsPane() {
        Pane pane = new Pane();
        changeSettingspane = new AppSettings();
        pane.getChildren().add(changeSettingspane.settingsPane());
        pane.setPadding(new Insets(10, 10, 10, 10));
        return pane;
    }
    /** Open nieuw venster om clubs te beheren
     * 
     * @return Paneel
     */
    public Pane ClubPaneel() {
        
            clubview = new ClubView(clubs, teams, afdelingen);
            return clubview.clubPane();
    }
    
    /** Open nieuw venster om umpires te beheren
     * 
     * @return Paneel
     */
    public Pane UmpirePaneel() {
            umpireview = new UmpireView(umpires, clubs, teams, afdelingen);
            return umpireview.umpirePane();
    }
    
    /** Afdeling toevoegen
     * 
     */
    public void addAfdeling() {
        try {
            // Check if afdeling exists before adding to database --> Error will occur
            //database.insertNewAfdelingToDatabase("8BB", "baseball", Boolean.TRUE);
            for (Afdeling afd : afdelingen) {
                if(database.checkIfAfdelingExists("APP.Afdelingen", afd.getAfdelingsNaam())) {
                    System.out.println("exists: " + afd.getAfdelingsNaam());
                } else {
                    System.out.println("does not exist: " + afd.getAfdelingsNaam());
                    database.insertNewAfdelingToDatabase(afd.getAfdelingsNaam().toString(), afd.getAfdelingsCategorie().toString(), Boolean.TRUE);
                    
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Club toevoegen
     * 
     */
    public void addClub() {
        try {
            // Check if afdeling exists before adding to database --> Error will occur
            //database.insertNewAfdelingToDatabase("8BB", "baseball", Boolean.TRUE);
            for (Club cl : clubs) {
                if(database.checkIfAfdelingExists("APP.Clubs", cl.getClubNaam())) {
                    System.out.println("exists: " + cl.getClubNaam());
                } else {
                    System.out.println("does not exist: " + cl.getClubNaam());
                    
                    database.insertNewClubToDatabase(cl.getClubNaam(), cl.getLiga(), cl.getClubNummer(), cl.getVoorzitter(), cl.getClubStraat(), cl.getClubStraatNummer(), cl.getClubPostcode(), cl.getClubStad(), cl.getClubEmail(), cl.getClubTelefoon(), cl.getClubWebsite(), cl.getVisible());
                    
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String localDateToString (LocalDate localdate) {
        String date = localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return date;
    }
    
    public LocalDate stringToLocalDate (String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate datum = LocalDate.parse(date, formatter);
        return datum;
    }
    
    public String localTimeToString (LocalTime localtime) {
        String time = localtime.format(DateTimeFormatter.ofPattern("HH:mm"));
        return time;
    }
    
    public LocalTime stringToLocalTime (String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime tijd = LocalTime.parse(time, formatter);
        return tijd;
    }    
}

