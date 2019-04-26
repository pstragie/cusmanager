/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusman;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author Pieter Stragier
 * @version 1.0
 * @since 1.0
 */
public class UmpireView {
    private MainPanel mainPanel;
    //private NewUmpire newUmpireWindow;
    private ObservableList<Afdeling> afdelingen;
    private ObservableList<Club> clubs;
    private ObservableList<Team> teams;
    private ObservableList<Umpire> umpires;
    private NewUmpire newUmpire;
    private Umpire umpireselection;
    private ApiLocationDistance apiLocationDistance;
    private Database database;
    //private ListView clubList;
    private ListView umpirelistview = new ListView();
    private TableView<Team> teamTable = new TableView<>();
    private Boolean nieuwUmpire = Boolean.FALSE;
    private BorderPane borderPane;
    private Button opslaan, wijzigingenAnnuleren;
    private BorderPane leftPane;
    private Pane lpane;
    private HBox hbox;
    private ObservableList<Umpire> data;
    
    /** Venster om umpires te beheren.
     * 
     * @param umpires
     * @param clubs
     * @param teams
     * @param afdelingen 
     */
    public UmpireView(ObservableList<Umpire> umpires, ObservableList<Club> clubs, ObservableList<Team> teams, ObservableList<Afdeling> afdelingen) {
        this.umpires = umpires;
        this.clubs = clubs;
        this.teams = teams;
        this.afdelingen = afdelingen;   
        database = new Database();
        borderPane = new BorderPane();
        apiLocationDistance = new ApiLocationDistance();
        
    }
    
    /** Paneel umpires
     * 
     * @return Pane
     */
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
        
        // Left Side: List of all Clubs in TabPane, Alphabetically
        // VerticalBox with Label, Filter and ListView 
        VBox umpireVBox = new VBox(5);
        ListView umpirelistView = new ListView();
        umpirelistView = getUmpireListView(umpireVBox, lpane);
        umpireVBox.getChildren().add(umpirelistView);
        // Right Side: TabPane (BB & SB) With List of teams per (selected) club --> Listview with Afdeling
        VBox teamVBox = new VBox(5);
        HBox newteamHBox = new HBox(5);
        
        
        
        //borderPane.setMargin(detailPane, new Insets(0, 10, 0, 10));
        borderPane.setLeft(umpireVBox);
        borderPane.setBottom(getButtonHBox());
        
        newUmpire = new NewUmpire();
        if (!umpireselection.getUmpireNaam().isEmpty()) {
            nieuwUmpire = false;
        }
        leftPane = new BorderPane();
        lpane = setDetails(umpireselection, nieuwUmpire);
        
        leftPane.setCenter(lpane);
        hbox = new HBox(5);
        
