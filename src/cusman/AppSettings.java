/*
 * Restricted License.
 * No dispersal allowed.
 */
package cusman;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author Pieter Stragier
 */
public class AppSettings {
    
    public Preferences pref;
    private Label seizoenLabel, startOfYear, defaultGamehour, landcodeLabel;
    private TextField seizoentf, startOfYeartf, kmvergoedingtf, defaultGamehourtf, landcodes;
    private ObservableList<Vergoedingskosten> vergoedingen;
    private Database database;
    /* TO DO:
    Eerste dag van de week kiezen, default zaterdag (6)
    */
    private int first_day_of_weekend; // Fri = 5, Sat = 6, Sun = 7
    
    
    public AppSettings(ObservableList<Vergoedingskosten> vergoedingen) {
        this.vergoedingen = vergoedingen;
        database = new Database();
    }
    
    public HBox settingsPane() {
        HBox paneBox = new HBox(5);
        
        paneBox.setPadding(new Insets(10, 10, 10, 10));
        VBox settingsVBox = new VBox(5);
        Label settingsLabel = new Label("Settings");
        settingsLabel.setPadding(new Insets(0, 0, 0, 5));
        settingsLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        settingsVBox.getChildren().add(settingsLabel);
        
        Pane settingsGrid = getSettingsGrid();
        
        settingsVBox.getChildren().add(settingsGrid);
        
        //borderPane.setCenter(settingsVBox);
        
        // Right: onkostenvergoeding per afdeling
        VBox vergoedingsBox = new VBox(5);
        Label vergoedingslabel = new Label("Vergoedingen");
        vergoedingslabel.setPadding(new Insets(0, 0, 0, 5));
        vergoedingslabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        vergoedingsBox.getChildren().add(vergoedingslabel);
        
        TableView vergTabelAfd = new TableView(vergoedingen);
        vergTabelAfd.setEditable(true);
        vergTabelAfd.setPrefHeight(600.0);
        vergTabelAfd.setMinWidth(200.0);
        TableColumn<Vergoedingskosten, String> afdCol = new TableColumn<>("Afdeling");
        afdCol.prefWidthProperty().bind(vergTabelAfd.widthProperty().divide(2));
        TableColumn<Vergoedingskosten, String> euroCol = new TableColumn<>("Euro");
        euroCol.prefWidthProperty().bind(vergTabelAfd.widthProperty().divide(2));
        afdCol.setCellValueFactory(new PropertyValueFactory<>("afdeling"));
        afdCol.setEditable(false);
        euroCol.setCellValueFactory(new PropertyValueFactory<>("euro"));
        euroCol.setEditable(true);
        euroCol.setCellFactory(TextFieldTableCell.forTableColumn());
        euroCol.setOnEditCommit((CellEditEvent<Vergoedingskosten, String> t) -> {
            ((Vergoedingskosten) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setEuroDec(t.getNewValue());
            database.updateVergoedingToDatabase(t.getTableView().getItems().get(t.getTablePosition().getRow()).getAfdeling(), t.getNewValue());
        });
        vergTabelAfd.getColumns().addAll(afdCol, euroCol);
        vergoedingsBox.getChildren().add(vergTabelAfd);
        try {
            if (database.checkIfVergoedingExists("km")) {
                kmvergoedingtf = new TextField(database.getVergoedingenFromDatabase("km"));
            } else {
                kmvergoedingtf = new TextField();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        Label kmvergoedingslabel = new Label("Onkostenvergoeding per km (€):");
        vergoedingsBox.getChildren().add(kmvergoedingslabel);
        kmvergoedingtf.setAlignment(Pos.CENTER_LEFT);
        kmvergoedingtf.setPromptText("0.000");
        kmvergoedingtf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    kmvergoedingtf.setText(oldValue);
                }
            }
        });
        vergoedingsBox.getChildren().add(kmvergoedingtf);
        
        //borderPane.setRight(vergoedingsBox);
        
        // Bottom: knoppen
        HBox buttonHBox = new HBox(5);
        Button saveButton = new Button("Opslaan");
        Button sluitButton = new Button("Sluiten");
        saveButton.setOnAction(save -> {
            // Store landcodes
            database.deleteAllLandcodesInDatabase();
            for (String lc : landcodes.getText().split(",")) {
                String landcode = lc.trim();
                database.storeLandCode(landcode);
            }
            // Store preferences
            pref = Preferences.userNodeForPackage(AppSettings.class);
            pref.put("Seizoen", seizoentf.getText());
            pref.put("StartOfYear", startOfYeartf.getText());
            pref.put("DefaultGameTime", defaultGamehourtf.getText());
            // Save vergoedingen from table
            String km = "km";
            try {
                if (database.checkIfVergoedingExists(km)) {
                    database.updateVergoedingToDatabase(km, kmvergoedingtf.getText());
                } else {
                    database.insertVergoedingToDatabase(km, kmvergoedingtf.getText());
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        sluitButton.setOnAction(sluit -> {
            ((Stage)(((Button)sluit.getSource()).getScene().getWindow())).close();
        });
        buttonHBox.getChildren().addAll(saveButton, sluitButton);
        settingsVBox.getChildren().add(buttonHBox);
        //borderPane.setBottom(buttonHBox);
        paneBox.getChildren().add(settingsVBox);
        paneBox.getChildren().add(vergoedingsBox);
        return paneBox;
    }
    
    private Pane getSettingsGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        pref = Preferences.userNodeForPackage(AppSettings.class);
        seizoenLabel = new Label("Seizoen (Jaartal)");
        seizoentf = new TextField(pref.get("seizoen", "2019"));
        seizoentf.setAlignment(Pos.CENTER_LEFT);
        landcodeLabel = new Label("Landcodes (BE, NL, FR)");
        // TO DO: Automatisch seizoen aanpassen
        Label warningSeizoen = new Label("Opnieuw opstarten vereist.");
        startOfYear = new Label("Kalender start op week:");
        startOfYeartf = new TextField(pref.get("StartOfYear", "15"));
        defaultGamehour = new Label("Default begin wedstrijd:");
        defaultGamehourtf = new TextField(pref.get("DefaultGameTime", "14:00"));
        defaultGamehourtf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}([\\:]\\d{0,2})?")) {
                    defaultGamehourtf.setText(oldValue);
                }
            }
        });
        ArrayList<String> lcArray = database.getLandcodesFromDatabase();
        String lcString = String.join(", ", lcArray);
        landcodes = new TextField(lcString);
        grid.add(seizoenLabel, 0, 1);
        grid.add(seizoentf, 1, 1);
        grid.add(warningSeizoen, 0, 2, 2, 1);
        grid.add(startOfYear, 0, 3);
        grid.add(startOfYeartf, 1, 3);
        grid.add(defaultGamehour, 0, 4);
        grid.add(defaultGamehourtf, 1, 4);
        grid.add(landcodeLabel, 0, 5);
        grid.add(landcodes, 1, 5);
        return grid;
    }
}
