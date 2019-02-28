/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
    private UmpireModel umpiremodel;
    private ClubModel clubmodel;
    private GameSchedule gameSchedule;

    public Button resetButton;
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

    public Pane MainPanel() {     
        
        
        afdelingenlijst = FXCollections.observableArrayList();
        
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
                                System.out.println("remitem afdelingenlijst");

                                
                                    
                            }
                            for (Afdeling additem : change.getAddedSubList()) {
                                System.out.println("additem afdelingenlijst");
                               
                                
                            }
                }
            });
        afdelingenlijst.add(new Afdeling("Gold", "Baseball"));
        afdelingenlijst.add(new Afdeling("1BB", "Baseball"));
        afdelingenlijst.add(new Afdeling("2BB", "Baseball"));
        afdelingenlijst.add(new Afdeling("1SP", "Softball"));
        
        // Club and teams
        clubs = FXCollections.observableArrayList();
            teams = FXCollections.observableArrayList();
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
                                    System.out.println("Data was added to clubs");
                                    // Write to file
                                    //clubList.refresh();
                                    // Save to database
                                    
                                    //GameSchedule.write(gameData, /home/pieter/wedstrijdschema.txt);
                                }
                            }
                        
                }
            });
            
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
        ArrayList<Team> teamArray = new ArrayList<>();
        teamArray.add(new Team("Wolverines Seniors", "Gold"));
        teamArray.add(new Team("Wolfkes", "BB Rookies"));
        clubs.add(new Club("Wolverines", "0058", "Pieter Stragier", "pstragier@gmail.com", "0486208014", "Coolstraat", "5", "9600", "Ronse", teamArray));
        ArrayList<Team> frogsteamArray = new ArrayList<>();
        frogsteamArray.add(new Team("Frogs Seniors", "4BB"));
        frogsteamArray.add(new Team("Slowpitch", "SP Red"));
        clubs.add(new Club("Frogs", "0012", "Jonas Hoebeke", "jonas.hoebeke@hotmail.com", "04xxxxxxxx", "Scheldekant", "4", "9700", "Oudenaarde", frogsteamArray));
        ArrayList<Afdeling> afdArray = new ArrayList<>();
        afdArray.add(new Afdeling("Gold", "Baseball"));
        afdArray.add(new Afdeling("1BB", "Baseball"));
        umpires = FXCollections.observableArrayList();
        umpires.add(new Umpire("Pieter Stragier", "050058", "Coolstraat", "5", "9600", "Ronse", "0486208014", "pstragier@gmail.com", "Wolverines", afdArray));
        
        
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = new ArrayList<>();
        //observableTabList.addAll(getAfdelingsnamenlijst());
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
                                
                                    
                            }
                            for (Afdeling additem : change.getAddedSubList()) {
                                System.out.println("additem");
                              
                                leftTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                rightTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                                centerTabPane.getTabs().add(new Tab(additem.getAfdelingsNaam()));  // Add from observableTabList to get the correct order!
                              
                                // TO DO: Store in database
                                
                            }
                }
            });
        
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
        borderPane.setLeft(leftPane);
        
        // Right Pane -> Umpires
        rightPane = new Pane();
        rightPane = createRightSidePane(rightTabPane);
        borderPane.setRight(rightPane);
        
        borderPane.setBottom(bottomPaneTextField);
        
        // Center Pane -> Game schedule
        centerPane = new Pane();
        centerPane = createCenterPane(centerTabPane);
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
        Menu menu1 = new Menu("Umpires");
        menubar.getMenus().add(menu1);
        MenuItem menuUmpireItem1 = new MenuItem("Umpire toevoegen");
        menu1.getItems().add(menuUmpireItem1);
        Button button = new Button("Exporteren");
        CustomMenuItem customMenuItem = new CustomMenuItem();
        customMenuItem.setContent(button);
        customMenuItem.setHideOnClick(false);
        menu1.getItems().add(customMenuItem);
        // Menu Clubs
        Menu menu2 = new Menu("Clubs");
        menubar.getMenus().add(menu2);        
        MenuItem menuClubItem1 = new MenuItem("Club toevoegen");
        menu2.getItems().add(menuClubItem1);

        menuClubItem1.setOnAction(e -> {
            
            System.out.println("menuClubItem1 Clicked");
            Stage stage = new Stage();
            Scene scene = new Scene(ClubPaneel(), 800, 800);
            //stage.setX(1000);
            //stage.setY(800);
            stage.setTitle("Clubs beheren");
            stage.setScene(scene);
            stage.show();
            
        });
        
        menuUmpireItem1.setOnAction(e -> {
            /*   
            System.out.println("menuUmpireItem1 Clicked");
            Stage stage = new Stage();
            Scene scene = new Scene(newPaneel("Umpire"));
            stage.setTitle("Umpire toevoegen");
            stage.setScene(scene);
            stage.show();
            */
        });
        
        // Menu Afdelingen
        Menu menu3 = new Menu("Afdelingen");
        menubar.getMenus().add(menu3);
        MenuItem menuAfdelingenItem1 = new MenuItem("Afdelingen wijzigen");
        menu3.getItems().add(menuAfdelingenItem1);
        
        menuAfdelingenItem1.setOnAction(e -> { 
            Stage stage = new Stage();
            Scene scene = new Scene(newAfdelingPaneel("Afdelingen", leftTabPane, rightTabPane, centerTabPane, afdelingenlijst));
            stage.setTitle("Afdelingen wijzigen");
            stage.setScene(scene);
            stage.show();
        } );
        
        return menubar;
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
    
    public VBox createHorBoxFilterClubs(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        VBox vbox = new VBox();
        Label umpireLabel = new Label("Teams");
        umpireLabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        umpireLabel.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(umpireLabel);
        
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
     
    
    
    public ArrayList<String> createListOfItems(String filename) {
        //ArrayList<String> arraylist = new ArrayList<>();
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getAfdelingenFromFile();
        return arraylist;
    }
    
    public Pane newAfdelingPaneel(String s, TabPane tabpaneleft, TabPane tabpaneright, TabPane tabpanecenter, ObservableList afdelingenlijst) {
        BorderPane border = new BorderPane();
        if(s == "Afdelingen") {
            changeAfdelingenpane = new Afdelingen(tabpaneleft, tabpaneright, tabpanecenter, afdelingenlijst);
            border.setCenter(changeAfdelingenpane.afdelingenPanel());
            
        }
        
        return border;
    }
    
    public Pane ClubPaneel() {
        
            clubview = new ClubView(clubs, teams);
            return clubview.clubPane();
    }
}

