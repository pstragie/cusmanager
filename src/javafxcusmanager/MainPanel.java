/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.util.ArrayList;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
    public ObservableList<String> observableTabList;
    private DocumentHandling documentHandler;
    private Pane leftPane = new Pane();
    private Pane rightPane = new Pane();
    public TabPane leftTabPane = new TabPane();
    public TabPane rightTabPane = new TabPane();
    private Pane centerPane = new Pane();
    public TabPane centerTabPane = new TabPane();
    private Afdelingen changeAfdelingenpane;
    private GameSchedule gameSchedule;
    private Clubs clublijst;
    private Umpires umpirelijst;
    private ArrayList<String> clublijstPerafdeling;
    private ArrayList<String> umpirelijstPerafdeling;
    public Button resetButton;
    private final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

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
            Scene scene = new Scene(newAfdelingPaneel("Afdelingen", leftTabPane, rightTabPane));
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
            HBox hbox = createHorBoxFilterClubs(sideTabPane);
        
        vbox.getChildren().add(hbox);
        
            // Create TabPane
            sideTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            sideTabPane.getTabs().addAll(getClubTabArrayListFromFile());
            //tabpane.getStylesheets().add(getClass().getResource("css/TabPaneStyles.css").toExternalForm());
            Text placeHolder = new Text( " Geen afdelingen gevonden." );
                    placeHolder.setFont( Font.font( null, FontWeight.BOLD, 20 ) );
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
            HBox hbox = createHorBoxFilterUmpires(sideTabPane);
            
        vbox.getChildren().add(hbox);
        
            // Create TabPane
            sideTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            
            sideTabPane.getTabs().addAll(getUmpireTabArrayListFromFile());
            //tabpane.getStylesheets().add(getClass().getResource("css/TabPaneStyles.css").toExternalForm());
            Text placeHolder = new Text( " Geen afdelingen gevonden." );
                    placeHolder.setFont( Font.font( null, FontWeight.BOLD, 20 ) );
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
        vbox.setBorder(new Border(new BorderStroke(Color.DARKSLATEBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3, 3, 3, 3))));

        middleTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        middleTabPane.getTabs().add(new Tab("Week"));
        middleTabPane.getTabs().add(new Tab("Maand"));
        middleTabPane.getTabs().add(new Tab("Jaar"));
        VBox calendarbox = new VBox();
            // Get empty Weekend game model
            TableView table = new TableView();
            final Label label = new Label("Game Schedule");
            label.setFont(new Font("Arial", 20));

            table.setEditable(true);

            TableColumn homeTeam = new TableColumn("Home team");
            TableColumn visitingTeam = new TableColumn("Visiting team");
            TableColumn umpire = new TableColumn("Umpire");

            table.getColumns().addAll(homeTeam, visitingTeam, umpire);

            calendarbox.setPadding(new Insets(10, 0, 0, 10));
            calendarbox.getChildren().addAll(label, table);
        vbox.getChildren().add(middleTabPane);
        vbox.getChildren().add(calendarbox);
        vbox.setPadding(new Insets(5, 10, 5, 10));
        return vbox;
    }
    public void resetRightTabpaneSide(TabPane sideTabPane) {
        // Reset the tabpane to show all tabs
        ObservableList<String> tablist = getTabsList();
        tablist.forEach(tab1 -> {
            sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1));
        });
        sideTabPane.getTabs().addAll(getUmpireTabArrayListFromFile());
    }
    
    public void resetLeftTabpaneSide(TabPane sideTabPane) {
        // Reset the tabpane to show all tabs
        ObservableList<String> tablist = getTabsList();
        tablist.forEach(tab1 -> {
            sideTabPane.getTabs().removeIf(tab -> tab.getText().contains(tab1));
        });
        sideTabPane.getTabs().addAll(getClubTabArrayListFromFile());
    }
    
    public HBox createHorBoxFilterUmpires(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                System.out.println("Text changed from "+oldText+" to "+newText);
                
                if (newText == null || newText.isEmpty()) {
                    System.out.println("current observableTabList: " + observableTabList);
                    System.out.println("Nothing to filter: " + observableTabList);
                    // Reset the tabpane to show all tabs
                    sideTabPane.getTabs().clear();
                    
                    sideTabPane.getTabs().addAll(getUmpireTabArrayListFromFile());
                } else {
                    System.out.println("Filter active: " + newText);
                    System.out.println("Filtered List = " + observableTabList.filtered(tab -> tab.contains(newText)));
                    
                    sideTabPane.getTabs().clear(); // Clear to restart on every change!
                    observableTabList = getObservableList();
                    observableTabList.filtered(tab -> tab.contains(newText)).forEach(t -> {
                        sideTabPane.getTabs().add(new Tab(t));
                    });
                    
                    System.out.println("sideTabPane: " + leftTabPane.getTabs());
                    
                }
            });
            
            resetButton = new Button();
            resetButton.setText("Reset");
            resetButton.setOnAction(event -> {
                System.out.println("Reset clicked, list: " + observableTabList);
                System.out.println("tabs in pane: " + sideTabPane.getTabs());
                sideTabPane.getTabs().clear();
                System.out.println("tabs in pane after removal: " + sideTabPane.getTabs());
                sideTabPane.getTabs().addAll(getUmpireTabArrayListFromFile());
                System.out.println("tabs in pane after adding all from file: " + sideTabPane.getTabs());
                filterField.setText("");
            });
            hbox.getStyleClass().add("bordered-titled-border");
            hbox.setHgrow(filterField, Priority.ALWAYS);
            hbox.getChildren().add(filterField);
            hbox.getChildren().add(resetButton);
        return hbox;
    }
    
    public HBox createHorBoxFilterClubs(TabPane sideTabPane) {
        /** Creates a VBox with textfield and button for filtering tabpanes
         * 
         */
        HBox hbox = new HBox();
            TextField filterField = new TextField();
            filterField.setPromptText("Filter tabs");
            filterField.textProperty().addListener((obs, oldText, newText) -> {
                System.out.println("Text changed from "+oldText+" to "+newText);
                
                if (newText == null || newText.isEmpty()) {
                    System.out.println("Nothing to filter: " + observableTabList);
                    // Reset the tabpane to show all tabs
                    sideTabPane.getTabs().clear();
                    sideTabPane.getTabs().addAll(getClubTabArrayListFromFile());
                } else {
                    System.out.println("Filter active: " + newText);
                    System.out.println("Filtered List = " + observableTabList.filtered(tab -> tab.contains(newText)));
                    
                    sideTabPane.getTabs().clear(); // Clear to restart on every change!
                    observableTabList = getObservableList();
                    observableTabList.filtered(tab -> tab.contains(newText)).forEach(t -> {
                        sideTabPane.getTabs().add(new Tab(t));
                    });
                    
                    System.out.println("sideTabPane: " + leftTabPane.getTabs());
                    
                }
            });
            
            Button filterButton = new Button();
            filterButton.setText("Reset");
            filterButton.setOnAction(event -> {
                System.out.println("Reset clicked, list: " + observableTabList);
                System.out.println("tabs in pane: " + sideTabPane.getTabs());
                sideTabPane.getTabs().clear();
                System.out.println("tabs in pane after removal: " + sideTabPane.getTabs());
                sideTabPane.getTabs().addAll(getClubTabArrayListFromFile());
                System.out.println("tabs in pane after adding all from file: " + sideTabPane.getTabs());
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
    
     public ArrayList<Tab> getClubTabArrayListFromFile() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        System.out.println("Get Tabs from file and create club content\n________________");
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(createListOfItems("afdelingen.txt"));
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(createClubContent(a)); // Set filtered content
            tabs.add(tab);
            
        });        
        return tabs;
    }
    
    
    public ArrayList<Tab> getUmpireTabArrayListFromFile() {
        /** Get tabs from the list and add content for that afdeling
         * 
         */
        System.out.println("Get Tabs from file and create umpire content\n________________");
        ArrayList<String> listOfItems = new ArrayList<>();
        listOfItems.addAll(createListOfItems("afdelingen.txt"));
        ArrayList<Tab> tabs = new ArrayList<>();
        listOfItems.forEach(a -> {
            Tab tab = new Tab(a);
            tab.setContent(createUmpireContent(a)); // Set filtered content
            tabs.add(tab);
            
        });        
        return tabs;
    }
    
    private VBox createClubContent(String afd) {
            ListView<String> clubListview = new ListView<>();
            clublijstPerafdeling = new ArrayList<>();
            clublijst = new Clubs();
            Map<String, String> clubmap = clublijst.getList();
            System.out.println("clubmap:" + clubmap);
            clubmap.forEach((k,val) ->  {
                if (val.equals(afd)) {
                    System.out.println(k);
                    clublijstPerafdeling.add(k);
                }
            });
            System.out.println("clublijstPerafdeling" + "(" + afd + "): " + clublijstPerafdeling);
            ObservableList<String> data = FXCollections.<String>observableArrayList(clublijstPerafdeling);
            clubListview.getItems().addAll(data);
            clubListview.setPrefSize(150, 800);
            clubListview.setOrientation(Orientation.VERTICAL);
            clubListview.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
                cell.setOnDragDetected(event -> {
                   if (! cell.isEmpty()) {
                       Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                       ClipboardContent cc = new ClipboardContent();
                       cc.putString(cell.getItem());
                       db.setContent(cc);
                       dragSource.set(cell);
                   }
               });

               cell.setOnDragOver(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString()) {
                       event.acceptTransferModes(TransferMode.MOVE);
                   }
               });

               cell.setOnDragDone(event -> clubListview.getItems().remove(cell.getItem()));

               cell.setOnDragDropped(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString() && dragSource.get() != null) {
                       // in this example you could just do
                       // listView.getItems().add(db.getString());
                       // but more generally:

                       ListCell<String> dragSourceCell = dragSource.get();
                       clubListview.getItems().add(dragSourceCell.getItem());
                       event.setDropCompleted(true);
                       dragSource.set(null);
                   } else {
                       event.setDropCompleted(false);
                   }
               });

               return cell ;
            });
            //clubListview.getItems().addAll(clublijstPerafdeling);
            
            VBox clubsBox = new VBox();
            clubsBox.getChildren().add(clubListview); // Add listview to Vertical Box
            
            return clubsBox; // return VBox with listview of clubs per afdeling
        }
     
    private VBox createUmpireContent(String afd) {
            ListView<String> umpireListview = new ListView<>();
            umpirelijstPerafdeling = new ArrayList<>();
            umpirelijst = new Umpires();
            Map<String, String> clubmap = umpirelijst.getList();
            System.out.println("umpiremap:" + clubmap);
            clubmap.forEach((k,val) ->  {
                if (val.equals(afd)) {
                    System.out.println(k);
                    umpirelijstPerafdeling.add(k);
                }
            });
            System.out.println("umpirelijstPerafdeling" + "(" + afd + "): " + umpirelijstPerafdeling);
            ObservableList<String> data = FXCollections.<String>observableArrayList(umpirelijstPerafdeling);
            umpireListview.getItems().addAll(data);
            umpireListview.setPrefSize(150, 800);
            umpireListview.setOrientation(Orientation.VERTICAL);
            umpireListview.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                    }
                };
                cell.setOnDragDetected(event -> {
                   if (! cell.isEmpty()) {
                       Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                       ClipboardContent cc = new ClipboardContent();
                       cc.putString(cell.getItem());
                       db.setContent(cc);
                       dragSource.set(cell);
                   }
               });

               cell.setOnDragOver(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString()) {
                       event.acceptTransferModes(TransferMode.MOVE);
                   }
               });

               cell.setOnDragDone(event -> umpireListview.getItems().remove(cell.getItem()));

               cell.setOnDragDropped(event -> {
                   Dragboard db = event.getDragboard();
                   if (db.hasString() && dragSource.get() != null) {
                       // in this example you could just do
                       // listView.getItems().add(db.getString());
                       // but more generally:

                       ListCell<String> dragSourceCell = dragSource.get();
                       umpireListview.getItems().add(dragSourceCell.getItem());
                       event.setDropCompleted(true);
                       dragSource.set(null);
                   } else {
                       event.setDropCompleted(false);
                   }
               });

               return cell ;
            });
            //umpireListview.getItems().addAll(umpirelijstPerafdeling);
            
            VBox umpiresBox = new VBox();
            umpiresBox.getChildren().add(umpireListview); // Add listview to Vertical Box
            
            return umpiresBox; // return VBox with listview of clubs per afdeling
        }
    
    public ArrayList<String> createListOfItems(String filename) {
        //ArrayList<String> arraylist = new ArrayList<>();
        documentHandler = new DocumentHandling();
        ArrayList<String> arraylist = (ArrayList<String>) documentHandler.getAfdelingenFromFile();
        return arraylist;
    }
    
    public Pane newAfdelingPaneel(String s, TabPane tabpaneleft, TabPane tabpaneright) {
        BorderPane border = new BorderPane();
        if(s == "Afdelingen") {
            changeAfdelingenpane = new Afdelingen(tabpaneleft, tabpaneright);
            border.setCenter(changeAfdelingenpane.afdelingenPanel());
            
        }
        
        return border;
    }
}

