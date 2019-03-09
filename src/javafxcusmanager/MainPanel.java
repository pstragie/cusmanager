/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.File;
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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

/**
 *
 * @author pieter
 */
public class MainPanel {
    
    private TextField topPaneTextField, bottomPaneTextField;
    private static MenuBar mainMenu;
    private Pane umpirepanel;
    //public ObservableList<String> observableTabList;
    public ObservableList<Afdeling> afdelingenlijst;
    public ObservableList<Club> clubs;
    public ObservableList<Team> teams;
    public ObservableList<Umpire> umpires;
    private DocumentHandling documentHandler;
    private Pane leftPane = new Pane();
    private Pane rightPane = new Pane();
    public TabPane leftTabPane = new TabPane();
    public TabPane rightTabPane = new TabPane();
    private Pane centerPane = new Pane();
    public TabPane centerTabPane = new TabPane();
    private Afdelingen changeAfdelingenpane;
    private ClubView clubview;
    private UmpireView umpireview;
    private UmpireModel umpiremodel;
    private ClubModel clubmodel;
    private GameSchedule gameSchedule;
    private Database database = new Database();
    public Button resetButton;

    public Pane MainPanel() {     
        
        
        afdelingenlijst = FXCollections.observableArrayList();
        // Get afdelingen from database
        afdelingenlijst.addAll(database.getAllAfdelingenFromDatabase());
        afdelingenlijst.addListener((ListChangeListener.Change<? extends Afdeling> change) -> { 
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

                                // TO DO: Store in Database
                                database.deleteAfdelingFromDatabase(remitem.getAfdelingsNaam());

                            }
                            for (Afdeling additem : change.getAddedSubList()) {
                                System.out.println("additem");

                                leftTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                rightTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                centerTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!

                                // TO DO: Store in database, Done when button is pressed

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
                    System.out.println("Clubs Update detected");
                    // Write to file?

                } else
                    if (change.wasPermutated()) {
                        System.out.println("Clubs Was permutated");
                    } 
                        else 
                            for (Umpire additem: change.getAddedSubList()) {
                            System.out.println("Data " + change + " was added to umpires");
                            // Write to database: Done when addButton is pressed

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
                        System.out.println("Update detected");
                        // Write to file

                    } else
                        if (change.wasPermutated()) {
                            System.out.println("Was permutated");
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
        afdelingenlijst.forEach(afd -> afdelingStrings.add(afd.getAfdelingsNaam()));
    
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
        Button button = new Button("Exporteren");
        CustomMenuItem customMenuItem = new CustomMenuItem();
        customMenuItem.setContent(button);
        customMenuItem.setHideOnClick(false);
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
                    System.out.println(cl);
                    database.insertNewClubToDatabase(cl.getClubNaam(), cl.getVoorzitter(), cl.getClubNummer(), cl.getClubStraat(), cl.getClubStraatNummer(), cl.getClubPostcode(), cl.getClubStad(), cl.getClubEmail(), cl.getClubTelefoon(), cl.getLiga(), cl.getClubWebsite());
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
        
        afdelingenBeheren.setOnAction(e -> { 
            Stage stage = new Stage();
            Scene scene = new Scene(newAfdelingPaneel(leftTabPane, rightTabPane, centerTabPane, afdelingenlijst));
            stage.setTitle("Afdelingen wijzigen");
            stage.setScene(scene);
            stage.show();
        });
        MenuItem afdelingenImporteren = new MenuItem("Import Afdelingen...");
        menuAfdelingen.getItems().add(afdelingenImporteren);
        afdelingenImporteren.setOnAction(e -> {
            Stage stage = new Stage();
            String absPath = getFilePath(stage, "Afdelingen");
            if (absPath != "") {
                // Wis de huidige afdelingen
                database.deleteAllAfdelingenInDatabase();
                // Import afdelingen in de database
                ArrayList<Afdeling> arrayAfdelingen = documentHandler.getAfdelingenFromFile(absPath);
                for(Afdeling afd : arrayAfdelingen) {
                    database.insertNewAfdelingToDatabase(afd.getAfdelingsNaam(), afd.getAfdelingsCategorie(), Boolean.TRUE);
                }
                afdelingenlijst.clear();
                afdelingenlijst.addAll(database.getAllAfdelingenFromDatabase());
            }
        });
        return menubar;
    }
    
    public String getFilePath(Stage stage, String naam) {
        
        
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Bestand selecteren");
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
                    System.out.println("current observableTabList: " + afdelingenlijst);
                    System.out.println("Nothing to filter: " + afdelingenlijst);
                    // Reset the tabpane to show all tabs
                    sideTabPane.getTabs().clear();
                    
                    sideTabPane.getTabs().addAll(getUmpireTabArrayList());
                } else {
                    System.out.println("Filter active: " + newText);
                    System.out.println("Filtered List = " + afdelingenlijst.filtered(tab -> tab.getAfdelingsNaam().contains(newText)));
                    
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
    
    public void resetSideTabPanes() {
        leftTabPane.getTabs().clear();
        leftTabPane.getTabs().addAll(getUmpireTabArrayList());
        rightTabPane.getTabs().clear();
        rightTabPane.getTabs().addAll(getClubTabArrayList());
    }
    public VBox createHorBoxFilterClubs(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        VBox vbox = new VBox();
        Label clubLabel = new Label("Teams");
        clubLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        clubLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(clubLabel);
        
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                
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
                filterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(filterField, Priority.ALWAYS);
            hbox.getChildren().add(filterField);
            hbox.getChildren().add(filterButton);
            vbox.getChildren().add(hbox);
        return vbox;
    }
    
    public VBox createHorBoxFilterGames(TabPane centerTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        VBox vbox = new VBox();
        Label gameLabel = new Label("Game Schedule");
        gameLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        gameLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gameLabel);
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                
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
        return afdelingenlijst;
    }
    
     public void setTabs(ObservableList<Afdeling> tabs) {
        this.afdelingenlijst = tabs;
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
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(getAfdelingsnamenlijst());
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(umpiremodel.createUmpireContent(a)); // Set filtered content
            tabs.add(tab);
            
        });        
        return tabs;
    }
    
