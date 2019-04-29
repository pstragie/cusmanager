/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

/**
 *
 * @author Pieter Stragier
 */
public class ShowDistances {
    
    private ObservableList<Umpire> umpires;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private ObservableList<Afdeling> afdelingen;
    private Umpire umpireselection;
    private Club clubselection;
    private UmpireView umpireview;
    private Database database;
    private Pane lpane;
    private ListView umpirelistview = new ListView();
    private Boolean nieuwUmpire = Boolean.FALSE;
    private ApiLocationDistance apiLocationDistance;
    private VBox vertDistances;
    private BorderPane bpane;
    private VBox mapBox;
    
    Task copyWorker;
    
    final ProgressBar progressBar = new ProgressBar(0);
    final ProgressIndicator progressIndicator = new ProgressIndicator(0);

    final Button sluitButton = new Button("Start");
    final Button cancelButton = new Button("Cancel");

    final Label statusLabel = new Label();
    
    
    /** Show Distances
     * 
     * @param clubs
     * @param umpires
     * @param teams
     * @param afdelingen 
     */
    public ShowDistances(ObservableList<Umpire> umpires, ObservableList<Club> clubs, ObservableList<Team> teams, ObservableList<Afdeling> afdelingen) {
            this.umpires = umpires;
            this.clubs = clubs;
            this.teams = teams;
            this.afdelingen = afdelingen;
            umpireview = new UmpireView(umpires, clubs, teams, afdelingen);
            database = new Database();
            umpireselection = umpires.get(0);
            clubselection = clubs.get(0);
            apiLocationDistance = new ApiLocationDistance();
    }
    
    public Task getLocationsBing() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Pair p = new Pair(0.0, 0.0);