        // MARK: - OPSLAAN
        opslaan = new Button( "Opslaan" );
        opslaan.setOnAction((ActionEvent event) -> {
            // Wijzigingen opslaan in database
            
            if (newUmpire.licentietf.getText() == null || "".equals(newUmpire.licentietf.getText())) {
                System.out.println("Licentienummer mag niet leeg zijn!");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Waarschuwing");
                alert.setHeaderText("Licentienummer mag niet leeg zijn!");
                alert.setContentText("Vul een uniek licentienummer in.");
                alert.showAndWait();
            } else if (newUmpire.afdelingenArray == null || newUmpire.afdelingenArray.isEmpty()) {
                System.out.println("Afdeling mag niet leeg zijn!");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Waarschuwing");
                alert.setHeaderText("Geen afdelingen gekozen.");
                alert.setContentText("Selecteer minstens één afdeling.");
                alert.showAndWait();
            } else if ((newUmpire.familienaamtf.getText() == null || "".equals(newUmpire.familienaamtf.getText())) && (newUmpire.voornaamtf.getText() == null || "".equals(newUmpire.voornaamtf.getText()))) {
                System.out.println("Naam mag niet leeg zijn!");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Waarschuwing");
                alert.setHeaderText("Geen naam ingevuld!");
                alert.setContentText("Vul een voornaam en/of een familienaam in.");
                alert.showAndWait();
            }else {
                // Convert afdeling to 1BB:Baseball and convert to String (to fit database)
                ArrayList<String> afdWithDis = new ArrayList<>();
                ArrayList<Afdeling> afdArray2 = new ArrayList<>();
                String afdelingArrayString = null;
                if (newUmpire.afdelingenArray.isEmpty()) {
                    afdelingArrayString = null;
                    //afdArray2 = new ArrayList<>();
                } else {
                    newUmpire.afdelingenArray.forEach((a) -> {
                        afdWithDis.add(a.getAfdelingsNaam() + ":" + a.getAfdelingsCategorie());
                    });
                    newUmpire.afdelingenArray.forEach((a) -> {
                        afdArray2.add(a);
                    });
                    afdelingArrayString = String.join(",", afdWithDis);
                }
                System.out.println("nieuw bool = " + nieuwUmpire);
                String clubstring = null;
                if (newUmpire.clubComboBox.getValue() == null) {
                    clubstring = "";
                } else {
                    clubstring = newUmpire.clubComboBox.getValue().getClubNummer();
                }
                Boolean Uexists = Boolean.TRUE;
                try {
                    // Check if umpire exists
                    if (database.checkIfUmpireExists(newUmpire.familienaamtf.getText())) {
                        System.out.println("Umpire already exists");
                        Uexists = Boolean.TRUE;
                    } else {
                        Uexists = Boolean.FALSE;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UmpireView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // Update lat lon after opslaan
                String lat = "";
                String lon = "";
                Umpire umpire = database.getUmpireFromDatabase(newUmpire.licentietf.getText());
                try {
                    lat = (String) Double.toString((Double) apiLocationDistance.getLocationUmpire(umpire).getKey());
                    lon = (String) Double.toString((Double) apiLocationDistance.getLocationUmpire(umpire).getValue());
                } catch (IOException ex) {
                    Logger.getLogger(UmpireView.class.getName()).log(Level.SEVERE, null, ex);
                }
                database.updateUmpireLocationInDatabase(umpire.getUmpireLicentie(), lat, lon);
                
                // Verifieer umpire
                if (nieuwUmpire && !Uexists) {
                    // Nieuwe umpire toevoegen
                    System.out.println("Nieuwe umpire bestaat nog niet.");
                    database.insertNewUmpireToDatabase(newUmpire.familienaamtf.getText(), newUmpire.voornaamtf.getText(), newUmpire.licentietf.getText(), newUmpire.straattf.getText(), newUmpire.huisnummertf.getText(), newUmpire.postcodetf.getText(), newUmpire.stadtf.getText(), newUmpire.landCodeUmp.getSelectionModel().getSelectedItem(), newUmpire.telefoontf.getText(), newUmpire.emailtf.getText(), clubstring, afdelingArrayString, newUmpire.getActiefCheckBoxValue(), lat, lon);
                    umpires.add(new Umpire(newUmpire.familienaamtf.getText(), newUmpire.voornaamtf.getText(), newUmpire.licentietf.getText(), newUmpire.straattf.getText(), newUmpire.huisnummertf.getText(), newUmpire.postcodetf.getText(), newUmpire.stadtf.getText(), newUmpire.landCodeUmp.getSelectionModel().getSelectedItem(), newUmpire.telefoontf.getText(), newUmpire.emailtf.getText(), newUmpire.clubComboBox.getValue(), afdArray2, newUmpire.getActiefCheckBoxValue(), lat, lon));
                    umpires.sort(umpireComparator);
                } else 
                    if (nieuwUmpire && Uexists) {
                        // Waarschuwen en terug sturen
                        System.out.println("Nieuwe umpire bestaat al!");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Waarschuwing");
                        alert.setHeaderText("Deze umpire is al aanwezig in de lijst.");
                        alert.setContentText("Controleer het licentienummer. Dit moet uniek zijn.");
                        alert.showAndWait();
                    } else 
                        if (!nieuwUmpire) {
                            // Updaten van gegevens
                            System.out.println("Bestaande umpire aanpassen.");
                            database.updateUmpireToDatabase(newUmpire.familienaamtf.getText(), newUmpire.voornaamtf.getText(), newUmpire.licentietf.getText(), newUmpire.straattf.getText(), newUmpire.huisnummertf.getText(), newUmpire.postcodetf.getText(), newUmpire.stadtf.getText(), newUmpire.landCodeUmp.getSelectionModel().getSelectedItem(), newUmpire.telefoontf.getText(), newUmpire.emailtf.getText(), clubstring, afdelingArrayString, newUmpire.getActiefCheckBoxValue());
                            FilteredList filtumpire = umpires.filtered(c -> c.getUmpireLicentie().equals(newUmpire.licentietf.getText()));
                            
                            int uIndex = umpires.indexOf(filtumpire.get(0));
                            System.out.println("umpire index = " + uIndex);
                            try {
                                umpires.set(uIndex, new Umpire(newUmpire.familienaamtf.getText(), newUmpire.voornaamtf.getText(), newUmpire.licentietf.getText(), newUmpire.straattf.getText(), newUmpire.huisnummertf.getText(), newUmpire.postcodetf.getText(), newUmpire.stadtf.getText(), newUmpire.landCodeUmp.getSelectionModel().getSelectedItem(), newUmpire.telefoontf.getText(), newUmpire.emailtf.getText(), newUmpire.clubComboBox.getValue(), afdArray2, newUmpire.getActiefCheckBoxValue(), lat, lon));
                            } catch (Error e) {
                                System.err.println("ArrayIndexOutOfBoundsException caught in action... Opslaan niet gelukt!");
                                System.out.println("umpires lijst = " + umpires);
                                
                            }
                            umpires.sort(umpireComparator);
                        }
                nieuwUmpire = Boolean.FALSE;
            }
            
        });
        wijzigingenAnnuleren = new Button( "Wijzigingen annuleren" );
        wijzigingenAnnuleren.setOnAction(event -> { 
            // Reset original data
            nieuwUmpire = Boolean.FALSE;
                // Refresh pane on the left side 
            lpane = setDetails(umpireselection, nieuwUmpire);
            leftPane.setCenter(lpane);
        });
        
        hbox.getChildren().addAll(opslaan, wijzigingenAnnuleren);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        leftPane.setBottom(hbox);
        borderPane.setCenter(leftPane);
        return borderPane;
    }
    
    
    /** Maak een horizontale box met knoppen
     * 
     * @return HBox
     */
    public HBox getButtonHBox() {
        // HBox with buttons
        HBox buttonHBox = new HBox(10);
        buttonHBox.setPadding(new Insets(5, 10, 5, 10));
        Button addUmpireButton = new Button("Umpire toevoegen");
        addUmpireButton.setOnAction(event -> { 
            // Show Pane in borderPane Right
            
            System.out.println("Umpire toevoegen");
            nieuwUmpire = Boolean.TRUE;
            // Refresh pane on the left side
            lpane = setDetails(umpireselection, nieuwUmpire);
            leftPane.setCenter(lpane);
            
        });                   
        
        
        Button closeButton = new Button("Sluiten");
        //Text layoutIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CLOSE);
        //layoutIcon.getStyleClass().addAll("button-icon", "layout-button-icon");
        //closeButton.setGraphic(layoutIcon);
        closeButton.setOnAction(event -> {
           // Close window (and update teams)
           Stage stage = (Stage) closeButton.getScene().getWindow();
           stage.close();
        });
        buttonHBox.getChildren().add(addUmpireButton);
        buttonHBox.getChildren().add(closeButton);
        
        return buttonHBox;
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
                    /*
                    MenuItem verbergen = new MenuItem("Umpire actief/niet actief");
                    cm.getItems().add(verbergen);
                    verbergen.setOnAction(wiscel -> {
                        System.out.println("Verberg umpire");
                        int newIndex = umpires.indexOf(umpires.get(cell.getIndex()));
                        if (umpires.get(newIndex).getActief()) {
                            umpires.get(newIndex).setActief(Boolean.FALSE);
                        } else {
                            umpires.get(newIndex).setActief(Boolean.TRUE);
                        }
                        
                    });
                    */
                    MenuItem wisUmp = new MenuItem("Wis umpire");
                    cm.getItems().add(wisUmp);
                    wisUmp.setOnAction(wisrij -> {
                        int newIndex = umpires.indexOf(umpires.get(cell.getIndex()));
                        database.deleteUmpireFromDatabase(umpires.get(newIndex).getUmpireLicentie());
                        umpires.remove(umpires.get(newIndex));
                        nieuwUmpire = Boolean.TRUE;
                        // Refresh pane on the left side
                        lpane = setDetails(umpireselection, nieuwUmpire);
                        leftPane.setCenter(lpane);
                    });
                    MenuItem calcDist = new MenuItem("Afstanden berekenen");
                    cm.getItems().add(calcDist);
                    calcDist.setOnAction((calc) -> {
                        apiLocationDistance = new ApiLocationDistance();
                        ArrayList<Club> clubarray = new ArrayList<>(clubs);
                        try {
                            apiLocationDistance.calculateDistance(umpireselection, clubarray);
                        } catch (IOException ex) {
                            Logger.getLogger(UmpireView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    cell.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(cell.itemProperty()))
                    .then(cm)
                    .otherwise((ContextMenu)null));
                }
                }); 
                
                
                cell.setOnMouseClicked(event -> {
                   if (! cell.isEmpty()) {
                        
                        umpireselection = cell.getItem();
                        nieuwUmpire = Boolean.FALSE;
                        // Refresh pane on the left side
                        lpane = setDetails(umpireselection, nieuwUmpire);
                        leftPane.setCenter(lpane);
                        
                       
                   }
                });
               return cell;
        });
        umpirelistview.getSelectionModel().select(0);
        return umpirelistview;
    }
    
    /** Paneel met umpire details en nieuwe umpire toevoegen
     * 
     * @param umpireselection
     * @param nieuwUmpire
     * @return 
     */
    private Pane setDetails(Umpire umpireselection, Boolean nieuwUmpire) {
        Pane p = new Pane(newUmpire.NewUmpire(umpireselection, afdelingen, nieuwUmpire, clubs, umpires));
        return p;
    }
    
    
}