    public ArrayList<Tab> getGameTabArrayList() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        gameSchedule = new GameSchedule();
        System.out.println("Get Tabs from file and create club content\n________________");
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(getAfdelingsnamenlijst());
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(gameSchedule.createCalendar(a)); // Set filtered content
            tabs.add(tab);
            
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
    
    public Pane ClubPaneel() {
        
            clubview = new ClubView(clubs, teams, afdelingenlijst);
            return clubview.clubPane();
    }
    
    public Pane UmpirePaneel() {
            umpireview = new UmpireView(umpires, clubs, teams, afdelingenlijst);
            return umpireview.umpirePane();
    }
    public void addAfdeling() {
        try {
            // Check if afdeling exists before adding to database --> Error will occur
            //database.insertNewAfdelingToDatabase("8BB", "baseball", Boolean.TRUE);
            for (Afdeling afd : afdelingenlijst) {
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
    
    public void addClub() {
        try {
            // Check if afdeling exists before adding to database --> Error will occur
            //database.insertNewAfdelingToDatabase("8BB", "baseball", Boolean.TRUE);
            for (Club cl : clubs) {
                if(database.checkIfAfdelingExists("APP.Clubs", cl.getClubNaam())) {
                    System.out.println("exists: " + cl.getClubNaam());
                } else {
                    System.out.println("does not exist: " + cl.getClubNaam());
                    
                    database.insertNewClubToDatabase(cl.getClubNaam(), cl.getLiga(), cl.getClubNummer(), cl.getVoorzitter(), cl.getClubStraat(), cl.getClubStraatNummer(), cl.getClubPostcode(), cl.getClubStad(), cl.getClubEmail(), cl.getClubTelefoon(), cl.getClubWebsite());
                    
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