                for (Club club : clubs) {
                    updateMessage(club.getClubNaam());
                    updateProgress(clubs.indexOf(club), clubs.size());
                    try {
                        p = apiLocationDistance.getLocationClub(club);

                    } catch (IOException ex) {
                        Logger.getLogger(ExistingClubDetails.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        String latitude = Double.toString((Double) p.getKey());
                        String longitude = Double.toString((Double) p.getValue());
                        database.updateClubLocationInDatabase(club.getClubNummer(), latitude, longitude);
                        int cindex = clubs.indexOf(club);
                        clubs.get(cindex).setLatitude(latitude);
                        clubs.get(cindex).setLongitude(longitude);
                        
                    }
                }
              
              
              
            return true;
            }
        };
    }
    
    /** Bereken de afstanden task
     * 
     * @return 
     */
    public Task calculateDistancesBing() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Pair<Double, Double> p = new Pair<>(0.0, 0.0);

                for (Umpire umpire : umpires) {
                    ArrayList<Club> clubArray = new ArrayList<>(clubs);
                    try {
                        if (umpire.getLatitude() == null || umpire.getLongitude() == null || umpire.getLatitude().isEmpty() || umpire.getLongitude().isEmpty()) {
                            System.out.println("Lat or Lon unknown for " + umpire.getUmpireNaam());
                            Pair<Double, Double> pair = apiLocationDistance.getLocationUmpire(umpire);
                            database.updateUmpireLocationInDatabase(umpire.getUmpireLicentie(), Double.toString(pair.getKey()), Double.toString(pair.getValue()));
                        }
                        //apiLocationDistance.calculateDistance(umpireselection, clubarray);
                    } catch (IOException ex) {
                        Logger.getLogger(UmpireView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        apiLocationDistance.calculateDistance(umpire, clubArray);
                    } catch (IOException ex) {
                        Logger.getLogger(ShowDistances.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 
                }

                return true;
            }
        };
    }
    
    /** Venster met afstanden tussen umpire en clubs
     * 
     * @return 
     */
    public Pane distancePane() {
        bpane = new BorderPane();
        statusLabel.setMinWidth(500);
        statusLabel.setTextFill(Color.BLUE);
        HBox horbottom = new HBox(10);
        Label bottomLabel = new Label("Afstanden met Bing Maps Dev");
        horbottom.getChildren().add(bottomLabel);
        bpane.setBottom(horbottom);
        
        // Menu
        MenuBar menubar = new MenuBar();

        // Menu Umpires
        Menu menuLocaties = new Menu("Afstanden");
        menubar.getMenus().add(menuLocaties);
        MenuItem locatiesbepalen = new MenuItem("Club locaties bepalen");
        
        locatiesbepalen.setOnAction(e -> {
            progressBar.setPrefWidth(350);
            progressBar.setPrefHeight(30);
            final Button sluitButton = new Button("Sluiten");
            final Button cancelButton = new Button("Cancel");
            final HBox hb2 = new HBox();
            hb2.setSpacing(5);
            hb2.setAlignment(Pos.CENTER);
            hb2.getChildren().addAll(cancelButton, sluitButton);
            final HBox hb = new HBox(5);
            hb.setSpacing(5);
            hb.setAlignment(Pos.CENTER);
            final Label label = new Label("Locaties berekenen:");
            hb.getChildren().addAll(label, progressBar);
            hb.setBackground(Background.EMPTY);
            final VBox vb = new VBox(20);
            vb.setPadding(new Insets(20, 20, 20, 20));
            vb.setBackground(Background.EMPTY);
            vb.getChildren().addAll(hb);
            ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
            GaussianBlur blur = new GaussianBlur(5);
            adj.setInput(blur);
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color: white;");
            //root.setEffect(adj);
            root.getChildren().add(vb);
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 150);
            stage.setTitle("Locaties berekenen");
            
            stage.alwaysOnTopProperty();
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            
            cancelButton.setOnAction((ActionEvent event) -> {
                cancelButton.setDisable(true);
                sluitButton.setDisable(false);
                copyWorker.cancel(true);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                System.out.println("cancelled.");
            });
            sluitButton.setOnAction(sluit -> {
               //stage = (Stage) sluitButton.getScene().getWindow();
               stage.close();
            });
            progressBar.setProgress(0);
            copyWorker = getLocationsBing();
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(copyWorker.progressProperty());
            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
              public void changed(ObservableValue<? extends String> observable,
                  String oldValue, String newValue) {
                System.out.println(newValue);
              }
            });
            copyWorker.setOnSucceeded(klaar -> {
                System.out.println("Klaar!!!");
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                stage.hide();
            });
            new Thread(copyWorker).start();
            
            
        });
        menuLocaties.getItems().add(locatiesbepalen);
        
        
        MenuItem afstandenHerberekenen = new MenuItem("Afstanden (her)berekenen");
        afstandenHerberekenen.setOnAction((ActionEvent bereken) -> {
            
            progressBar.setPrefWidth(350);
            progressBar.setPrefHeight(30);
            final Button sluitButton = new Button("Sluiten");
            final Button cancelButton = new Button("Cancel");
            final HBox hb2 = new HBox();
            hb2.setSpacing(5);
            hb2.setAlignment(Pos.CENTER);
            hb2.getChildren().addAll(cancelButton, sluitButton);
            final HBox hb = new HBox(5);
            hb.setSpacing(5);
            hb.setAlignment(Pos.CENTER);
            final Label label = new Label("Afstanden berekenen:");
            hb.getChildren().addAll(label, progressBar);
            hb.setBackground(Background.EMPTY);
            final VBox vb = new VBox(20);
            vb.setPadding(new Insets(20, 20, 20, 20));
            vb.setBackground(Background.EMPTY);
            vb.getChildren().addAll(hb);
            ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
            GaussianBlur blur = new GaussianBlur(5);
            adj.setInput(blur);
            StackPane root = new StackPane();
            root.setStyle("-fx-background-color: white;");
            //root.setEffect(adj);
            root.getChildren().add(vb);
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 150);
            stage.setTitle("Locaties berekenen");
            
            stage.alwaysOnTopProperty();
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            
            cancelButton.setOnAction((ActionEvent event) -> {
                cancelButton.setDisable(true);
                sluitButton.setDisable(false);
                copyWorker.cancel(true);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                System.out.println("cancelled.");
            });
            sluitButton.setOnAction(sluit -> {
               //stage = (Stage) sluitButton.getScene().getWindow();
               stage.close();
            });
            progressBar.setProgress(0);
            copyWorker = calculateDistancesBing();
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(copyWorker.progressProperty());
            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
              public void changed(ObservableValue<? extends String> observable,
                  String oldValue, String newValue) {
                System.out.println(newValue);
              }
            });
            copyWorker.setOnSucceeded(klaar -> {
                System.out.println("Klaar!!!");
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                stage.hide();
            });
            new Thread(copyWorker).start();
            
            
        });
        menuLocaties.getItems().add(afstandenHerberekenen);
        bpane.setTop(menubar);
        
       
        // Umpires
        VBox vertUmpBox = new VBox(5);
        vertUmpBox.getChildren().add(umpirePane());
        bpane.setLeft(vertUmpBox);
        
        vertDistances = new VBox(5);
        vertDistances.setPrefHeight(1000.0);
        final Label label = new Label("Afstanden (km)");
        label.setPadding(new Insets(1, 0, 1, 5));
        label.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        vertDistances.getChildren().add(label);
        vertDistances.setPadding(new Insets(0, 5, 5, 0));
        vertDistances.getChildren().add(distanceTable(umpires.get(0)));
        vertDistances.setPrefHeight(1000.0);
        bpane.setRight(vertDistances);
        
        VBox centerVbox = new VBox(5);
        final Label kaartlabel = new Label("Kaart");
        kaartlabel.setPadding(new Insets(1, 0, 1, 5));
        kaartlabel.setFont(Font.font( null, FontWeight.BOLD, 20));
        centerVbox.getChildren().add(kaartlabel);
        mapBox = new VBox(5);
        
        Double bh = 600.0;
        Double bw = 600.0;
        try {
            mapBox = apiLocationDistance.showMap(umpireselection, clubs.get(0), bh, bw);
        } catch (IOException ex) {
            Logger.getLogger(ShowDistances.class.getName()).log(Level.SEVERE, null, ex);
        }
        mapBox.setAlignment(Pos.CENTER);
        centerVbox.getChildren().add(mapBox);
        bpane.setCenter(centerVbox);
        
        
        
        
        return bpane;
        
    }
    
    
    
    public Pane umpirePane() {
        // New Window
        Comparator<Umpire> umpireComparator = Comparator.comparing(Umpire::getUmpireNaam);

        if (umpires.size() > 0) {
            umpireselection = umpires.get(0);
        } else {
            ArrayList<Afdeling> emptyArray = new ArrayList<>();
            Club emptyClub = new Club("", "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, "", "", "", emptyArray);
            umpireselection = new Umpire("", "", "", "", "", "", "", "", "", "", emptyClub, emptyArray, Boolean.FALSE, "", "");
        }
        
        // Left Side: List of all Umpires in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox umpireVBox = new VBox(5);
        ListView umpirelistView = new ListView();
        umpirelistView = getUmpireListView(umpireVBox, lpane);
        umpireVBox.getChildren().add(umpirelistView);
        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        VBox teamVBox = new VBox(5);
        HBox newteamHBox = new HBox(5);
        
        return umpireVBox;
    }
        
    public TableView distanceTable(Umpire ump) {
        ArrayList<String> distArray = new ArrayList<>();
        for (Club c : clubs) {
            String distance = database.getDistFromUmpireClub(umpireselection.getUmpireLicentie(), c.getClubNummer());
            distArray.add(distance);
        }
        ObservableList<String> distlist = FXCollections.observableArrayList(distArray);
        
        TableView distanceTable = new TableView(distlist);
        distanceTable.setEditable(false);
        distanceTable.setPrefHeight(1000.0);
        distanceTable.setPrefWidth(500);
        TableColumn<Club, String> clubCol = new TableColumn("Club");
        clubCol.setMinWidth(30);
        clubCol.setSortable(false);
        clubCol.prefWidthProperty().bind(distanceTable.widthProperty().divide(2));
      
        TableColumn<String, String> distCol = new TableColumn("Afstand");
        distCol.setSortable(false);
        TableColumn<Club, String> latCol = new TableColumn("Latitude");
        latCol.setSortable(false);
        TableColumn<Club, String> lonCol = new TableColumn("Longitude");
        lonCol.setSortable(false);
        
        clubCol.setCellValueFactory(new PropertyValueFactory<>("clubnaam"));
        latCol.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        lonCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        
        distCol.setCellValueFactory((p) -> {
            int i = distanceTable.getItems().indexOf(p.getValue());
            //System.out.println("index = " + i);
            return new ReadOnlyStringWrapper(distArray.get(i));
        });
        
        
        distanceTable.getColumns().addAll(clubCol, distCol, latCol, lonCol);
        distanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
               //clubselection = newSelection.
               System.out.println("selection = " + obs + "& " + newSelection.getClass());
               clubselection = (Club) newSelection;
                try {
                    VBox centerVbox = new VBox(5);
                    final Label kaartlabel = new Label("Kaart");
                    kaartlabel.setPadding(new Insets(1, 0, 1, 5));
                    kaartlabel.setFont(Font.font( null, FontWeight.BOLD, 20));
                    centerVbox.getChildren().add(kaartlabel);
                    mapBox = new VBox(5);
                    Double bh = 650.0;
                    Double bw = 650.0;
                    mapBox = apiLocationDistance.showMap(umpireselection, clubselection, bh, bw);
                    mapBox.setAlignment(Pos.CENTER);
                    centerVbox.getChildren().add(mapBox);
                    bpane.setCenter(centerVbox);
                } catch (IOException ex) {
                    Logger.getLogger(ShowDistances.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        distanceTable.setItems(clubs);
        distanceTable.setFixedCellSize(25);
        distanceTable.setMaxHeight(1000);
        distanceTable.setMinHeight(600);
        distanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return distanceTable;
    }
    
    /** Maak de listview van umpires.
     * 
     * @param umpirevbox
     * @param pane
     * @return 
     */
    private ListView getUmpireListView(VBox umpirevbox, Pane pane) {
        Label umpirelabel = new Label("Umpires");
        umpirelabel.setPadding(new Insets(0, 0, 0, 5));
        umpirelabel.setFont(Font.font( null, FontWeight.BOLD, 20 ));
        umpirevbox.getChildren().add(umpirelabel);
        umpirevbox.setPadding(new Insets(0, 0, 0, 5));
        //clubvbox.setBorder(new Border);
        
        //data = FXCollections.observableArrayList(umpires);
        FilteredList<Umpire> filteredData = new FilteredList<>(umpires, s -> true);
        
        
        HBox filterHBox = new HBox(5);
        filterHBox.setPadding(new Insets(2, 5, 2, 0));
        TextField filterInput = new TextField();
        filterInput.setPromptText("Umpire zoeken");
        filterInput.textProperty().addListener(obs -> {
            String filter = filterInput.getText();
                        
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
                
            } else {
                // Filter op voornaam en/of familienaam
                filteredData.setPredicate(s -> s.getUmpireNaam().contains(filter) || s.getUmpireVoornaam().contains(filter));
            }
        });
        Button resetButton = new Button("Reset");
        resetButton = new Button();
        resetButton.setText("Reset");
        resetButton.setOnAction(event -> {
            
            filterInput.setText("");
        });
        filterHBox.getStyleClass().add("bordered-titled-border");
        HBox.setHgrow(filterInput, Priority.ALWAYS);
        filterHBox.getChildren().add(filterInput);
        filterHBox.getChildren().add(resetButton);
        
        umpirevbox.getChildren().add(filterHBox);
        umpirelistview.setItems(filteredData);
        umpirelistview.setPrefHeight(1200);
        //clubs.sorted().forEach(c -> {
        //    clubListView.getItems().add(c);
        //});
        umpirelistview.setCellFactory(lv -> {
                ListCell<Umpire> cell = new ListCell<Umpire>() {
                    @Override
                    public void updateItem(Umpire item, boolean empty) {
                        super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getUmpireVoornaam() + " " + item.getUmpireNaam());
                    }
                    }
                };
                cell.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                
                @Override
                public void handle(ContextMenuEvent menuEvent) {
                    System.out.println("Context menu requested.");
                    final ContextMenu cm = new ContextMenu();
                    
                    
                    MenuItem calcDist = new MenuItem("Afstanden berekenen");
                    cm.getItems().add(calcDist);
                    calcDist.setOnAction((calc) -> {
                        apiLocationDistance = new ApiLocationDistance();
                        ArrayList<Club> clubarray = new ArrayList<>(clubs);
                        try {
                            if (umpireselection.getLatitude() == null || umpireselection.getLongitude() == null || umpireselection.getLatitude().isEmpty() || umpireselection.getLongitude().isEmpty()) {
                                System.out.println("Lat or Lon unknown for " + umpireselection.getUmpireNaam());
                                Pair<Double, Double> pair = apiLocationDistance.getLocationUmpire(umpireselection);
                                database.updateUmpireLocationInDatabase(umpireselection.getUmpireLicentie(), Double.toString(pair.getKey()), Double.toString(pair.getValue()));
                            }
                            apiLocationDistance.calculateDistance(umpireselection, clubarray);
                        } catch (IOException ex) {
                            Logger.getLogger(UmpireView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        umpireselection = cell.getItem();
                        vertDistances = new VBox(5);
                        
                        final Label label = new Label("Afstanden (km)");
                        label.setPadding(new Insets(1, 0, 1, 5));
                        label.setFont(Font.font( null, FontWeight.BOLD, 20 ));
                        vertDistances.getChildren().add(label);
                        vertDistances.setPadding(new Insets(0, 5, 5, 0));
                        vertDistances.getChildren().add(distanceTable(umpires.get(0)));
                        bpane.setRight(vertDistances);
                    });
                    
                    MenuItem showDetails = new MenuItem("Details");
                    cm.getItems().add(showDetails);
                    showDetails.setOnAction((show) -> {
                        //Show umpire details
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Umpire details");
                        alert.setHeaderText(umpireselection.getUmpireVoornaam() + " " + umpireselection.getUmpireNaam());
                        alert.setContentText("Licentienummer: " + umpireselection.getUmpireLicentie() + 
                                "\nClub: " + umpireselection.getUmpireClub().getClubNaam() + 
                                "\nStraat: " + umpireselection.getUmpireStraat() + " " + umpireselection.getUmpireHuisnummer() +
                                "\nStad: " + umpireselection.getUmpirePostcode() + " " + umpireselection.getUmpireStad() +
                                "\nLand: " + umpireselection.getUmpireLand() +
                                "\nEmail: " + umpireselection.getUmpireEmail() + 
                                "\nTel: " + umpireselection.getUmpireTelefoon());
                        
                        Optional<ButtonType> result = alert.showAndWait();
                        
                    });
                    
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                
                
                cell.setOnMouseClicked(event -> {
                   if (! cell.isEmpty()) {
                        
                        umpireselection = cell.getItem();
                        vertDistances = new VBox(5);
                        
                        final Label label = new Label("Afstanden (km)");
                        label.setPadding(new Insets(1, 0, 1, 5));
                        label.setFont(Font.font( null, FontWeight.BOLD, 20 ));
                        vertDistances.getChildren().add(label);
                        vertDistances.setPadding(new Insets(0, 5, 5, 0));
                        vertDistances.getChildren().add(distanceTable(umpires.get(0)));
                        bpane.setRight(vertDistances);
                       
                   }
                });
               return cell;
        });
        umpirelistview.getSelectionModel().select(0);
        return umpirelistview;
    }
    
    
}
