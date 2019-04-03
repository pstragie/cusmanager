/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author pieter
 */
public class NewClub {
    /**
	 * @param args
	 */
	
	

    
	
        /* Paneel met invoervakken voor nieuwe club */
	// Attributen
	private boolean confirmed = false;
        private ObservableList<String> afdelingen;
        private ObservableList<Club> clubs;
	public TextField clubnaamtf, ligatf, websitetf, clubnummertf, clubemailtf, clubtelefoontf, voorzittertf, straattf, huisnummertf, postcodetf, stadtf, landcodetf;
	private ComboBox landcodeCombobox;
        private Label clubLabel,  ligaLabel, websiteLabel, clubnummerLabel, clubemailLabel, clubtelefoonLabel, voorzitterLabel, straatLabel, huisnrLabel, pcLabel, stadLabel, landLabel;
	private Button toevoegen, annuleren;
        private Database database;
        private NewClub newclub;
        private ApiLocationDistance location;
        // Constructor
        public NewClub(ObservableList clubs, ObservableList afdelingen) {
            this.afdelingen = afdelingen;
            this.clubs = clubs;
                 
        }
        
        private ObservableList getLandcodes() {
            database = new Database();
            ObservableList landcodes = FXCollections.observableArrayList(database.getLandcodesFromDatabase());
            
            return landcodes;
        }
        
	// Constructor
	public Pane clubPanel() {
             
		// Schakel lay-outmanager uit
		GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER_LEFT);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

		// Maak de tekstvakken
                
		clubnaamtf = new TextField( );
                clubnaamtf.setAlignment(Pos.CENTER_LEFT);
                clubnaamtf.setMinWidth(250.0);
                clubnummertf = new TextField();
                clubnummertf.setAlignment(Pos.CENTER_LEFT);
		voorzittertf = new TextField ( );
		voorzittertf.setAlignment(Pos.CENTER_LEFT);
                clubemailtf = new TextField();
                clubemailtf.setAlignment(Pos.CENTER_LEFT);
                clubtelefoontf = new TextField();
                clubtelefoontf.setAlignment(Pos.CENTER_LEFT);
		straattf = new TextField ( );
		straattf.setAlignment(Pos.CENTER_LEFT);
		huisnummertf = new TextField ( );
		huisnummertf.setAlignment(Pos.CENTER_LEFT);
		postcodetf = new TextField ( );
		postcodetf.setAlignment(Pos.CENTER_LEFT);
		stadtf = new TextField ( );
		stadtf.setAlignment(Pos.CENTER_LEFT);
                landcodeCombobox = new ComboBox(getLandcodes());
                landcodeCombobox.getSelectionModel().selectFirst();
		ligatf = new TextField ( );
                ligatf.setAlignment(Pos.CENTER_LEFT);
                websitetf = new TextField ( );
                websitetf.setAlignment(Pos.CENTER_LEFT);
		// Maak de labels
		clubLabel = new Label( "Clubnaam *" );
                clubLabel.setMinWidth(100);
		clubnummerLabel = new Label( "Club nummer *" );
		voorzitterLabel = new Label( "Voorzitter" );
                clubemailLabel = new Label( "Email" );
                clubtelefoonLabel = new Label( "Telefoon" );
		straatLabel = new Label( "Straat" );
		huisnrLabel = new Label( "Huisnummer" );
		pcLabel = new Label( "Postcode" );
		stadLabel = new Label( "Stad *" );
                landLabel = new Label ("Landcode");
		ligaLabel = new Label ("Liga");
                websiteLabel = new Label ("Website");
		toevoegen = new Button( "Toevoegen" );
                toevoegen.setPrefWidth(100);
		annuleren = new Button( "Annuleren" );
                annuleren.setPrefWidth(100);
		
		toevoegen.setOnAction((ActionEvent event) -> {
                    if (clubnaamtf.getText() == null || "".equals(clubnaamtf.getText())) {
                        System.out.println("Clubnaam mag niet leeg zijn!");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Waarschuwing");
                        alert.setHeaderText("Clubnaam mag niet leeg zijn!");
                        alert.setContentText("Vul een clubnaam in.");
                        alert.showAndWait();
                    } else if (clubnummertf.getText() == null || clubnummertf.getText().isEmpty()) {
                        System.out.println("Clubnummer mag niet leeg zijn!");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Waarschuwing");
                        alert.setHeaderText("Geen clubnummer ingevoerd.");
                        alert.setContentText("Vul een clubnummer in.");
                        alert.showAndWait();
                    } else if (stadtf.getText() == null || stadtf.getText().isEmpty()) {
                        System.out.println("Stad mag niet leeg zijn!");
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Waarschuwing");
                        alert.setHeaderText("Geen stad ingevoerd.");
                        alert.setContentText("Vul een stad in.");
                        alert.showAndWait();
                    }else {
                        confirmed = true;
                        ArrayList<Team> emptyArray = new ArrayList<>();
                        clubs.add(new Club(clubnaamtf.getText(), ligatf.getText(), clubnummertf.getText(), voorzittertf.getText(), straattf.getText(), huisnummertf.getText(), postcodetf.getText(), stadtf.getText(), landcodeCombobox.getSelectionModel().getSelectedItem().toString(), clubemailtf.getText(), clubtelefoontf.getText(), websitetf.getText(), emptyArray, Boolean.TRUE, "", ""));
                        database.insertNewClubToDatabase(clubnaamtf.getText(), voorzittertf.getText(), clubnummertf.getText(), straattf.getText(), huisnummertf.getText(), postcodetf.getText(), stadtf.getText(), landcodeCombobox.getSelectionModel().getSelectedItem().toString(), clubemailtf.getText(), clubtelefoontf.getText(), ligatf.getText(), websitetf.getText(), confirmed, "", "");
                        // Close window
                        Stage stage = (Stage) toevoegen.getScene().getWindow();
                        stage.close();
                    }
                });
		annuleren.setOnAction((ActionEvent event) -> {
                    // Close window
                    Stage stage = (Stage) annuleren.getScene().getWindow();
                    stage.close();
		});
	
		// Bepaal van alle componenten de plaats en afmeting
                
		grid.add(clubLabel, 0, 1 );
		grid.add(clubnaamtf, 1, 1 );
		grid.add(clubnummerLabel, 0, 2 );
		grid.add(clubnummertf, 1, 2 );
		grid.add(voorzitterLabel, 0, 3 );
		grid.add(voorzittertf, 1, 3 );
                grid.add(clubemailLabel, 0, 4);
                grid.add(clubemailtf, 1, 4);
                grid.add(clubtelefoonLabel, 0, 5);
                grid.add(clubtelefoontf, 1, 5);
		grid.add(straatLabel, 0, 6 );
		grid.add(straattf, 1, 6 );
		grid.add(huisnrLabel, 0, 7 );
		grid.add(huisnummertf, 1, 7 );
		grid.add(pcLabel, 0, 8 );
		grid.add(postcodetf, 1, 8 );
		grid.add(stadLabel, 0, 9 );
		grid.add(stadtf, 1, 9 );
                grid.add(landLabel, 0, 10);
                grid.add(landcodeCombobox, 1, 10);
                grid.add(ligaLabel, 0, 11);
                grid.add(ligatf, 1, 11);
                grid.add(websiteLabel, 0, 12);
                grid.add(websitetf, 1, 12);
		grid.add(toevoegen, 0, 13 );
		grid.add(annuleren, 1, 13 );
		
		return grid;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	}

	public String getInputFieldText() {
	    return clubnaamtf.getText();
	}
    }

