/*
 * Restricted License.
 * No dispersal allowed.
 */
package javafxcusmanager;

import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Pieter Stragier <pstragier@gmail.be>
 */
public class AppSettings {
    
    public Preferences pref;
    private Label seizoenLabel, startOfYear;
    private TextField seizoentf;
    
    
    
    public AppSettings() {
        
    }
    
    public Pane settingsPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        VBox settingsVBox = new VBox(5);
        Label settingsLabel = new Label("Settings");
        settingsVBox.getChildren().add(settingsLabel);
        
        Pane settingsGrid = getSettingsGrid();
        
        settingsVBox.getChildren().add(settingsGrid);
        
        borderPane.setCenter(settingsVBox);
        
        HBox buttonHBox = new HBox(5);
        Button saveButton = new Button("Opslaan");
        Button sluitButton = new Button("Sluiten");
        saveButton.setOnAction(save -> {
            pref = Preferences.userNodeForPackage(AppSettings.class);
            pref.put("Seizoen", seizoentf.getText());
            
        });
        sluitButton.setOnAction(sluit -> {
            ((Stage)(((Button)sluit.getSource()).getScene().getWindow())).close();
        });
        buttonHBox.getChildren().addAll(saveButton, sluitButton);
        
        borderPane.setBottom(buttonHBox);
        return borderPane;
    }
    
    private Pane getSettingsGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        seizoenLabel = new Label("Seizoen (Jaartal)");
        seizoentf = new TextField();
        seizoentf.setAlignment(Pos.CENTER_LEFT);
        Label warningSeizoen = new Label("Opnieuw opstarten vereist.");
        startOfYear = new Label("Kalender start op:");
        
        grid.add(seizoenLabel, 0, 1);
        grid.add(seizoentf, 1, 1);
        grid.add(warningSeizoen, 2, 1);
        grid.add(startOfYear, 0, 2);
        
        
        return grid;
    }
}
