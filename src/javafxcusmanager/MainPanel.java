/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author pieter
 */
public class MainPanel {
    
    private TextField topPaneTextField, bottomPaneTextField;
    private ClubModel clubpane;
    private static MenuBar mainMenu;
    private Pane umpirepanel;
    public ObservableList<String> observableTabList;
    private DocumentHandling documentHandler;
    private Pane leftPane = new Pane();
    private Pane rightPane = new Pane();
    public TabPane leftTabPane = new TabPane();
    public TabPane rightTabPane = new TabPane();
    private Afdelingen changeAfdelingenpane;
    
    public Pane MainPanel() {     
        
        // Fill observableList for the first (and only) time
        observableTabList = FXCollections.observableArrayList();
        
        
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getAfdelingenFromFile();
        
        observableTabList.addAll(arraylist);
        
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
        
        leftPane = createSidePane(leftTabPane);
        borderPane.setLeft(leftPane);
        
        // Right Pane -> Umpires
        rightPane = new Pane();
        rightPane = createSidePane(rightTabPane);
        borderPane.setRight(rightPane);
        
        borderPane.setBottom(bottomPaneTextField);
        return borderPane;
    }
    
    public ObservableList<String> getObservableList() { 
        observableTabList = FXCollections.observableArrayList();
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getAfdelingenFromFile();
        ArrayList<String> tabs = new ArrayList<>();
        arraylist.forEach(a -> {
                tabs.add(a);
            });
        observableTabList.addAll(tabs);
        System.out.println("getObservableList: " + observableTabList);
        return observableTabList;
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
            /*
            System.out.println("menuClubItem1 Clicked");
            Stage stage = new Stage();
            Scene scene = new Scene(newPaneel("Club"));
            stage.setTitle("Club toevoegen");
            stage.setScene(scene);
            stage.show();
            */
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
            Scene scene = new Scene(newPaneel("Afdelingen", leftTabPane));
            stage.setTitle("Afdelingen wijzigen");
            stage.setScene(scene);
            stage.show();
        } );
        
        return menubar;
    }
    
    public VBox createSidePane(TabPane sideTabPane) {
        // Create a VBox with HBox for filter, and TabPane
        VBox vbox = new VBox();
        
            // Create HBox with textfield and button
            HBox hbox = createHorBoxFilter(sideTabPane);
            
        vbox.getChildren().add(hbox);
        
            // Create TabPane
           // TabPane tabpane = new TabPane();
            sideTabPane = createTabPane(); // Get Afdelingen from file
            //tabpane.getStylesheets().add(getClass().getResource("css/TabPaneStyles.css").toExternalForm());
        vbox.getChildren().add(sideTabPane);
        vbox.setPrefSize(300, 800);
        
        return vbox;
    }
    
    
    public void resetTabpaneSide(TabPane sideTabPane) {
        // Reset the tabpane to show all tabs
        ObservableList<String> tablist = getTabsList();
        tablist.forEach(tab1 -> {
            sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1));
        });
        sideTabPane.getTabs().addAll(getTabArrayList());
    }
    
    public HBox createHorBoxFilter(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                System.out.println("Text changed from "+oldText+" to "+newText);
                
                if (newText == null || newText.isEmpty()) {
                    System.out.println("Nothing to filter");
                    // Reset the tabpane to show all tabs
                    ObservableList<String> tablist = getTabsList();
                    tablist.forEach(tab1 -> {
                        sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1));
                    });
                    sideTabPane.getTabs().addAll(getTabArrayList());
                } else {
                    System.out.println("Filter active: " + newText);
                    // Get all tabs back in the tabpane
                    //ObservableList<Tab> tablist = getTabsList();
                    //System.out.println("tablist = " + tablist);
                    /*
                    observableTabList.forEach(tab1 -> {
                        sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1.getText()));
                        System.out.println("sideTabPane: " + sideTabPane.getTabs());
                    });
                    
                    
                    sideTabPane.getTabs().addAll(observableTabList);
                    System.out.println("sideTabPane: " + sideTabPane.getTabs());
                    // Remove tabs that do not contain newText
                    sideTabPane.getTabs().removeIf(tab -> !tab.getText().contains(newText));
                    System.out.println("sideTabPane: " + sideTabPane.getTabs());
                    */
                    //observableTabList.filtered(tab -> tab.contains(newText));
                    System.out.println("Filtered List = " + observableTabList.filtered(tab -> tab.contains(newText)));
                    observableTabList.filtered(tab -> !tab.contains(newText)).forEach(t -> {
                        leftTabPane.getTabs().remove(new Tab(t));
                    });
                    //resetTabpaneSide(sideTabPane); // Puts the tabs from the document back
                    //sideTabPane.getTabs().removeAll(observableTabList);
                    //sideTabPane.getTabs().addAll(observableTabList.filtered(tab -> tab.contains(newText)));
                    
                    System.out.println("sideTabPane: " + leftTabPane.getTabs());
                    
                }
            });
            
            Button filterButton = new Button();
            filterButton.setText("Reset");
            filterButton.setOnAction(event -> {
                ObservableList<String> tablist = getTabsList();
                tablist.forEach(tab1 -> {
                    sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1));
                });
                sideTabPane.getTabs().addAll(getTabArrayList());
                filterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(filterField, Priority.ALWAYS);
            hbox.getChildren().add(filterField);
            hbox.getChildren().add(filterButton);
        return hbox;
    }
    
     public ObservableList<String> getTabsList() {
        return observableTabList;
    }
    
     public void setTabs(ObservableList<String> tabs) {
        this.observableTabList = tabs;
    }
    
     public ArrayList<Tab> getTabArrayList() {
        ArrayList<String> listOfItems = createListOfItems("afdelingen.txt");
        ArrayList<Tab> tabs = new ArrayList<>();
        observableTabList.forEach(a -> {
            tabs.add(new Tab(a));
        });        
        return tabs;
    }
    
    public TabPane createTabPane() {
        TabPane tabpane2 = new TabPane();
        
        // Observable Tab List        
        leftTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        leftTabPane.getTabs().addAll(getTabArrayList());
            
        return leftTabPane;
    }
    
    public ArrayList<String> createListOfItems(String filename) {
        //ArrayList<String> arraylist = new ArrayList<>();
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getAfdelingenFromFile();
        return arraylist;
    }
    
    public Pane newPaneel(String s, TabPane tabpane) {
        BorderPane border = new BorderPane();
        if(s == "Afdelingen") {
            changeAfdelingenpane = new Afdelingen(tabpane);
            border.setCenter(changeAfdelingenpane.afdelingenPanel());
            
        }
        /*
        if(s == "Umpire") {
            newUmpirepane = new NewUmpire.UmpirePanel();
            border.setCenter(newUmpirepane.umpirePanel());
        }
        else 
        if(s == "Club") {
            newClubpane = new NewClub.ClubPanel();
            border.setCenter(newClubpane.clubPanel());
        }
        
        else
        
        */
        return border;
    }
}

